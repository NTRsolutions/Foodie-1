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
import com.foodie.utils.SharedPrefrenceManager;
import com.foodie.utils.ValidationUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by WebPlanetDeveloper on 3/18/2018.
 */

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtForgotEmail;
    private String strForgotEmail;
    private Button btnForgotSubmit;

    private ViewGroup forgotForm;
    private  ImageView btnBack;

    private APIInterface apiInterface;
    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        TextView txtForgot = (TextView) findViewById(R.id.txtForgotHeading);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/exo350db.ttf");
        txtForgot.setTypeface(typeface);

        TextView txtForgotSub1 = (TextView) findViewById(R.id.txtForgotSub1);
        TextView txtForgotSub2 = (TextView) findViewById(R.id.txtForgotSub2);
        txtForgotSub1.setTypeface(typeface);
        txtForgotSub2.setTypeface(typeface);

        btnForgotSubmit = (Button) findViewById(R.id.btnForgotSubmit);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        btnForgotSubmit.setOnClickListener(this);
        forgotForm = (ViewGroup) findViewById(R.id.forgotForm);
        mProgressDialog = CommonUtils.ProgressBar(this, "");
    }

    @Override
    public void onClick(View view) {
        CommonUtils.hideKeyboard(ForgotPasswordActivity.this);
        if(view==btnBack){
            finish();
        }else if(view==btnForgotSubmit){
            forgotAction();
        }
    }

    //Register Action Get All input fields value
    private void  forgotAction(){
        edtForgotEmail = (EditText) findViewById(R.id.edtForgotEmail);

        strForgotEmail = edtForgotEmail.getText().toString().trim();
        if(forgotValidate()){
            //CommonUtils.clearErrorFromView(forgotForm);
            if(InternetConnect.isConnected(ForgotPasswordActivity.this)) {
                mProgressDialog.show();
                forgotWebService();
            }else{
                CommonUtils.showAlertMessage(ForgotPasswordActivity.this,getString(R.string.error),getString(R.string.net_connection_error_title),getString(R.string.net_connection_error_msg),getString(R.string.net_connection_error_btn));
            }
        }
    }
    /**
     * this function use for validate register form.
     * @params none;
     * @return boolean true/false;
     */
    private boolean forgotValidate(){
        if(strForgotEmail.equals("")){
            CommonUtils.setErrorOnView(edtForgotEmail, getString(R.string.error_field_required));
        }else if(!ValidationUtils.isEmailValid(strForgotEmail)){
            CommonUtils.setErrorOnView(edtForgotEmail, getString(R.string.error_invalid_email));
        }else{
            return true;
        }
        return false;
    }

    /**
     * this function use for call forgot Password webservice.
     * @params none;
     * @return none;
     */
    private void forgotWebService(){
        apiInterface = APIClient.getInterface();
        Call<ServerResponse> call = apiInterface.forgot_password(strForgotEmail);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                mProgressDialog.dismiss();

                ServerResponse responseData = response.body();

                if(responseData.getCode().toString().equals("1")){
                    SharedPrefrenceManager.getInstance(ForgotPasswordActivity.this).setForgotEmail(strForgotEmail);
                    Intent intent = new Intent(ForgotPasswordActivity.this, ValidateForgotOtpActivity.class);
                    intent.putExtra("message",responseData.getMessage().toString());
                    finish();
                    startActivity(intent);
                    //startActivity(new Intent(ForgotPasswordActivity.this, ValidateForgotOtpActivity.class));
                }else{
                    CommonUtils.showAlertMessage(ForgotPasswordActivity.this,getString(R.string.error),getString(R.string.error),responseData.getMessage(),getString(R.string.ok));
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

