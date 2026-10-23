# Smart Pantry Manager

A Java Android Studio application for reducing food waste by suggesting recipes that can be made strictly from ingredients already in the pantry.

## Requirements covered

- Java-only Android implementation.
- SQLiteOpenHelper database with persistent pantry CRUD operations.
- Pantry list using a custom `ListView` adapter.
- Add, edit, and delete pantry items with name, quantity, unit, and optional expiry date.
- 18 recipes seeded into the database on first launch.
- Suggested Recipes screen that excludes every partial match.
- Robust ingredient normalization for singular/plural names such as `tomato`/`tomatoes`, plus basic kg/g and l/ml conversion.
- Quantity checks: a recipe is shown only when every ingredient meets the required quantity.
- Recipe detail screen with ingredients and preparation method.
- Settings screen with expiring-soon alert preference.
- Bottom navigation between Pantry, Suggested, and Settings.
- Empty-state feedback when the pantry has no items or no recipe is strictly available.
- Input validation for required fields and positive numeric quantities.
- No Google Maps, location, GPS, or shopping-location features.

## Open in Android Studio

1. Open `/home/ubuntu/smart-pantry-manager` in Android Studio.
2. Allow Gradle sync to download the Android Gradle Plugin if prompted.
3. Use an Android emulator or connected device with API 23+.
4. Run the `app` configuration.

The sandbox used to create this project does not include a Gradle executable or Android SDK, so the project was validated structurally rather than assembled here. Android Studio will perform the normal Gradle sync/build on a configured Android workstation.

## Main screens

- `MainActivity`: pantry list and add/edit entry point.
- `AddEditIngredientActivity`: validated pantry form and delete action.
- `SuggestedRecipesActivity`: strict recipe matching results.
- `RecipeDetailActivity`: full recipe ingredient list and method.
- `SettingsActivity`: profile/settings preference.

## Core matching implementation

`PantryDbHelper#getStrictMatches()` aggregates pantry quantities by a normalized ingredient key. A recipe is included only if every required ingredient exists and `enough(...)` confirms the required quantity. Missing ingredients, partial matches, and insufficient quantities are excluded from the main list.
