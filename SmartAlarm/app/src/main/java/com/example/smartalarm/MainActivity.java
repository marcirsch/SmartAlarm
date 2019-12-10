package com.example.smartalarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
    BottomNavigationView navigationView;
    public double latitude, longitude = 12;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check if we have the required permissions for GPS, if not, request it
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
            //return;
        }

        //initialize location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //retrieve initial location
        retrieveLocation();

        navigationView = findViewById(R.id.nav_bar);
        navigationView.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;

            int id = menuItem.getItemId();
            switch (id) {
                case R.id.nav_home:
                    //Toast.makeText(MainActivity.this, "Pressed home", Toast.LENGTH_SHORT).show();
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.nav_alarm:
                    //Toast.makeText(MainActivity.this, "Pressed alarm", Toast.LENGTH_SHORT).show();
                    selectedFragment = new AlarmFragment();
                    break;
                case R.id.nav_joke:
                    //Toast.makeText(MainActivity.this, "Pressed joke", Toast.LENGTH_SHORT).show();
                    selectedFragment = new JokeFragment();
                    break;
                case R.id.nav_settings:
                    //Toast.makeText(MainActivity.this, "Pressed settings", Toast.LENGTH_SHORT).show();
                    selectedFragment = new SettingsFragment();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + id);
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            }
            return true;
        }
    };

    public void retrieveLocation() {
        //retrieve location
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                //we have last known location
                if (location != null)
                {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
            }
        });
    }


}
