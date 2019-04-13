package hr.foi.tourschedulebasicdisplay;


import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentContainer;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BasicDisplayTourFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BasicDisplayTourFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BasicDisplayTourFragment extends Fragment {
    // TODO: Rename and change types of parameters
    String address;
    AppCompatActivity mActivity;
    Fragment myFragment;
    EditText touristNote;

    public BasicDisplayTourFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static BasicDisplayTourFragment newInstance(String param1, String param2) {
        BasicDisplayTourFragment fragment = new BasicDisplayTourFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //inflater kreira XML i to

        View fragmentView = inflater.inflate(R.layout.fragment_basic_display_tour, container, false);

        touristNote = (EditText) fragmentView.findViewById(R.id.etAdresa); //BasicDisplayTourFragment.findViewById(R.id.etAdresa);
        touristNote.setText("Please come to the following address to meet the guide:\n\n" + this.address);
        return fragmentView;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        if(activity!=null)
            mActivity = (AppCompatActivity) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setTourData(double destinationLat, double destinationLng, String address) {
        this.address = address;

        //meetingPoint = new LatLng(destinationLat, destinationLng);
    }
}
