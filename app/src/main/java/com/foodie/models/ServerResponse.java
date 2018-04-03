package com.foodie.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

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
    @SerializedName("ingredients")
    private ArrayList<Ingredient> ingredients;
    @SerializedName("foodPosts")
    private ArrayList<FoodPost> foodPosts;
    @SerializedName("food_taste_type")
    private ArrayList<FoodTasteType> food_taste_type;

    public ArrayList<FoodTasteType> getFood_taste_type() {
        return food_taste_type;
    }

    public void setFood_taste_type(ArrayList<FoodTasteType> food_taste_type) {
        this.food_taste_type = food_taste_type;
    }

    public ArrayList<FoodPost> getFoodPosts() {
        return foodPosts;
    }

    public void setFoodPosts(ArrayList<FoodPost> food_posts) {
        this.foodPosts = food_posts;
    }

    public String getSocial_id() {
        return social_id;
    }

    public void setSocial_id(String social_id) {
        this.social_id = social_id;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

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
