package com.uca.jiniguez.itutor;


import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class VotesFragment extends Fragment {
    List<HashMap<String, Object>> votes = new ArrayList<>();
    private UserData userData;

    public VotesFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater,container,savedInstanceState);
        final View v =  inflater.inflate(R.layout.fragment_votes, container, false);

        // Keys used in Hashmap
        String[] from = { "voterName","voteText", "voteRating"};

        // Ids of views in listview_layout
        int[] to = { R.id.voterName, R.id.voteText, R.id.voteRating};

        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
        //final TeacherListAdapter adapter = new TeacherListAdapter(v.getContext(), votes, R.layout.single_teacher, from, to);

        final VoteListAdapter adapter = new VoteListAdapter(v.getContext(), votes, R.layout.single_vote, from, to);
        // Getting a reference to listview of main.xml layout file
        final ListView listView = ( ListView ) v.findViewById(R.id.votesList);
        adapter.setViewBinder(new MyBinder());
        // Setting the adapter to the listView
        listView.setAdapter(adapter);
        return v;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
        votes.clear();
        for(VoteData vote : userData.getVotes()){
            HashMap<String, Object> hm = new HashMap<>();
            hm.put("voterName", vote.voterUser);
            hm.put("voteText", vote.comment);
            hm.put("voteRating", vote.rating);
            votes.add(hm);
        }
    }
}

class MyBinder implements SimpleAdapter.ViewBinder {
    @Override
    public boolean setViewValue(View view, Object data, String textRepresentation) {
        if(view.getId() == R.id.voteRating){
            String stringval = data.toString();
            float ratingValue = Float.parseFloat(stringval);
            RatingBar ratingBar = (RatingBar) view;
            ratingBar.setRating(ratingValue);
            return true;
        }
        return false;
    }
}
