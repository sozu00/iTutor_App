package com.uca.jiniguez.itutor;

import android.view.View;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;

public class MyBinder implements SimpleAdapter.ViewBinder {
    @Override
    public boolean setViewValue(View view, Object data, String textRepresentation) {
        if (view.getId() == R.id.voteRating) {
            String stringval = data.toString();
            float ratingValue = Float.parseFloat(stringval);
            RatingBar ratingBar = (RatingBar) view;
            ratingBar.setRating(ratingValue);
            return true;
        }
        return false;
    }
}
