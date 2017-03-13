package project.year.afinal.studentadvice;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;
import com.google.android.gms.tasks.Task;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.Firebase;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A login screen that offers login via email/password or facebook.
 */
public class LoginActivity extends AppCompatActivity{

    private static final String TAG = "LoginActivity";
    private Firebase mRef = new Firebase("https://student-advice.firebaseio.com/");
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    public User user;

    //facebook callback manager
    private CallbackManager callbackManager;

    // UI references.
    private LoginButton buttonLogin;
    private LoginButton facebookLogin;
    private TextView textViewSignup;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private ProgressDialog mProgressDialog;
    private ImageView profilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            // User is signed in
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            String uid = mAuth.getCurrentUser().getUid();
            String image=mAuth.getCurrentUser().getPhotoUrl().toString();
            intent.putExtra("user_id", uid);
            if(image!=null || image!=""){
                intent.putExtra("profile_picture",image);
            }
            startActivity(intent);
            finish();
            Log.d(TAG, "onAuthStateChanged:signed_in:" + mUser.getUid());
        }

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mUser = firebaseAuth.getCurrentUser();
                if (mUser != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + mUser.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        };

        //Facebook
        callbackManager = CallbackManager.Factory.create();
        facebookLogin = (LoginButton) findViewById(R.id.facebookLogin);
        facebookLogin.setReadPermissions("email", "public_profile");
        facebookLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                signInWithFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel(){
                Log.d(TAG, "facebook:onCancel:");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError:" + error);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //UI references
        buttonLogin = (LoginButton) findViewById(R.id.buttonLogin);
        facebookLogin = (LoginButton) findViewById(R.id.facebookLogin);
        textViewSignup = (TextView) findViewById(R.id.textViewSignup);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    //FaceBook
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    //This method sets up a new User by fetching the user entered details.
    protected void setUpUser() {
        user = new User();
        user.setEmail(editTextEmail.getText().toString());
        user.setPassword(editTextPassword.getText().toString());
    }

    public void onSignUpClicked(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    public void onLoginClicked(View view) {
        setUpUser();
        login(editTextEmail.getText().toString().trim(),
                editTextPassword.getText().toString().trim());
    }

    private void login(String email, String password) {
        Log.d(TAG, "Login:" + email);
        if (!isTextValidateForLogin()) {
            return;
        }
        showProgressDialog();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            String uid = mAuth.getCurrentUser().getUid();
                            intent.putExtra("user_id", uid);
                            startActivity(intent);
                            finish();
                        }
                        hideProgressDialog();
                    }
                });
    }

    private Boolean isTextValidateForLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        Boolean isValid = true;

        //Email validation to ensure email is input correctly
        String regexEmail = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        //validate email
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Required.");
            isValid = false;
        }else if (email.length() > 40){
            editTextEmail.setError("40 character limit");
            isValid = false;
        }else if (!isRegexValid(email, regexEmail)){
            editTextEmail.setError("Invalid Email.");
            isValid = false;
        }

        //Validate Password
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Required.");
            isValid = false;
        }else if (password.length() < 6){
            editTextPassword.setError("6 or more characters.");
            isValid = false;
        }else if(password.length() > 40){
            editTextPassword.setError("40 character limit");
            isValid = false;
        }
        return isValid;
    }

    @NonNull
    private Boolean isRegexValid(String check, String Regex){
        Pattern pattern = Pattern.compile(Regex);
        Matcher matcher = pattern.matcher(check);
        Log.d(TAG,"isRegexValid(" + check.toString() + Regex.toString()
                + ") Regex result = " + matcher.matches());
        return matcher.matches();
    }

    private void signInWithFacebook(AccessToken token) {
        Log.d(TAG, "signInWithFacebook:" + token);

        showProgressDialog();

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            String uid=task.getResult().getUser().getUid();
                            String name=task.getResult().getUser().getDisplayName();
                            String email=task.getResult().getUser().getEmail();
                            String image=task.getResult().getUser().getPhotoUrl().toString();

                            //Create a new User and Save it in Firebase database
                            User user = new User(uid,name,email,null,null);

                            mRef.child(uid).setValue(user);

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("user_id",uid);
                            intent.putExtra("profile_picture",image);
                            startActivity(intent);
                            finish();
                        }
                        hideProgressDialog();
                    }
                });
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}

