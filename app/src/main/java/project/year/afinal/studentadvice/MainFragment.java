package project.year.afinal.studentadvice;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.listeners.ItemRemovedListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private static final String TAG = "MainFragment";
    private SwipePlaceHolderView mSwipeView;
    private Context mContext;
    private Firebase mRef;
    private FirebaseAuth mAuth;
    private ProgressDialog m_ProgressDialog;
    private long viewedAdviceCount;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Student Advice");
        mContext = ((MainActivity) getActivity()).getContext();

        if (!isNetworkAvailable()){
            Toast.makeText(getContext(), "Network Unavailable", Toast.LENGTH_LONG).show();
        }else{
            m_ProgressDialog = ProgressDialog.show(getActivity(),
                    "Please wait...", "Retrieving data ...", true);
        }

        mSwipeView = (SwipePlaceHolderView) view.findViewById(R.id.swipeView);

        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.swipe_in_message_view)
                        .setSwipeOutMsgLayoutId(R.layout.swipe_out_message_view));


        mRef = ((MainActivity) getActivity()).myFirebaseRef;
        mAuth = ((MainActivity) getActivity()).mAuth;

        getViewedCount();

        //get the data from the database and add it to the swipe view
        pollFromDatabase();

        view.findViewById(R.id.downBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(false);
            }
        });

        view.findViewById(R.id.upBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(true);
            }
        });

        view.findViewById(R.id.saveBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Object> t = mSwipeView.getAllResolvers();
                t.size();
                AdviceCard adviceCard = (AdviceCard) t.get(0);

                //when the user updates the save counter they set this value to true
                //this is used so the user can only save one value at a time
                if (adviceCard.hasUserSaved()) {
                    Toast.makeText(getContext(), "Advice already Saved",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                //update the local save count
                adviceCard.updateSave();

                Post post = adviceCard.getPost();

                //add the advice key to the user node under savedAdvice
                String uid = mAuth.getCurrentUser().getUid();
                mRef.child("users/"+uid+"/savedAdvice/"+post.getAdviceKey()).setValue("true");

                //update Server value
                mRef.child("/advice/"+post.getAdviceKey()+"/saveCount").runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        if (mutableData.getValue() == null) {
                            mutableData.setValue(1);
                        } else {
                            mutableData.setValue((Long) mutableData.getValue() + 1);
                        }
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(FirebaseError firebaseError, boolean b, DataSnapshot dataSnapshot) {
                        if (firebaseError != null) {
                            Log.d(TAG, "Firebase saved counter increment failed. Firebase error: " + firebaseError.getMessage());
                        } else {
                            Log.d(TAG, "Firebase saved counter increment succeeded.");
                        }
                    }
                });
                Toast.makeText(getContext(), "Saved",
                        Toast.LENGTH_SHORT).show();
            }
        });

        //listening for new card
        mSwipeView.addItemRemoveListener(new ItemRemovedListener() {
            @Override
            public void onItemRemoved(int count) {
                String uid = mAuth.getCurrentUser().getUid();

                //update the local variable
                viewedAdviceCount += 1;

                //update Server value
                mRef.child("users/"+uid+"/viewed").runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        if (mutableData.getValue() == null) {
                            mutableData.setValue(1);
                        } else {
                            mutableData.setValue((Long) mutableData.getValue() + 1);
                        }
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(FirebaseError firebaseError, boolean b, DataSnapshot dataSnapshot) {
                        if (firebaseError != null) {
                            Log.d(TAG, "Firebase viewed counter increment failed. Firebase error: " + firebaseError.getMessage());
                        } else {
                            Log.d(TAG, "Firebase viewed counter increment succeeded.");
                        }
                    }
                });
            }
        });

        return view;
    }

    private void pollFromDatabase()
    {
        mRef.child("advice").addListenerForSingleValueEvent(new ValueEventListener() {
            //onDataChange is called every time the name of the User changes in your Firebase Database
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String advice = postSnapshot.toString();
                    Log.d(TAG, "Advice:" + advice);
                    Post post = postSnapshot.getValue(Post.class);
                    post.setAdviceKey(postSnapshot.getKey());
                    mSwipeView.addView(new AdviceCard(mContext, post));
                }
                if (m_ProgressDialog != null && m_ProgressDialog.isShowing()) {
                    m_ProgressDialog.dismiss();
                }else
                {
                    Toast.makeText(getContext(), "Network Available", Toast.LENGTH_LONG).show();
                }
            }
            //onCancelled is called in case of any error
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d(TAG, "Firebase error: " + firebaseError.getMessage());
                Toast.makeText(getContext(), "Firebase error: " + firebaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getViewedCount()
    {
        String uid = mAuth.getCurrentUser().getUid();
        //Referring to the name of the User who has logged in currently and adding a valueChangeListener
        mRef.child("users/" + uid + "/viewed").addListenerForSingleValueEvent(new ValueEventListener() {
            //onDataChange is called every time the name of the User changes in your Firebase Database
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                viewedAdviceCount = dataSnapshot.getValue(long.class);
            }

            //onCancelled is called in case of any error
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(getContext(), "" + firebaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
