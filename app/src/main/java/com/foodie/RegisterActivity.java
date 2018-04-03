package com.foodie;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.foodie.models.ServerResponse;
import com.foodie.networkUtils.InternetConnect;
import com.foodie.retrofit.APIClient;
import com.foodie.retrofit.APIInterface;
import com.foodie.utils.CommonUtils;
import com.foodie.utils.ValidationUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by WebPlanetDeveloper on 3/18/2018.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    APIInterface apiInterface;
    private EditText edtFName,edtLName,edtUsername,edtEmail,edtPassword,edtConfirmPassword;

    private String strFName,strLName,strUsername,strEmail,strPassword,strConfirmPassword;

    private ImageView btnBack;
    private Button btnSubmit;

    private ProgressDialog mProgressDialog;

    private ViewGroup regForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView txtReg = (TextView) findViewById(R.id.txtRegHeading);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/exo350db.ttf");
        txtReg.setTypeface(typeface);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        regForm = (ViewGroup) findViewById(R.id.regForm);

        mProgressDialog = CommonUtils.ProgressBar(this, "");
    }

    @Override
    public void onClick(View view) {
        CommonUtils.hideKeyboard(RegisterActivity.this);
        if(view==btnBack){
            finish();
        }else if(view==btnSubmit){
            registerAction();
        }
    }

    /**
     * this function use for validate registration form.
     * @return boolean true/false;
     */
    //Register Action Get All input fields value
    private void  registerAction(){
        edtFName = (EditText) findViewById(R.id.edtRegFirstName);
        edtLName = (EditText) findViewById(R.id.edtRegLastName);
        edtUsername = (EditText) findViewById(R.id.edtRegUsername);
        edtEmail = (EditText) findViewById(R.id.edtRegEmail);
        edtPassword = (EditText) findViewById(R.id.edtRegPassword);
        edtConfirmPassword = (EditText) findViewById(R.id.edtRegConfirmPassword);

        strFName = edtFName.getText().toString().trim();
        strLName = edtLName.getText().toString().trim();
        strUsername = edtUsername.getText().toString().trim();
        strEmail = edtEmail.getText().toString().trim();
        strPassword = edtPassword.getText().toString().trim();
        strConfirmPassword = edtConfirmPassword.getText().toString().trim();

        if(registerValidate()){
            CommonUtils.clearErrorFromView(regForm);
            if(InternetConnect.isConnected(RegisterActivity.this)) {
                mProgressDialog.show();
                registerWebService();
            }else{
                CommonUtils.showAlertMessage(RegisterActivity.this,getString(R.string.error),getString(R.string.net_connection_error_title),getString(R.string.net_connection_error_msg),getString(R.string.net_connection_error_btn));
                //CommonUtils.ShowToastMessages(getApplicationContext()," check network connection country "+strCountry);
            }
        }
    }
    /**
     * this function use for validate register form.
     * @params none;
     * @return boolean true/false;
     */
    private boolean registerValidate(){
        if(strFName.equals("")){
            CommonUtils.setErrorOnView(edtFName, getString(R.string.error_field_required));
        }else if(strLName.equals("")){
            CommonUtils.setErrorOnView(edtLName, getString(R.string.error_field_required));
        }else if(strUsername.equals("")){
            CommonUtils.setErrorOnView(edtUsername, getString(R.string.error_field_required));
        }else if(!ValidationUtils.isUsernameValid(strUsername)){
            CommonUtils.setErrorOnView(edtUsername,getString(R.string.error_invalid_username));
        }else if(strEmail.equals("")){
            CommonUtils.setErrorOnView(edtEmail, getString(R.string.error_field_required));
        }else if(!ValidationUtils.isEmailValid(strEmail)){
            CommonUtils.setErrorOnView(edtEmail, getString(R.string.error_invalid_email));
        }else if(strPassword.equals("")){
            CommonUtils.setErrorOnView(edtPassword, getString(R.string.error_field_required));
        }else if(strConfirmPassword.equals("")){
            CommonUtils.setErrorOnView(edtConfirmPassword, getString(R.string.error_field_required));
        }else if(!strConfirmPassword.equals(strPassword)){
            CommonUtils.setErrorOnView(edtConfirmPassword, getString(R.string.error_match_password));
        }else{
            return true;
        }
        return false;
    }

    /**
     * this function use for call register webservice.
     * @params none;
     * @return none;
     */
    private void registerWebService(){
        apiInterface = APIClient.getInterface();

        Call<ServerResponse> call = apiInterface.register(strFName,strLName,strUsername,strEmail,strPassword);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                mProgressDialog.dismiss();
                //Log.e("Register Web Service "," in success");
                ServerResponse responseData = response.body();
                if(responseData.getCode().toString().equals("1")){
                    //CommonUtils.clearForm(regForm);
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("message",responseData.getMessage().toString());
                    finish();
                    startActivity(intent);
                }else{
                    CommonUtils.showAlertMessage(RegisterActivity.this,getString(R.string.error),getString(R.string.error),responseData.getMessage(),getString(R.string.ok));
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
}

