package com.example.smartpantrymanager;

import android.app.Activity;
import android.content.Intent;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/** Wires the bottom navigation bar to the three main screens using Intents. */
public final class BottomNav {
    private BottomNav() { }

    public static void setup(Activity activity, int selectedId) {
        BottomNavigationView nav = activity.findViewById(R.id.bottom_nav);
        nav.setSelectedItemId(selectedId);
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == selectedId) return true;
            Class<?> target;
            if (id == R.id.nav_pantry) target = PantryListActivity.class;
            else if (id == R.id.nav_suggested) target = SuggestedRecipesActivity.class;
            else target = SettingsActivity.class;
            activity.startActivity(new Intent(activity, target));
            activity.overridePendingTransition(0, 0);
            activity.finish();
            return true;
        });
    }
}
