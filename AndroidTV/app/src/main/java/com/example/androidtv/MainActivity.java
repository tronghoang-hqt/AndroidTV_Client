package com.example.androidtv;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    Button fragmentBtn1;
    Button fragmentBtn2;
    Button fragmentBtn3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentBtn1 = findViewById(R.id.movies);
        fragmentBtn2 = findViewById(R.id.tvshow);
        fragmentBtn3 = findViewById(R.id.watchlist);
        loadFragment(new Fragment1());
        fragmentBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentBtn1.setTextColor(Color.WHITE);
                fragmentBtn2.setTextColor(Color.LTGRAY);
                fragmentBtn3.setTextColor(Color.LTGRAY);
                loadFragment(new Fragment1());
            }
        });
        fragmentBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentBtn1.setTextColor(Color.LTGRAY);
                fragmentBtn2.setTextColor(Color.WHITE);
                fragmentBtn3.setTextColor(Color.LTGRAY);
                loadFragment(new Fragment2());
            }
        });
        fragmentBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentBtn1.setTextColor(Color.LTGRAY);
                fragmentBtn2.setTextColor(Color.LTGRAY);
                fragmentBtn3.setTextColor(Color.WHITE);
                loadFragment(new Fragment3());
            }
        });


    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}
