package com.uca.jiniguez.itutor;


import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    List<HashMap<String, Object>> teachers = new ArrayList<>();

    public SearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater,container,savedInstanceState);
        final View v =  inflater.inflate(R.layout.fragment_search, container, false);
        final ImageButton searchButton = v.findViewById(R.id.searchButton);
        // Keys used in Hashmap
        String[] from = { "mName","mPhone", "mDescription", "icon", "mRating", "mImage"};

        // Ids of views in listview_layout
        int[] to = { R.id.voterName, R.id.teacherPhone, R.id.teacherQuote, R.id.phoneButton, R.id.voteRating, R.id.profilePicView};

        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
        final TeacherListAdapter adapter = new TeacherListAdapter(v.getContext(), teachers, R.layout.single_teacher, from, to);


        // Getting a reference to listview of main.xml layout file
        final ListView listView = ( ListView ) v.findViewById(R.id.teachersSearched);
        ActivityCompat.requestPermissions(this.getActivity(),
                new String[]{Manifest.permission.CALL_PHONE},
                0);
        // Setting the adapter to the listView
        adapter.setViewBinder(new MyBinder());
        listView.setAdapter(adapter);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

                fragmentTransaction.replace(R.id.main_frame, new FiltersFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return v;
    }

    public void setUserData(List<UserData> allUsers) {
        teachers.clear();
        for(UserData user : allUsers){
            HashMap<String, Object> hm = new HashMap<>();
            hm.put("id", user.mID);
            hm.put("mName", user.mName);
            hm.put("mPhone", user.mPhone);
            hm.put("mDescription", user.mDescription);
            hm.put("mRating", user.mRating);
            hm.put("mImage", user.profilePic);
            hm.put("icon", Integer.toString(R.drawable.ic_call));
            hm.put("data", user);
            teachers.add(hm);
        }
    }
}
