package com.jayway.klab.rx;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewAfterTextChangeEvent;
import com.jayway.klab.rx.github.User;
import com.koushikdutta.ion.Ion;

import java.util.List;
import java.util.Observable;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    @Bind(R.id.query)
    EditText queryView;

    @Bind(R.id.list)
    ListView listView;

    private Subscription subscription;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        ArrayAdapter<String> userNameAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1);
        listView.setAdapter(userNameAdapter);

        subscription = RxTextView.afterTextChangeEvents(queryView)
                .subscribeOn(AndroidSchedulers.mainThread())
                .map(textChangeEvent -> textChangeEvent.editable().toString())

                .filter(text -> text.length() > 2)
                .debounce(500, TimeUnit.MILLISECONDS)

                .flatMap(query -> searchGithubUsers(query).observeOn(Schedulers.io()))
                .retry()

                .observeOn(AndroidSchedulers.mainThread())
                .filter(searchResult -> searchResult != null && searchResult.users != null)
                .map(searchResult -> searchResult.users)
                .flatMap(users -> rx.Observable.from(users).take(10).map(user -> user.login).toList())

                .subscribe(userList -> {
                    userNameAdapter.clear();
                    userNameAdapter.addAll(userList);
                });
        ;
        return view;
    }

    @NonNull
    private rx.Observable<User.SearchResult> searchGithubUsers(String query) {
        String url = "https://api.github.com/search/users?q=" + Uri.encode(query);
        return rx.Observable.from(Ion.with(getContext()).load(url).as(User.SearchResult.class));
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        subscription.unsubscribe();
        super.onDestroyView();
    }
}
