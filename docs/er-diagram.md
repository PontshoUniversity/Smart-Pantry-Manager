# Entity Relationship Diagram

Smart Pantry Manager stores its data in three SQLite tables. One recipe has many ingredients (a real foreign key). A pantry item is matched to a recipe ingredient by its `normalized_name` at run time, so that link is logical and not a foreign key.

```mermaid
erDiagram
    RECIPE ||--|{ RECIPE_INGREDIENT : "has"
    PANTRY_ITEM }o..o{ RECIPE_INGREDIENT : "matched by normalized_name"

    PANTRY_ITEM {
        INTEGER id PK
        TEXT name
        TEXT normalized_name
        REAL quantity
        TEXT unit
        TEXT expiry_date
    }

    RECIPE {
        INTEGER id PK
        TEXT name
        TEXT steps
    }

    RECIPE_INGREDIENT {
        INTEGER id PK
        INTEGER recipe_id FK
        TEXT ingredient_name
        TEXT normalized_name
        REAL quantity
        TEXT unit
    }
```
