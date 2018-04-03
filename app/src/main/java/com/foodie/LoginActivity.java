package com.foodie;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.foodie.constant.Constant;
import com.foodie.models.ServerResponse;
import com.foodie.networkUtils.InternetConnect;
import com.foodie.retrofit.APIClient;
import com.foodie.retrofit.APIInterface;
import com.foodie.utils.CommonUtils;
import com.foodie.utils.SharedPrefrenceManager;
import com.foodie.utils.ValidationUtils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by WebPlanetDeveloper on 3/18/2018.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private ViewGroup loginForm;
    private Intent intentActivity;

    private EditText edtUsername,edtPassword;
    private TextView txtForgot;
    private Button btnLogin,btnReg;

    private String strUsername,strPassword,fbId,fbName,fbEmail,fbGender,fbBirthday;

    private LinearLayout layFacebook,layGoogle,chkRemember,layGoogleSignOut;

    private ProgressDialog mProgressDialog;

    APIInterface apiInterface;

    // Facebook login things
    private CallbackManager callbackManager;
    private LoginButton btnFbLogin;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;

    //CallBack Facbook Login Function
    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            Log.v("LoginActivity", response.toString());
                            Log.e("Gopalsharma", response.toString());

                            // Application code
                            try {
                                socialLoginWebService(object,Constant.FACEBOOK);
                                /*Log.d("tttttt",object.getString("id"));
                                String birthday="";
                                if(object.has("birthday")){
                                    birthday = object.getString("birthday"); // 01/31/1980 format
                                }

                                String fnm = object.getString("first_name");
                                String lnm = object.getString("last_name");
                                String mail = object.getString("email");
                                String gender = object.getString("gender");
                                String fid = object.getString("id");
                                //tvdetails.setText(fnm+" "+lnm+" \n"+mail+" \n"+gender+" \n"+fid+" \n"+birthday);*/

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id, first_name, last_name, email, gender, birthday, location");
            request.setParameters(parameters);
            request.executeAsync();

        }

        @Override
        public void onCancel() {
            mProgressDialog.dismiss();
            System.out.println("onCancel");
        }

        @Override
        public void onError(FacebookException exception) {
            mProgressDialog.dismiss();
            System.out.println("onError");
            Log.v("LoginActivity", exception.getCause().toString());
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        facebookSDKInitialize();//Facebook initialize
        setContentView(R.layout.activity_login);

        //googleInitialize();//Google initialize function
        //AppController.googleInitialize(this,getString(R.string.gmail_web_client_id));

        String lastMessage = null;
        if(savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                lastMessage = null;
            } else {
                lastMessage = extras.getString("message");
            }
        }else{
            lastMessage = (String) savedInstanceState.getSerializable("message");
        }

        if(lastMessage != null){
            CommonUtils.showAlertMessage(LoginActivity.this,getString(R.string.success),getString(R.string.success),lastMessage,getString(R.string.ok));
        }

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
        layGoogleSignOut = (LinearLayout)findViewById(R.id.layGoogleLogout);

        //Button
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnReg = (Button)findViewById(R.id.btnReg);

        btnLogin.setOnClickListener(this);
        btnReg.setOnClickListener(this);
        txtForgot.setOnClickListener(this);
        layFacebook.setOnClickListener(this);
        layGoogle.setOnClickListener(this);
        layGoogleSignOut.setOnClickListener(this);

        mProgressDialog = CommonUtils.ProgressBar(this, "");

        //Facebook Login Events
        btnFbLogin = (LoginButton) findViewById(R.id.btnFbLogin);
        btnFbLogin.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
        getFbLoginDetails(btnFbLogin);
    }

    //FacebookDKInitialize function
    protected void facebookSDKInitialize() {
        //FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
    }
    protected void getFbLoginDetails(LoginButton fbLoginBtn){
        fbLoginBtn.registerCallback(callbackManager, callback);
    }

    @Override
    public void onClick(View view) {
        if(view==layGoogleSignOut){
            gmailSignOutClick();
        }else if(view==layGoogle){
            mProgressDialog.show();
            gmailSignInClick();
        }else if(view==layFacebook){
            mProgressDialog.show();
            btnFbLogin.performClick();
        }else if(view==btnLogin){
            //intentActivity = new Intent(this,HomeActivity.class);
            //startActivity(intentActivity);
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

            CommonUtils.clearErrorFromView(loginForm);
            CommonUtils.hideKeyboard(LoginActivity.this);
            if(InternetConnect.isConnected(LoginActivity.this)) {
                mProgressDialog.show();
                loginWebService();
                /*finish();
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));*/
            }else{
                CommonUtils.showAlertMessage(LoginActivity.this,getString(R.string.error),getString(R.string.net_connection_error_title),getString(R.string.net_connection_error_msg),getString(R.string.net_connection_error_btn));
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

    //Gmail Sign In Function
    private void gmailSignInClick () {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(AppController.mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        //Log.e("gopal sharma","Gmail login activity function");
    }
    private void gmailSignOutClick(){
        Auth.GoogleSignInApi.signOut(AppController.mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        if(status.isSuccess()){
                            layGoogle.setVisibility(View.VISIBLE);
                            layGoogleSignOut.setVisibility(View.GONE);
                        }

                        // [END_EXCLUDE]
                    }
                });
    }
    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        //Facebook Callback
        callbackManager.onActivityResult(requestCode, responseCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    //Gmail Result Handle
    private void handleSignInResult(GoogleSignInResult result){
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            try{
                GoogleSignInAccount acct = result.getSignInAccount();
                String gIdToken = acct.getIdToken();

                JSONObject gMailResult = new JSONObject();
                gMailResult.put("id", acct.getId());
                gMailResult.put("email", acct.getEmail());
                String fname = acct.getGivenName();
                if(fname == null){
                    gMailResult.put("first_name", JSONObject.NULL);
                }else{
                    gMailResult.put("first_name", fname);
                }
                String lname = acct.getFamilyName();
                if(fname == null){
                    gMailResult.put("last_name", JSONObject.NULL);
                }else{
                    gMailResult.put("last_name", lname);
                }
                socialLoginWebService(gMailResult,Constant.GMAIL);

                layGoogle.setVisibility(View.GONE);
                layGoogleSignOut.setVisibility(View.VISIBLE);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else{
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        //accessTokenTracker.stopTracking();
        //profileTracker.stopTracking();
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        //Log.e("code", ""+profile.toString());
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * this function use for call login webservice.
     * @params none;
     * @return none;
     */
    private void loginWebService(){
        apiInterface = APIClient.getInterface();
        Call<ServerResponse> call = apiInterface.login(strUsername,strPassword,CommonUtils.getDeviceToken(LoginActivity.this),Constant.DEVICE_NAME);
        //Call<ServerResponse> call = apiInterface.createUser();
        //Call<ServerResponse> call = apiInterface.createUserData();
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                try {
                    ServerResponse responseData = response.body();
                    //Log.e("Login WebService", responseData.getCode().toString() + " " + responseData.getUser().getUsername());
                    if (responseData.getCode().toString().equals("1")) {
                        SharedPrefrenceManager.getInstance(LoginActivity.this).setUserDetails(responseData.getUser());
                        AppController.getSessionData(getApplicationContext());
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();
                        mProgressDialog.dismiss();
                    } else {
                        mProgressDialog.dismiss();
                        CommonUtils.showAlertMessage(LoginActivity.this, getString(R.string.error), getString(R.string.error), responseData.getMessage(), getString(R.string.ok));
                    }
                }catch(Exception e){
                    CommonUtils.showAlertMessage(LoginActivity.this, getString(R.string.error), getString(R.string.error), e.getMessage(), getString(R.string.ok));
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
    /**
     * this function use for call login webservice.
     * @params none;
     * @return none;
     */
    private void socialLoginWebService(JSONObject object, final String socialType) throws JSONException {
        /*String birthday = "";
        if(object.has("birthday")){
            birthday = object.getString("birthday"); // 01/31/1980 format
        }*/
        Log.e("message Social Login",object.toString());
        String sFname = object.getString("first_name");
        String sLname = object.getString("last_name");
        String sEmail = object.getString("email");
        //String gender = object.getString("gender");
        String sId = object.getString("id");
        //Log.e("Social Login Para,s",fname+" "+lname+" "+sEmail+" "+sId);
        apiInterface = APIClient.getInterface();
        Call<ServerResponse> call = apiInterface.social_login(sFname,sLname,sEmail,sId,socialType,CommonUtils.getDeviceToken(LoginActivity.this),Constant.DEVICE_NAME);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse responseData = response.body();
                //Log.e("Social Gmail","Code :"+responseData.getCode().toString()+" ScialId :"+responseData.getSocialId().toString()+responseData.getUser().getFirstName()+responseData.getUser().getLastName());
                if(responseData.getCode().toString().equals("1")){
                    SharedPrefrenceManager.getInstance(LoginActivity.this).setUserDetails(responseData.getUser());
                    SharedPrefrenceManager.getInstance(LoginActivity.this).setSocialData(responseData.getSocialId().toString(), socialType);
                    AppController.getSessionData(getApplicationContext());
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    mProgressDialog.dismiss();
                    finish();
                }else{
                    mProgressDialog.dismiss();
                    CommonUtils.showAlertMessage(LoginActivity.this,getString(R.string.error),getString(R.string.error),responseData.getMessage(),getString(R.string.ok));
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                t.printStackTrace();
                mProgressDialog.dismiss();
                call.cancel();
            }
        });
    }
}