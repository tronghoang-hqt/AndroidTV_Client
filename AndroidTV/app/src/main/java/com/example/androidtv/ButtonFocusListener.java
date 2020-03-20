package com.example.androidtv;

import android.graphics.Color;
import android.view.View;

public class ButtonFocusListener implements View.OnFocusChangeListener {
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){
            v.setBackgroundColor(Color.argb(70,240,240,240));
        }else{
            v.setBackgroundResource(0);
        }
    }
}
