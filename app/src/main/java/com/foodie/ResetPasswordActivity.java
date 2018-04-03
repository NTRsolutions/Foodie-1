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

public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtResetPassword,edtResetConfirmPassword;
    private String strResetPassword,strResetConfirmPassword;
    private Button btnResetSubmit;

    private ViewGroup resetForm;
    private  ImageView btnBack;

    private APIInterface apiInterface;
    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);

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
            CommonUtils.showAlertMessage(ResetPasswordActivity.this,getString(R.string.success),getString(R.string.success),lastMessage,getString(R.string.ok));
        }

        TextView txtResetHeading = (TextView) findViewById(R.id.txtResetHeading);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/exo350db.ttf");
        txtResetHeading.setTypeface(typeface);

        TextView txtResetSub = (TextView) findViewById(R.id.txtResetSub);
        txtResetSub.setTypeface(typeface);

        btnResetSubmit = (Button) findViewById(R.id.btnResetPassSubmit);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        btnResetSubmit.setOnClickListener(this);

        resetForm = (ViewGroup) findViewById(R.id.resetForm);
        mProgressDialog = CommonUtils.ProgressBar(this, "");
    }

    @Override
    public void onClick(View view) {
        CommonUtils.hideKeyboard(ResetPasswordActivity.this);
        if(view==btnBack){
            finish();
        }else if(view==btnResetSubmit){
            resetPassAction();
        }
    }

    //Reset Password Action Get All input fields value
    private void  resetPassAction(){
        edtResetPassword = (EditText) findViewById(R.id.edtResetPassword);
        edtResetConfirmPassword = (EditText) findViewById(R.id.edtResetConfirmPassword);

        strResetPassword = edtResetPassword.getText().toString().trim();
        strResetConfirmPassword = edtResetConfirmPassword.getText().toString().trim();
        if(resetValidate()){
            CommonUtils.clearErrorFromView(resetForm);
            if(InternetConnect.isConnected(ResetPasswordActivity.this)) {
                mProgressDialog.show();
                resetWebService();
            }else{
                CommonUtils.showAlertMessage(ResetPasswordActivity.this,getString(R.string.error),getString(R.string.net_connection_error_title),getString(R.string.net_connection_error_msg),getString(R.string.net_connection_error_btn));
            }
        }
    }
    /**
     * this function use for validate register form.
     * @params none;
     * @return boolean true/false;
     */
    private boolean resetValidate(){
        if(strResetPassword.equals("")){
            CommonUtils.setErrorOnView(edtResetPassword, getString(R.string.error_field_required));
        }else if(strResetConfirmPassword.equals("")){
            CommonUtils.setErrorOnView(edtResetConfirmPassword, getString(R.string.error_field_required));
        }else if(!strResetPassword.equals(strResetConfirmPassword)){
            CommonUtils.setErrorOnView(edtResetConfirmPassword, getString(R.string.error_match_password));
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
    private void resetWebService(){
        apiInterface = APIClient.getInterface();
        String strEmail = SharedPrefrenceManager.getInstance(ResetPasswordActivity.this).getForgotEmail();
        Call<ServerResponse> call = apiInterface.reset_password(strEmail,strResetPassword);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                mProgressDialog.dismiss();
                ServerResponse responseData = response.body();
                if(responseData.getCode().toString().equals("1")){
                    SharedPrefrenceManager.getInstance(ResetPasswordActivity.this).deleteForgotEmail();

                    Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("message",responseData.getMessage().toString());
                    finish();
                    startActivity(intent);
                }else{
                    CommonUtils.showAlertMessage(ResetPasswordActivity.this,getString(R.string.error),getString(R.string.error),responseData.getMessage(),getString(R.string.ok));
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

