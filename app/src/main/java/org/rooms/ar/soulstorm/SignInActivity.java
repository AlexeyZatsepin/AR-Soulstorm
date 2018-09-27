package org.rooms.ar.soulstorm;

import android.content.res.Configuration;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.transition.Scene;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;

public class SignInActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener {
    private Camera mCamera;

    private Scene mSceneLogin;
    private Scene mSceneRegistration;
    private Scene mScenePasswordReset;
    private ViewGroup mSceneRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        TextureView mTextureView = findViewById(R.id.camera_preview);
        mTextureView.setSurfaceTextureListener(this);

        mSceneRoot = findViewById(R.id.scene_root);

        findViewById(R.id.create_account).setOnClickListener(v -> TransitionManager.go(mSceneRegistration));

        mSceneLogin = Scene.getSceneForLayout(mSceneRoot, R.layout.scene_login, getApplicationContext());
        mSceneRegistration = Scene.getSceneForLayout(mSceneRoot, R.layout.scene_registration, getApplicationContext());
        mScenePasswordReset = Scene.getSceneForLayout(mSceneRoot, R.layout.scene_forgot_password, getApplicationContext());
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mCamera = Camera.open();
        try {
            Camera.Parameters params = mCamera.getParameters();
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            mCamera.setParameters(params);

            mCamera.setDisplayOrientation(90);
            mCamera.setPreviewTexture(surface);
            mCamera.startPreview();
        } catch (IOException ioe) {
            // Something bad happened
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            mCamera.setDisplayOrientation(90);
        } else {
            mCamera.setDisplayOrientation(0);
        }
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mCamera.stopPreview();
        mCamera.release();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }
}
