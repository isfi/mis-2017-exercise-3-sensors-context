package com.example.pavus.assignment03;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.R.attr.factor;


public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mAccelerator;

    acc_view Acc_View;
    fft_view FFT_View;
    SeekBar seekBar1;
    SeekBar seekBar2;
    TextView TextView;
    TextView TextView2;


    public int pro;
    public float size, resize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerator = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        seekBar1 = (SeekBar) findViewById(R.id.seekBar1);
        seekBar2 = (SeekBar) findViewById(R.id.seekBar2);
        TextView = (TextView) findViewById(R.id.TextView);
        TextView2 = (TextView) findViewById(R.id.textView2);
        Acc_View = (acc_view) findViewById(R.id.acc_view);
        FFT_View = (fft_view) findViewById(R.id.fft_view);



        final List<String> Acc_X = new ArrayList<String>();

        seekBar1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar1, int progressValue, boolean fromUser) {

                pro = seekBar1.getProgress();


                TextView.setText("Change sample size to:" + String.valueOf(pro));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {

                Acc_View.samplesize = pro;

                if(Acc_View.samplesize < Acc_View.Acc_X.size() ){
                    int temp = Acc_View.Acc_X.size() - Acc_View.samplesize;
                    for(int i = 0; i<temp; i++) {
                        Acc_View.Acc_X.remove(0);
                    }
                }

                if(Acc_View.samplesize < Acc_View.Acc_Y.size() ){
                    int temp = Acc_View.Acc_Y.size() - Acc_View.samplesize;
                    for(int i = 0; i<temp; i++) {
                        Acc_View.Acc_Y.remove(0);
                    }
                }

                if(Acc_View.samplesize < Acc_View.Acc_Z.size() ){
                    int temp = Acc_View.Acc_Z.size() - Acc_View.samplesize;
                    for(int i = 0; i<temp; i++) {
                        Acc_View.Acc_Z.remove(0);
                    }
                }

                if(Acc_View.samplesize < Acc_View.Acc_Mag.size() ){
                    int temp = Acc_View.Acc_Mag.size() - Acc_View.samplesize;
                    for(int i = 0; i<temp; i++) {
                        Acc_View.Acc_Mag.remove(0);
                    }
                }


            }
        });

        seekBar2.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar2, int progressValue, boolean fromUser) {

                size = seekBar2.getProgress();
                resize = size/100;
                //lParams1.height = resize;
                FFT_View.RecWidth = Math.round(FFT_View.RecWidthInit *resize);
                FFT_View.RecHeight = Math.round(FFT_View.RecHeightInit *resize);

                TextView2.setText(String.valueOf(FFT_View.max));

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
        // calculation for values from : https://developer.android.com/reference/android/hardware/SensorEvent.html#values
        float[] linear_acceleration = event.values;
        float[] gravity = new float[]{9.81f,9.81f,9.81f};
        double magnitude;
        final float alpha = 0.8f;

        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        linear_acceleration[0] = event.values[0] - gravity[0];
        linear_acceleration[1] = event.values[1] - gravity[1];
        linear_acceleration[2] = event.values[2] - gravity[2];
        magnitude = Math.pow(linear_acceleration[0], 2)+Math.pow(linear_acceleration[1], 2)+Math.pow(linear_acceleration[2], 2);
        magnitude = Math.sqrt(magnitude);

        Acc_View.Acc_X.add(String.valueOf(linear_acceleration[0]));
        Acc_View.Acc_Y.add(String.valueOf(linear_acceleration[1]));
        Acc_View.Acc_Z.add(String.valueOf(linear_acceleration[2]));
        Acc_View.Acc_Mag.add(String.valueOf(magnitude));

        double[] x = new double[512];
        double[] y = new double[512];
        Arrays.fill(x,0);
        Arrays.fill(y,0);

        for(int i = 0; i < Acc_View.Acc_Mag.size(); i++){
            x[i] = Double.parseDouble(Acc_View.Acc_Mag.get(i));
        }

        FFT fftobject = new FFT(512);
        fftobject.fft(x,y);

        for(int i = 0; i<512; i++){
            x[i] = Math.pow(x[i], 2)+Math.pow(y[i], 2);
            x[i] = Math.sqrt(x[i]);
            FFT_View.FFT[i]=x[i];
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
