package com.foodie;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.foodie.models.ServerResponse;
import com.foodie.networkUtils.InternetConnect;
import com.foodie.retrofit.APIClient;
import com.foodie.retrofit.APIInterface;
import com.foodie.utils.CommonUtils;
import com.foodie.utils.SharedPrefrenceManager;
import com.foodie.utils.ValidationUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by WebPlanetDeveloper on 3/18/2018.
 */

public class FacebookActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewGroup loginForm;
    private Intent intentActivity;

    private EditText edtUsername,edtPassword;
    private TextView txtForgot;
    private Button btnLogin,btnReg;

    private String strUsername,strPassword,fbId,fbName,fbEmail,fbGender,fbBirthday;

    private LinearLayout layFacebook,layGoogle,chkRemember;

    private ProgressDialog mProgressDialog;

    APIInterface apiInterface;

    private CallbackManager callbackManager;
    private LoginButton btnFbLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();
        btnFbLogin = (LoginButton) findViewById(R.id.btnFbLogin);

        //CommonUtils.printKeyHash(this);
        TextView txtLogin = (TextView) findViewById(R.id.txtLoginHeading);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/exo350db.ttf");
        txtLogin.setTypeface(typeface);

        TextView txtRemember = (TextView) findViewById(R.id.txtRemember);
        txtRemember.setTypeface(typeface);
        TextView txtForgotPassword = (TextView) findViewById(R.id.txtForgotPassword);
        txtForgotPassword.setTypeface(typeface);

        TextView txtOR= (TextView) findViewById(R.id.txtOR);
        Typeface orTypeFace = Typeface.createFromAsset(getAssets(),"fonts/bodonbd.ttf");
        txtOR.setTypeface(orTypeFace);

        /*Typeface squareTypeFace = Typeface.createFromAsset(getAssets(),"fonts/square721bt.ttf");
        TextView txtFacebook = (TextView) findViewById(R.id.txtFacebook);
        TextView txtGoogle = (TextView) findViewById(R.id.txtGoogle);
        */
        loginForm = (ViewGroup)findViewById(R.id.loginForm);
        //Link
        txtForgot = (TextView)findViewById(R.id.txtForgotPassword);

        //LinerLayout
        chkRemember = (LinearLayout)findViewById(R.id.chkRemember);
        layFacebook = (LinearLayout)findViewById(R.id.layFacebook);
        layGoogle = (LinearLayout)findViewById(R.id.layGoogle);

        //Button
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnReg = (Button)findViewById(R.id.btnReg);

        btnLogin.setOnClickListener(this);
        btnReg.setOnClickListener(this);
        txtForgot.setOnClickListener(this);
        layFacebook.setOnClickListener(this);

        mProgressDialog = CommonUtils.ProgressBar(this, "");


        //Facebook Callback Function
        List< String > permissionNeeds = Arrays.asList("user_photos", "email",
                "user_birthday", "public_profile", "AccessToken");
        btnFbLogin.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        System.out.println("onSuccess");

                        String accessToken = loginResult.getAccessToken()
                                .getToken();
                        Log.i("accessToken", accessToken);

                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {

                                        Log.i("LoginActivity",
                                                response.toString());
                                        Log.e("json Object",
                                                object.toString());
                                        try {
                                            fbId = object.getString("id");
                                            try {
                                                URL profile_pic = new URL("http://graph.facebook.com/" + fbId + "/picture?type=large");
                                                Log.i("profile_pic",
                                                        profile_pic + "");

                                            } catch (MalformedURLException e) {
                                                e.printStackTrace();
                                            }

                                            fbName = object.getString("name");
                                            fbEmail = object.getString("email");
                                            fbGender = object.getString("gender");
                                            fbBirthday = object.getString("birthday");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields","id,name,email,gender, birthday");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        System.out.println("onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        System.out.println("onError");
                        Log.v("LoginActivity", exception.getCause().toString());
                    }
                }
        );
    }

    @Override
    public void onClick(View view) {
        if(view==layFacebook){
            btnFbLogin.performClick();
        }else if(view==btnLogin){
            loginAction();
        }else if (view==btnReg){
            intentActivity = new Intent(this,RegisterActivity.class);
            startActivity(intentActivity);
        }else if(view==txtForgot){
            intentActivity = new Intent(this,ForgotPasswordActivity.class);
            startActivity(intentActivity);
        }
    }

    //Login Action Get All input fields value
    private void  loginAction(){
        edtUsername = (EditText)findViewById(R.id.edtLogUsername);
        edtPassword = (EditText)findViewById(R.id.edtLogPass);

        strUsername = edtUsername.getText().toString().trim();
        strPassword = edtPassword.getText().toString().trim();
        if(loginValidate()){
            //CommonUtils.clearErrorFromView(loginForm);
            if(InternetConnect.isConnected(FacebookActivity.this)) {
                mProgressDialog.show();
                loginWebService();
                /*finish();
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));*/
            }else{
                CommonUtils.showAlertMessage(FacebookActivity.this,getString(R.string.error),getString(R.string.net_connection_error_title),getString(R.string.net_connection_error_msg),getString(R.string.net_connection_error_btn));
            }
        }
    }
    /**
     * this function use for validate login form.
     * @params none;
     * @return boolean true/false;
     */
    private boolean loginValidate(){
        if(strUsername.equals("")){
            CommonUtils.setErrorOnView(edtUsername, getString(R.string.error_field_required));
        }else if(!ValidationUtils.isUsernameValid(strUsername)){
            CommonUtils.setErrorOnView(edtUsername, getString(R.string.error_invalid_username));
        }else if(strPassword.equals("")){
            CommonUtils.setErrorOnView(edtPassword, getString(R.string.error_field_required));
        }else{
            return true;
        }
        return false;
    }
    /**
     * this function use for call login webservice.
     * @params none;
     * @return none;
     */
    private void loginWebService(){
        apiInterface = APIClient.getInterface();
        Call<ServerResponse> call = apiInterface.login(strUsername,strPassword,CommonUtils.getDeviceToken(FacebookActivity.this),"Android");
        //Call<ServerResponse> call = apiInterface.createUser();
        //Call<ServerResponse> call = apiInterface.createUserData();
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                mProgressDialog.dismiss();
                Log.e("Gopal Web Service "," in success");
                ServerResponse responseData = response.body();
                if(responseData.getCode().toString().equals("1")){
                    SharedPrefrenceManager.getInstance(FacebookActivity.this).setUserDetails(responseData.getUser());
                    AppController.getSessionData(getApplicationContext());
                    startActivity(new Intent(FacebookActivity.this, HomeActivity.class));
                    finish();
                }else{
                    CommonUtils.showAlertMessage(FacebookActivity.this,getString(R.string.error),getString(R.string.error),responseData.getMessage(),getString(R.string.ok));
                }
                //Log.e("Gopal Web Service ",responseData.getCode()+' '+ responseData.getMessage());
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                t.printStackTrace();
                mProgressDialog.dismiss();
                call.cancel();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        callbackManager.onActivityResult(requestCode, responseCode, data);
    }
}

