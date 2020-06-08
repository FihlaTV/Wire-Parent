package com.shubzz.wireparent;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.shubzz.wireparent.speed.SpeedoMeterView;


public class SpeedActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_speed);

        SpeedoMeterView speedoMeterView = findViewById(R.id.speedometerview);
        speedoMeterView.setSpeed(60, true);//speed set 0 to 140
        speedoMeterView.setisborder(!speedoMeterView.isborder());//add or remove border
        speedoMeterView.setLinecolor(Color.WHITE);//set line and textcolor
        speedoMeterView.setNeedlecolor(Color.WHITE);//set speed needle color
        speedoMeterView.setbackImageResource(R.drawable.background);//you set image resource or color resource


    }
}
