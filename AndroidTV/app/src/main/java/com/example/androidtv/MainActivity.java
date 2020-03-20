package com.example.androidtv;

import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends Activity{
    ImageButton searchBtn, notiBtn, mailBtn, contactBtn;
    Button sideTabClosingBtn, moviesBtn, tvShowsBtn, watchListBtn, accountBtn,settingsBtn, activitiesBtn, downloadBtn, logoutBtn;
    CircleImageView avatarBtn;
    LinearLayout layout,topLinearLayout;
    FrameLayout frameLayout;
    Fragment1 fragment1;
    Fragment2 fragment2;
    Fragment3 fragment3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        fragment1 = new Fragment1();
        loadFragment(fragment1);
        setButtonOnClickListener();
        setButtonOnFocusListener();
    }

    private void init(){
        searchBtn = findViewById(R.id.search_btn);
        notiBtn = findViewById(R.id.btn_noti);
        mailBtn = findViewById(R.id.btn_mail);
        contactBtn = findViewById(R.id.btn_contact);
        avatarBtn = findViewById(R.id.btn_avatar);
        moviesBtn = findViewById(R.id.btn_movies);
        tvShowsBtn = findViewById(R.id.btn_tvshow);
        watchListBtn = findViewById(R.id.btn_watchlist);
        accountBtn = findViewById(R.id.btn_account);
        settingsBtn = findViewById(R.id.btn_settings);
        activitiesBtn = findViewById(R.id.btn_activities);
        downloadBtn = findViewById(R.id.btn_download);
        logoutBtn = findViewById(R.id.btn_logout);
        sideTabClosingBtn = findViewById(R.id.side_tab_close_btn);
        layout = findViewById(R.id.side_tab_menu_layout);
        topLinearLayout = findViewById(R.id.top_linear_layout);
        frameLayout = findViewById(R.id.frameLayout);
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void setButtonOnClickListener(){
        moviesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moviesBtn.setTextColor(Color.WHITE);
                tvShowsBtn.setTextColor(Color.LTGRAY);
                watchListBtn.setTextColor(Color.LTGRAY);
                fragment1 = new Fragment1();
                loadFragment(fragment1);
            }
        });
        tvShowsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moviesBtn.setTextColor(Color.LTGRAY);
                tvShowsBtn.setTextColor(Color.WHITE);
                watchListBtn.setTextColor(Color.LTGRAY);
                fragment2 = new Fragment2();
                loadFragment(fragment2);
            }
        });
        watchListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moviesBtn.setTextColor(Color.LTGRAY);
                tvShowsBtn.setTextColor(Color.LTGRAY);
                watchListBtn.setTextColor(Color.WHITE);
                fragment3 = new Fragment3();
                loadFragment(fragment3);
            }
        });
        avatarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Transition transition = new Slide(Gravity.RIGHT);
                transition.addTarget(R.id.side_tab_menu_layout);
                transition.setDuration(300);
                TransitionManager.beginDelayedTransition(layout,transition);
                avatarBtn.setNextFocusForwardId(R.id.btn_account);
                accountBtn.requestFocus();
                layout.setVisibility(View.VISIBLE);
            }
        });
        sideTabClosingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Transition transition = new Slide(Gravity.RIGHT);
                transition.addTarget(R.id.side_tab_menu_layout);
                transition.setDuration(300);
                TransitionManager.beginDelayedTransition(layout,transition);
                sideTabClosingBtn.setNextFocusForwardId(R.id.btn_avatar);
                layout.setVisibility(View.GONE);
            }
        });
    }

    private void setButtonOnFocusListener(){
        ButtonFocusListener focusListener = new ButtonFocusListener();
        searchBtn.setOnFocusChangeListener(focusListener);
        notiBtn.setOnFocusChangeListener(focusListener);
        mailBtn.setOnFocusChangeListener(focusListener);
        contactBtn.setOnFocusChangeListener(focusListener);
        avatarBtn.setOnFocusChangeListener(focusListener);
        moviesBtn.setOnFocusChangeListener(focusListener);
        tvShowsBtn.setOnFocusChangeListener(focusListener);
        watchListBtn.setOnFocusChangeListener(focusListener);
        accountBtn.setOnFocusChangeListener(focusListener);
        settingsBtn.setOnFocusChangeListener(focusListener);
        activitiesBtn.setOnFocusChangeListener(focusListener);
        downloadBtn.setOnFocusChangeListener(focusListener);
        logoutBtn.setOnFocusChangeListener(focusListener);
        sideTabClosingBtn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    sideTabClosingBtn.setBackgroundColor(Color.argb(70,240,240,240));
                }else{
                    sideTabClosingBtn.setBackgroundResource(R.drawable.round_shape_button);
                }
            }
        });
    }


}
