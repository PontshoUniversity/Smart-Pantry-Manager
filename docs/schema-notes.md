# Schema Notes

## Why SQLite
- Works fully offline and needs no backend or account setup.
- Built into Android, so the app is easy to run and demo (data persists after the app is closed and reopened).
- Fits the size of this project. Firebase adds authentication and configuration, and PostgreSQL would need a separate server.

The database is created and managed through `SQLiteOpenHelper` (`DatabaseHelper.java`).

## Tables

### pantry_item
Ingredients the user currently has.

| Column | Type | Notes |
|---|---|---|
| id | INTEGER | Primary key, auto-increment |
| name | TEXT | Name as the user typed it |
| normalized_name | TEXT | Lowercase, trimmed, singularized (used for matching) |
| quantity | REAL | Must be greater than 0 |
| unit | TEXT | pcs, g, kg, ml, l, cup, tbsp, tsp |
| expiry_date | TEXT | yyyy-MM-dd, optional |

### recipe
| Column | Type | Notes |
|---|---|---|
| id | INTEGER | Primary key, auto-increment |
| name | TEXT | Recipe title |
| steps | TEXT | Numbered steps as plain text |

### recipe_ingredient
| Column | Type | Notes |
|---|---|---|
| id | INTEGER | Primary key, auto-increment |
| recipe_id | INTEGER | Foreign key to recipe.id |
| ingredient_name | TEXT | Display name |
| normalized_name | TEXT | Used for matching against pantry items |
| quantity | REAL | Amount required |
| unit | TEXT | Same unit list as pantry items |

Recipes and ingredients are split into two tables because one recipe has many ingredients. This avoids repeating recipe data and gives a clean one-to-many relationship.

## Seed data
15 recipes are inserted in `onCreate`, which only runs when the database file is first created.

## Strict matching rules
1. Names are normalized (lowercase, trim, simple singularization: ies to y, oes to o, trailing s removed), so "Tomatoes" matches "tomato".
2. Units are converted to a base unit: kg to g, l/cup/tbsp/tsp to ml. Items counted in pieces have no conversion.
3. A recipe is suggested only if every ingredient is in the pantry with at least the required amount. If one ingredient is missing or short, the recipe is excluded.
