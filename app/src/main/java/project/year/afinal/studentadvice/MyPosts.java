package project.year.afinal.studentadvice;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import android.app.ProgressDialog;
import android.app.AlertDialog;


public class MyPosts extends ListFragment {

    private static final String TAG = "MyPostsFragment";
    private Firebase mRef;
    private FirebaseAuth mAuth;
    private ArrayList<Post> adviceList;
    private ListView myAdvice;
    private TextView noAdviceMessage;
    private List<Map<String, String>> data;
    private ProgressDialog m_ProgressDialog;

    public MyPosts() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        View view = inflater.inflate(R.layout.fragment_my_posts, container, false);
        myAdvice = (ListView) view.findViewById(android.R.id.list);
        noAdviceMessage = (TextView) view.findViewById(R.id.nothingToShow);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("My Posts");
        adviceList = new ArrayList<>();

        mRef = ((MainActivity) getActivity()).myFirebaseRef;
        mAuth = ((MainActivity) getActivity()).mAuth;

        data = new ArrayList<>();

        new setProgress();

        String uid = mAuth.getCurrentUser().getUid();
        mRef.child("advice").orderByChild("uid").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            //onDataChange is called every time the name of the User changes in your Firebase Database
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String advice = postSnapshot.toString();
                    Log.d(TAG, "Advice:" + advice);
                    Post post = postSnapshot.getValue(Post.class);
                    post.setAdviceKey(postSnapshot.getKey());
                    adviceList.add(post);

                    Map<String, String> datum = new HashMap<>(2);
                    datum.put("title", post.getTitle());
                    datum.put("message", post.getMassagePreview(40));
                    data.add(datum);
                }
                m_ProgressDialog.dismiss();
                if ( data.size() == 0 ) {
                    noAdviceMessage.setVisibility(View.VISIBLE);
                }else {
                    addToAdapter();
                }
            }

            //onCancelled is called in case of any error
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d(TAG, "Firebase error: " + firebaseError.getMessage());
                Toast.makeText(getContext(), "Firebase error: " + firebaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    private void addToAdapter() {
        Log.d(TAG, "addToAdapter");
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), data,
                android.R.layout.simple_list_item_2,
                new String[]{"title", "message"},
                new int[]{android.R.id.text1,
                        android.R.id.text2});
        myAdvice.setAdapter(adapter);

        myAdvice.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "setOnItemClickListener.onItemClick " + position);
                AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                adb.setTitle(adviceList.get(position).getTitle());
                adb.setMessage(adviceList.get(position).getMassage());
                adb.setPositiveButton("Ok", null);
                adb.show();
            }
        });
    }

    private class setProgress {
        setProgress(){
            Log.d(TAG, "setProgress Constructor");
            m_ProgressDialog = ProgressDialog.show(getActivity(),
                    "Please wait...", "Retrieving data ...", true);
        }
    }
}
