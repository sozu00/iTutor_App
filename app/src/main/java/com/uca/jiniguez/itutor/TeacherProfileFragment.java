package com.uca.jiniguez.itutor;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * A simple {@link Fragment} subclass.
 */
public class TeacherProfileFragment extends Fragment implements OnMapReadyCallback {

    View v;
    private ImageButton delete;
    private GoogleMap mGoogleMap;
    private MapView mMapView;
    private UserData userData;
    private TextView nameTextView;
    private TextView phoneTextView;
    private TextView mailTextView;
    private TextView quoteTextView;
    private SkillListAdapter adapter;
    private RatingBar ratingBar;
    private ImageButton votesInfo;

    public TeacherProfileFragment() {
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }
    private void loadAllData(){
        nameTextView.setText(userData.mName);
        phoneTextView.setText(userData.mPhone);
        mailTextView.setText(userData.mEmail);
        quoteTextView.setText(userData.mDescription);
        ratingBar.setRating(userData.mRating);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        v = inflater.inflate(R.layout.fragment_profile_teacher, container, false);

        nameTextView = v.findViewById(R.id.nameTextView);
        phoneTextView = v.findViewById(R.id.phoneTextView);
        mailTextView = v.findViewById(R.id.mailTextView);
        quoteTextView = v.findViewById(R.id.quoteTextView);
        ratingBar = v.findViewById(R.id.voteRating);
        votesInfo = v.findViewById(R.id.votesInfoTeacher);
        //mMapView = v.findViewById(R.id.mapView);
        if(mMapView!=null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }


        final ListView listView = v.findViewById(R.id.SkillsList);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_list_item_1);
        adapter.addAll(userData.mSkills);
        // Setting the adapter to the listView
        listView.setAdapter(adapter);
        loadAllData();

        votesInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                VotesFragment votesFragment = new VotesFragment();
                votesFragment.setUserData(userData);

                fragmentTransaction.replace(R.id.main_frame, votesFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        listView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        return v;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());

        mGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.addMarker(new MarkerOptions().position(new LatLng(0,0)));

        CameraPosition Position = CameraPosition.builder().target(new LatLng(0,0)).zoom(16).bearing(0).tilt(45).build();

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Position));
    }
}
