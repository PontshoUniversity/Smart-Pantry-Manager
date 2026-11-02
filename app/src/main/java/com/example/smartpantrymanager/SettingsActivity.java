package com.example.smartpantrymanager;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class SettingsActivity extends AppCompatActivity {
    public static final String PREFS = "pantry_prefs";
    public static final String KEY_HIGHLIGHT = "highlight_expiring";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        SwitchCompat toggle = findViewById(R.id.switch_expiring);
        toggle.setChecked(prefs.getBoolean(KEY_HIGHLIGHT, true));
        toggle.setOnCheckedChangeListener((button, checked) ->
                prefs.edit().putBoolean(KEY_HIGHLIGHT, checked).apply());

        BottomNav.setup(this, R.id.nav_settings);
    }
}
