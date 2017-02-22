package project.year.afinal.studentadvice;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
    View view;

    public Login() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(getActivity());

        view = inflater.inflate(R.layout.fragment_login, container, false);

        buttonLogin = (Button) view.findViewById(R.id.buttonLogin);
        textViewSignup = (TextView) view.findViewById(R.id.textViewSignup);
        editTextEmail = (EditText) view.findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) view.findViewById(R.id.editTextPassword);

        buttonLogin.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);

        // Inflate the layout for this fragment
        return view;
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

        MainFragment fragment = new MainFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    public void onClick(View view){
        if(view == buttonLogin){
           loginUser();
        }

        if(view == textViewSignup){
            //open Signup fragment
            Signup fragment = new Signup();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            //fragmentTransaction.addToBackStack("Signup");
            fragmentTransaction.commit();
        }
    }

}
