package com.uca.jiniguez.itutor;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private ProfileFragment profileFragment;
    SearchFragment searchFragment;
    private FavTeachersFragment favTeachersFragment;

    public static UserData userData = new UserData();
    private List<UserData> allUsers = new ArrayList<>();
    private JSONObject datos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String json = getIntent().getStringExtra("json");
        try {
             datos = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        userData.getDataFromJson(datos);
        //userData.downloadData();
        allUsers = UserData.getUsers("",0,99,0, new ArrayList<>(Arrays.asList(true,true,true,true)));
        BottomNavigationView mMainNav = findViewById(R.id.main_nav);
        ImageButton exitButton = findViewById(R.id.exitButton);
        profileFragment = new ProfileFragment();
        searchFragment = new SearchFragment();
        favTeachersFragment = new FavTeachersFragment();

        profileFragment.setUserData(userData);
        searchFragment.setUserData(allUsers);
        setFragment(searchFragment);

        mMainNav.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.nav_search:
                    allUsers = UserData.getUsers("",0,99,0, new ArrayList<>(Arrays.asList(true,true,true,true)));
                    searchFragment.setUserData(allUsers);
                    setFragment(searchFragment);
                    return true;
                case R.id.nav_profile:
                    profileFragment.setUserData(userData);
                    setFragment(profileFragment);
                    return true;
                case R.id.nav_teachers:
                    favTeachersFragment.setUserData(userData);
                    setFragment(favTeachersFragment);
                    return true;
                default:
                    return false;
            }
        });

        mMainNav.setOnNavigationItemReselectedListener(item -> {
                switch (item.getItemId()){
                    case R.id.nav_search:
                        allUsers = UserData.getUsers("",0,99,0, new ArrayList<>(Arrays.asList(true,true,true,true)));
                        searchFragment.setUserData(allUsers);
                        setFragment(searchFragment);
                        break;
                    case R.id.nav_profile:
                        profileFragment.setUserData(userData);
                        setFragment(profileFragment);
                        break;
                    case R.id.nav_teachers:
                        favTeachersFragment.setUserData(userData);
                        setFragment(favTeachersFragment);
                        break;
                    default:
                        break;
                }
            });

        exitButton.setOnClickListener(view -> {
            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Cerrar sesión");
            builder.setMessage("¿Estás seguro?");
            builder.setPositiveButton("Sí", (dialogInterface, i) ->{
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            });
            builder.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());

            AlertDialog dialog = builder.create();
            dialog.show();
        });

    }

    public void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
}
