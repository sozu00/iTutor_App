package com.uca.jiniguez.itutor;


import android.Manifest;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FiltersFragment extends Fragment {

    public FiltersFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater,container,savedInstanceState);
        final View v =  inflater.inflate(R.layout.fragment_search_filters, container, false);

        final SeekBar seekBar = v.findViewById(R.id.seekBar);
        final TextView seekBarValue = v.findViewById(R.id.seekbarValue);
        final TextView skillFilter = v.findViewById(R.id.skillSearchText);
        final Button applyFilters = v.findViewById(R.id.applyFiltersButton);
        final Button clearFilters = v.findViewById(R.id.clearFiltersButton);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                seekBarValue.setText("Hasta "+String.valueOf(progress) + " km");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        clearFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seekBar.setProgress(0);
                skillFilter.setText("");
            }
        });

        return v;
    }
}

