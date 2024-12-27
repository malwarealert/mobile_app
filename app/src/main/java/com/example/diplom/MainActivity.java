package com.example.diplom;


import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import android.view.Gravity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.diplom.databinding.ActivityMainBinding;

import androidx.annotation.NonNull;

import com.example.diplom.databinding.ActivityMain2Binding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity
         {
//    BottomNavigationView bottomNavigationView;
    private ActivityMainBinding binding;
    private Button English;
    private Button Practice;
    private Button Category;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        English=findViewById(R.id.English);
        Practice=findViewById(R.id.Practice);
        Category=findViewById(R.id.cs2);
        English.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Язык выбран!",
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 0);
                toast.show();
            }
        });
        Practice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),TrainingFragment.class);
                startActivity(intent);
            }
        });
        Category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),CategoryFragment.class);
                startActivity(intent);
            }
        });

        setContentView(R.layout.activity_main);

//        bottomNavigationView
//                = findViewById(R.id.bottomNav);
//        bottomNavigationView
//                .setOnNavigationItemSelectedListener(this);
//        bottomNavigationView.setSelectedItemId(R.id.Training);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.bottomNav);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.Category, R.id.Training, R.id.Profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.flFragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.bottomNav, navController);
    }
    CategoryFragment CategoryFragment = new CategoryFragment();
    TrainingFragment TrainingFragment = new TrainingFragment();
    ProfileFragment ProfileFragment = new ProfileFragment();
//
//    @Override
//    public boolean
//    onNavigationItemSelected(@NonNull MenuItem item)
//    {
//        switch (item.getItemId()) {
//            case R.id.Category:
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.flFragment, CategoryFragment)
//                        .commit();
//                return true;
//
//            case R.id.Training:
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.flFragment, TrainingFragment)
//                        .commit();
//                return true;
//
//            case R.id.Profile:
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.flFragment, ProfileFragment)
//                        .commit();
//                return true;
//        }
//        return false;
//    }




}