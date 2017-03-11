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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.firebase.client.Firebase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.util.Log;

public class SignupActivity extends AppCompatActivity {

    private Button buttonSignup;
    private TextView textViewLogin;
    private EditText editTextName;
    private DatePicker date_pickerDOB;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editRetypeTextPassword;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private User user;
    private Firebase ref = new Firebase("https://student-advice.firebaseio.com/");


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
        buttonSignup = (Button) findViewById(R.id.buttonSignup);
        textViewLogin = (TextView) findViewById(R.id.textViewLogin);
        progressDialog = new ProgressDialog(this);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void onSignupClicked(View view){
        if (isTextValidateForSignup()){
            //login
        }
    }

    public void facebookSignupClicked(View view){
        //get facebook data on success
    }

    protected void setUpUser(){
        user = new User();
        user.setName(editTextName.getText().toString());
        user.setEmail(editTextEmail.getText().toString());
        user.setPassword(editTextPassword.getText().toString());
        user.setDOB(date_pickerDOB.getYear(),
                date_pickerDOB.getMonth(),
                date_pickerDOB.getDayOfMonth());
    }

    private Boolean isTextValidateForSignup() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String rePassword = editRetypeTextPassword.getText().toString().trim();

        Boolean error = false;

        /*
        Regex ensures the name is just letters, spaces and some special characters
        such as O'néil and
         */
        String regexName = "^[\\p{L} .'-]+$";

        //Email validation to ensure email is input correctly
        String regexEmail = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";;

        //Validate name
        if (TextUtils.isEmpty(name)) {
            editTextName.setError("Required.");
            error = true;
        }else if(!isRegexValid(name, regexName)){
            editTextName.setError("Invalid Name");
            error = true;
        }

        //validate email
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Required.");
            error = true;
        }else if (!isRegexValid(email, regexEmail)){
            editTextEmail.setError("Invalid Email.");
            error = true;
        }

        //Validate Password
        if (TextUtils.isEmpty(password)) {
            editTextEmail.setError("Required.");
            error = true;
        }
        if (TextUtils.isEmpty(rePassword)) {
            editRetypeTextPassword.setError("Required.");
            error = true;
        }

        return error;
    }

    private Boolean isRegexValid(String check, String Regex){
        Pattern pattern = Pattern.compile(Regex);
        Matcher matcher = pattern.matcher(check);
        Log.d("isRegexValid(" + check.toString() + Regex.toString() + ")",
                "Regex result = " + matcher.matches());
        return matcher.matches();
    }

    public void onClick(View view) {
        if (view == buttonSignup) {
            //set data entered // TODO: 10/03/2017
            createUser("", "");
        }

        if (view == textViewLogin) {
            //open Signup fragment
            Intent myIntent = new Intent(SignupActivity.this, LoginActivity.class);
            SignupActivity.this.startActivity(myIntent);
        }

        //if validation is OK
        //Display progress dialog
        progressDialog.setMessage("Registering User...");
        progressDialog.show();
    }

    private void createUser(String email, String password){

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isComplete()) {
                            Toast.makeText(SignupActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignupActivity.this, "Registered Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
