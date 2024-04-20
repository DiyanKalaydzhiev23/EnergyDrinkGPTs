package com.example.enrgydrinksgpts;

import android.os.Bundle;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.media.MediaPlayer;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 200;
    private static final int SHAKE_STOP_THRESHOLD = 50;
    private ImageView imageView;
    private boolean isTiltedRight = true;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.classicRedBull);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mediaPlayer = MediaPlayer.create(this, R.raw.shake_sound);
    }

    private void tiltImage() {
        if (isTiltedRight) {
            animateTiltLeft(imageView);
        } else {
            animateTiltRight(imageView);
        }
        isTiltedRight = !isTiltedRight;
    }

    private void animateTiltLeft(ImageView view) {
        Animation tiltLeftAnim = AnimationUtils.loadAnimation(this, R.anim.tilt_left);
        view.startAnimation(tiltLeftAnim);
    }

    private void animateTiltRight(ImageView view) {
        Animation tiltRightAnim = AnimationUtils.loadAnimation(this, R.anim.tilt_right);
        view.startAnimation(tiltRightAnim);
    }

    private void playSound() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start(); // Start playback only if it is not already playing
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 50) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    tiltImage();
                    playSound();
                } else if (speed < SHAKE_STOP_THRESHOLD && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause(); // Stop playback if shaking stops
                }

                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Can be safely ignored for this case
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);

        if (mediaPlayer != null) {
            mediaPlayer.release();  // Release media player resources
            mediaPlayer = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.shake_sound);
        }
    }
}
