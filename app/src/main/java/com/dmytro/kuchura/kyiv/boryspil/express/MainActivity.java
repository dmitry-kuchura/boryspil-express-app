package com.dmytro.kuchura.kyiv.boryspil.express;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.dmytro.kuchura.kyiv.boryspil.express.fragments.InfoFragment;
import com.dmytro.kuchura.kyiv.boryspil.express.fragments.MainFragment;
import com.dmytro.kuchura.kyiv.boryspil.express.fragments.ScheduleFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    final Fragment mainFragment = new MainFragment();
    final Fragment scheduleFragment = new ScheduleFragment();
    final Fragment infoFragment = new InfoFragment();
    final FragmentManager fragmentManager = getSupportFragmentManager();

    private BottomNavigationView navigation;

    Fragment active = mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        navigation.setOnNavigationItemReselectedListener(onNavigationItemReselectedListener);

        fragmentManager.beginTransaction().add(R.id.main_container, infoFragment, "3").hide(infoFragment).commit();
        fragmentManager.beginTransaction().add(R.id.main_container, scheduleFragment, "2").hide(scheduleFragment).commit();
        fragmentManager.beginTransaction().add(R.id.main_container, mainFragment, "1").commit();
    }

    public void changeMenu(int menuId) {
        navigation.setSelectedItemId(menuId);
    }

    public void changeFragmentToSchedule() {
        fragmentManager.beginTransaction().hide(active).show(scheduleFragment).commit();
        active = scheduleFragment;
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (String.valueOf(item)) {
                case "Home":
                    fragmentManager.beginTransaction().hide(active).show(mainFragment).commit();
                    active = mainFragment;
                    return true;

                case "Schedule":
                    fragmentManager.beginTransaction().hide(active).show(scheduleFragment).commit();
                    active = scheduleFragment;
                    return true;

                case "Information":
                    fragmentManager.beginTransaction().hide(active).show(infoFragment).commit();
                    active = infoFragment;
                    return true;
            }
            return false;
        }
    };

    private final BottomNavigationView.OnNavigationItemReselectedListener onNavigationItemReselectedListener = new BottomNavigationView.OnNavigationItemReselectedListener() {
        @Override
        public void onNavigationItemReselected(@NonNull MenuItem item) {
            switch (String.valueOf(item)) {
                case "Home":
                    fragmentManager.beginTransaction().hide(active).show(mainFragment).commit();
                    active = mainFragment;

                case "Schedule":
                    fragmentManager.beginTransaction().hide(active).show(scheduleFragment).commit();
                    active = scheduleFragment;

                case "Information":
                    fragmentManager.beginTransaction().hide(active).show(infoFragment).commit();
                    active = infoFragment;
            }
        }
    };
}