# Smart Pantry Manager

An Android app (Java, SQLite) that tracks the ingredients in your pantry and suggests recipes you can make with what you already have. Built for Mobile App Development 700 at Richfield.

## Features
- Add, edit and delete pantry ingredients, with input validation (empty name, quantity of 0 or less, invalid date)
- Delete confirmation dialog
- Suggested recipes screen using strict matching: a recipe appears only when every ingredient is available in enough quantity
- Empty-state messages, recipe detail screen with ingredients and steps
- Settings screen (SharedPreferences) to highlight items that expire within 3 days
- Bottom navigation between Pantry, Suggested and Settings
- Data persists after the app is closed and reopened

## Database choice
The app uses **SQLite** through `SQLiteOpenHelper`. It works offline, needs no backend or account, is built into Android, and is simple to demonstrate and justify for a project of this size. See [docs/schema-notes.md](docs/schema-notes.md) for the tables and matching rules.

## Documentation
- [ER diagram](docs/er-diagram.md)
- [Screen flow](docs/screen-flow.md)
- [Schema notes](docs/schema-notes.md)

## Tech
- Java only (no Kotlin), XML layouts, Activities with Intents, RecyclerView and adapters
- minSdk 24, tested on an Android emulator (API 34)
- JUnit tests for the matching logic (`RecipeMatcherTest`)

## Setup and run
1. Clone the repo: `git clone https://github.com/PontshoUniversity/Smart-Pantry-Manager.git`
2. Open the folder in Android Studio.
3. Wait for Gradle to sync (File > Sync Project with Gradle Files).
4. Create or select an emulator (for example Pixel, API 34).
5. Press the green Run button.

## Run the unit tests
In Android Studio, right-click `RecipeMatcherTest` (in `app/src/test`) and choose **Run 'RecipeMatcherTest'**.

## Project structure
- `DatabaseHelper`: creates the tables, seeds recipes, CRUD methods
- `PantryItem`, `Recipe`: model classes
- `Normalizer`, `RecipeMatcher`: name and unit normalization and the strict matching algorithm
- `PantryAdapter`, `RecipeAdapter`: RecyclerView adapters
- `PantryListActivity`, `AddEditIngredientActivity`, `SuggestedRecipesActivity`, `RecipeDetailActivity`, `SettingsActivity`: screens
