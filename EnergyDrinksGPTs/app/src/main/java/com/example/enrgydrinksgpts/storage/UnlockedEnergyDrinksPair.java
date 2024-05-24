package com.example.enrgydrinksgpts.storage;

public class UnlockedEnergyDrinksPair {
    private String energyDrink;
    private boolean unlocked;

    public String getEnergyDrink() {
        return energyDrink;
    }

    public void setEnergyDrink(String energyDrink) {
        this.energyDrink = energyDrink;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }
}