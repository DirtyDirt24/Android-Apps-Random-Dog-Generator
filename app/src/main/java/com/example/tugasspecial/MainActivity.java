package com.example.tugasspecial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;



public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FrameLayout container;
    HomeFragment homeFragment = new HomeFragment();
    MyFragment myFragment = new MyFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        container = findViewById(R.id.container);
        homeFragment = new HomeFragment();
        myFragment = new MyFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if(itemId == R.id.home)
                getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();
            else if(itemId == R.id.myfragment)
                getSupportFragmentManager().beginTransaction().replace(R.id.container,myFragment).commit();

            return true;
        });

    }
}