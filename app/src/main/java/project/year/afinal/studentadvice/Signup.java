package project.year.afinal.studentadvice;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class Signup extends Fragment implements View.OnClickListener {

    //Screen Attributes
    private Button buttonSignup;
    private TextView textViewLogin;
    private EditText editTextEmail;
    private EditText editTextPassword;

    private ProgressDialog progressDialog; // Dialog for when waiting for signup response
    private FirebaseAuth firebaseAuth; // firebase Auth

    public Signup() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getActivity());

        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        buttonSignup = (Button) view.findViewById(R.id.buttonSignup);
        textViewLogin = (TextView) view.findViewById(R.id.textViewLogin);
        editTextEmail = (EditText) view.findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) view.findViewById(R.id.editTextPassword);


        buttonSignup.setOnClickListener(this);
        textViewLogin.setOnClickListener(this);

        // Inflate the layout for this fragment
        return view;
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

        /*
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isComplete()){
                            Toast.makeText(getActivity(), "Successfully Registered", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getActivity(), "Registered Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        */

        MainFragment fragment = new MainFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    public void onClick(View view) {
        if (view == buttonSignup) {
            signupUser();
        }

        if (view == textViewLogin) {
            //open Login Activity
            Login fragment = new Login();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            //fragmentTransaction.addToBackStack("Login");
            fragmentTransaction.commit();
        }


    }
}
