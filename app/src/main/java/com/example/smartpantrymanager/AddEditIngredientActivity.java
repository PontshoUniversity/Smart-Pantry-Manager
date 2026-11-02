package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;

/** Form used for both adding (no extra) and editing (EXTRA_ITEM_ID passed by Intent). */
public class AddEditIngredientActivity extends AppCompatActivity {
    public static final String EXTRA_ITEM_ID = "item_id";
    private static final String[] UNITS = {"pcs", "g", "kg", "ml", "l", "cup", "tbsp", "tsp"};

    private EditText nameInput;
    private EditText quantityInput;
    private EditText expiryInput;
    private Spinner unitSpinner;
    private DatabaseHelper db;
    private long editId = -1; // -1 means "adding a new item"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);
        db = new DatabaseHelper(this);

        nameInput = findViewById(R.id.input_name);
        quantityInput = findViewById(R.id.input_quantity);
        expiryInput = findViewById(R.id.input_expiry);
        unitSpinner = findViewById(R.id.spinner_unit);
        unitSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, UNITS));

        TextView title = findViewById(R.id.title);
        editId = getIntent().getLongExtra(EXTRA_ITEM_ID, -1);
        if (editId != -1) {
            title.setText("Edit ingredient");
            PantryItem p = db.getItem(editId);
            if (p != null) {
                nameInput.setText(p.getName());
                quantityInput.setText(p.getQuantityText());
                expiryInput.setText(p.getExpiryDate());
                unitSpinner.setSelection(Math.max(0, Arrays.asList(UNITS).indexOf(p.getUnit())));
            }
        } else {
            title.setText("Add ingredient");
        }

        Button save = findViewById(R.id.button_save);
        save.setOnClickListener(v -> save());
    }

    private void save() {
        String name = nameInput.getText().toString().trim();
        if (name.isEmpty()) { nameInput.setError("Name is required"); return; }

        double qty;
        try {
            qty = Double.parseDouble(quantityInput.getText().toString().trim());
        } catch (NumberFormatException e) {
            quantityInput.setError("Enter a number");
            return;
        }
        if (qty <= 0) { quantityInput.setError("Must be greater than 0"); return; }

        String expiry = expiryInput.getText().toString().trim();
        if (!expiry.isEmpty() && !isValidDate(expiry)) { expiryInput.setError("Use yyyy-MM-dd"); return; }

        PantryItem item = new PantryItem(editId, name, qty, (String) unitSpinner.getSelectedItem(), expiry);
        if (editId == -1) db.insertItem(item); else db.updateItem(item);
        finish();
    }

    private boolean isValidDate(String s) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        f.setLenient(false); // reject things like 2026-13-45
        try { f.parse(s); return true; } catch (ParseException e) { return false; }
    }
}
