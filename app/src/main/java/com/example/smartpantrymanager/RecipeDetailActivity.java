package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {
    public static final String EXTRA_RECIPE_ID = "recipe_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        Recipe recipe = new DatabaseHelper(this).getRecipe(getIntent().getLongExtra(EXTRA_RECIPE_ID, -1));
        if (recipe == null) { finish(); return; }

        ((TextView) findViewById(R.id.recipe_title)).setText(recipe.getName());
        StringBuilder sb = new StringBuilder();
        for (Recipe.Ingredient i : recipe.getIngredients()) {
            sb.append("\u2022 ").append(Normalizer.formatQuantity(i.getQuantity())).append(" ")
              .append(i.getUnit()).append(" ").append(i.getName()).append("\n");
        }
        ((TextView) findViewById(R.id.recipe_ingredients)).setText(sb.toString().trim());
        ((TextView) findViewById(R.id.recipe_steps)).setText(recipe.getSteps());
    }
}
