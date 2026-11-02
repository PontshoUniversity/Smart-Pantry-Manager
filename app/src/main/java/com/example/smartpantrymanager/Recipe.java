package com.example.smartpantrymanager;

import java.util.ArrayList;
import java.util.List;

/** A recipe and its ingredients (recipe + recipe_ingredient tables). */
public class Recipe {
    private final long id;
    private final String name;
    private final String steps;
    private final List<Ingredient> ingredients = new ArrayList<>();

    public Recipe(long id, String name, String steps) {
        this.id = id;
        this.name = name;
        this.steps = steps;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public String getSteps() { return steps; }
    public List<Ingredient> getIngredients() { return ingredients; }
    public void addIngredient(Ingredient i) { ingredients.add(i); }

    /** One required ingredient of a recipe. */
    public static class Ingredient {
        private final String name;
        private final String normalizedName;
        private final double quantity;
        private final String unit;

        public Ingredient(String name, double quantity, String unit) {
            this.name = name;
            this.normalizedName = Normalizer.normalizeName(name);
            this.quantity = quantity;
            this.unit = unit;
        }

        public String getName() { return name; }
        public String getNormalizedName() { return normalizedName; }
        public double getQuantity() { return quantity; }
        public String getUnit() { return unit; }
    }
}
