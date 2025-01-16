package com.example.urifoodie;

public class Recipe {
    private String ingredients;

    // Empty constructor required for Firestore deserialization
    public Recipe() {
    }

    public Recipe(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }
}
