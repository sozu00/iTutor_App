package com.uca.jiniguez.itutor;

import android.content.Context;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;

class VoteListAdapter extends SimpleAdapter {
    public VoteListAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
    }

}
