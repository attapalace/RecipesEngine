package com.examples.apps.atta.recipesengine.UI;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.examples.apps.atta.recipesengine.Data.RecipeContract;
import com.examples.apps.atta.recipesengine.Model.Carbs;
import com.examples.apps.atta.recipesengine.Model.Cholesterol;
import com.examples.apps.atta.recipesengine.Model.Fat;
import com.examples.apps.atta.recipesengine.Model.Fiber;
import com.examples.apps.atta.recipesengine.Model.Ingredient;
import com.examples.apps.atta.recipesengine.Model.Protein;
import com.examples.apps.atta.recipesengine.Model.Recipe;
import com.examples.apps.atta.recipesengine.Model.Sugar;
import com.examples.apps.atta.recipesengine.Model.TotalDaily;
import com.examples.apps.atta.recipesengine.Model.TotalNutrients;
import com.examples.apps.atta.recipesengine.R;
import com.examples.apps.atta.recipesengine.adapters.FavouriteListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.examples.apps.atta.recipesengine.UI.MainActivity.RECIPE_DETAIL;

public class FavouritesActivity extends AppCompatActivity
        implements FavouriteListAdapter.FavouriteRecipeImageOnClickHandler ,
        LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.favourite_results_list) RecyclerView favouritesList;
    @BindView(R.id.empty_view_favourites) TextView emptyFavouritesText;
    @BindView(R.id.toolbar_custom) Toolbar toolbar;

    private static final int FAVOURITES_LOADER_ID = 0;

    private FavouriteListAdapter adapter;

    private static final String[] RECIPE_COLUMNS = {RecipeContract.RecipeEntry.COLUMN_RECIPE_LABEL,
            RecipeContract.RecipeEntry.COLUMN_RECIPE_IMAGE,
            RecipeContract.RecipeEntry.COLUMN_CALORIES,
            RecipeContract.RecipeEntry.COLUMN_SOURCE_LINK,
            RecipeContract.RecipeEntry.COLUMN_SOURCE_NAME,
            RecipeContract.RecipeEntry.COLUMN_HEALTH_LABELS,
            RecipeContract.RecipeEntry.COLUMN_DIET_LABELS,
            RecipeContract.RecipeEntry.COLUMN_INGREDIENTS,
            RecipeContract.RecipeEntry.COLUMN_FAT_NUTRIENT,
            RecipeContract.RecipeEntry.COLUMN_CARBS_NUTRIENT,
            RecipeContract.RecipeEntry.COLUMN_CHOLESTROL_NUTRIENT,
            RecipeContract.RecipeEntry.COLUMN_FIBER_NUTRIENT,
            RecipeContract.RecipeEntry.COLUMN_PROTEIN_NUTRIENT,
            RecipeContract.RecipeEntry.COLUMN_SUGAR_NUTRIENT};

    public static final int COL_RECIPE_LABEL = 0;
    public static final int COL_RECIPE_IMAGE = 1;
    public static final int COL_RECIPE_CALORIES = 2;
    public static final int COL_SOURCE_LINK = 3;
    public static final int COL_SOURCE_NAME = 4;
    public static final int COL_HEALTH_LABELS = 5;
    public static final int COL_DIET_LABELS = 6;
    public static final int COL_INGREDIENTS = 7;
    public static final int COL_FAT_NUT = 8;
    public static final int COL_CARBS_NUT = 9;
    public static final int COL_CHOLES_NUT = 10;
    public static final int COL_FIBER_NUT = 11;
    public static final int COL_PROTEIN_NUT = 12;
    public static final int COL_SUGAR_NUT = 13;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        ButterKnife.bind(this);

        adapter = new FavouriteListAdapter(this, this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,adjustGridColumns());
        favouritesList.setLayoutManager(gridLayoutManager);
        favouritesList.setHasFixedSize(true);

        getSupportLoaderManager().initLoader(FAVOURITES_LOADER_ID, null, this);
        favouritesList.setAdapter(adapter);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getApplicationInfo().labelRes);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(FAVOURITES_LOADER_ID,null,this);
        adapter.notifyDataSetChanged();
    }

    private int adjustGridColumns(){
        int gridColumns;

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            gridColumns = 2;
        }else {
            gridColumns = 3;
        }
        return gridColumns;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {


        switch (id) {
            case FAVOURITES_LOADER_ID:
                // Returns a new CursorLoader
                return new CursorLoader(
                        this,   // Parent activity context
                        RecipeContract.RecipeEntry.CONTENT_URI,        // Table to query
                        RECIPE_COLUMNS,     // Projection to return
                        null,            // No selection clause
                        null,            // No selection arguments
                        null             // Default sort order
                );
            default:
                // An invalid id was passed in
                return null;
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        ArrayList<Recipe> recipes = new ArrayList<>();
        if(data!= null && data.moveToFirst()) {
            do {
                Recipe recipe = new Recipe();
                recipe.recipeLabel = data.getString(COL_RECIPE_LABEL);
                recipe.imageUri = data.getString(COL_RECIPE_IMAGE);
                recipe.calories = data.getFloat(COL_RECIPE_CALORIES);
                recipe.recipeSourceUrl = data.getString(COL_SOURCE_LINK);
                recipe.recipeSourceName = data.getString(COL_SOURCE_NAME);
                recipe.healthLabels = Collections.singletonList(data.getString(COL_HEALTH_LABELS));
                recipe.dietLabels = Collections.singletonList(data.getString(COL_DIET_LABELS));

                String ingredients = data.getString(COL_INGREDIENTS);
                recipe.setIngredients(GetIngredientsArrayList(ingredients));

                TotalNutrients totalNutrients = new TotalNutrients();
                TotalDaily totalDaily = new TotalDaily();

                if (!data.getString(COL_FAT_NUT).equals("")){
                    addFatToRecipe(recipe,data.getString(COL_FAT_NUT),totalNutrients,totalDaily);
                }

                if (!data.getString(COL_CARBS_NUT).equals("")){
                    addCarbsToRecipe(recipe,data.getString(COL_CARBS_NUT),totalNutrients,totalDaily);
                }

                if (!data.getString(COL_CHOLES_NUT).equals("")){
                    addCholesterolToRecipe(recipe,data.getString(COL_CHOLES_NUT),totalNutrients,totalDaily);
                }

                if (!data.getString(COL_FIBER_NUT).equals("")){
                    addFiberToRecipe(recipe,data.getString(COL_FIBER_NUT),totalNutrients,totalDaily);
                }

                if (!data.getString(COL_PROTEIN_NUT).equals("")){
                    addProteinToRecipe(recipe,data.getString(COL_PROTEIN_NUT) ,totalNutrients,totalDaily);
                }

                if (!data.getString(COL_SUGAR_NUT).equals("")){
                    addSugarToRecipe(recipe,data.getString(COL_SUGAR_NUT),totalNutrients);
                }

                recipes.add(recipe);
            }while (data.moveToNext());
        }
        if(recipes.size() == 0){
            emptyFavouritesText.setVisibility(View.VISIBLE);
        }
        adapter.setFavouriteRecipeData(recipes,getApplicationContext());
    }


    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    public void onClick(Recipe recipe, ImageView imageView) {
        Intent intent = new Intent(this,RecipeDetailActivity.class);
        intent.putExtra(RECIPE_DETAIL , recipe);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                imageView,getString(R.string.transition_name));
        startActivity(intent,optionsCompat.toBundle());
    }

    private ArrayList<Ingredient> GetIngredientsArrayList (String ingredientsString){
        ArrayList<String> ingredientsList
                = new ArrayList<>(Arrays.asList(ingredientsString.split(",")));

        ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();

        for (String ingred : ingredientsList){
            Ingredient ingredient = new Ingredient();
            ingredient.setText(ingred);
            ingredientArrayList.add(ingredient);
        }
        return ingredientArrayList;
    }

    private void addFatToRecipe(Recipe recipe,String fatNutrient,TotalNutrients totalNutrients,TotalDaily totalDaily){
        ArrayList<String> fatNutList
                = new ArrayList<>(Arrays.asList(fatNutrient.split(",")));

        Fat fat = new Fat();
        fat.setLabel(fatNutList.get(0));
        fat.setQuantity(Float.valueOf(fatNutList.get(1)));
        fat.setUnit(fatNutList.get(2));

        totalNutrients.setFAT(fat);

        Fat fat1 = new Fat();
        fat1.setQuantity(Float.valueOf(fatNutList.get(3)));
        fat1.setUnit(fatNutList.get(4));
        totalDaily.setFAT(fat1);

        recipe.setTotalNutrients(totalNutrients);
        recipe.setTotalDaily(totalDaily);
    }

    private void addCarbsToRecipe(Recipe recipe,String carbsNutrient,TotalNutrients totalNutrients,TotalDaily totalDaily){
        ArrayList<String> carbsNutList
                = new ArrayList<>(Arrays.asList(carbsNutrient.split(",")));

        Carbs carbs = new Carbs();
        carbs.setLabel(carbsNutList.get(0));
        carbs.setQuantity(Float.valueOf(carbsNutList.get(1)));
        carbs.setUnit(carbsNutList.get(2));

        totalNutrients.setCarbs(carbs);

        Carbs carbs1 = new Carbs();
        carbs1.setQuantity(Float.valueOf(carbsNutList.get(3)));
        carbs1.setUnit(carbsNutList.get(4));
        totalDaily.setCarbs(carbs1);

        recipe.setTotalNutrients(totalNutrients);
        recipe.setTotalDaily(totalDaily);
    }

    private void addCholesterolToRecipe(Recipe recipe,String cholesterolNutrient ,TotalNutrients totalNutrients,
                                        TotalDaily totalDaily){
        ArrayList<String> cholesterolNutList
                = new ArrayList<>(Arrays.asList(cholesterolNutrient.split(",")));

        Cholesterol cholesterol = new Cholesterol();
        cholesterol.setLabel(cholesterolNutList.get(0));
        cholesterol.setQuantity(Float.valueOf(cholesterolNutList.get(1)));
        cholesterol.setUnit(cholesterolNutList.get(2));

        totalNutrients.setCHOLE(cholesterol);

        Cholesterol cholesterol1 = new Cholesterol();
        cholesterol1.setQuantity(Float.valueOf(cholesterolNutList.get(3)));
        cholesterol1.setUnit(cholesterolNutList.get(4));
        totalDaily.setCholesterol(cholesterol1);

        recipe.setTotalNutrients(totalNutrients);
        recipe.setTotalDaily(totalDaily);
    }

    private void addFiberToRecipe(Recipe recipe, String fiberNutrient, TotalNutrients totalNutrients, TotalDaily totalDaily) {
        ArrayList<String> fiberNutList
                = new ArrayList<>(Arrays.asList(fiberNutrient.split(",")));

        Fiber fiber = new Fiber();
        fiber.setLabel(fiberNutList.get(0));
        fiber.setQuantity(Float.valueOf(fiberNutList.get(1)));
        fiber.setUnit(fiberNutList.get(2));

        totalNutrients.setFiber(fiber);

        Fiber fiber1 = new Fiber();
        fiber1.setQuantity(Float.valueOf(fiberNutList.get(3)));
        fiber1.setUnit(fiberNutList.get(4));
        totalDaily.setFiber(fiber1);

        recipe.setTotalNutrients(totalNutrients);
        recipe.setTotalDaily(totalDaily);
    }

    private void addProteinToRecipe(Recipe recipe, String proteinNutrient
            , TotalNutrients totalNutrients, TotalDaily totalDaily) {
        ArrayList<String> proteinNutList
                = new ArrayList<>(Arrays.asList(proteinNutrient.split(",")));

        Protein protein = new Protein()   ;
        protein.setLabel(proteinNutList.get(0));
        protein.setQuantity(Float.valueOf(proteinNutList.get(1)));
        protein.setUnit(proteinNutList.get(2));

        totalNutrients.setProtein(protein);

        Protein protein1 = new Protein();
        protein1.setQuantity(Float.valueOf(proteinNutList.get(3)));
        protein1.setUnit(proteinNutList.get(4));
        totalDaily.setProtein(protein1);

        recipe.setTotalNutrients(totalNutrients);
        recipe.setTotalDaily(totalDaily);
    }

    private void addSugarToRecipe(Recipe recipe , String sugarNutrient ,TotalNutrients totalNutrients){
        ArrayList<String> sugarNutList
                = new ArrayList<>(Arrays.asList(sugarNutrient.split(",")));

        Sugar sugar = new Sugar()   ;
        sugar.setLabel(sugarNutList.get(0));
        sugar.setQuantity(Float.valueOf(sugarNutList.get(1)));
        sugar.setUnit(sugarNutList.get(2));

        totalNutrients.setSUGAR(sugar);

        recipe.setTotalNutrients(totalNutrients);
    }
}

