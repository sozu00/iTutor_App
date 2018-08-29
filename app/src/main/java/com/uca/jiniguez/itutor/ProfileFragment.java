package com.uca.jiniguez.itutor;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment{

    private View v;
    private UserData userData;
    private String newSkill = "";
    private SkillListAdapter adapter;
    private ImageButton addSkill;
    private Button saveData;
    private ListView listView;
    private EditText nameTextView;
    private EditText phoneTextView;
    private TextView mailTextView;
    private EditText quoteTextView;
    private RatingBar ratingBar;
    private ImageButton votesInfo;
    private ImageView photo;
    private Spinner formation;
    private EditText price;
    private List<CheckBox> levels;

    public ProfileFragment() {}

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        v = inflater.inflate(R.layout.fragment_profile, container, false);
        addSkill = v.findViewById(R.id.addSkillbtn);
        saveData = v.findViewById(R.id.saveDataButton);
        listView = v.findViewById(R.id.SkillsList);
        nameTextView = v.findViewById(R.id.nameEditText);
        phoneTextView = v.findViewById(R.id.phoneEditText);
        mailTextView = v.findViewById(R.id.mailEditText);
        quoteTextView = v.findViewById(R.id.QuoteEditText);
        ratingBar = v.findViewById(R.id.voteRating);
        votesInfo = v.findViewById(R.id.votesInfo);
        photo = v.findViewById(R.id.profilePicView);
        formation = v.findViewById(R.id.spinner);
        price = v.findViewById(R.id.priceEditText);
        levels = new ArrayList<>();
        levels.add(v.findViewById(R.id.basicCheck));
        levels.add(v.findViewById(R.id.midCheck));
        levels.add(v.findViewById(R.id.advancedCheck));
        levels.add(v.findViewById(R.id.profesionalCheck));

        String[] from = { "flag","txt"};
        int[] to = { R.id.imageButton,R.id.txtList};
        adapter = new SkillListAdapter(v.getContext(), new ArrayList<HashMap<String, String>>(), R.layout.single_skill, from, to);
        listView.setAdapter(adapter);

        loadAllData();
        setListeners();
        return v;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setListeners() {
        addSkill.setOnClickListener(view -> getNewSkill());

        saveData.setOnClickListener(view -> saveAllData());

        votesInfo.setOnClickListener(view -> {
            FragmentTransaction fragmentTransaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
            VotesFragment votesFragment = new VotesFragment();
            votesFragment.setUserData(userData);

            fragmentTransaction.replace(R.id.main_frame, votesFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
        // Setting on Touch Listener for handling the touch inside ScrollView
        listView.setOnTouchListener((v, event) -> {
            // Disallow the touch request for parent scroll on touch of child view
            v.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        });

        photo.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Selecciona tu foto de perfil"), 1 );
        });
    }

    private void saveAllData() {

        userData.mName = nameTextView.getText().toString();
        userData.mPhone = phoneTextView.getText().toString();
        userData.mDescription = quoteTextView.getText().toString();
        userData.mSkills = adapter.getSkills();
        userData.formacion = formation.getSelectedItemPosition();
        userData.price = Float.valueOf(price.getText().toString());

        for (int i = 0; i < levels.size(); i++)
            userData.levels.set(i,levels.get(i).isChecked());

        if(!userData.newProfilePic.equals(""))
            Utilities.uploadWithTransferUtility(v, userData);

        userData.uploadData();
        MainActivity.userData = userData;
        Toast.makeText(v.getContext(), "Guardado realizado con éxito", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("SetTextI18n")
    private void loadAllData(){
        nameTextView.setText(userData.mName);
        phoneTextView.setText(userData.mPhone);
        mailTextView.setText(userData.mEmail);
        quoteTextView.setText(userData.mDescription);
        adapter.addSkills(userData.mSkills);
        ratingBar.setRating(userData.mRating);
        formation.setSelection(userData.formacion);
        price.setText(Float.valueOf(userData.price).toString());
        Utilities.downloadWithTransferUtility(v, userData);

        for (int i = 0; i < userData.levels.size(); i++)
            levels.get(i).setChecked(userData.levels.get(i));
        final File imgFile = new File(v.getContext().getCacheDir().getAbsolutePath()+"/"+userData.mID+".png");
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


    public void setUserData(UserData userData) {
        this.userData = userData;
    }


    private void getNewSkill(){
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Nueva materia");
        newSkill = "";

        final EditText input = new EditText(v.getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            newSkill = input.getText().toString();
            HashMap<String, String> hm = new HashMap<>();
            hm.put("txt", newSkill);
            hm.put("flag", Integer.toString(R.drawable.ic_delete));
            adapter.elements.add(hm);
            adapter.notifyDataSetChanged();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                if (Utilities.checkPermissionREAD_EXTERNAL_STORAGE(v.getContext())) {
                    userData.newProfilePic = Utilities.getPath(data.getData(),v);
                    Bitmap myBitmap = BitmapFactory.decodeFile(Utilities.getPath(data.getData(),v));
                    photo.setImageBitmap(myBitmap);
                }

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Utilities.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(v.getContext(), "GET_ACCOUNTS Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }
}


