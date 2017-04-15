package project.year.afinal.studentadvice;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Saved extends Fragment {

    private static final String TAG = "SavedAdvice";
    private Firebase mRef;
    private FirebaseAuth mAuth;
    private ArrayList<Post> adviceList;
    private ListView myAdvice;
    private TextView noAdviceMessage;
    private List<Map<String, String>> data;
    private ProgressDialog m_ProgressDialog;

    public Saved() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Saved Advice");

        Log.d(TAG, "onCreate");

        View view = inflater.inflate(R.layout.fragment_my_posts, container, false);
        myAdvice = (ListView) view.findViewById(android.R.id.list);
        noAdviceMessage = (TextView) view.findViewById(R.id.nothingToShow);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("My Posts");
        adviceList = new ArrayList<>();

        mRef = ((MainActivity) getActivity()).myFirebaseRef;
        mAuth = ((MainActivity) getActivity()).mAuth;

        data = new ArrayList<>();

        new Saved.setProgress();

        String uid = mAuth.getCurrentUser().getUid();
        mRef.child("advice").orderByKey().equalTo("root/").addListenerForSingleValueEvent(new ValueEventListener() {
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
                    datum.put("title", post.getTitle() + "\n" + post.getDateTime(getContext()));
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

        myAdvice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "setOnItemClickListener.onItemClick " + position);
                AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                adb.setTitle(adviceList.get(position).getTitle());
                adb.setMessage(adviceList.get(position).getMassage()+"\n"+
                        adviceList.get(position).getDisagreeMsg() + "     "+
                        adviceList.get(position).getAgreeMsg());
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