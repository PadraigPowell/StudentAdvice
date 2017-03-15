package project.year.afinal.studentadvice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.util.Log;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";
    private EditText editTextName;
    private DatePicker date_pickerDOB;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editRetypeTextPassword;
    private FirebaseAuth firebaseAuth;
    private User user;
    private ProgressDialog mProgressDialog;
    private Firebase mRef = new Firebase("https://student-advice.firebaseio.com/");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();
    }
    @Override
    protected void onStart() {
        super.onStart();
        editTextName = (EditText) findViewById(R.id.editTextName);
        date_pickerDOB = (DatePicker) findViewById(R.id.date_pickerDOB);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editRetypeTextPassword = (EditText) findViewById(R.id.editRetypeTextPassword);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void onSignupClicked(View view){
        if (isTextValidateForSignup()){
            createUser();
            showProgressDialog();
        }
    }

    public void openLoginClicked(View view) {
        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        finish();
    }

    private void createUser(){
        //This method sets up a new User by fetching the user entered details.
        setUpUser();
        //This method  method  takes in an email address and password, validates them and then creates a new user
        // with the createUserWithEmailAndPassword method.
        // If the new account was created, the user is also signed in, and the AuthStateListener runs the onAuthStateChanged callback.
        // In the callback, you can use the getCurrentUser method to get the user's account data.

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        hideProgressDialog();

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            onAuthenticationSuccess(task.getResult().getUser());
                        }
                    }
                });
    }

    private void onAuthenticationSuccess(FirebaseUser mUser) {
        // Write new user
        saveNewUser(mUser.getUid(), user.getName(), user.getEmail(), user.getPassword());
        firebaseAuth.signOut();

        // Go to LoginActivity
        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        finish();
    }

    private void saveNewUser(String userId, String name, String email, String password) {
        User user = new User(userId,name,email,password);
        mRef.child("users").child(userId).setValue(user);
    }

    protected void setUpUser(){

        user = new User();
        user.setName(editTextName.getText().toString());
        user.setEmail(editTextEmail.getText().toString());
        user.setPassword(editTextPassword.getText().toString());
    }

    private Boolean isTextValidateForSignup() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String rePassword = editRetypeTextPassword.getText().toString().trim();

        Boolean isValid = true;

        //Regex ensures the name is just letters, spaces and some special characters such as O'néil
        String regexName = "^[\\p{L} .'-]+$";

        //Email validation to ensure email is input correctly
        String regexEmail = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        //Regex strings to validate password
        String hasUpperCase = "[A-Z]";
        String hasLowerCase = "[a-Z]";

        //Validate name
        if (TextUtils.isEmpty(name)) {
            editTextName.setError("Required.");
            isValid = false;
        }else if(name.length() > 40){
            editTextName.setError("40 Character limit.");
            isValid = false;
        }else if(!isRegexValid(name, regexName, true)) {
            editTextName.setError("Invalid Name");
            isValid = false;
        }

        //validate email
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Required.");
            isValid = false;
        }else if (email.length() > 40){
            editTextEmail.setError("40 character limit");
            isValid = false;
        }else if (!isRegexValid(email, regexEmail, true)){
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
        }else if (!isRegexValid(password, hasUpperCase, false)
                && !isRegexValid(password, hasLowerCase, false)){
            editTextPassword.setError("Requires 1 Upper and Lower Case Character");
            isValid = false;
        }

        //Validate that the password is re-entered correctly
        if (TextUtils.isEmpty(rePassword)) {
            editRetypeTextPassword.setError("Required.");
            isValid = false;
        }
        if(isValid) {
            if (password != rePassword) {
                Toast.makeText(SignupActivity.this, "Passwords don't Match",
                        Toast.LENGTH_SHORT).show();
                editRetypeTextPassword.setError("Password doesn't Match");
                isValid = false;
            }
        }
        return isValid;
    }

    @NonNull
    private Boolean isRegexValid(String check, String Regex, Boolean log){
        Pattern pattern = Pattern.compile(Regex);
        Matcher matcher = pattern.matcher(check);

        //This boolean is to allow only approved reg checks to be logged.
        //i.e not the password
        if (log) {
            Log.d(TAG,"isRegexValid(" + check.toString() + Regex.toString()
                    + ") Regex result = " + matcher.matches());
        }
        return matcher.matches();
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
