package com.uca.jiniguez.itutor;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {


    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;

    private ProfileFragment profileFragment;
    private SearchFragment searchFragment;
    private FavTeachersFragment favTeachersFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainFrame = findViewById(R.id.main_frame);
        mMainNav = findViewById(R.id.main_nav);

        profileFragment = new ProfileFragment();
        searchFragment = new SearchFragment();
        favTeachersFragment = new FavTeachersFragment();

        setFragment(searchFragment);

        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_search:
                        setFragment(searchFragment);
                        return true;
                    case R.id.nav_profile:
                        profileFragment.skills= new String[] {
                            "TESTEANDO",
                            "Lengua",
                            "Bicicleta"
                        };
                        setFragment(profileFragment);
                        return true;
                    case R.id.nav_teachers:
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
                            setFragment(searchFragment);
                            break;
                        case R.id.nav_profile:
                            profileFragment.skills= new String[] {
                                    "PROBADO RESELECT",
                                    "Lengua",
                                    "Bicicleta"
                            };
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
        fragmentTransaction.commit();

    }
}
