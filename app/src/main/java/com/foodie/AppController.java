package com.foodie;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.facebook.appevents.AppEventsLogger;
import com.foodie.models.User;
import com.foodie.utils.SharedPrefrenceManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by WebPlanetDeveloper on 3/21/2018.
 */

public class AppController extends Application {

    public static final String TAG = AppController.class
            .getSimpleName();


    private static AppController mInstance;

    public static User aSessionUserData;

    public static GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        //Facebook SDK Initialize from Complete Instance
        AppEventsLogger.activateApp(AppController.this);
        googleInitialize(AppController.this);
        printHashKey();
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    /**
     * Get Session User Details
     * @param mCtx (Context object)
     */
    public static void getSessionData(Context mCtx){
        aSessionUserData = SharedPrefrenceManager.getInstance(mCtx).getUserDetails();
    }

    public void printHashKey(){
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.foodie.thiredpartylogin",
                    PackageManager.GET_SIGNATURES);
            Log.e("ErrorGopal:"," error occire ");
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }
    //Gmail SDK Initialize
    public void googleInitialize(Context mCtx){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.gmail_web_client_id))
                .requestProfile()
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(mCtx)
                //.enableAutoManage(mCtx, mCtx)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();

    }
}
