package com.example.enrgydrinksgpts;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.enrgydrinksgpts.canOpenTransition.CanOpenTransitionActivity;
import com.example.enrgydrinksgpts.energyDrinksCards.CardAdapter;
import com.example.enrgydrinksgpts.energyDrinksCards.CardItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<CardItem> mItems;
    private RecyclerView mRecyclerView;
    private CardAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.energy_drinks_cards_view);

        mRecyclerView = findViewById(R.id.recycler_view);
        mItems = new ArrayList<>();

        mItems.add(new CardItem(R.drawable.classic_red_bull, "Classic Red Bull", "A classic Red Bull for your daily basis needs, just like a normal gpt without any predefined settings", ContextCompat.getColor(this, R.color.classic_red_bull_background)));
        mItems.add(new CardItem(R.drawable.red_red_bull, "Watermelon Red Bull","The watermelon Red Bull (Red Red Bull) is know for his love suggestions.", ContextCompat.getColor(this, R.color.red_red_bull_background)));

        mAdapter = new CardAdapter(mItems, item -> {
            Intent intent = new Intent(MainActivity.this, CanOpenTransitionActivity.class);
            intent.putExtra("imageResId", item.getImageResourceId()); // Pass the image resource ID
            startActivity(intent);
        });

        mRecyclerView.setAdapter(mAdapter);
    }

}
