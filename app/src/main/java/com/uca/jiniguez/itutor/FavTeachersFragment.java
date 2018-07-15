package com.uca.jiniguez.itutor;


import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavTeachersFragment extends Fragment {

    String[] names = new String[] {
            "Manolito perez",
            "Pepito pere",
            "juan valdes"
    };

    List<HashMap<String, String>> teachers = new ArrayList<>();
    private UserData userData;

    public FavTeachersFragment() {

        for(String name : names){
            HashMap<String, String> hm = new HashMap<>();
            hm.put("name", name);
            hm.put("phone", "123456");
            hm.put("quote", "ESTA ES UNA SITA PERFEITasdasdasdasdasdsaAPERFEITasdasdasda dasdasdsaAPERFEITasd asdasdasdasdsaA ");
            hm.put("icon", Integer.toString(R.drawable.ic_call ));
            teachers.add(hm);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater,container,savedInstanceState);
        final View v =  inflater.inflate(R.layout.fragment_fav_teacher, container, false);

        // Keys used in Hashmap
        String[] from = { "name","phone", "quote", "icon"};

        // Ids of views in listview_layout
        int[] to = { R.id.teacherName, R.id.teacherPhone, R.id.teacherQuote, R.id.phoneButton};

        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
        final TeacherListAdapter adapter = new TeacherListAdapter(v.getContext(), teachers, R.layout.single_teacher, from, to);


        // Getting a reference to listview of main.xml layout file
        final ListView listView = ( ListView ) v.findViewById(R.id.teachersList);
        ActivityCompat.requestPermissions(this.getActivity(),
                new String[]{Manifest.permission.CALL_PHONE},
                0);
        // Setting the adapter to the listView
        listView.setAdapter(adapter);


        return v;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }
}
