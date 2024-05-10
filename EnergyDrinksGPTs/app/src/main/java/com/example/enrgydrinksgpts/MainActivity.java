package com.example.enrgydrinksgpts;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudinary.android.MediaManager;
import com.example.enrgydrinksgpts.canOpenTransition.CanOpenTransitionActivity;
import com.example.enrgydrinksgpts.energyDrinksCards.CardAdapter;
import com.example.enrgydrinksgpts.energyDrinksCards.CardItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private List<CardItem> CanCards;
    private RecyclerView CansRecyclerView;
    private CardAdapter CansAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.energy_drinks_cards_view);

        Map config = new HashMap();
        config.put("api_secret", BuildConfig.cloudinaryApiSecret);
        config.put("api_key", BuildConfig.cloudinaryApiKey);
        MediaManager.init(this, config);

        CansRecyclerView = findViewById(R.id.recycler_view);
        CanCards = new ArrayList<>();

        CanCards.add(
            new CardItem(
                R.drawable.classic_red_bull,
                "Classic Red Bull",
                "A classic Red Bull for your daily basis needs, just like a normal gpt without any predefined settings",
                "You are a classic AI assistant that helps with everyday tasks.",
                ContextCompat.getColor(this, R.color.classic_red_bull_background)
            )
        );
        CanCards.add(
                new CardItem( 
                        R.drawable.red_red_bull,
                        "Watermelon Red Bull",
                        "The watermelon Red Bull (Red Red Bull) is know for his love suggestions.",
                        "You are AI that helps with love problems. Even tho you don't have feelings the user knows that everything you say its just a suggestion and you don't need to remind them that you are just an AI.",
                        ContextCompat.getColor(this, R.color.red_red_bull_background)
                )
        );

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
}
