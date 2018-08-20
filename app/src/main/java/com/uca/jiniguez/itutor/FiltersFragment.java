package com.uca.jiniguez.itutor;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FiltersFragment extends Fragment {

    private SeekBar distanceBar;
    private SeekBar ratingBar;
    private TextView distanceBarValue;
    private TextView skillFilter;
    private TextView ratingBarValue;
    private Button applyFilters;
    private Button clearFilters;

    public FiltersFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater,container,savedInstanceState);
        final View v =  inflater.inflate(R.layout.fragment_search_filters, container, false);

        distanceBar = v.findViewById(R.id.distanceBar);
        ratingBar = v.findViewById(R.id.ratingFilter);
        distanceBarValue = v.findViewById(R.id.distanceBarValue);
        ratingBarValue = v.findViewById(R.id.ratingBarValue);
        skillFilter = v.findViewById(R.id.skillSearchText);
        applyFilters = v.findViewById(R.id.applyFiltersButton);
        clearFilters = v.findViewById(R.id.clearFiltersButton);

        setSeekBarsListeners();
        setButtonsListeners(v);

        return v;
    }

    private void setSeekBarsListeners() {
        distanceBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                distanceBarValue.setText("Hasta "+String.valueOf(progress) + " km");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        ratingBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                ratingBarValue.setText("Desde "+String.valueOf(progress));
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void setButtonsListeners(final View v) {
        clearFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                distanceBar.setProgress(0);
                skillFilter.setText("");
            }
        });

        applyFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity myActivity;
                myActivity = (MainActivity) v.getContext();
                List<UserData> users = UserData.getUsers(skillFilter.getText().toString(), distanceBar.getProgress(), null, ratingBar.getProgress());
                myActivity.searchFragment.setUserData(users);
                myActivity.setFragment(myActivity.searchFragment);
            }
        });
    }
}

