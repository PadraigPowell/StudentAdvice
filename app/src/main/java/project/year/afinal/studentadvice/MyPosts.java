package project.year.afinal.studentadvice;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import android.app.ProgressDialog;


public class MyPosts extends ListFragment {

    private static final String TAG = "MyPostsFragment";
    private Firebase mRef;
    private FirebaseAuth mAuth;
    private ArrayList<Post> adviceList;
    private ListView myAdvice;
    private MyAdviceAdapter m_adapter;
    private ProgressDialog m_ProgressDialog;
    private Runnable viewAdvice;

    public MyPosts() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_my_posts, container, false);
        //myAdvice = (ListView) view.findViewById(R.id.android:list);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("My Posts");
        adviceList = new ArrayList<Post>();

        mRef = ((MainActivity) getActivity()).myFirebaseRef;
        mAuth = ((MainActivity) getActivity()).mAuth;

        this.m_adapter = new MyAdviceAdapter(getContext(), R.layout.my_advice_row, adviceList);
        setListAdapter(this.m_adapter);

        viewAdvice = new Runnable(){
            @Override
            public void run() {
                getAdvice();
            }
        };
        Thread thread =  new Thread(null, viewAdvice, "MagentoBackground");
        thread.start();
        m_ProgressDialog = ProgressDialog.show(getContext(),
                "Please wait...", "Retrieving data ...", true);

        return view;
    }

    private Runnable returnRes = new Runnable() {

        @Override
        public void run() {
            if (adviceList != null && adviceList.size() > 0) {
                m_adapter.notifyDataSetChanged();
                for (int i = 0; i < adviceList.size(); i++)
                    m_adapter.add(adviceList.get(i));
            }
            m_ProgressDialog.dismiss();
            m_adapter.notifyDataSetChanged();
        }
    };

    private void getAdvice(){
        String uid = mAuth.getCurrentUser().getUid();

        mRef.child("advice").orderByChild("uid").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            //onDataChange is called every time the name of the User changes in your Firebase Database
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String advice = postSnapshot.toString();
                    Log.d(TAG, "Advice:" + advice);
                    Post post = postSnapshot.getValue(Post.class);
                    post.setAdviceKey(postSnapshot.getKey());
                    adviceList.add(post);
                }
            }

            //onCancelled is called in case of any error
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(getContext(), "" + firebaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        getActivity().runOnUiThread(returnRes);
    }

    private class MyAdviceAdapter extends ArrayAdapter<Post> {

        List<Post> adviceList;

        public MyAdviceAdapter(Context context, int textViewResourceId, ArrayList<Post> items) {
            super(context, textViewResourceId, items);
            this.adviceList = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.my_advice_row, null);
            }
            Post Advice = adviceList.get(position);
            if (Advice != null) {
                TextView title = (TextView) v.findViewById(R.id.title);
                TextView messPre = (TextView) v.findViewById(R.id.messagePreview);
                if (title != null) {
                    title.setText("Name: " + Advice.getTitle());
                }
                if (messPre != null) {
                    messPre.setText("Status: " + Advice.getMassagePreview(20));
                }
            }
            return v;
        }
    }
}
