package com.example.enrgydrinksgpts.energyDrinksCards;

public class CardItem {
    private final int imageResourceId;
    private final String description;
    private final String name;
    private final int backgroundColor;

    public CardItem(int imageResourceId, String name, String text, int backgroundColor) {
        this.imageResourceId = imageResourceId;
        this.name = name;
        this.description = text;
        this.backgroundColor = backgroundColor;
    }

    public String getName() {
        return name;
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
}
