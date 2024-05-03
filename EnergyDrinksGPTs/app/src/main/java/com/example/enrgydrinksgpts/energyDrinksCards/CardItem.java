package com.example.enrgydrinksgpts.energyDrinksCards;

public class CardItem {
    private final int imageResourceId;
    private final String text;

    public CardItem(int imageResourceId, String text) {
        this.imageResourceId = imageResourceId;
        this.text = text;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public String getText() {
        return text;
    }
}
