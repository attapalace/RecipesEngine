package com.examples.apps.atta.recipesengine.UI;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.examples.apps.atta.recipesengine.Data.RecipeContract;
import com.examples.apps.atta.recipesengine.Model.Recipe;
import com.examples.apps.atta.recipesengine.R;
import com.examples.apps.atta.recipesengine.Widget.RecipeWidgetProvider;
import com.examples.apps.atta.recipesengine.adapters.ViewPagerAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.examples.apps.atta.recipesengine.UI.MainActivity.RECIPE_DETAIL;

public class RecipeDetailActivity extends AppCompatActivity {

    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.recipe_tabs_viewPager) ViewPager viewPager;
    @BindView(R.id.recipe_title) TextView recipeTitle;
    @BindView(R.id.recipe_detail_image) ImageView recipeImage;
    @BindView(R.id.calories_tv) TextView recipeCalories;
    @BindView(R.id.full_recipe_link) TextView recipeSource;
    @BindView(R.id.health_diet_labels) TextView healthDietLabels;
    @BindView(R.id.fab_share) FloatingActionButton fab;
    @BindView(R.id.adView) AdView mAdView;
    @BindView(R.id.toolbar_custom) Toolbar toolbar;

    public static final String SHARED_PREFS_KEY = "SHARED_PREFS_KEY";
    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        ButterKnife.bind(this);

        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        Intent intent = getIntent();
        if (intent.getParcelableExtra(RECIPE_DETAIL)!= null){
            recipe = intent.getParcelableExtra(RECIPE_DETAIL);
        }

        displayRecipeInfo();

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),recipe);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getApplicationInfo().labelRes);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createShareIntent();
            }
        });

        makeData();
        sendBroadcast();
    }

    private void displayRecipeInfo() {

        recipeTitle.setText(recipe.getRecipeLabel());
        recipeCalories.setText(new StringBuilder()
                .append(String.format("%.2f", recipe.getCalories())).append(" Calories").toString());

        Spanned text = Html.fromHtml(String.format(
                "<a href='%s'>%s</a>",recipe.getRecipeSourceUrl(),recipe.getRecipeSourceName()));
        recipeSource.setText(text);
        recipeSource.setMovementMethod(LinkMovementMethod.getInstance());

        Glide.with(this)
                .load(recipe.getImageUri())
                .into(recipeImage);

        recipeImage.setContentDescription(recipe.getRecipeLabel());

        String healthDietText = "";
        for (String label : recipe.getDietLabels()){
            healthDietText +=  label + ", ";
        }
        for (String label : recipe.getHealthLabels()){
            healthDietText += label + ", ";
        }
        healthDietText = healthDietText.substring(0, healthDietText.length() - 2);
        healthDietLabels.setText(healthDietText);
    }

    private void makeData() {
        String ingredients = "";
        for (int i = 0 ; i<recipe.getIngredients().size() ; i++){
            ingredients += recipe.getIngredients().get(i).getText() + ",";
        }
        ingredients = ingredients.substring(0,ingredients.length()-1);

        ArrayList<String> ingredientsList =  new ArrayList<>(Arrays.asList(ingredients.split(",")));
        Gson gson = new Gson();
        String json = gson.toJson(ingredientsList);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SHARED_PREFS_KEY, json).apply();
    }

    private void sendBroadcast() {

        Intent intent = new Intent(this, RecipeWidgetProvider.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        sendBroadcast(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail,menu);

        MenuItem favouriteItem = menu.findItem(R.id.action_add_to_favourites);

        favouriteItem.setContentDescription(getString(R.string.add_to_favourites_description));

        if(isFavourite(getApplicationContext(),recipe.getRecipeLabel())){
            favouriteItem.setIcon(android.R.drawable.star_on);
        }else {
            favouriteItem.setIcon(android.R.drawable.star_off);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_add_to_favourites:
                if (isFavourite(getApplicationContext(),recipe.getRecipeLabel())){
                    deleteFromFavourites();
                    item.setIcon(android.R.drawable.star_off);
                }else {
                    addToFavourites();
                    item.setIcon(android.R.drawable.star_on);
                }
                return true;
            case android.R.id.home:
                supportFinishAfterTransition();
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean isFavourite(Context context , String label ){
        Cursor cursor = context.getContentResolver().query(
                RecipeContract.RecipeEntry.CONTENT_URI,
                null,
                RecipeContract.RecipeEntry.COLUMN_RECIPE_LABEL + " = ?",
                new String[] {label},null);
        if (cursor.getCount()<= 0 ){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    private void addToFavourites(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                ContentValues values = new ContentValues();

                String healthLabels = String.valueOf(recipe.getHealthLabels());
                String dietLabels = String.valueOf(recipe.getDietLabels());

                String ingredients = "";
                for (int i = 0 ; i<recipe.getIngredients().size() ; i++){
                    ingredients += recipe.getIngredients().get(i).getText() + ",";
                }
                ingredients = ingredients.substring(0,ingredients.length()-1);

                String fatNutrient = "";
                if (recipe.getTotalNutrients().getFAT() != null){
                    fatNutrient += recipe.getTotalNutrients().getFAT().getLabel() + ",";
                    fatNutrient += recipe.getTotalNutrients().getFAT().getQuantity().toString() + ",";
                    fatNutrient += recipe.getTotalNutrients().getFAT().getUnit() + ",";
                    fatNutrient += recipe.getTotalDaily().getFAT().getQuantity() + ",";
                    fatNutrient += recipe.getTotalDaily().getFAT().getUnit();
                }

                String carbsNutrient = "";
                if (recipe.getTotalNutrients().getCarbs() != null){
                    carbsNutrient += recipe.getTotalNutrients().getCarbs().getLabel() + ",";
                    carbsNutrient += recipe.getTotalNutrients().getCarbs().getQuantity().toString() + ",";
                    carbsNutrient += recipe.getTotalNutrients().getCarbs().getUnit() + ",";
                    carbsNutrient += recipe.getTotalDaily().getCarbs().getQuantity() + ",";
                    carbsNutrient += recipe.getTotalDaily().getCarbs().getUnit();
                }

                String cholesterolNutrient = "";
                if (recipe.getTotalNutrients().getCHOLE() != null){
                    cholesterolNutrient += recipe.getTotalNutrients().getCHOLE().getLabel() + ",";
                    cholesterolNutrient += recipe.getTotalNutrients().getCHOLE().getQuantity().toString() + ",";
                    cholesterolNutrient += recipe.getTotalNutrients().getCHOLE().getUnit() + ",";
                    cholesterolNutrient += recipe.getTotalDaily().getCholesterol().getQuantity() + ",";
                    cholesterolNutrient += recipe.getTotalDaily().getCholesterol().getUnit();
                }

                String fiberNutrient = "";
                if (recipe.getTotalNutrients().getFiber() != null){
                    fiberNutrient += recipe.getTotalNutrients().getFiber().getLabel() + ",";
                    fiberNutrient += recipe.getTotalNutrients().getFiber().getQuantity().toString() + ",";
                    fiberNutrient += recipe.getTotalNutrients().getFiber().getUnit() + ",";
                    fiberNutrient += recipe.getTotalDaily().getFiber().getQuantity() + ",";
                    fiberNutrient += recipe.getTotalDaily().getFiber().getUnit();
                }

                String proteinNutrient = "";
                if (recipe.getTotalNutrients().getProtein() != null){
                    proteinNutrient += recipe.getTotalNutrients().getProtein().getLabel() + ",";
                    proteinNutrient += recipe.getTotalNutrients().getProtein().getQuantity().toString() + ",";
                    proteinNutrient += recipe.getTotalNutrients().getProtein().getUnit() + ",";
                    proteinNutrient += recipe.getTotalDaily().getProtein().getQuantity() + ",";
                    proteinNutrient += recipe.getTotalDaily().getProtein().getUnit();
                }

                String sugarNutrient = "";
                if (recipe.getTotalNutrients().getSUGAR() != null){
                    sugarNutrient += recipe.getTotalNutrients().getSUGAR().getLabel() + ",";
                    sugarNutrient += recipe.getTotalNutrients().getSUGAR().getQuantity().toString() + ",";
                    sugarNutrient += recipe.getTotalNutrients().getSUGAR().getUnit();
                }


                values.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_LABEL , recipe.getRecipeLabel());
                values.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_IMAGE,recipe.getImageUri());
                values.put(RecipeContract.RecipeEntry.COLUMN_CALORIES,recipe.getCalories());
                values.put(RecipeContract.RecipeEntry.COLUMN_SOURCE_LINK,recipe.getRecipeSourceUrl());
                values.put(RecipeContract.RecipeEntry.COLUMN_SOURCE_NAME,recipe.getRecipeSourceName());
                values.put(RecipeContract.RecipeEntry.COLUMN_HEALTH_LABELS, healthLabels.substring(1,healthLabels.length()-1));
                values.put(RecipeContract.RecipeEntry.COLUMN_DIET_LABELS,dietLabels.substring(1,dietLabels.length()-1));
                values.put(RecipeContract.RecipeEntry.COLUMN_INGREDIENTS, ingredients);
                values.put(RecipeContract.RecipeEntry.COLUMN_FAT_NUTRIENT,fatNutrient);
                values.put(RecipeContract.RecipeEntry.COLUMN_CARBS_NUTRIENT ,carbsNutrient);
                values.put(RecipeContract.RecipeEntry.COLUMN_CHOLESTROL_NUTRIENT,cholesterolNutrient);
                values.put(RecipeContract.RecipeEntry.COLUMN_FIBER_NUTRIENT,fiberNutrient);
                values.put(RecipeContract.RecipeEntry.COLUMN_PROTEIN_NUTRIENT,proteinNutrient);
                values.put(RecipeContract.RecipeEntry.COLUMN_SUGAR_NUTRIENT,sugarNutrient);

                getContentResolver().insert(RecipeContract.RecipeEntry.CONTENT_URI,values);

            }
        }).start();
        Toast.makeText(this, R.string.added_to_favourites,Toast.LENGTH_SHORT).show();

    }

    private void deleteFromFavourites(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                getContentResolver().delete(RecipeContract.RecipeEntry.CONTENT_URI,
                        RecipeContract.RecipeEntry.COLUMN_RECIPE_LABEL + " = ?",
                        new String[]{recipe.getRecipeLabel()});
            }
        }).start();
        Toast.makeText(this, R.string.deleted_from_favourites,Toast.LENGTH_SHORT).show();
    }


    private void createShareIntent(){
        ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setChooserTitle(R.string.share_chooser_title)
                .setText(recipe.getShareAs())
                .startChooser();
    }
}
