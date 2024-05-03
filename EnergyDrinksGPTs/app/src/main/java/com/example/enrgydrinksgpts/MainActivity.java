package com.example.enrgydrinksgpts;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.enrgydrinksgpts.energyDrinksCards.CardAdapter;
import com.example.enrgydrinksgpts.energyDrinksCards.CardItem;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.energy_drinks_cards_view);

        RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
        List<CardItem> mItems = new ArrayList<>();

        mItems.add(new CardItem(R.drawable.classic_red_bull, "Sample Text 1"));
        mItems.add(new CardItem(R.drawable.classic_red_bull, "Sample Text 2"));

        CardAdapter mAdapter = new CardAdapter(mItems, item -> {
            System.out.println("Clicked on: " + item.getText());
        });

        mRecyclerView.setAdapter(mAdapter);
    }
}
