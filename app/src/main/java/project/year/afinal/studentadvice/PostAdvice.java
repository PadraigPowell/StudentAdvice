package project.year.afinal.studentadvice;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;
import java.util.HashMap;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostAdvice extends Fragment  implements View.OnClickListener{

    private static final String TAG = "PostAdviceFragment";
    public Firebase mRef;
    public FirebaseAuth mAuth;

    // UI references.
    private TextView textSuccess;
    private EditText editTextTitle;
    private EditText editTextPost;
    private Button post;
    private ProgressDialog mProgressDialog;


    public PostAdvice() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_advice, container, false);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Post Advice");
        mRef = ((MainActivity) getActivity()).myFirebaseRef;
        mAuth = ((MainActivity) getActivity()).mAuth;

        //UI references
        editTextTitle = (EditText) view.findViewById(R.id.editText_AdviceHeader);
        editTextPost = (EditText) view.findViewById(R.id.editText_Advice);
        textSuccess = (TextView) view.findViewById(R.id.Text_Success);
        post = (Button) view.findViewById(R.id.buttonPostAdvice);
        post.setOnClickListener(this);


        // Inflate the layout for this fragment
        return view;
    }

    public void postAdvice()
    {
        if(isTextValidate()) {
            String uid = mAuth.getCurrentUser().getUid();
            String name = mAuth.getCurrentUser().getDisplayName();
            String title = editTextTitle.getText().toString().trim();
            String message = editTextPost.getText().toString().trim();

            String key = mRef.child("advice").push().getKey();
            Post post = new Post(uid, name, title, message, 0, 0);
            Map<String, Object> postValues = post.toMap();
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/advice/" + key, postValues);

            mRef.updateChildren(childUpdates);

            postSuccess();
        }else{
            Toast.makeText(getContext(), "Post failed fix Errors", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Post failed fix Errors");
        }
    }

    private Boolean isTextValidate() {
        String title = editTextTitle.getText().toString().trim();
        String message = editTextPost.getText().toString().trim();

        Boolean isValid = true;

        //Validate name
        if (TextUtils.isEmpty(title)) {
            editTextTitle.setError("Please Enter a Title.");
            isValid = false;
        }else if(title.length() > 40){
            editTextTitle.setError("40 char Max For Title:" + title.length());
            isValid = false;
        }

        //validate email
        if (TextUtils.isEmpty(message)) {
            editTextPost.setError("Enter Advice");
            isValid = false;
        }else if (message.length() > 1000){
            editTextPost.setError("1000 character limit:" + message.length());
            isValid = false;
        }
        return isValid;
    }

    public void postSuccess(){
        //UI references
        post.setVisibility(View.INVISIBLE);
        textSuccess.setVisibility(View.VISIBLE);

        Toast.makeText(getContext(), "Advice Successfully Posted", Toast.LENGTH_SHORT).show();
    }

    public void onClick(View view)
    {
        if(view.getId() == R.id.buttonPostAdvice)
        {
            postAdvice();
        }
    }
}
