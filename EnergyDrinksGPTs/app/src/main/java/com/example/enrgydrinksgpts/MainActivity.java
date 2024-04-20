package com.example.enrgydrinksgpts;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements ShakeDetector.OnShakeListener {
    private ShakeDetector shakeDetector;
    private MediaPlayerHandler mediaPlayerHandler;
    private ImageView imageView;
    private boolean isTiltedRight = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.classicRedBull);
        mediaPlayerHandler = new MediaPlayerHandler(this, R.raw.shake_sound);
        shakeDetector = new ShakeDetector(this);
        shakeDetector.register((SensorManager) getSystemService(SENSOR_SERVICE));
    }

    @Override
    public void onShake() {
        tiltImage();
        mediaPlayerHandler.play();
    }

    @Override
    public void onStopShake() {
        mediaPlayerHandler.stop();
    }

    private void tiltImage() {
        if (isTiltedRight) {
            animateTiltLeft();
        } else {
            animateTiltRight();
        }

        isTiltedRight = !isTiltedRight;
    }

    private void animateTiltLeft() {
        Animation tiltLeftAnim = AnimationUtils.loadAnimation(this, R.anim.tilt_left);
        imageView.startAnimation(tiltLeftAnim);
    }

    private void animateTiltRight() {
        Animation tiltRightAnim = AnimationUtils.loadAnimation(this, R.anim.tilt_right);
        imageView.startAnimation(tiltRightAnim);
    }

    @Override
    protected void onPause() { // when app is in the background or closed
        super.onPause();
        shakeDetector.unregister((SensorManager) getSystemService(SENSOR_SERVICE));
        mediaPlayerHandler.release();
    }

    @Override
    protected void onResume() { // when app is reopened
        super.onResume();
        shakeDetector.register((SensorManager) getSystemService(SENSOR_SERVICE));
    }
}
