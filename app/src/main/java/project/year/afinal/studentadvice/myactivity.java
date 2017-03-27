package project.year.afinal.studentadvice;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class myactivity extends Fragment {


    public myactivity() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("My Activity");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_myactivity, container, false);
    }

}
