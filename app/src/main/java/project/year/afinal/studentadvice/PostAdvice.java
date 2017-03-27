package project.year.afinal.studentadvice;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.login.widget.LoginButton;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostAdvice extends Fragment  implements View.OnClickListener{

    private static final String TAG = "PostAdviceFragment";
    public Firebase mRef;
    public FirebaseAuth mAuth;

    // UI references.
    private EditText editTextHeading;
    private EditText editTextPost;
    private Button post;
    private ProgressDialog mProgressDialog;


    public PostAdvice() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Post Advice");

        //UI references
        editTextHeading = (EditText) getView().findViewById(R.id.editTextEmail);
        editTextPost = (EditText) getView().findViewById(R.id.editTextPassword);
        post = (Button) getView().findViewById(R.id.buttonPostAdvice);

        post.setOnClickListener(this);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_advice, container, false);
    }

    public void postAdvice()
    {
        mRef = ((MainActivity) getActivity()).myFirebaseRef;
        mAuth = ((MainActivity) getActivity()).mAuth;

        String uid = mAuth.getCurrentUser().getUid();
        String name = mAuth.getCurrentUser().getDisplayName();
        String email = mAuth.getCurrentUser().getEmail();
        String heading = editTextHeading.getText().toString().trim();
        String message = editTextPost.getText().toString().trim();

        Post post = new Post(heading, message);

        Firebase userRef = mRef.child("users/" + uid);
        /*userRef.setValue(user, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null){
                    Log.e(TAG, "Firebase add to db failed" + firebaseError.getMessage());
                }
            }
        });*/
    }

    public void onClick(View view)
    {
        if(view.getId() == R.id.buttonPostAdvice)
        {
            postAdvice();
        }
    }
}
