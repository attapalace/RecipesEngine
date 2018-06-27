package com.examples.apps.atta.recipesengine.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.examples.apps.atta.recipesengine.BuildConfig;
import com.examples.apps.atta.recipesengine.Model.Hit;
import com.examples.apps.atta.recipesengine.Model.JsonResponse;
import com.examples.apps.atta.recipesengine.Model.Recipe;
import com.examples.apps.atta.recipesengine.MultiSelectionSpinner;
import com.examples.apps.atta.recipesengine.R;
import com.examples.apps.atta.recipesengine.Retrofit.RecipeInterface;
import com.examples.apps.atta.recipesengine.Retrofit.RetrofitClientInstance;
import com.examples.apps.atta.recipesengine.adapters.RecipeListAdapter;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements RecipeListAdapter.recipeImageOnClickHandler
        ,MultiSelectionSpinner.OnMultipleItemsSelectedListener{

    private RecipeListAdapter mRecipeListAdapter;
    private FirebaseAnalytics mFirebaseAnalytics;

    @BindView(R.id.main_recipes_list) RecyclerView recyclerView;
    @BindView(R.id.search_view) EditText searchEditText;
    @BindView(R.id.categorySpinner) MultiSelectionSpinner multiSelectionSpinner;
    @BindView(R.id.empty_view_main) TextView emptyView;
    @BindView(R.id.toolbar_custom) Toolbar toolbar;

    private String TAG = MainActivity.class.getSimpleName();

    public static final String RECIPE_DETAIL = "recipe intent";
    public static final String SEARCH_TERM = "search";
    public static final String LABELS_INTENT = "labels";
    public static final String SCROLL_STATE = "scroll state";
    public static final String EDIT_TEXT_STRING = "edittext string";
    public static final String HITS_LIST = "hits list";

    private ArrayList<Hit> hits;
    private Parcelable mSavedRecyclerLayoutState;

    private List<String> labels;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setTitle(getApplicationInfo().labelRes);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,adjustGridColumns());
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        mRecipeListAdapter = new RecipeListAdapter(this,this);

        multiSelectionSpinner.setItems(getResources().getStringArray(R.array.health_categories),
                getResources().getStringArray(R.array.diet_categories));

        multiSelectionSpinner.setListener(this);

        if(isNetworkAvailable()){
            if (hits == null){
                updateRecipes();
            }
        }else {
            Toast.makeText(this, R.string.connection_error_message,Toast.LENGTH_SHORT).show();
        }
        recyclerView.setAdapter(mRecipeListAdapter);

        searchEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                final int DRAWABLE_RIGHT = 2;
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (searchEditText.getRight() - searchEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        if (isNetworkAvailable()) {
                            if (!searchEditText.getText().toString().equals("")){
                                updateSearchResults();
                                return true;
                            }else {
                                Toast.makeText(getApplicationContext()
                                        , R.string.no_search_item_message,Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(MainActivity.this
                                    , R.string.connection_error_message,Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                return false;
            }
        });
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(isNetworkAvailable()){
                    if (!searchEditText.getText().toString().equals("")){
                        if (actionId == EditorInfo.IME_ACTION_SEARCH){
                            updateSearchResults();
                            return true;
                        }
                    }else {
                        Toast.makeText(getApplicationContext()
                                , R.string.no_search_item_message,Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(MainActivity.this
                            , R.string.connection_error_message,Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

    }

    private int adjustGridColumns(){
        int gridColumns;

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            if(getResources().getConfiguration().screenWidthDp >= 600){
                gridColumns = 3;
            }else {
                gridColumns = 2;
            }
        }else{
            if (getResources().getConfiguration().screenWidthDp >= 900){
                gridColumns = 4;
            }else {
                gridColumns = 3;
            }
        }
        return gridColumns;
    }

    @Override
    public void onClick(Recipe recipe, ImageView imageView) {
        Intent intent = new Intent(MainActivity.this,RecipeDetailActivity.class);
        intent.putExtra(RECIPE_DETAIL , recipe);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                imageView,getString(R.string.transition_name));
        startActivity(intent,optionsCompat.toBundle());
    }


    private void updateRecipes() {

        RecipeInterface recipeInterface = RetrofitClientInstance.getInstance()
                .create(RecipeInterface.class);

        final Call<JsonResponse> jsonResponse = recipeInterface.getResponse("",
                BuildConfig.app_id,BuildConfig.app_key,getString(R.string.balanced_recipes_params));

        jsonResponse.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if (response.body() != null) {
                    hits = response.body().getHits();
                    mRecipeListAdapter.setRecipeData(hits, getApplicationContext());
                    mRecipeListAdapter.notifyDataSetChanged();

                    recyclerView.getLayoutManager().onRestoreInstanceState(mSavedRecyclerLayoutState);

                    emptyView.setVisibility(View.GONE);
                }else {
                    Toast.makeText(MainActivity.this, R.string.no_response_message,Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {
                Log.v(getString(R.string.failed_json_string) ,t.toString());

                emptyView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void updateSearchResults(){
        String searchParam = searchEditText.getText().toString();

        searchEditText.clearFocus();
        InputMethodManager in = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if (in != null) {
            in.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
        }

        Log.v(TAG , "search params = " + searchParam);
        Intent intent = new Intent(MainActivity.this , ResultsActivity.class);
        intent.putExtra(SEARCH_TERM , searchParam);
        intent.putExtra(LABELS_INTENT , (Serializable) labels);
        startActivity(intent);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void selectedStrings(List<String> strings) {
        labels = strings;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.action_favourites:
                Intent intent = new Intent(MainActivity.this,FavouritesActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_about:
                Intent aboutIntent = new Intent(MainActivity.this,AboutActivity.class);
                startActivity(aboutIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(SCROLL_STATE, recyclerView.getLayoutManager().onSaveInstanceState());
        outState.putString(EDIT_TEXT_STRING , searchEditText.getText().toString());
        outState.putParcelableArrayList(HITS_LIST , hits);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mSavedRecyclerLayoutState = savedInstanceState.getParcelable(SCROLL_STATE);
        recyclerView.getLayoutManager().onRestoreInstanceState(mSavedRecyclerLayoutState);

        searchEditText.setText(savedInstanceState.getString(EDIT_TEXT_STRING));
        hits = savedInstanceState.getParcelableArrayList(HITS_LIST);
    }
}
