package com.uca.jiniguez.itutor;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
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

import java.io.File;


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
    private ImageView photo;
    private ImageView call;
    private ImageView mail;

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
        v = inflater.inflate(R.layout.fragment_teacher_profile, container, false);

        nameTextView = v.findViewById(R.id.nameTextView);
        phoneTextView = v.findViewById(R.id.phoneTextView);
        mailTextView = v.findViewById(R.id.mailTextView);
        quoteTextView = v.findViewById(R.id.quoteTextView);
        ratingBar = v.findViewById(R.id.voteRating);
        votesInfo = v.findViewById(R.id.votesInfo);
        //mMapView = v.findViewById(R.id.mapView);
        photo = v.findViewById(R.id.profilePicView);
        call = v.findViewById(R.id.phoneImgView);
        mail = v.findViewById(R.id.mailImgView);

        if(mMapView!=null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
        Utilities.downloadWithTransferUtility(v, "public/example-image.png");
        File imgFile = new File(v.getContext().getFilesDir().getAbsolutePath()+"/photo.png");
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            photo.setImageBitmap(myBitmap);
        }

        final ListView listView = v.findViewById(R.id.SkillsList);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_list_item_1);
        adapter.addAll(userData.mSkills);
        // Setting the adapter to the listView
        listView.setAdapter(adapter);
        loadAllData();

        setListeners(listView);

        return v;
    }

    private void setListeners(ListView listView) {
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

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+phoneTextView.getText().toString()));
                // No explanation needed; request the permission
                if (ActivityCompat.checkSelfPermission(v.getContext(),
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                v.getContext().startActivity(callIntent);
            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Enviar email", "");
                String[] TO = {
                       mailTextView.getText().toString()
                };
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hola! He visto tu perfil en iTutor!");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Hola! Que tal?");
                try {
                    v.getContext().startActivity(Intent.createChooser(emailIntent, "Enviar mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(v.getContext(), "Necesitas un cliente de correo.", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
