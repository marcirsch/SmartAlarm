package com.example.smartalarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {


    BottomNavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        navigationView = findViewById(R.id.nav_bar);
        navigationView.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;

            int id = menuItem.getItemId();
            switch (id) {
                case R.id.nav_home:
                    Toast.makeText(MainActivity.this, "Pressed home", Toast.LENGTH_SHORT).show();
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.nav_alarm:
                    Toast.makeText(MainActivity.this, "Pressed alarm", Toast.LENGTH_SHORT).show();
                    selectedFragment = new AlarmFragment();
                    break;
                case R.id.nav_joke:
                    Toast.makeText(MainActivity.this, "Pressed joke", Toast.LENGTH_SHORT).show();
                    selectedFragment = new JokeFragment();
                    break;
                case R.id.nav_settings:
                    Toast.makeText(MainActivity.this, "Pressed settings", Toast.LENGTH_SHORT).show();
                    selectedFragment = new SettingsFragment();
                    break;
            }

            if(selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            }
            return true;
        }
    };


}
