package com.foodie.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.foodie.R;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Developer on 9/12/2017.
 */

public class CommonUtils {



    public static void showAlertMessage(final Context mCtx,String strType,String strTitle,String strMsg,String strBtn){
        AlertDialog alertDialog = new AlertDialog.Builder(mCtx).create();

        // Setting Dialog Title
        //alertDialog.setTitle(strTitle);
        TextView title =  new TextView(mCtx);
        title.setText(strTitle);
        //title.setGravity(Gravity.CENTER);
        title.setTextSize(20);
        title.setPadding(20,20,0,20);
        //title.setBackgroundColor(Color.GRAY);
        title.setTextColor(Color.BLACK);
        alertDialog.setCustomTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(strMsg);
        // Setting Icon to Dialog
        if(strType.trim().toLowerCase().equals("error")){
            //alertDialog.setIcon(R.drawable.ic_cross);
        }else{
            alertDialog.setIcon(R.drawable.ic_tick);
        }
        // Setting OK Button
        alertDialog.setButton(strBtn.toString().trim().toUpperCase(), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog,int which)
            {
                // Write your code here to execute after dialog closed
                //Toast.makeText(mCtx,"You clicked on OK", Toast.LENGTH_SHORT).show();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
    /**
     * Show input error message at that point
     * @param mEdt : input field
     * @param MsgStr : message string which we want to show
     */
    public static void setErrorOnView(EditText mEdt, String MsgStr){
        mEdt.setError(MsgStr);
        mEdt.requestFocus();
    }

    public static void removeErrorOnView(EditText mEdt){
        mEdt.setError(null);
        mEdt.clearFocus();
    }
    public static void clearErrorFromView(ViewGroup group){
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText)view).setError(null);
                ((EditText)view).clearFocus();
            }
            if(view instanceof ViewGroup && (((ViewGroup)view).getChildCount() > 0)) {
                //clearForm((ViewGroup) view);
            }
        }
    }

    /**
     * progress dialog box
     * @param mCtx
     * @param MsgStr
     */
    public static ProgressDialog ProgressBar(Context mCtx, String MsgStr){
        ProgressDialog progressBar = new ProgressDialog(mCtx);
        progressBar.setMessage("Loading....");
        progressBar.setCancelable(false);
        return progressBar;
    }
    /**
     * Show all type message which comes after execution
     * @param mCtx
     * @param MsgStr
     */
    public static void ShowToastMessages(Context mCtx, String MsgStr){
        Toast.makeText(mCtx, MsgStr, Toast.LENGTH_SHORT).show();
    }
    /**
     * Get device token
     * @param mCtx
     * @return String
     */
    public static String getDeviceToken(Context mCtx){
        return Settings.Secure.getString(mCtx.getContentResolver(),Settings.Secure.ANDROID_ID);
    }
    /**
     * Get DeviceName
     * @return string
     */
    public static String getDeviceName() {


        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        String product = Build.PRODUCT;
        String DeviceName = Build.DEVICE;
        return DeviceName+" "+manufacturer+" "+model+" "+product;
        /*
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }*/
    }
    /**
     * Hide open keybord
     * @param activity
     */
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    /**
     * Clear form data after use
     * @param group
     */
    public static void clearForm(ViewGroup group)
    {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText)view).getText().clear();
            }
            if (view instanceof Spinner) {
                ((Spinner) view).setSelection(0);
            }
            if(view instanceof ViewGroup && (((ViewGroup)view).getChildCount() > 0)) {
                clearForm((ViewGroup) view);
            }
        }
    }
    /**
     * Get Device window height and width
     */
    public static int getScreenWidth(Context mCtx)
    {
        //return Resources.getSystem().getDisplayMetrics().widthPixels;
        int columnWidth;
        Context _context = mCtx;
        WindowManager wm = (WindowManager) _context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();
        try {
            display.getSize(point);
        } catch (NoSuchMethodError ignore) { // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        columnWidth = point.x;
        return columnWidth;
    }
    public static int getScreenHeight()
    {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    /**
     * @use get facebook hash key
     * @param context
     * @return string(Hash Key)
     */
    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        }
        catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

    /**
     * Check Device has Camera or not.
     * @param context
     * @return
     */
    private static boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    /** Create a file Uri for saving an image or video */
    public static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    public static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
}
