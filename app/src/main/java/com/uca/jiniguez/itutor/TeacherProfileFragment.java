package com.uca.jiniguez.itutor;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class TeacherProfileFragment extends Fragment {

    private View v;
    private UserData userData;
    private TextView nameTextView;
    private TextView phoneTextView;
    private TextView mailTextView;
    private TextView quoteTextView;
    private RatingBar ratingBar;
    private ImageButton votesInfo;
    private ImageView photo;
    private ImageView call;
    private ImageView mail;
    private TextView formation;
    private TextView price;
    private List<CheckBox> levels;
    private Button voteButton;
    private ImageView isFavourite;
    private ListView skillsList;
    private Boolean isFavouriteB = false;


    public TeacherProfileFragment() {
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }
    @SuppressLint("SetTextI18n")
    private void loadAllData(){
        nameTextView.setText(userData.mName);
        phoneTextView.setText(userData.mPhone);
        mailTextView.setText(userData.mEmail);
        quoteTextView.setText(userData.mDescription);
        ratingBar.setRating(userData.mRating);
        String form[] = getResources().getStringArray(R.array.list_academy);
        formation.setText(form[userData.formacion]);
        price.setText(Float.valueOf(userData.price).toString());
        for (int i = 0; i < userData.levels.size(); i++)
            levels.get(i).setChecked(userData.levels.get(i));
        for(String mTeacherID : MainActivity.userData.mTeachers)
            if(mTeacherID.equals(userData.mID))
                isFavouriteB = true;
        isFavourite.setImageResource(isFavouriteB?R.drawable.big_star_btn_on:R.drawable.big_star_btn_off);
        isFavourite.invalidate();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        v = inflater.inflate(R.layout.fragment_teacher_profile, container, false);

        nameTextView = v.findViewById(R.id.nameTextView);
        phoneTextView = v.findViewById(R.id.phoneTextView);
        mailTextView = v.findViewById(R.id.mailTextView);
        quoteTextView = v.findViewById(R.id.quoteTextView);
        ratingBar = v.findViewById(R.id.voteRating);
        votesInfo = v.findViewById(R.id.votesInfo);
        photo = v.findViewById(R.id.profilePicView);
        call = v.findViewById(R.id.phoneImgView);
        mail = v.findViewById(R.id.mailImgView);
        formation = v.findViewById(R.id.academyTextView);
        price = v.findViewById(R.id.price2TextView);
        levels = new ArrayList<>();
        levels.add(v.findViewById(R.id.basicCheck));
        levels.add(v.findViewById(R.id.midCheck));
        levels.add(v.findViewById(R.id.advancedCheck));
        levels.add(v.findViewById(R.id.profesionalCheck));
        voteButton = v.findViewById(R.id.voteButton);
        isFavourite = v.findViewById(R.id.isFavouriteView);
        skillsList = v.findViewById(R.id.SkillsList);

        final File imgFile = new File(v.getContext().getCacheDir().getAbsolutePath()+"/"+userData.mID+".png");
        handleImageChanges(imgFile);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_list_item_1);
        adapter.addAll(userData.mSkills);
        skillsList.setAdapter(adapter);

        loadAllData();

        setListeners();

        return v;
    }

    private void handleImageChanges(final File imgFile) {
        final Handler handler = new Handler();
        new Thread(() -> {
            int tries=0;
            do{
                try {
                    if(imgFile.exists()) {
                        final Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        handler.post(() -> {
                            photo.setImageBitmap(myBitmap);
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
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setListeners() {
        votesInfo.setOnClickListener(view -> {
            FragmentTransaction fragmentTransaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
            VotesFragment votesFragment = new VotesFragment();
            votesFragment.setUserData(userData);

            fragmentTransaction.replace(R.id.main_frame, votesFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
        // Setting on Touch Listener for handling the touch inside ScrollView
        skillsList.setOnTouchListener((v, event) -> {
            // Disallow the touch request for parent scroll on touch of child view
            v.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        });

        call.setOnClickListener(view -> {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+phoneTextView.getText().toString()));
            // No explanation needed; request the permission
            if (ActivityCompat.checkSelfPermission(v.getContext(),
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            v.getContext().startActivity(callIntent);
        });

        mail.setOnClickListener(view -> {
            Log.i("Enviar email", "");
            String[] TO = {
                   mailTextView.getText().toString()
            };
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hola! He visto tu perfil en iTutor!");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Hola! Que tal?");
            try {
                v.getContext().startActivity(Intent.createChooser(emailIntent, "Enviar mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(v.getContext(), "Necesitas un cliente de correo.", Toast.LENGTH_SHORT).show();
            }
        });

        voteButton.setOnClickListener(view -> {
            Boolean hasBeenVotedByYou = false;
            for (VoteData v : userData.getVotes()){
                if(v.voterID.equals(MainActivity.userData.mID))
                    hasBeenVotedByYou = true;
            }
            if(hasBeenVotedByYou)
                Toast.makeText(v.getContext(), "Ya has votado a este usuario", Toast.LENGTH_SHORT).show();
            else {
                final RatingBar voteBar;
                final EditText voteComment;
                final Dialog voteDialog = new Dialog(v.getContext());
                voteDialog.setContentView(R.layout.popup_vote);
                voteDialog.setCancelable(true);
                voteBar = voteDialog.findViewById(R.id.voteBar);
                voteComment = voteDialog.findViewById(R.id.voteComment);
                voteBar.setRating(0);

                Button updateButton = voteDialog.findViewById(R.id.confirmVoteButton);
                updateButton.setOnClickListener(v -> {
                    VoteData vote = new VoteData();
                    vote.voterID = MainActivity.userData.mID;
                    vote.rating = voteBar.getProgress();
                    vote.comment = voteComment.getText().toString();
                    vote.receivingUser = userData.mID;
                    vote.voterName = MainActivity.userData.mName;
                    vote.voteUser();
                    Toast.makeText(v.getContext(), "Tu voto ha sido registrado", Toast.LENGTH_SHORT).show();
                    voteDialog.dismiss();
                    v.invalidate();
                });
                //now that the dialog is set up, it's time to show it
                voteDialog.show();
            }
        });


        isFavourite.setOnClickListener(view -> {
            isFavouriteB = !isFavouriteB;
            isFavourite.setImageResource(isFavouriteB?R.drawable.big_star_btn_on:R.drawable.big_star_btn_off);
            if(isFavouriteB)
                MainActivity.userData.mTeachers.add(userData.mID);
            else
                MainActivity.userData.mTeachers.remove(userData.mID);
            isFavourite.invalidate();
            MainActivity.userData.uploadData();
        });
    }
}
