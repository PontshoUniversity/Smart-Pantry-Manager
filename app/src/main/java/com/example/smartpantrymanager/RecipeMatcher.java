package com.example.smartpantrymanager;

import java.util.ArrayList;
import java.util.List;

/** Strict matching: a recipe qualifies only if EVERY ingredient is in the pantry in enough quantity. */
public final class RecipeMatcher {
    private RecipeMatcher() { }

    public static boolean qualifies(Recipe recipe, List<PantryItem> pantry) {
        if (recipe.getIngredients().isEmpty()) return false;
        for (Recipe.Ingredient ing : recipe.getIngredients()) {
            String family = Normalizer.baseUnit(ing.getUnit());
            double have = 0;
            // Add up every pantry row with the same normalized name and the same unit family
            for (PantryItem p : pantry) {
                if (p.getNormalizedName().equals(ing.getNormalizedName())
                        && Normalizer.baseUnit(p.getUnit()).equals(family)) {
                    have += Normalizer.toBaseQuantity(p.getQuantity(), p.getUnit());
                }
            }
            double need = Normalizer.toBaseQuantity(ing.getQuantity(), ing.getUnit());
            if (have + 1e-9 < need) return false; // missing or short -> exclude the whole recipe
        }
        return true;
    }

    public static List<Recipe> findMatches(List<Recipe> recipes, List<PantryItem> pantry) {
        List<Recipe> result = new ArrayList<>();
        for (Recipe r : recipes) {
            if (qualifies(r, pantry)) result.add(r);
        }
        return result;
    }
}
