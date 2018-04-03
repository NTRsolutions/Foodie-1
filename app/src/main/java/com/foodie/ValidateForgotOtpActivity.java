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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by WebPlanetDeveloper on 3/18/2018.
 */

public class ValidateForgotOtpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtValidateOtp;
    private String strValidateOtp;
    private Button btnValidateOtpSubmit;

    private ViewGroup validateOtpForm;
    private  ImageView btnBack;

    private APIInterface apiInterface;
    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_otp);

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
            CommonUtils.showAlertMessage(ValidateForgotOtpActivity.this,getString(R.string.success),getString(R.string.success),lastMessage,getString(R.string.ok));
        }

        TextView txtValidate = (TextView) findViewById(R.id.txtValidateOtpHeading);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/exo350db.ttf");
        txtValidate.setTypeface(typeface);

        TextView txtValidateOtpSub = (TextView) findViewById(R.id.txtValidateOtpSub);
        TextView txtValidateOtpSub1 = (TextView) findViewById(R.id.txtValidateOtpSub1);
        txtValidateOtpSub.setTypeface(typeface);
        txtValidateOtpSub1.setTypeface(typeface);

        btnValidateOtpSubmit = (Button) findViewById(R.id.btnValidateOtpSubmit);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        btnValidateOtpSubmit.setOnClickListener(this);

        validateOtpForm = (ViewGroup) findViewById(R.id.validateOtpForm);
        mProgressDialog = CommonUtils.ProgressBar(this, "");
    }

    @Override
    public void onClick(View view) {
        CommonUtils.hideKeyboard(ValidateForgotOtpActivity.this);
        if(view==btnBack){
            finish();
        }else if(view==btnValidateOtpSubmit){
            validateOtpAction();
        }
    }

    //Validate OTP  Action Get All input fields value
    private void  validateOtpAction(){
        edtValidateOtp = (EditText) findViewById(R.id.edtValidateOtp);

        strValidateOtp = edtValidateOtp.getText().toString().trim();
        if(validateOtpValidate()){
            CommonUtils.clearErrorFromView(validateOtpForm);
            if(InternetConnect.isConnected(ValidateForgotOtpActivity.this)) {
                mProgressDialog.show();
                validateOtpWebService();
            }else{
                CommonUtils.showAlertMessage(ValidateForgotOtpActivity.this,getString(R.string.error),getString(R.string.net_connection_error_title),getString(R.string.net_connection_error_msg),getString(R.string.net_connection_error_btn));
            }
        }
    }
    /**
     * this function use for validate register form.
     * @params none;
     * @return boolean true/false;
     */
    private boolean validateOtpValidate(){
        if(strValidateOtp.equals("")){
            CommonUtils.setErrorOnView(edtValidateOtp, getString(R.string.error_field_required));
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
    private void validateOtpWebService(){
        apiInterface = APIClient.getInterface();
        String strEmail = SharedPrefrenceManager.getInstance(ValidateForgotOtpActivity.this).getForgotEmail();
        Call<ServerResponse> call = apiInterface.validate_forgot_otp(strEmail,strValidateOtp);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                mProgressDialog.dismiss();
                ServerResponse responseData = response.body();
                if(responseData.getCode().toString().equals("1")){
                    Intent intent = new Intent(ValidateForgotOtpActivity.this, ResetPasswordActivity.class);
                    intent.putExtra("message",responseData.getMessage().toString());
                    finish();
                    startActivity(intent);
                }else{
                    CommonUtils.showAlertMessage(ValidateForgotOtpActivity.this,getString(R.string.error),getString(R.string.error),responseData.getMessage(),getString(R.string.ok));
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

