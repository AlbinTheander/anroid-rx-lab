package com.jayway.klab.rx.github;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User {

    public String login;

    public static class SearchResult {
        @SerializedName("total_count")
        public int count;

        @SerializedName("items")
        public List<User> users;
    }
/*
    {
        "total_count": 12,
            "incomplete_results": false,
            "items": [
        {
            "login": "mojombo",
                "id": 1,
                "avatar_url": "https://secure.gravatar.com/avatar/25c7c18223fb42a4c6ae1c8db6f50f9b?d=https://a248.e.akamai.net/assets.github.com%2Fimages%2Fgravatars%2Fgravatar-user-420.png",
                "gravatar_id": "",
                "url": "https://api.github.com/users/mojombo",
                "html_url": "https://github.com/mojombo",
                "followers_url": "https://api.github.com/users/mojombo/followers",
                "subscriptions_url": "https://api.github.com/users/mojombo/subscriptions",
                "organizations_url": "https://api.github.com/users/mojombo/orgs",
                "repos_url": "https://api.github.com/users/mojombo/repos",
                "received_events_url": "https://api.github.com/users/mojombo/received_events",
                "type": "User",
                "score": 105.47857
        }
        ]
    }
    */
}
