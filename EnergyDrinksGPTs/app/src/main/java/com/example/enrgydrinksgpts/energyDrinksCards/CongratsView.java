package com.example.enrgydrinksgpts.energyDrinksCards; // Replace with your actual package name

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
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
//                .placeholder(R.drawable.loading_placeholder) // optional
                .into(imageView);


        ImageView gifImageView = findViewById(R.id.gifImageView);
        Glide.with(this)
                .asGif()
                .load(R.drawable.confeti)
                .into(gifImageView);
    }
}
