package com.shubzz.wireparent;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.shubzz.wireparent.speed.SpeedoMeterView;

public class SpeedDialog extends DialogFragment {


    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setCancelable(true);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialog);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_speed, container, false);
        SpeedoMeterView speedoMeterView = v.findViewById(R.id.speedometerview);
        speedoMeterView.setSpeed(30, true);//speed set 0 to 140
        speedoMeterView.setisborder(!speedoMeterView.isborder());//add or remove border
        speedoMeterView.setLinecolor(Color.WHITE);//set line and textcolor
        speedoMeterView.setNeedlecolor(Color.WHITE);//set speed needle color
        speedoMeterView.setbackImageResource(R.drawable.background);//you set image resource or color resource
        return v;
    }

    public void setContext(Context mapsActivity) {
        this.mContext = mapsActivity;
    }
}
