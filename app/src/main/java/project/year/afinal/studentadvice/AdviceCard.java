package project.year.afinal.studentadvice;

import android.content.Context;
//import android.view.View;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.google.firebase.auth.FirebaseAuth;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.*;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.SwipePlaceHolderView;

/**
 * Created by Lorcan on 13/04/2017.
 */

@Layout(R.layout.advice_card_view)
public class AdviceCard {
    @View(R.id.head)
    private TextView HeadMsg;

    @View(R.id.message)
    private TextView MessageText;

    @View(R.id.save_comment_count)
    private TextView save_comment_msg;

    @View(R.id.AgreeCount)
    private TextView AgreeMsg;

    @View(R.id.DisagreeCount)
    private TextView DisagreeMsg;

    private Context mContext;
    private Post mPost;
    private SwipePlaceHolderView mSwipeView;
    private static final String TAG = "AdviceCard";
    private Firebase value;

    private Firebase mRef;
    private FirebaseAuth mAuth;

    public AdviceCard(Context context, Post post, SwipePlaceHolderView swipeView, FirebaseAuth mAuth, Firebase mRef) {
        mContext = context;
        mPost = post;
        mSwipeView = swipeView;

        this.mAuth = mAuth;
        this.mRef = mRef;

        value = new Firebase("https://student-advice.firebaseio.com/advice/" + mPost.getAdviceKey());
    }
    public Post getPost()
    {
        return this.mPost;
    }

    public void updateSave()
    {
        this.mPost.incrementSave();
        save_comment_msg.setText("   " + mPost.getSaveMsg() + "   " + mPost.getCommentMsg());
    }

    @Resolve
    private void onResolved(){
        HeadMsg.setText(mPost.getAuther() + ": " + mPost.getTitle() + ".\nPosted: " + mPost.getDateTime(this.mContext));
        MessageText.setText(mPost.getMassage());
        DisagreeMsg.setText(mPost.getDisagreeMsg());
        AgreeMsg.setText(mPost.getAgreeMsg());
        save_comment_msg.setText("   " + mPost.getSaveMsg() + "   " + mPost.getCommentMsg());
    }


    @SwipeOut
    private void onSwipedOut(){
        Log.d("EVENT", "onSwipedOut");

        //add the that you have disagreed with this advice to your user node
        //String uid = mAuth.getCurrentUser().getUid();
        //mRef.child("users/"+uid+"/disagreedAdvice/"+mPost.getAdviceKey()).setValue("true");

        value.child("disagreeCount").runTransaction(new Transaction.Handler() {
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
                    Log.d(TAG, "Firebase counter increment failed. Firebase error: " + firebaseError.getMessage());
                } else {
                    Log.d(TAG, "Firebase counter increment succeeded.");
                }
            }
        });
    }

    @SwipeIn
    private void onSwipeIn(){
        Log.d("EVENT", "onSwipedIn");

        //add the that you have agreed with this advice to your user node
        //String uid = mAuth.getCurrentUser().getUid();
        //mRef.child("users/"+uid+"/agreedAdvice/"+mPost.getAdviceKey()).setValue("true");

        value.child("agreeCount").runTransaction(new Transaction.Handler() {
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
                    Log.d(TAG, "Firebase counter increment failed. Firebase error: " + firebaseError.getMessage());
                } else {
                    Log.d(TAG, "Firebase counter increment succeeded.");
                }
            }
        });
    }

    @SwipeCancelState
    private void onSwipeCancelState(){
        Log.d("EVENT", "onSwipeCancelState");
    }


    @SwipeInState
    private void onSwipeInState(){
        Log.d("EVENT", "onSwipeInState");
    }

    @SwipeOutState
    private void onSwipeOutState(){
        Log.d("EVENT", "onSwipeOutState");
    }
}
