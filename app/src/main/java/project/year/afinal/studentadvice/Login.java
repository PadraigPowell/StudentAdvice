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
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Login extends Fragment implements View.OnClickListener{

    private Button buttonLogin;
    private TextView textViewSignup;
    private EditText editTextEmail;
    private EditText editTextPassword;

    ProgressDialog progressDialog;

    public Login() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(getActivity());

        buttonLogin = (Button) getView().findViewById(R.id.buttonLogin);
        textViewSignup = (TextView) getView().findViewById(R.id.textViewSignup);
        editTextEmail = (EditText) getView().findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) getView().findViewById(R.id.editTextPassword);

        buttonLogin.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    private void loginUser(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            //kj
        }
        if (TextUtils.isEmpty(password)){
            //hj
        }
    }

    public void onClick(View view){
        if(view == buttonLogin){
           loginUser();
        }

        if(view == textViewSignup){
            //open Signup Activity
        }
    }

}
