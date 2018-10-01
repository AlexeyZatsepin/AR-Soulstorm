package org.rooms.ar.soulstorm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.rooms.ar.soulstorm.model.SignInState;
import org.rooms.ar.soulstorm.view.DrawingView;

import java.io.IOException;

public class SignInActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener {
    private static final String TAG = SignInActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 100;

    private Camera mCamera;

    private LottieAnimationView mAnimationView;
    private TextView mExploreView;

    private ViewGroup mSceneRoot;

    //basic views
    private TextInputLayout mFirstInputLayout;
    private EditText mEmailEditText;
    private TextInputLayout mSecondInputLayout;
    private EditText mPasswordEditText;
    private Button mLoginButton;
    private Button mGoogleButton;
    private TextView mForgotPassword;
    private TextView mCreateAccountPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }

        setContentView(R.layout.activity_sign_in);

        TextureView mTextureView = findViewById(R.id.camera_preview);
        mTextureView.setSurfaceTextureListener(this);

        mAnimationView = findViewById(R.id.animation_view);
        mExploreView = findViewById(R.id.explore);
        mExploreView.animate()
                .setStartDelay(100)
                .setDuration(500)
                .scaleX(2)
                .scaleY(2)
                .withEndAction(() -> mExploreView.animate()
                        .setStartDelay(100)
                        .setDuration(500)
                        .scaleX(1)
                        .scaleY(1)
                        .start())
                .start();
        DrawingView drawingView = findViewById(R.id.drawingView);
        drawingView.addOnTouchListener( (view,event) -> {
            mAnimationView.setVisibility(View.GONE);
            mExploreView.setVisibility(View.GONE);
            drawingView.performClick();
            return true;
        });

        mSceneRoot = findViewById(R.id.scene_root);

        mFirstInputLayout = findViewById(R.id.first_input);
        mEmailEditText = findViewById(R.id.edit_email);
        mSecondInputLayout = findViewById(R.id.second_input);
        mPasswordEditText = findViewById(R.id.edit_password);
        mLoginButton = findViewById(R.id.login);
        mGoogleButton = findViewById(R.id.google);
        mForgotPassword = findViewById(R.id.forgot_password);
        mCreateAccountPassword = findViewById(R.id.create_account);

        mPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mLoginButton.setEnabled(s.length() > 0 && mEmailEditText.getText().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mLoginButton.setOnClickListener(this::login);
        mGoogleButton.setOnClickListener(this::loginWithGoogle);
        mForgotPassword.setOnClickListener(this::openForgotPasswordScreen);
        mCreateAccountPassword.setOnClickListener(this::openSignUpScreen);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        onLoginSuccess(currentUser);
    }

    private void login(View view) {
        mAuth.signInWithEmailAndPassword(mEmailEditText.getText().toString(), mPasswordEditText.getText().toString())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        onLoginSuccess(user);
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(getApplicationContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        onLoginSuccess(null);
                    }

                });
    }

    private void loginWithGoogle(View view) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

    }

    private void createAccount(View view) {
        mAuth.createUserWithEmailAndPassword(mEmailEditText.getText().toString(), mPasswordEditText.getText().toString())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        onLoginSuccess(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(SignInActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        onLoginSuccess(null);
                    }
                });
    }

    private void openLoginScreen(View view) {
        mSceneRoot.addView(mGoogleButton, 3);
        mSceneRoot.addView(mForgotPassword, 4);
        mCreateAccountPassword.setOnClickListener(this::openSignUpScreen);
        mLoginButton.setOnClickListener(this::login);
        mForgotPassword.setText(R.string.forgot_password);
    }

    private void openSignUpScreen(View view) {
        mSceneRoot.removeView(mGoogleButton);
        mSceneRoot.removeView(mForgotPassword);
        mCreateAccountPassword.setOnClickListener(this::openLoginScreen);
        mLoginButton.setOnClickListener(this::createAccount);
        mForgotPassword.setText(R.string.back_to_login);
    }

    private void openForgotPasswordScreen(View view) {

    }

    private void onLoginSuccess(FirebaseUser currentUser) {
        if (currentUser != null) {
            SignInState.getInstance().setUser(currentUser);
            startActivity(new Intent(this, ARActivity.class));
        }
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

    /**
     * Returns false and displays an error message if Sceneform can not run, true if Sceneform can run
     * on this device.
     * <p>
     * <p>Sceneform requires Android N on the device as well as OpenGL 3.0 capabilities.
     * <p>
     * <p>Finishes the activity if Sceneform can not run
     */
    @SuppressLint("ObsoleteSdkInt")
    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < BuildConfig.MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        return true;
    }
}
