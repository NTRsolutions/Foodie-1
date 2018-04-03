package com.foodie.retrofit;

import com.foodie.constant.WebServiceConstant;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by WebPlanetDeveloper on 3/21/2018.
 */

public class APIClient {

    private static Retrofit retrofit = null;
    private static APIInterface apiInterface = null;
    private static String base_url = WebServiceConstant.ROOT_URL;


    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(base_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static APIInterface getInterface() {
        if (apiInterface==null) {
            apiInterface = getClient().create(APIInterface.class);
        }
        return apiInterface;
    }
}
