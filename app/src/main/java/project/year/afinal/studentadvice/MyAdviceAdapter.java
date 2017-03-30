package project.year.afinal.studentadvice;


import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

/**
 * Created by Lorcan on 30/03/2017.
 */

public class MyAdviceAdapter extends ArrayAdapter
{

    List<Post> adviceList;

    public MyAdviceAdapter(Context context, int textViewResourceId, ArrayList<Post> items) {
        super(context, textViewResourceId, items);
        this.adviceList = items;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.my_advice_row, null);
        }
        Post Advice = adviceList.get(position);
        if (Advice != null) {
            TextView title = (TextView) v.findViewById(R.id.title);
            TextView messPre = (TextView) v.findViewById(R.id.messagePreview);
            if (title != null) {
                title.setText("Name: "+ Advice.getTitle());                            }
            if(messPre != null){
                messPre.setText("Status: "+ Advice.getMassagePreview(20));
            }
        }
        return v;
    }

}
