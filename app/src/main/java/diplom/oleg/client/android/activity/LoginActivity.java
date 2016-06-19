package diplom.oleg.client.android.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.AsyncTask;


import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import diplom.oleg.client.android.R;
import diplom.oleg.client.android.RestClient;
import diplom.oleg.client.android.model.User;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

    private static final String TAG = "LoginActivity";
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private View mSettingsButton;
    private View mSettingsView;
    private EditText mServerIPView;
    //Firebase myFirebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Firebase.setAndroidContext(this);
        //myFirebaseRef = new Firebase("https://dazzling-torch-5301.firebaseio.com/");

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mSettingsView = findViewById(R.id.settings_layout);
        mServerIPView = (EditText) findViewById(R.id.serverIPEditText);

        mSettingsButton = findViewById(R.id.settingsButton);
        mSettingsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int visibility = mSettingsView.getVisibility();
                if (mSettingsView.getVisibility() == View.VISIBLE){
                    mSettingsView.setVisibility(View.INVISIBLE);
                }else{
                    mSettingsView.setVisibility(View.VISIBLE);
                }
            }
        });

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        SharedPreferences prefs = getSharedPreferences("com.oleg.diplom", MODE_PRIVATE);
        if (prefs.contains("email")){
            mEmailView.setText(prefs.getString("email", ""));
        }
        if (prefs.contains("password")){
            mPasswordView.setText(prefs.getString("password", ""));
        }
        if (prefs.contains("serverIP")){
            mServerIPView.setText(prefs.getString("serverIP", ""));
        }

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();
        final String ip = mServerIPView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password, ip);
            mAuthTask.execute();
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return true;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return true;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {

        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });

    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, User> {

        private final String mEmail;
        private final String mPassword;
        private final String mIP;

        UserLoginTask(String email, String password, String IP) {
            mEmail = email;
            mPassword = password;
            mIP = IP;
        }

        @Override
        protected User doInBackground(Void... params) {
            try {
                RestClient.init(mEmail, mPassword);
                User user = RestClient.login();
                SharedPreferences prefs = getSharedPreferences("com.oleg.diplom", MODE_PRIVATE);
                prefs.edit().putString("email", mEmail).apply();
                prefs.edit().putString("password", mPassword).apply();
                prefs.edit().putString("serverIP", mIP).apply();
                return user;
            } catch (IOException e) {
                Log.e(TAG, "doInBackground: ", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(final User user) {
            mAuthTask = null;
            showProgress(false);

            if (user != null) {
                startActivity(new Intent(LoginActivity.this, FirstScreenActivity.class));
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

