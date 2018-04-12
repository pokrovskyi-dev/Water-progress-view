package com.mobismarthealth.sample;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.mobismarthealth.library.WaterProgressView;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener{

    Context context = this;

    WaterProgressView waterProgressView;

    // Main
    SeekBar mainArcProgressSeekBar, mainArcStartAngleSeekBar, mainArcWidthSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        waterProgressView = (WaterProgressView)findViewById(R.id.waterProgressView);
        mainArcProgressSeekBar = (SeekBar)findViewById(R.id.mainArcProgressSeekBar);
        mainArcStartAngleSeekBar = (SeekBar)findViewById(R.id.mainArcStartAngleSeekBar);
        mainArcWidthSeekBar = (SeekBar)findViewById(R.id.mainArcWidthSeekBar);

        mainArcProgressSeekBar.setOnSeekBarChangeListener(this);
        mainArcStartAngleSeekBar.setOnSeekBarChangeListener(this);
        mainArcWidthSeekBar.setOnSeekBarChangeListener(this);

        mainArcProgressSeekBar.setProgress(waterProgressView.getMainArcProgress());
        mainArcStartAngleSeekBar.setProgress(waterProgressView.getMainArcStartAngle());
        mainArcWidthSeekBar.setProgress(waterProgressView.getMainArcWidth());

        waterProgressView.setAmplitudeRatio(20);
        waterProgressView.setWaveEnable(true);

        waterProgressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Click!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
        switch (seekBar.getId()){
            case R.id.mainArcProgressSeekBar:
                waterProgressView.setMainArcProgress(progress);
                break;
            case R.id.mainArcStartAngleSeekBar:
                waterProgressView.setMainArcStartAngle(progress);
                break;
            case R.id.mainArcWidthSeekBar:
                waterProgressView.setMainArcWidth(progress);
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // no need, so empty
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // no need, so empty
    }
}
