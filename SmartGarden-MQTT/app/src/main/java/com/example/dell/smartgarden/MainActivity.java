package com.example.dell.smartgarden;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.v4.app.Fragment;


import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.eclipse.paho.android.service.MqttAndroidClient;


public class MainActivity extends AppCompatActivity  implements BottomNavigationView.OnNavigationItemSelectedListener {

    private MqttAndroidClient client;

    private PahoMqttClient pahoMqttClient;
    private Toolbar toolbar;
    private String TAG = "MainActivity";
    FirebaseAuth auth;
    FirebaseUser user;
    ProgressDialog PD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);

        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }

        toolbar = findViewById(R.id.toolbar);
        loadFragment(new HomeFragment());
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {

            case R.id.navigation_home:
                fragment = new HomeFragment();
                toolbar.setTitle("Home");
                break;

            case R.id.navigation_plants:
                fragment = new PlantsFragment();
                toolbar.setTitle("Plants");
                break;

            case R.id.navigation_setting:
                fragment = new SettingFragment();
                toolbar.setTitle("Setting");
                break;
        }

        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            FragmentTransaction xfrag = getSupportFragmentManager().beginTransaction();
            xfrag.replace(R.id.fragment_container, fragment).commit();
            xfrag.addToBackStack("HomeFragment");
            return true;
        }
        return false;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.tabs) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack("HomeFragment", 0);
        } else {
            super.onBackPressed();
        }
    }
}



