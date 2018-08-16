package com.uca.jiniguez.itutor;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment  implements OnMapReadyCallback {

    private View v;
    private UserData userData;
    private String newSkill = "";
    private GoogleMap mGoogleMap;
    private MapView mMapView;
    private SkillListAdapter adapter;
    private ImageButton addSkill;
    private Button saveData;
    private ListView listView;
    private EditText nameTextView;
    private EditText phoneTextView;
    private TextView mailTextView;
    private EditText quoteTextView;
    private RatingBar ratingBar;

    public ProfileFragment() {}

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        v = inflater.inflate(R.layout.fragment_profile, container, false);

        addSkill = v.findViewById(R.id.addSkillBtn);
        mMapView = v.findViewById(R.id.mapView);
        saveData = v.findViewById(R.id.saveDataButton);
        listView = v.findViewById(R.id.SkillsList);
        nameTextView = v.findViewById(R.id.nameEditText);
        phoneTextView = v.findViewById(R.id.phoneEditText);
        mailTextView = v.findViewById(R.id.mailEditText);
        quoteTextView = v.findViewById(R.id.QuoteEditText);
        ratingBar = v.findViewById(R.id.ratingBar);

        if(mMapView!=null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

        String[] from = { "flag","txt"};
        int[] to = { R.id.imageButton,R.id.txtList};
        adapter = new SkillListAdapter(v.getContext(), new ArrayList<HashMap<String, String>>(), R.layout.single_skill, from, to);
        listView.setAdapter(adapter);

        addSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNewSkill();
            }
        });

        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAllData();
            }
        });
        loadAllData();


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

    private void saveAllData() {
        userData.mName = nameTextView.getText().toString();
        userData.mPhone = phoneTextView.getText().toString();
        userData.mDescription = quoteTextView.getText().toString();
        userData.mSkills = adapter.getSkills();
        userData.uploadData();
        ((MainActivity) v.getContext()).userData = userData;
    }

    private void loadAllData(){
        nameTextView.setText(userData.mName);
        phoneTextView.setText(userData.mPhone);
        mailTextView.setText(userData.mEmail);
        quoteTextView.setText(userData.mDescription);
        adapter.addSkills(userData.mSkills);
        ratingBar.setRating(userData.mRating);
    }


    public void setUserData(UserData userData) {
        this.userData = userData;
    }


    public void getNewSkill(){
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Nueva materia");
        newSkill = "";

        final EditText input = new EditText(v.getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newSkill = input.getText().toString();
                HashMap<String, String> hm = new HashMap<>();
                hm.put("txt", newSkill);
                hm.put("flag", Integer.toString(R.drawable.ic_delete));
                adapter.elements.add(hm);
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());

        mGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.addMarker(new MarkerOptions().position(userData.mPosition));

        CameraPosition Position = CameraPosition.builder().target(userData.mPosition).zoom(16).bearing(0).tilt(45).build();

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Position));
    }
}
