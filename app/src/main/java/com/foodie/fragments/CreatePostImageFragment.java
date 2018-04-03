package com.foodie.fragments;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.foodie.R;
import com.foodie.utils.CameraPreview;
import com.foodie.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

/**
 * Created by WebPlanetDeveloper on 4/3/2018.
 */

public class CreatePostImageFragment extends Fragment {

    private Camera mCamera;
    private CameraPreview mPreview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);

        // Create an instance of Camera
        mCamera = CommonUtils.getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(getActivity(), mCamera);
        FrameLayout preview = (FrameLayout) view.findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        Button captureButton = (Button) view.findViewById(R.id.button_capture);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        mCamera.takePicture(null, null, mPicture);
                    }
                }
        );

        return view;
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            try {
                File pictureFile = CommonUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
                Log.e("Picture Name",pictureFile.toString()+" Gopal Sharma ");
                if (pictureFile == null){
                    //Log.d("asd", "Error creating media file, check storage permissions: " +e.getMessage());
                    return;
                }
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d("asd", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("asd", "Error accessing file: " + e.getMessage());
            }
        }
    };
}
