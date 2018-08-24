package com.uca.jiniguez.itutor;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FiltersFragment extends Fragment {
    private SeekBar ratingBar;
    private SeekBar maxPriceBar;
    private TextView maxPriceBarValue;
    private TextView skillFilter;
    private TextView ratingBarValue;
    private Button applyFilters;
    private Button clearFilters;
    private List<CheckBox> levels;
    private Spinner formacion;

    public FiltersFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater,container,savedInstanceState);
        final View v =  inflater.inflate(R.layout.fragment_search_filters, container, false);

        ratingBar = v.findViewById(R.id.ratingFilter);
        maxPriceBar = v.findViewById(R.id.maxPriceFilter);
        maxPriceBarValue = v.findViewById(R.id.maxPriceBarValue);
        ratingBarValue = v.findViewById(R.id.ratingBarValue);
        skillFilter = v.findViewById(R.id.skillSearchText);
        applyFilters = v.findViewById(R.id.applyFiltersButton);
        clearFilters = v.findViewById(R.id.clearFiltersButton);
        levels = new ArrayList<>();
        levels.add((CheckBox) v.findViewById(R.id.basicCheckFilter));
        levels.add((CheckBox) v.findViewById(R.id.midCheckFilter));
        levels.add((CheckBox) v.findViewById(R.id.advancedCheckFilter));
        levels.add((CheckBox) v.findViewById(R.id.profesionalCheckFilter));
        formacion = v.findViewById(R.id.spinnerFilter);

        resetFilters();
        setSeekBarsListeners();
        setButtonsListeners(v);

        return v;
    }

    private void setSeekBarsListeners() {
        ratingBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                ratingBarValue.setText("Desde "+String.valueOf(progress));
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        maxPriceBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                maxPriceBarValue.setText("Hasta "+String.valueOf(progress)+"€/h");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void setButtonsListeners(final View v) {
        clearFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetFilters();
            }
        });

        applyFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity myActivity;
                myActivity = (MainActivity) v.getContext();

                final List<Boolean> lvl = new ArrayList<>();
                for (CheckBox L: levels) {
                    lvl.add(L.isChecked());
                }
                List<UserData> users = UserData.getUsers(skillFilter.getText().toString(),
                        ratingBar.getProgress(),
                        maxPriceBar.getProgress(),
                        formacion.getSelectedItemPosition(),
                        lvl);
                myActivity.searchFragment.setUserData(users, v);
                myActivity.setFragment(myActivity.searchFragment);
            }
        });
    }

    private void resetFilters() {
        skillFilter.setText("");
        maxPriceBar.setProgress(99);
        ratingBar.setProgress(0);
        ratingBarValue.setText("Desde 0");
        maxPriceBarValue.setText("Hasta 99€/h");
        for(CheckBox c : levels)
            c.setChecked(false);
    }
}

