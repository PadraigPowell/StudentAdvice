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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonSignup;
    private TextView textViewLogin;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editRetypeTextPassword;

    private ProgressDialog progressDialog; // Dialog for when waiting for signup response
    private FirebaseAuth firebaseAuth; // firebase Auth


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        buttonSignup = (Button) findViewById(R.id.buttonSignup);
        textViewLogin = (TextView) findViewById(R.id.textViewLogin);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);


        buttonSignup.setOnClickListener(SignupActivity);
        textViewLogin.setOnClickListener(this);
    }

    private void signupUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String rePassword = editRetypeTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter Email/Username", Toast.LENGTH_SHORT).show();
            //Stopping the function executing
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter Password", Toast.LENGTH_SHORT).show();
            //Stopping the function executing
            return;
        }

    public void onClick(View view){
        if(view == buttonSignup){
            //signUpUser();
        }

        if(view == textViewLogin){
            //open Signup fragment
            Intent myIntent = new Intent(SignupActivity.this, LoginActivity.class);
            SignupActivity.this.startActivity(myIntent);
        }
    }

        //if validation is OK
        //Display progress dialog
        progressDialog.setMessage("Registering User...");
        progressDialog.show();

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
