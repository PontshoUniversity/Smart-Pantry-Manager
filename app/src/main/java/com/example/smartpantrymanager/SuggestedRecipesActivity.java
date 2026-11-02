package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SuggestedRecipesActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private RecipeAdapter adapter;
    private TextView emptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested);
        db = new DatabaseHelper(this);
        emptyText = findViewById(R.id.empty_text);

        adapter = new RecipeAdapter(recipe -> {
            Intent intent = new Intent(this, RecipeDetailActivity.class);
            intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, recipe.getId());
            startActivity(intent);
        });
        RecyclerView recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);

        BottomNav.setup(this, R.id.nav_suggested);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Re-run the strict match every time the screen is shown
        List<Recipe> matches = RecipeMatcher.findMatches(db.getAllRecipes(), db.getAllItems());
        adapter.setData(matches);
        emptyText.setVisibility(matches.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
