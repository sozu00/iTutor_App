package com.uca.jiniguez.itutor;

import android.Manifest;
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

public class VoteListAdapter extends SimpleAdapter {
    public VoteListAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

}
