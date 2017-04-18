package project.year.afinal.studentadvice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.firebase.client.ValueEventListener;
import com.firebase.client.DataSnapshot;
import android.widget.TextView;
import android.widget.ImageView;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A login screen that offers login via email/password or facebook.
 */
public class LoginActivity extends AppCompatActivity{

    private static final String TAG = "LoginActivity";
    private Firebase mRef;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;

    //facebook callback manager
    private CallbackManager callbackManager;

    // UI references.
    private LoginButton facebookLogin;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private ProgressDialog mProgressDialog;

    //make this public so it can be passed in as an intent to the main activity
    public String nameGlobal;

    public String uid;
    public String name;
    public String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d(TAG, "Login Activity onCreate");
        mRef = new Firebase("https://student-advice.firebaseio.com");

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            Log.d(TAG, "onAuthStateChanged:signed_in:" + mUser.getUid());

            // User is signed in
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            String uid = mAuth.getCurrentUser().getUid();
            String name = mAuth.getCurrentUser().getDisplayName();
            String email = mAuth.getCurrentUser().getEmail();

            //if the name is nu
            if(name != null){
                String image=mAuth.getCurrentUser().getPhotoUrl().toString();
                intent.putExtra("profile_picture",image);
            }else{
                name = getNameFB(uid);
            }
            intent.putExtra("user_id", uid);
            intent.putExtra("user_name", name);
            intent.putExtra("user_email", email);
            startActivity(intent);
            finish();
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
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        mAuth.addAuthStateListener(mAuthListener);
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

    public void onSignUpClicked(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    public void onLoginClicked(View view) {
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
                            String uid=task.getResult().getUser().getUid();
                            String name=task.getResult().getUser().getDisplayName();
                            String email=task.getResult().getUser().getEmail();

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                            if (name==null)
                                name = getNameFB(uid);

                            intent.putExtra("user_id",uid);
                            intent.putExtra("user_name",name);
                            intent.putExtra("user_email",email);
                            startActivity(intent);
                            finish();
                        }
                        hideProgressDialog();
                    }
                });
    }

    private String getNameFB(String uid)
    {
        //Referring to the name of the User who has logged in currently and adding a valueChangeListener
        mRef.child("users/" + uid).child("name").addValueEventListener(new ValueEventListener() {
            //onDataChange is called every time the name of the User changes in your Firebase Database
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.getValue(String.class);
                nameGlobal = userName;
            }

            //onCancelled is called in case of any error
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(getApplicationContext(), "" + firebaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        return nameGlobal;
    }

    private Boolean isTextValidateForLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        Boolean isValid = true;

        //Email validation to ensure email is input correctly
        String regexEmail = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        //validate email
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Required.");
            isValid = false;
        }else if (email.length() > 40){
            editTextEmail.setError("40 character limit:" + email.length());
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
            editTextPassword.setError("6 or more characters:" + password.length());
            isValid = false;
        }else if(password.length() > 40){
            editTextPassword.setError("40 character limit:" + password.length());
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
                            uid=task.getResult().getUser().getUid();
                            name=task.getResult().getUser().getDisplayName();
                            email=task.getResult().getUser().getEmail();
                            String image=task.getResult().getUser().getPhotoUrl().toString();

                            //check if user exists on the database, if not add them
                            addUserFB();

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("user_id",uid);
                            intent.putExtra("user_name",name);
                            intent.putExtra("user_email",email);
                            intent.putExtra("profile_picture",image);
                            startActivity(intent);
                            finish();
                        }
                        hideProgressDialog();
                    }
                });
    }

    private void addUserFB()
    {
        mRef.child("users/"+uid+"/name").addListenerForSingleValueEvent(new ValueEventListener() {
            //onDataChange is called every time the name of the User changes in your Firebase Database
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String tmpname = dataSnapshot.getValue(String.class);

                if (tmpname != null)
                    return;

                //Create a new User and Save it in Firebase database
                User user = new User(name,email);

                Firebase userRef = mRef.child("users/" + uid);
                userRef.setValue(user, new Firebase.CompletionListener() {
                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                        if (firebaseError != null){
                            Log.e(TAG, "Firebase add to db failed" + firebaseError.getMessage());
                        }
                    }
                });
            }
            //onCancelled is called in case of any error
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d(TAG, "Firebase error: " + firebaseError.getMessage());
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

