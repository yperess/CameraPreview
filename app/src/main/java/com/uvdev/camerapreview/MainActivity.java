package com.uvdev.camerapreview;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    private static final int RC_ACCESS_CAMERA = 0;

    private CameraPreview mCameraPreview;

    private Camera mCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide the window title.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA },
                    RC_ACCESS_CAMERA);
        } else {
            startCameraPreview();
        }
    }

    @Override
    protected void onPause() {
        if (mCamera != null) {
            mCameraPreview.setCamera(null);
            mCamera.release();
            mCamera = null;
        }
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RC_ACCESS_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    startCameraPreview();
                } else {
                    finish();
                }
                return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startCameraPreview() {
        if (mCameraPreview == null) {
            mCameraPreview = new CameraPreview(this);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mCameraPreview.setLayoutParams(layoutParams);
            ((ViewGroup) findViewById(R.id.activity_main)).addView(mCameraPreview);
        }
        mCamera = Camera.open();
        mCameraPreview.setCamera(mCamera);
    }
}
