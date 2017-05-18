package com.example.pavus.assignment03;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class MoveActivity extends AppCompatActivity implements SensorEventListener, LocationListener {
    private List<Double> AccMag = new ArrayList<Double>();
    private FFT curve;
    private TextView statusText;

    private SensorManager sensorManager;
    private Sensor accelerator;

    private LocationManager locationManager;

    private Button chooseRunningSong;
    private Button chooseCyclingSong;

    private Uri runningSongPath = null;
    private Uri cyclingSongPath = null;

    private MediaPlayer runningMp;
    private MediaPlayer cyclingMp;

    private long lastRunning = 0;
    private float currentSpeed = 0;
    private boolean couldBeRunning = false;

    private int state = 0;

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onLocationChanged(Location location) {
        currentSpeed = location.getSpeed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move);

        runningMp = new MediaPlayer();
        cyclingMp = new MediaPlayer();

        curve = new FFT(128);
        statusText = (TextView) findViewById(R.id.statusText);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerator = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
        }

        chooseRunningSong = (Button) findViewById(R.id.chooseRunningSong);
        chooseCyclingSong = (Button) findViewById(R.id.chooseCyclingSong);

        chooseRunningSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

        chooseCyclingSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
            }
        });
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // choose running song
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                runningSongPath = intent.getData();
                try {
                    runningMp.setDataSource(runningSongPath.getPath());
                    runningMp.prepare();
                }catch(Exception e){
                    statusText.setText("Cannot load file.");
                }
            }
        }else if(requestCode == 2){ // choose cycling song
            if (resultCode == RESULT_OK) {
                cyclingSongPath = intent.getData();
                try {
                    cyclingMp.setDataSource(cyclingSongPath.getPath());
                    cyclingMp.prepare();
                }catch(Exception e){
                    statusText.setText("Cannot load file.");
                }
            }
        }
    }

    public boolean isRunning(double[] fftMag){
        boolean guessRunning = (fftMag[28] + fftMag[29] + fftMag[30] + fftMag[31] + fftMag[32]) > 50;
        boolean isRunning = false;

        long now = System.currentTimeMillis();

        if((now - lastRunning) < 1000){
            isRunning = true;
        }

        if(guessRunning){
            lastRunning = now;
        }

        return isRunning;
    }

    private void pauseRunningSong(){
        if(runningMp.isPlaying()){
            runningMp.pause();
        }
    }

    private void continueRunningSong(){
        if(!runningMp.isPlaying() && runningSongPath != null) {
            runningMp.start();
        }
    }

    private void pauseCyclingSong(){
        if(cyclingMp.isPlaying()){
            cyclingMp.pause();
        }
    }

    private void continueCyclingSong(){
        if(!cyclingMp.isPlaying() && cyclingSongPath != null) {
            cyclingMp.start();
        }
    }

    private void updateState(){
        if(currentSpeed < (6.0 / 3.6)){ // less than 6 km/h
            state = 0; // not moving
            pauseRunningSong();
            pauseCyclingSong();
            statusText.setText("Status: Not Moving");
        }else if(currentSpeed < (20 / 3.6) && couldBeRunning){ // less than 20 km/h and sufficiently shaking
            state = 1; // running
            continueRunningSong();
            pauseCyclingSong();
            statusText.setText("Status: Running");
        }else if(currentSpeed < (40 / 3.6)){ // less than 40 km/h
            state = 2; // cycling
            pauseRunningSong();
            continueCyclingSong();
            statusText.setText("Status: Cycling");
        }else{
            state = 3; // driving
            pauseRunningSong();
            pauseCyclingSong();
            statusText.setText("Status: Driving");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] linear_acceleration = event.values;
        double magnitude = Math.sqrt(Math.pow(linear_acceleration[0], 2) + Math.pow(linear_acceleration[1], 2) + Math.pow(linear_acceleration[2], 2));

        AccMag.add(magnitude);

        while(AccMag.size() > 30){
            AccMag.remove(0);
        }

        double[] x = new double[128];
        double[] y = new double[128];

        for(int n = 0; n < x.length; ++n){
            if(n < AccMag.size()){
                x[n] = AccMag.get(n);
            }else{
                x[n] = 0;
            }

            y[n] = 0;
        }

        curve.fft(x, y);

        for(int n = 0; n < x.length; ++n){
            x[n] = Math.sqrt(x[n] * x[n] + y[n] * y[n]);
        }



        couldBeRunning = isRunning(x);
        updateState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerator, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}
