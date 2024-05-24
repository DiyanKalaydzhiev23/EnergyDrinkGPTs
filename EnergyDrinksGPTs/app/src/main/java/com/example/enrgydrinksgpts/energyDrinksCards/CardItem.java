package com.example.enrgydrinksgpts.energyDrinksCards;

import android.content.Context;
import androidx.core.content.ContextCompat;
import com.example.enrgydrinksgpts.R;

public class CardItem {
    private final int imageResourceId;
    private final String description;
    private final String name;
    private final String aiInstruction;
    private int backgroundColor;
    private final Context context;
    private final boolean unlocked;

    public CardItem(int imageResourceId, String name, String text, String aiInstruction, int backgroundColor, boolean unlocked, Context context) {
        this.imageResourceId = imageResourceId;
        this.name = name;
        this.description = text;
        this.aiInstruction = aiInstruction;
        this.unlocked = unlocked;
        this.context = context;
        this.setBackgroundColor(context, backgroundColor);
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public String getAiInstruction() {
        return aiInstruction;
    }

    public String getName() {
        return name;
    }

    public Context getContext() {
        return context;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public String getDescription() {
        return description;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Context context, int backgroundColor) {
        if (!this.unlocked) {
            this.backgroundColor = ContextCompat.getColor(context, R.color.grey_locked);
        } else {
            this.backgroundColor = backgroundColor;
        }
    }
}