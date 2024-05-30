package com.example.enrgydrinksgpts.energyDrinksCards; // Replace with your actual package name

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.enrgydrinksgpts.MainActivity;
import com.example.enrgydrinksgpts.R;

public class CongratsView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_best_match);

        String bestMatchUrl = getIntent().getStringExtra("bestMatchUrl");

        ImageView imageView = findViewById(R.id.energyDrinkCongrats);
        Glide.with(this)
                .load(bestMatchUrl)
                .into(imageView);

        ImageView gifImageView = findViewById(R.id.gifImageView);
        Glide.with(this)
                .asGif()
                .load(R.drawable.confeti)
                .into(gifImageView);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(CongratsView.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}