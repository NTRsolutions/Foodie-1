package com.foodie.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by WebPlanetDeveloper on 3/21/2018.
 */

public class ServerResponse {

    @SerializedName("message")
    private String message;
    @SerializedName("code")
    private String code;
    @SerializedName("user")
    private User user;
    @SerializedName("social_id")
    private String social_id;

    public String getSocialId() {
        return social_id;
    }

    public void setSocialId(String social_id) {
        this.social_id = social_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
