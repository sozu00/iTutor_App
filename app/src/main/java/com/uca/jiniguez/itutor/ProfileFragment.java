package com.uca.jiniguez.itutor;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment  implements OnMapReadyCallback {

    View v;
    ImageButton delete;
    private UserData userData;
    String newSkill = "";
    GoogleMap mGoogleMap;
    MapView mMapView;
    SkillListAdapter adapter;
    ImageButton addSkill;
    Button saveData;
    ListView listView;
    EditText nameTextView;
    EditText phoneTextView;
    EditText mailTextView;
    EditText quoteTextView;

    public ProfileFragment() {}

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
        return v;
    }

    private void saveAllData() {
        userData.userName = nameTextView.getText().toString();
        userData.phoneNumber = phoneTextView.getText().toString();
        userData.email = mailTextView.getText().toString();
        userData.quote = quoteTextView.getText().toString();
        userData.skills = adapter.getSkills();
        userData.uploadData();
        ((MainActivity) v.getContext()).userData = userData;
    }

    private void loadAllData(){
        nameTextView.setText(userData.userName);
        phoneTextView.setText(userData.phoneNumber);
        mailTextView.setText(userData.email);
        quoteTextView.setText(userData.quote);
        adapter.addSkills(userData.skills);
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
        googleMap.addMarker(new MarkerOptions().position(userData.position));

        CameraPosition Position = CameraPosition.builder().target(userData.position).zoom(16).bearing(0).tilt(45).build();

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Position));
    }
}
