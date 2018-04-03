package com.foodie;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.foodie.utils.SharedPrefrenceManager;


public class SplashActivity extends AppCompatActivity {

    /** Duration of wait **/
    public final int SPLASH_DISPLAY_LENGTH = 1000;

    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        /* New Handler to start the home-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Home-Activity. */
                Intent mainIntent;
                if(SharedPrefrenceManager.getInstance(SplashActivity.this).isLoggedIn()){
                    AppController.getSessionData(getApplicationContext());
                    mainIntent = new Intent(SplashActivity.this,HomeActivity.class);
                }else{
                    mainIntent = new Intent(SplashActivity.this,LoginActivity.class);
                }
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
