package com.uca.jiniguez.itutor;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeacherListAdapter extends SimpleAdapter {
    List<HashMap<String, String>>elements;
    public TeacherListAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        elements = (List<HashMap<String, String>>) data;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        final View v = super.getView(position, convertView, parent);
        ImageButton callTeacher = v.findViewById(R.id.callTeacherButton);
        callTeacher.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v2) {
                String phn = elements.get(position).get("phone");
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+phn));
                // No explanation needed; request the permission
                if (ActivityCompat.checkSelfPermission(v2.getContext(),
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                v2.getContext().startActivity(callIntent);
            }
        });
        return v;
    }
}
