package com.uca.jiniguez.itutor;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;

    private ProfileFragment profileFragment;
    SearchFragment searchFragment;
    private FavTeachersFragment favTeachersFragment;

    public UserData userData = new UserData();
    public List<UserData> allUsers = new ArrayList<>();
    JSONObject datos;
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
        allUsers = UserData.getUsers("",0,null,0);
        mMainFrame = findViewById(R.id.main_frame);
        mMainNav = findViewById(R.id.main_nav);

        profileFragment = new ProfileFragment();
        searchFragment = new SearchFragment();
        favTeachersFragment = new FavTeachersFragment();

        profileFragment.setUserData(userData);
        searchFragment.setUserData(allUsers);
        setFragment(searchFragment);

        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_search:
                        allUsers = UserData.getUsers("",0,null,0);
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
            }
        });

        mMainNav.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.nav_search:
                            allUsers = UserData.getUsers("",0,null,0);
                            searchFragment.setUserData(allUsers);
                            setFragment(searchFragment);
                            break;
                        case R.id.nav_profile:
                            profileFragment.setUserData(userData);
                            setFragment(profileFragment);
                            break;
                        case R.id.nav_teachers:
                            setFragment(favTeachersFragment);
                            break;
                        default:
                            break;
                    }
                }
            });

    }

    public void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
}
