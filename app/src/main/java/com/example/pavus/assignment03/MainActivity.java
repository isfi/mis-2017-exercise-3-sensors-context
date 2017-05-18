package com.example.pavus.assignment03;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mAccelerator;

    AccView accView;
    FFTView fftView;
    SeekBar sampleSizeBar;
    SeekBar fftSizeBar;
    TextView sampleSizeText;
    TextView fftSizeText;
    Button goRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerator = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        sampleSizeBar = (SeekBar) findViewById(R.id.seekBar1);
        fftSizeBar = (SeekBar) findViewById(R.id.seekBar2);
        sampleSizeText = (TextView) findViewById(R.id.TextView);
        fftSizeText = (TextView) findViewById(R.id.textView2);
        accView = (AccView) findViewById(R.id.acc_view);
        fftView = (FFTView) findViewById(R.id.fft_view);
        goRunning = (Button) findViewById(R.id.goRunning);

        goRunning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MoveActivity.class);

                startActivity(intent);
            }
        });

        sampleSizeBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progressValue, boolean fromUser) {
                int progress = seekBar1.getProgress();
                accView.sampleSize = progress;
                sampleSizeText.setText("Sample size: " + String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {

            }
        });

        fftSizeBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar2, int progressValue, boolean fromUser) {
                double size = (double)seekBar2.getProgress() / 100.0;
                fftView.recWidth = Math.round(fftView.recWidthInit * size);
                fftView.recHeight = Math.round(fftView.recHeightInit * size);

                fftSizeText.setText("FFT Window size factor: " + String.valueOf(size));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar2) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar2) {

            }
        });
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        float[] linear_acceleration = event.values;
        double magnitude = Math.sqrt(Math.pow(linear_acceleration[0], 2) + Math.pow(linear_acceleration[1], 2) + Math.pow(linear_acceleration[2], 2));

        accView.Acc_X.add(new Double(linear_acceleration[0]));
        accView.Acc_Y.add(new Double(linear_acceleration[1]));
        accView.Acc_Z.add(new Double(linear_acceleration[2]));
        accView.Acc_Mag.add(new Double(magnitude));

        double[] x = new double[128];
        double[] y = new double[128];

        Arrays.fill(x, 0);
        Arrays.fill(y, 0);

        for(int i = 0; i < accView.Acc_Mag.size(); i++){
            x[i] = accView.Acc_Mag.get(i).doubleValue();
        }

        FFT curve = new FFT(fftView.FFT.length);
        curve.fft(x,y);

        for(int i = 0; i < fftView.FFT.length; i++){
            fftView.FFT[i] = Math.sqrt(Math.pow(x[i], 2) + Math.pow(y[i], 2));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerator, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}
