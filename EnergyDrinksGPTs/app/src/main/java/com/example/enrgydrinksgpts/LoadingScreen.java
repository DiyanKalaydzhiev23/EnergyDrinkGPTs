package com.example.enrgydrinksgpts;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.enrgydrinksgpts.energyDrinksCards.CongratsView;
import com.example.enrgydrinksgpts.storage.DataManager;
import com.example.enrgydrinksgpts.storage.UnlockedEnergyDrinksPair;
import com.example.enrgydrinksgpts.utils.camera.ImageCompareUtil;

public class LoadingScreen extends AppCompatActivity implements ImageCompareUtil.BestMatchCallback {
    private DataManager dataManager;
    private String bestMatchUrl;
    private double percentDifference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen);

        // Load the GIF using Glide
        ImageView loadingGif = findViewById(R.id.loadingGif);
        ImageView loadingGif2 = findViewById(R.id.loadingGif2);
        ImageView loadingGif3 = findViewById(R.id.loadingGif3);

        Glide.with(this).asGif().load(R.drawable.loading_gif).into(loadingGif);
        Glide.with(this).asGif().load(R.drawable.loading_gif).into(loadingGif2);
        Glide.with(this).asGif().load(R.drawable.loading_gif).into(loadingGif3);

        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra("imageUrl");
        this.dataManager = new DataManager(this);

        ImageCompareUtil imageCompareUtil = new ImageCompareUtil();
        imageCompareUtil.findBestMatch(imageUrl, this);
    }

    @Override
    public void onBestMatchResult(String bestMatchUrl, double percentDifference) {
        this.bestMatchUrl = bestMatchUrl;
        this.percentDifference = percentDifference;

        new Handler().postDelayed(() -> {
            UnlockedEnergyDrinksPair unlockedEnergyDrinksPair = new UnlockedEnergyDrinksPair();
            unlockedEnergyDrinksPair.setEnergyDrink(ImageCompareUtil.imageUrls.get(bestMatchUrl));
            unlockedEnergyDrinksPair.setUnlocked(true);

            dataManager.saveData(unlockedEnergyDrinksPair);

            Intent congratsIntent = new Intent(LoadingScreen.this, CongratsView.class);
            congratsIntent.putExtra("bestMatchUrl", bestMatchUrl);
            congratsIntent.putExtra("percentDifference", percentDifference);
            startActivity(congratsIntent);
            finish();
        }, 3000);
    }

    @Override
    public void onError(String error) {
        // Handle error here, e.g., show a toast or log the error
        runOnUiThread(() -> {
            // Show an error message or handle the error appropriately
        });
    }
}