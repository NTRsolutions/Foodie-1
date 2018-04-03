package com.foodie.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.foodie.constant.Constant;
import com.foodie.models.User;


/**
 * Created by Developer on 9/20/2017.
 */

public class SharedPrefrenceManager {

    private static SharedPrefrenceManager mInstance;
    private static Context mCtx;

    private SharedPrefrenceManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefrenceManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefrenceManager(context);
        }
        return mInstance;
    }
    /**
     * @param : String param
     * @return : SharedPreferences object
     */
    private static SharedPreferences getSharedPrefrences(String sharedTag) {
        return mCtx.getSharedPreferences(sharedTag, Context.MODE_PRIVATE);
    }

    public void setUserDetails(User user) {
        SharedPreferences.Editor editor = getSharedPrefrences(Constant.USER_SHARED_PREFRENCES).edit();
        editor.putBoolean(Constant.IS_LOGGED,true);
        editor.putInt(Constant.USER_ID, user.getId());
        editor.putString(Constant.USER_NAME, user.getUsername());
        editor.putString(Constant.USER_EMAIL, user.getEmail());
        editor.putString(Constant.USER_FNAME, user.getFirstName());
        editor.putString(Constant.USER_LNAME, user.getLastName());
        editor.apply();
    }
    public User getUserDetails() {
        SharedPreferences sharedPreferences = getSharedPrefrences(Constant.USER_SHARED_PREFRENCES);
        return new User(sharedPreferences.getString(Constant.USER_NAME,null),sharedPreferences.getString(Constant.USER_EMAIL, null),sharedPreferences.getString(Constant.USER_FNAME, null),sharedPreferences.getString(Constant.USER_LNAME, null),sharedPreferences.getInt(Constant.USER_ID,0));
        //return sharedPreferences.getString(USER_ID, null);
    }
    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPrefrences(Constant.USER_SHARED_PREFRENCES);
        return sharedPreferences.getBoolean(Constant.IS_LOGGED, false);
    }
    public void removeSession() {
        SharedPreferences.Editor editor = getSharedPrefrences(Constant.USER_SHARED_PREFRENCES).edit();
        editor.clear();
        editor.apply();
    }

    /*
     * set forgot Email for OTP validation
     */
    public void setForgotEmail(String strEmail) {
        SharedPreferences.Editor editor = getSharedPrefrences(Constant.USER_FORGOT_TAG).edit();
        editor.putString(Constant.USER_EMAIL, strEmail);
        editor.apply();
    }
    /*
     * get forgot Email for OTP validation
     */
    public String getForgotEmail() {
        SharedPreferences sharedPreferences = getSharedPrefrences(Constant.USER_FORGOT_TAG);
        return sharedPreferences.getString(Constant.USER_EMAIL, null);
    }
    /*
     * delete forgot Email
     */
    public boolean deleteForgotEmail() {
        SharedPreferences.Editor editor = getSharedPrefrences(Constant.USER_FORGOT_TAG).edit();
        editor.remove(Constant.USER_EMAIL);
        editor.apply();
        return true;
    }
    /*
     * All action with social media
     */
    public void setSocialData(String sId,String Socialtype){
        SharedPreferences.Editor editor = getSharedPrefrences(Constant.SOCIAL_MEDIA).edit();
        editor.putBoolean(Constant.SOCIAL_LOGGED,true);
        editor.putString(Constant.SOCIAL_ID, sId);
        editor.putString(Constant.SOCIAL_TYPE, Socialtype);
        editor.apply();
    }
    public Boolean isSocialLogged(){
        SharedPreferences sharedPreferences = getSharedPrefrences(Constant.SOCIAL_MEDIA);
        return sharedPreferences.getBoolean(Constant.SOCIAL_LOGGED,false);
    }
    public String getSocialId(){
        SharedPreferences sharedPreferences = getSharedPrefrences(Constant.SOCIAL_MEDIA);
        return sharedPreferences.getString(Constant.SOCIAL_ID,null);
    }
    public String getSocialType(){
        SharedPreferences sharedPreferences = getSharedPrefrences(Constant.SOCIAL_MEDIA);
        return sharedPreferences.getString(Constant.SOCIAL_TYPE,null);
    }

    public boolean removeSocialData(){
        SharedPreferences.Editor editor = getSharedPrefrences(Constant.SOCIAL_MEDIA).edit();
        editor.remove(Constant.SOCIAL_LOGGED);
        editor.remove(Constant.SOCIAL_ID);
        editor.remove(Constant.SOCIAL_TYPE);
        editor.apply();
        return true;
    }

    public void setTmpSaveData(String edtType,String key,String Val) {
        SharedPreferences.Editor editor = getSharedPrefrences(edtType).edit();
        editor.putString(key, Val);
        editor.apply();
    }
    public String getTmpSaveData(String edtType,String key) {
        SharedPreferences editor = getSharedPrefrences(edtType);
        return editor.getString(key,null);
    }
    /*
     * set,get and remove user id for reset password
     */
    /*
    public void setResetUserId(Integer userId) {
        SharedPreferences.Editor editor = getSharedPrefrences(Constant.USER_RESET_PASSWORD).edit();
        editor.putInt(Constant.USER_ID, userId);
        editor.apply();
    }
    public Integer getResetUserId() {
        SharedPreferences sharedPreferences = getSharedPrefrences(Constant.USER_RESET_PASSWORD);
        return sharedPreferences.getInt(Constant.USER_ID,0);
    }
    public boolean deleteResetUserId() {
        SharedPreferences.Editor editor = getSharedPrefrences(Constant.USER_RESET_PASSWORD).edit();
        editor.remove(Constant.USER_ID);
        editor.apply();
        return true;
    }*/
}
