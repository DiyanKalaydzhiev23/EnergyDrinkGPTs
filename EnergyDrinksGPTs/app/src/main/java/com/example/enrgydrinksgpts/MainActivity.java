package com.example.enrgydrinksgpts;

import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.enrgydrinksgpts.chats.ChatViewActivity;

public class MainActivity extends AppCompatActivity implements ShakeDetector.OnShakeListener {
    private ShakeDetector shakeDetector;
    private MediaPlayerHandler mediaPlayerHandler;
    private ImageView sodaCanImageView;
    private ImageView sodaFoamImage;
    private boolean isTiltedRight = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayerHandler = new MediaPlayerHandler(this, R.raw.shake_sound);

        sodaCanImageView = findViewById(R.id.classicRedBull);
        sodaFoamImage = findViewById(R.id.sodaFoam);

        shakeDetector = new ShakeDetector(this);
        shakeDetector.register((SensorManager) getSystemService(SENSOR_SERVICE));
    }

    private void setupSodaFoamImageView() {
        Animation scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_animation);

        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                sodaFoamImage.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(MainActivity.this, ChatViewActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Not needed, but must be overridden
            }
        });

        sodaFoamImage.startAnimation(scaleAnimation);
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

    @Override
    public void onCanOpen() {
        mediaPlayerHandler.stop(); // Stop any ongoing sound
        mediaPlayerHandler = new MediaPlayerHandler(this, R.raw.can_open_sound);
        mediaPlayerHandler.play(); // Play the can opening sound

        setupSodaFoamImageView(); // Start the foam animation

        shakeDetector.unregister((SensorManager) getSystemService(SENSOR_SERVICE));
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
        sodaCanImageView.startAnimation(tiltLeftAnim);
    }

    private void animateTiltRight() {
        Animation tiltRightAnim = AnimationUtils.loadAnimation(this, R.anim.tilt_right);
        sodaCanImageView.startAnimation(tiltRightAnim);
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
