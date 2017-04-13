package project.year.afinal.studentadvice;

import android.content.Context;
//import android.view.View;
import android.widget.TextView;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.*;
import com.mindorks.placeholderview.annotations.Resolve;
//import com.mindorks.placeholderview.annotations.Click;
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

    public AdviceCard(Context context, Post post, SwipePlaceHolderView swipeView) {
        mContext = context;
        mPost = post;
        mSwipeView = swipeView;
    }

    @Resolve
    private void onResolved(){
        HeadMsg.setText(mPost.getAuther() + ": " + mPost.getTitle() + "\n" + mPost.getDateTime(this.mContext));
        MessageText.setText(mPost.getMassage());
        DisagreeMsg.setText(mPost.getDisagreeMsg());
        AgreeMsg.setText(mPost.getAgreeMsg());
        save_comment_msg.setText(mPost.getAgreeMsg());
    }

    @SwipeOut
    private void onSwipedOut(){
        Log.d("EVENT", "onSwipedOut");
        mSwipeView.addView(this);
    }

    @SwipeCancelState
    private void onSwipeCancelState(){
        Log.d("EVENT", "onSwipeCancelState");
    }

    @SwipeIn
    private void onSwipeIn(){
        Log.d("EVENT", "onSwipedIn");
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
