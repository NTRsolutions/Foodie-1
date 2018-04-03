package com.foodie;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.foodie.constant.Constant;
import com.foodie.fragments.CreateImageFragment;
import com.foodie.fragments.CreatePostLocationFragment;
import com.foodie.fragments.FoodItemListsFragment;
import com.foodie.utils.SharedPrefrenceManager;
import com.google.android.gms.auth.api.Auth;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap mImageBitmap;
    private String mCurrentPhotoPath;
    private ImageView mImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //mImageView = (ImageView)this.findViewById(R.id.imageView1);
        /*Button photoButton = (Button) this.findViewById(R.id.btnImage);
        photoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                //startActivityForResult(cameraIntent, CAMERA_REQUEST);

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        Log.i("asd", "IOException");
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            }
        });*/


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displaySelectedScreen(R.id.nav_create_post);
                *//*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*//*
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        TextView userName = (TextView)header.findViewById(R.id.loggedUserName);
        TextView userEmail = (TextView)header.findViewById(R.id.loggedUserEmail);
        //imgloggedUser = (ImageView)header.findViewById(R.id.imgHeaderUserImg);
        userName.setText(AppController.aSessionUserData.getUsername().toString());
        userEmail.setText(AppController.aSessionUserData.getEmail().toString());

        displaySelectedScreen(0);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );
        Log.e("Image Save DIR",image.getAbsolutePath());
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
                mImageView.setImageBitmap(mImageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            FragmentManager manager = getSupportFragmentManager();
            if(manager.getBackStackEntryCount() > 1) {
                super.onBackPressed();
                Fragment currentFragment = manager.findFragmentById(R.id.flContent);
                //HomeFragment currentFragment = (HomeFragment) manager.findFragmentById(R.id.flContent);
                //navigationView.getMenu().getItem(0).setChecked(true);
                setCheckedCurrentMenu(currentFragment);
            }else{
                finish();
            }
        }
    }
    /**
     * function use for checked current menu according to fragment change
     * @param //currentFragment
     */
    private void setCheckedCurrentMenu(Fragment currentFragment){
        if(currentFragment instanceof FoodItemListsFragment){
            navigationView.setCheckedItem(R.id.nav_post);//.getMenu().getItem(0).setChecked(true);
        } else if(currentFragment instanceof CreatePostLocationFragment){
            navigationView.setCheckedItem(R.id.nav_create_post);
        } /*else if(currentFragment instanceof ProfileFragment){
            navigationView.setCheckedItem(R.id.nav_profile);
        } else if(currentFragment instanceof EditProfileFragment){
            navigationView.setCheckedItem(R.id.nav_edit_profile);
        } else if(currentFragment instanceof ChangePasswordFragment){
            navigationView.setCheckedItem(R.id.nav_change_password);
        }*/
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * @use this function use to update fragment with current selected menu.
     * @param itemId
     */
    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;
        Log.e("Dashboard Menu item", String.valueOf(itemId));
        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_create_post:
                fragment = new CreateImageFragment();
                break;
            /*case R.id.nav_gallery:
                fragment = new CreatePostImageFragment();
                break;*/
            /*case R.id.nav_manage:
                fragment = new FoodTasteTypeFragment();
                break;*/
            /*case R.id.nav_slideshow:
                fragment = new FoodItemListsFragment();
                break;
            case R.id.nav_share:
                fragment = new AddFoodIngredientFragment();
                break;*/
            case R.id.nav_logout://logout current session
            {
                //Remove Social Login session
                if(SharedPrefrenceManager.getInstance(this).isSocialLogged()){
                    if(SharedPrefrenceManager.getInstance(this).getSocialType().equals(Constant.FACEBOOK)){
                        LoginManager.getInstance().logOut();
                    }else if(SharedPrefrenceManager.getInstance(this).getSocialType().equals(Constant.GMAIL)){
                        Auth.GoogleSignInApi.signOut(AppController.mGoogleApiClient);
                    }
                    SharedPrefrenceManager.getInstance(this).removeSocialData();
                }
                //Remove Main Session
                SharedPrefrenceManager.getInstance(this).removeSession();

                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
            }
            default:
                fragment = new FoodItemListsFragment();
                break;
        }

        if(fragment != null ){
            //replacing the fragment
            attachFragmentOnUi(fragment, fragment.toString(),itemId);
            setCheckedCurrentMenu(fragment);
        }
    }

    /**
     * @uses function use for attach select menu fragment
     * @param fragment
     * @param tag
     * @param navId
     */
    public void attachFragmentOnUi(Fragment fragment, String tag,int navId){
        if(fragment !=  null){
            Log.e("Navigation id",""+navId);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            if(navId==R.id.nav_post){
                Log.e("Stack ","null back stack ");
                fragmentTransaction.add(R.id.flContent, fragment);
                fragmentTransaction.addToBackStack(null);
            }else{
                fragmentTransaction.replace(R.id.flContent, fragment);
                fragmentTransaction.addToBackStack(tag);
            }

            fragmentTransaction.commit();
            navigationView.setCheckedItem(navId);
            //navigationView.getMenu().getItem(position).setChecked(true);
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
    }
    public void clearBackStackFragment(){
        FragmentManager frg = getSupportFragmentManager();
        for(int j =0;j<frg.getBackStackEntryCount();j++){
            frg.popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        displaySelectedScreen(item.getItemId());
        /*
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);*/
        return true;
    }


}
