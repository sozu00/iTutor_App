package com.uca.jiniguez.itutor;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomListAdapter extends SimpleAdapter {
    List<HashMap<String, String>>elements;
    public CustomListAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        elements = (List<HashMap<String, String>>) data;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        ImageButton deleteImageView = v.findViewById(R.id.imageButton);
        deleteImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v2) {
                elements.remove(position);
                notifyDataSetChanged();
            }
        });
        return v;
    }
}
