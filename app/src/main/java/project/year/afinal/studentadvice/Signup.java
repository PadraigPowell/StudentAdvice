package project.year.afinal.studentadvice;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class Signup extends Fragment implements View.OnClickListener {

    private Button buttonSignup;
    private TextView textViewLogin;
    private EditText editTextEmail;
    private EditText editTextPassword;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    public Signup() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(getActivity());

        buttonSignup = (Button) getView().findViewById(R.id.buttonSignup);
        textViewLogin = (TextView) getView().findViewById(R.id.textViewLogin);
        editTextEmail = (EditText) getView().findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) getView().findViewById(R.id.editTextPassword);


        buttonSignup.setOnClickListener(this);
        textViewLogin.setOnClickListener(this);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    private void signupUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getActivity(), "Please enter Email/Username", Toast.LENGTH_SHORT).show();
            //Stopping the function executing
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "Please enter Password", Toast.LENGTH_SHORT).show();
            //Stopping the function executing
            return;
        }
        //if validation is OK
        //Display progress dialog
        progressDialog.setMessage("Registering User...");
        progressDialog.show();
    }

    public void onClick(View view) {
        if (view == buttonSignup) {
            signupUser();
        }

        if (view == textViewLogin) {
            //open Signup Activity
        }


    }
}
