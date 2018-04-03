package com.foodie.retrofit;

import com.foodie.constant.WebServiceConstant;
import com.foodie.models.ServerResponse;
import com.foodie.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Khatushyam on 3/21/2018.
 */

public interface APIInterface {

    @GET("createUser")
    //Call<ServerResponse> createUser(@Body User user);
    Call<ServerResponse> createUser();

    @POST("createUserData")
    Call<ServerResponse> createUserData(@Body User user);

    @POST(WebServiceConstant.REG_URL)
    @FormUrlEncoded
    Call<ServerResponse> register(@Field("first_name") String first_name,
                                  @Field("last_name") String last_name,
                                  @Field("username") String username,
                                  @Field("email") String email,
                                  @Field("password") String password);

    @POST(WebServiceConstant.LOGIN_URL)
    @FormUrlEncoded
    Call<ServerResponse> login(@Field("username") String username,
                               @Field("password") String password,
                               @Field("device_token") String device_token,
                               @Field("device_type") String device_type);

    @GET(WebServiceConstant.FORGOTPASS_URL)
        //Call<ServerResponse> createUser(@Body User user);
    Call<ServerResponse> forgot_password(@Path("email") String email);

    @POST(WebServiceConstant.FORGOTOTP_URL)
    @FormUrlEncoded
    Call<ServerResponse> validate_forgot_otp(@Field("email") String email,
                                             @Field("otp") String otp);

    @POST(WebServiceConstant.RESETPASS_URL)
    @FormUrlEncoded
    Call<ServerResponse> reset_password(@Field("email") String email,
                                       @Field("password") String password);

    @POST(WebServiceConstant.SOCIAL_LOGIN_URL)
    @FormUrlEncoded
    Call<ServerResponse> social_login(@Field("first_name") String first_name,
                                  @Field("last_name") String last_name,
                                  @Field("email") String email,
                                  @Field("social_id") String sId,
                                  @Field("social_type") String social_type,
                                  @Field("device_token") String device_token,
                                  @Field("device_type") String device_type);
}