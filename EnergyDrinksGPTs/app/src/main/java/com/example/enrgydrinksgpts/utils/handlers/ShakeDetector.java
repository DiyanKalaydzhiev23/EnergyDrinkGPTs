package com.example.enrgydrinksgpts.utils.handlers;

import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class ShakeDetector implements SensorEventListener {

    private static final int SHAKE_THRESHOLD = 200;
    private static final int SHAKE_STOP_THRESHOLD = 50;
    private static final int CAN_OPEN_THRESHOLD = 1200;
    private long lastUpdate = 0;
    private long timeStartedPlaying;
    private float last_x, last_y, last_z;
    private final OnShakeListener listener;

    public interface OnShakeListener {
        void onShake();
        void onStopShake();
        void onCanOpen();
    }

    public ShakeDetector(OnShakeListener listener) {
        this.listener = listener;
        this.timeStartedPlaying = 0;
    }

    public void register(SensorManager sensorManager) {
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregister(SensorManager sensorManager) {
        sensorManager.unregisterListener(this);
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

                long diffTimeFromShakingStart = curTime -  this.timeStartedPlaying;

                if (diffTimeFromShakingStart >= CAN_OPEN_THRESHOLD && this.timeStartedPlaying != 0) {
                    listener.onCanOpen();
                }

                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    if (this.timeStartedPlaying == 0) {
                        this.timeStartedPlaying = curTime;
                    }

                    listener.onShake();
                } else if (speed < SHAKE_STOP_THRESHOLD) {
                    this.timeStartedPlaying = 0;
                    listener.onStopShake();
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
}
