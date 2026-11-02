package com.example.smartpantrymanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class RecipeMatcherTest {
    private Recipe recipeWith(Recipe.Ingredient... ings) {
        Recipe r = new Recipe(1, "Test", "steps");
        for (Recipe.Ingredient i : ings) r.addIngredient(i);
        return r;
    }

    @Test
    public void fiveNeededFourInPantry_notSuggested() {
        Recipe r = recipeWith(new Recipe.Ingredient("egg", 5, "pcs"));
        List<PantryItem> pantry = Arrays.asList(new PantryItem(1, "egg", 4, "pcs", ""));
        assertFalse(RecipeMatcher.qualifies(r, pantry));
    }

    @Test
    public void tomatoMatchesTomatoes() {
        Recipe r = recipeWith(new Recipe.Ingredient("tomato", 2, "pcs"));
        List<PantryItem> pantry = Arrays.asList(new PantryItem(1, "Tomatoes", 3, "pcs", ""));
        assertTrue(RecipeMatcher.qualifies(r, pantry));
    }

    @Test
    public void kilogramsSatisfyGramsRequirement() {
        Recipe r = recipeWith(new Recipe.Ingredient("flour", 200, "g"));
        List<PantryItem> pantry = Arrays.asList(new PantryItem(1, "flour", 1, "kg", ""));
        assertTrue(RecipeMatcher.qualifies(r, pantry));
    }

    @Test
    public void oneMissingIngredientExcludesRecipe() {
        Recipe r = recipeWith(new Recipe.Ingredient("egg", 1, "pcs"), new Recipe.Ingredient("milk", 100, "ml"));
        List<PantryItem> pantry = Arrays.asList(new PantryItem(1, "egg", 6, "pcs", ""));
        assertFalse(RecipeMatcher.qualifies(r, pantry));
    }

    @Test
    public void normalizeNames() {
        assertEquals("tomato", Normalizer.normalizeName(" Tomatoes "));
        assertEquals("egg", Normalizer.normalizeName("eggs"));
        assertEquals("potato", Normalizer.normalizeName("potatoes"));
        assertEquals("berry", Normalizer.normalizeName("berries"));
    }
}
