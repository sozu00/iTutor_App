package com.uca.jiniguez.itutor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeacherListAdapter extends SimpleAdapter {
    private List<HashMap<String, Object>>elements;
    private MainActivity myActivity;
    TeacherListAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        myActivity = (MainActivity) context;
        elements = (List<HashMap<String, Object>>) data;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        final View v = super.getView(position, convertView, parent);
        final UserData u = (UserData) elements.get(position).get("data");

        ImageButton callTeacher = v.findViewById(R.id.phoneButton);
        RatingBar rBar = v.findViewById(R.id.voteRating);
        final ImageView profilePic = v.findViewById(R.id.profilePicView);
        rBar.setRating((float)elements.get(position).get("mRating"));

        Utilities.downloadWithTransferUtility(v, u);
        callTeacher.setOnClickListener(v2 -> {
            String phn = u.mPhone;
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+phn));
            // No explanation needed; request the permission
            if (ActivityCompat.checkSelfPermission(v2.getContext(),
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            v2.getContext().startActivity(callIntent);
        });

        ImageButton moreInfo = v.findViewById(R.id.moreInfoButton);
        moreInfo.setOnClickListener(v2 -> {
            TeacherProfileFragment profileFragment = new TeacherProfileFragment();
            profileFragment.setUserData(u);
            myActivity.setFragment(profileFragment);
        });

        //Utilities.downloadWithTransferUtility(v, u);
        final File imgFile = new File(v.getContext().getCacheDir().getAbsolutePath()+"/"+u.mID+".png");

        final Handler handler = new Handler();
        new Thread(() -> {
            int tries=0;
            do{
                try {
                    if(imgFile.exists()) {
                        final Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        handler.post(() -> {
                            profilePic.setImageBitmap(myBitmap);
                            v.invalidate();
                        });

                        tries = 1000;
                    }
                    Thread.sleep(300);
                    tries++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }while(tries <1000);
        }).start();

        return v;
    }

}
