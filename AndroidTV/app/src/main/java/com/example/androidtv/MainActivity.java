package com.example.androidtv;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
    Button fragmentBtn1;
    Button fragmentBtn2;
    Button fragmentBtn3;
    ImageButton notiBtn;
    LinearLayout layout;
    Button sideTabClosingButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentBtn1 = findViewById(R.id.fragment_btn1);
        fragmentBtn2 = findViewById(R.id.fragmen_btn2);
        fragmentBtn3 = findViewById(R.id.fragmen_btn3);
        notiBtn = findViewById(R.id.btn_noti);
        layout = findViewById(R.id.side_tab_menu_layout);
        sideTabClosingButton = findViewById(R.id.sidE_tab_close_btn);
        loadFragment(new Fragment1());
        fragmentBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new Fragment1());
            }
        });
        fragmentBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new Fragment2());
            }
        });
        fragmentBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new Fragment3());
            }
        });
        notiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.VISIBLE);
            }
        });
        sideTabClosingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.GONE);
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
