package project.year.afinal.studentadvice;


import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.app.ProgressDialog;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by Lorcan on 30/03/2017.
 */

public class AdviceView extends ListActivity {

    private ProgressDialog m_ProgressDialog = null;
    private MyAdviceAdapter m_adapter;
    private Runnable viewOrders;
    private ArrayList<Post> adviceList;

    public AdviceView(ArrayList<Post> items) {
        this.adviceList = items;
        this.m_adapter = new MyAdviceAdapter(this, R.layout.my_advice_row, adviceList);
        setListAdapter(this.m_adapter);
        runOnUiThread(returnRes);
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

    private void getOrders(){
        try{
            adviceList = new ArrayList<Post>();
            Post o1 = new Post();
            Post o2 = new Post();
            adviceList.add(o1);
            adviceList.add(o2);
            Thread.sleep(5000);
            Log.i("ARRAY", ""+ adviceList.size());
        } catch (Exception e) {
            Log.e("BACKGROUND_PROC", e.getMessage());
        }
        runOnUiThread(returnRes);
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



