package com.example.enrgydrinksgpts;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.enrgydrinksgpts.canOpenTransition.CanOpenTransitionActivity;
import com.example.enrgydrinksgpts.energyDrinksCards.CardAdapter;
import com.example.enrgydrinksgpts.energyDrinksCards.CardItem;
import com.example.enrgydrinksgpts.storage.DataManager;
import com.example.enrgydrinksgpts.storage.UnlockedEnergyDrinksPair;
import com.example.enrgydrinksgpts.utils.cloudinary.CloudinaryMethods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private List<CardItem> CanCards;
    private RecyclerView CansRecyclerView;
    private CardAdapter CansAdapter;
    private DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.energy_drinks_cards_view);

        dataManager = new DataManager(this);

        if (!CloudinaryMethods.hasBeenInitialized()) {
            Map<String, String> config = new HashMap<>();
            config.put("cloud_name", BuildConfig.cloudinaryApiCloudName);
            config.put("api_secret", BuildConfig.cloudinaryApiSecret);
            config.put("api_key", BuildConfig.cloudinaryApiKey);
            CloudinaryMethods.initialize(this, config);
        }

        CansRecyclerView = findViewById(R.id.recycler_view);
        CanCards = new ArrayList<>();

        addCardItems();

        CansAdapter = new CardAdapter(CanCards, item -> {
            Intent intent = new Intent(MainActivity.this, CanOpenTransitionActivity.class);
            intent.putExtra("imageResId", item.getImageResourceId());
            intent.putExtra("aiInstruction", item.getAiInstruction());
            startActivity(intent);
        });

        CansRecyclerView.setAdapter(CansAdapter);

        Button openCameraButton = findViewById(R.id.openCameraButton);
        openCameraButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CameraActivity.class);
            startActivity(intent);
        });
    }

    private void addCardItems() {
        UnlockedEnergyDrinksPair unlockedData = dataManager.loadData();

        CanCards.add(
                new CardItem(
                        R.drawable.classic_red_bull,
                        "Classic Red Bull",
                        "A classic Red Bull for your daily basis needs, just like a normal gpt without any predefined settings",
                        "You are a classic AI assistant that helps with everyday tasks.",
                        ContextCompat.getColor(this, R.color.classic_red_bull_background),
                        true,
                        this
                )
        );
        CanCards.add(
                new CardItem(
                        R.drawable.red_red_bull,
                        "Watermelon Red Bull",
                        "The Watermelon Red Bull (Red Red Bull) is known for his love suggestions.",
                        "You are AI that helps with love problems. Even tho you don't have feelings the user knows that everything you say is just a suggestion and you don't need to remind them that you are just an AI.",
                        ContextCompat.getColor(this, R.color.red_red_bull_background),
                        isUnlocked(unlockedData, "Watermelon Red Bull"),
                        this
                )
        );
        CanCards.add(
                new CardItem(
                        R.drawable.sugar_free_red_bull,
                        "Sugar Free Red Bull",
                        "The Sugar Free Red Bull is really good at giving healthy life advices.",
                        "You are AI that gives the best healthy life instructions. Even tho you are not a professional trainer or doctor users know that and you don't need to remind them, just give them the best advice you can about their health.",
                        ContextCompat.getColor(this, R.color.sugar_free_red_bull_background),
                        isUnlocked(unlockedData, "Sugar Free Red Bull"),
                        this
                )
        );
    }

    private boolean isUnlocked(UnlockedEnergyDrinksPair unlockedData, String energyDrinkName) {
        return unlockedData != null && energyDrinkName.equals(unlockedData.getEnergyDrink()) && unlockedData.isUnlocked();
    }
}
