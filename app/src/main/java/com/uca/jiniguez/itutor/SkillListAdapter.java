package com.uca.jiniguez.itutor;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
class SkillListAdapter extends SimpleAdapter {
    final List<HashMap<String, String>>elements;
    public SkillListAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        elements = (List<HashMap<String, String>>) data;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        ImageButton deleteImageView = v.findViewById(R.id.imageButton);
        deleteImageView.setOnClickListener(v2 -> {
            elements.remove(position);
            notifyDataSetChanged();
        });
        return v;
    }

    public void addSkills(List<String> skills) {
        for(String skill : skills){
            HashMap<String, String> hm = new HashMap<>();
            hm.put("txt", skill);
            hm.put("flag", Integer.toString(R.drawable.ic_delete ));
            elements.add(hm);
        }
    }

    public List<String> getSkills(){
        List<String> skills = new ArrayList<>();
        for(HashMap<String, String> a: elements){
            skills.add(a.get("txt"));
        }
        return skills;
    }
}
