package com.uca.jiniguez.itutor;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

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
public class TeacherProfileFragment extends Fragment implements OnMapReadyCallback {

    String[] skills = new String[] {
            "Matematicas",
            "Lengua",
            "Bicicleta"
    };

    View v;
    ImageButton delete;
    GoogleMap mGoogleMap;
    MapView mMapView;
    public TeacherProfileFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        v = inflater.inflate(R.layout.fragment_profile_teacher, container, false);

        mMapView = (MapView)v.findViewById(R.id.mapView);
        if(mMapView!=null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

        // Each row in the list stores country name, currency and flag
        List<HashMap<String,String>> aList = new ArrayList<>();

        for(String skill : skills){
            HashMap<String, String> hm = new HashMap<>();
            hm.put("txt", skill);
            hm.put("flag", Integer.toString(R.drawable.ic_delete ));
            aList.add(hm);
        }

        // Keys used in Hashmap
        String[] from = { "flag","txt"};

        // Ids of views in listview_layout
        int[] to = { R.id.imageButton,R.id.txtList};

        // Getting a reference to listview of main.xml layout file
        final ListView listView = ( ListView ) v.findViewById(R.id.SkillsList);
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_list_item_1);
        adaptador.addAll(skills);
        // Setting the adapter to the listView
        listView.setAdapter(adaptador);
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
