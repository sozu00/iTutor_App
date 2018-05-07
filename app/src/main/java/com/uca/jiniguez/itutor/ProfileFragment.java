package com.uca.jiniguez.itutor;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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
public class ProfileFragment extends Fragment {

    String[] skills = new String[] {
            "Matematicas",
            "Lengua",
            "Bicicleta"
    };

    View v;
    ImageButton delete;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        v = inflater.inflate(R.layout.fragment_profile, container, false);

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

        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
        final SkillListAdapter adapter = new SkillListAdapter(v.getContext(), aList, R.layout.single_skill, from, to);


        // Getting a reference to listview of main.xml layout file
        final ListView listView = ( ListView ) v.findViewById(R.id.SkillsList);

        // Setting the adapter to the listView
        listView.setAdapter(adapter);

        ImageButton addSkill = v.findViewById(R.id.addSkillBtn);
        addSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> hm = new HashMap<>();
                hm.put("txt", "TESTING");
                hm.put("flag", Integer.toString(R.drawable.ic_delete ));

                adapter.elements.add(hm);
                adapter.notifyDataSetChanged();
            }
        });

        return v;
    }



}
