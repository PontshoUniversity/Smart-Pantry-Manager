package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/** Creates the SQLite database, seeds recipes on first run and offers CRUD methods. */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "pantry.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE pantry_item(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL,"
                + " normalized_name TEXT NOT NULL, quantity REAL NOT NULL, unit TEXT, expiry_date TEXT)");
        db.execSQL("CREATE TABLE recipe(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, steps TEXT NOT NULL)");
        db.execSQL("CREATE TABLE recipe_ingredient(id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " recipe_id INTEGER NOT NULL REFERENCES recipe(id), ingredient_name TEXT NOT NULL,"
                + " normalized_name TEXT NOT NULL, quantity REAL NOT NULL, unit TEXT)");
        seedRecipes(db); // runs only when the database file is first created
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS recipe_ingredient");
        db.execSQL("DROP TABLE IF EXISTS recipe");
        db.execSQL("DROP TABLE IF EXISTS pantry_item");
        onCreate(db);
    }

    // ---------- Pantry CRUD ----------

    private ContentValues toValues(PantryItem p) {
        ContentValues v = new ContentValues();
        v.put("name", p.getName());
        v.put("normalized_name", p.getNormalizedName());
        v.put("quantity", p.getQuantity());
        v.put("unit", p.getUnit());
        v.put("expiry_date", p.getExpiryDate());
        return v;
    }

    private PantryItem readItem(Cursor c) {
        return new PantryItem(c.getLong(0), c.getString(1), c.getDouble(2), c.getString(3), c.getString(4));
    }

    private static final String[] ITEM_COLS = {"id", "name", "quantity", "unit", "expiry_date"};

    public long insertItem(PantryItem p) {
        return getWritableDatabase().insert("pantry_item", null, toValues(p));
    }

    public int updateItem(PantryItem p) {
        return getWritableDatabase().update("pantry_item", toValues(p), "id=?", new String[]{String.valueOf(p.getId())});
    }

    public int deleteItem(long id) {
        return getWritableDatabase().delete("pantry_item", "id=?", new String[]{String.valueOf(id)});
    }

    public List<PantryItem> getAllItems() {
        List<PantryItem> list = new ArrayList<>();
        try (Cursor c = getReadableDatabase().query("pantry_item", ITEM_COLS, null, null, null, null, "name COLLATE NOCASE")) {
            while (c.moveToNext()) list.add(readItem(c));
        }
        return list;
    }

    public PantryItem getItem(long id) {
        try (Cursor c = getReadableDatabase().query("pantry_item", ITEM_COLS, "id=?",
                new String[]{String.valueOf(id)}, null, null, null)) {
            return c.moveToFirst() ? readItem(c) : null;
        }
    }

    // ---------- Recipes ----------

    public List<Recipe> getAllRecipes() {
        List<Recipe> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor c = db.query("recipe", new String[]{"id", "name", "steps"}, null, null, null, null, "name")) {
            while (c.moveToNext()) {
                Recipe r = new Recipe(c.getLong(0), c.getString(1), c.getString(2));
                loadIngredients(db, r);
                list.add(r);
            }
        }
        return list;
    }

    public Recipe getRecipe(long id) {
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor c = db.query("recipe", new String[]{"id", "name", "steps"}, "id=?",
                new String[]{String.valueOf(id)}, null, null, null)) {
            if (!c.moveToFirst()) return null;
            Recipe r = new Recipe(c.getLong(0), c.getString(1), c.getString(2));
            loadIngredients(db, r);
            return r;
        }
    }

    private void loadIngredients(SQLiteDatabase db, Recipe r) {
        try (Cursor c = db.query("recipe_ingredient", new String[]{"ingredient_name", "quantity", "unit"},
                "recipe_id=?", new String[]{String.valueOf(r.getId())}, null, null, null)) {
            while (c.moveToNext()) r.addIngredient(new Recipe.Ingredient(c.getString(0), c.getDouble(1), c.getString(2)));
        }
    }

    // ---------- Seed data ----------

    /** Each ingredient is written as "name|quantity|unit". */
    private void addRecipe(SQLiteDatabase db, String name, String steps, String... ingredients) {
        ContentValues rv = new ContentValues();
        rv.put("name", name);
        rv.put("steps", steps);
        long recipeId = db.insert("recipe", null, rv);
        for (String s : ingredients) {
            String[] p = s.split("\\|");
            ContentValues iv = new ContentValues();
            iv.put("recipe_id", recipeId);
            iv.put("ingredient_name", p[0]);
            iv.put("normalized_name", Normalizer.normalizeName(p[0]));
            iv.put("quantity", Double.parseDouble(p[1]));
            iv.put("unit", p[2]);
            db.insert("recipe_ingredient", null, iv);
        }
    }

    private void seedRecipes(SQLiteDatabase db) {
        addRecipe(db, "Scrambled Eggs", "1. Beat the eggs.\n2. Melt butter in a pan.\n3. Stir the eggs gently until just set.", "egg|3|pcs", "butter|10|g");
        addRecipe(db, "Tomato Omelette", "1. Chop the tomato.\n2. Beat the eggs and pour into a hot pan.\n3. Add tomato, fold and serve.", "egg|2|pcs", "tomato|1|pcs");
        addRecipe(db, "Cheese Omelette", "1. Beat the eggs.\n2. Cook in a pan.\n3. Add cheese, fold and serve.", "egg|3|pcs", "cheese|40|g");
        addRecipe(db, "Pancakes", "1. Whisk flour, milk and egg into a batter.\n2. Fry ladles of batter until golden on both sides.", "flour|200|g", "milk|300|ml", "egg|1|pcs");
        addRecipe(db, "Mashed Potatoes", "1. Peel and boil the potatoes until soft.\n2. Mash with butter and warm milk.", "potato|4|pcs", "butter|50|g", "milk|100|ml");
        addRecipe(db, "Cheese Toast", "1. Top the bread with cheese.\n2. Grill until melted and golden.", "bread|2|pcs", "cheese|50|g");
        addRecipe(db, "French Toast", "1. Whisk eggs and milk.\n2. Dip the bread.\n3. Fry until golden on both sides.", "bread|4|pcs", "egg|2|pcs", "milk|100|ml");
        addRecipe(db, "Tomato Pasta", "1. Boil the pasta.\n2. Fry onion, add chopped tomatoes and simmer.\n3. Toss with the pasta.", "pasta|200|g", "tomato|3|pcs", "onion|1|pcs");
        addRecipe(db, "Egg Fried Rice", "1. Fry onion.\n2. Add cooked rice and eggs.\n3. Stir-fry until hot.", "rice|200|g", "egg|2|pcs", "onion|1|pcs");
        addRecipe(db, "Garlic Butter Rice", "1. Cook the rice.\n2. Fry garlic in butter.\n3. Mix through the rice.", "rice|200|g", "butter|20|g", "garlic|2|pcs");
        addRecipe(db, "Chicken and Rice", "1. Brown the chicken with onion.\n2. Add rice and water.\n3. Cover and simmer until cooked.", "chicken|300|g", "rice|200|g", "onion|1|pcs");
        addRecipe(db, "Vegetable Soup", "1. Chop the vegetables.\n2. Simmer in water for 25 minutes.", "carrot|2|pcs", "potato|2|pcs", "onion|1|pcs", "water|500|ml");
        addRecipe(db, "Banana Smoothie", "1. Blend bananas and milk until smooth.", "banana|2|pcs", "milk|250|ml");
        addRecipe(db, "Tuna Salad", "1. Flake the tuna.\n2. Chop tomatoes and onion.\n3. Mix together.", "tuna|150|g", "tomato|2|pcs", "onion|1|pcs");
        addRecipe(db, "Oat Porridge", "1. Heat oats and milk, stirring, for 5 minutes.", "oats|80|g", "milk|300|ml");
    }
}
