package com.examples.apps.atta.recipesengine.UI;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.examples.apps.atta.recipesengine.BuildConfig;
import com.examples.apps.atta.recipesengine.Model.Hit;
import com.examples.apps.atta.recipesengine.Model.JsonResponse;
import com.examples.apps.atta.recipesengine.Model.Recipe;
import com.examples.apps.atta.recipesengine.R;
import com.examples.apps.atta.recipesengine.Retrofit.RecipeInterface;
import com.examples.apps.atta.recipesengine.Retrofit.RetrofitClientInstance;
import com.examples.apps.atta.recipesengine.adapters.RecipeListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.examples.apps.atta.recipesengine.UI.MainActivity.LABELS_INTENT;
import static com.examples.apps.atta.recipesengine.UI.MainActivity.RECIPE_DETAIL;
import static com.examples.apps.atta.recipesengine.UI.MainActivity.SEARCH_TERM;

public class ResultsActivity extends AppCompatActivity implements RecipeListAdapter.recipeImageOnClickHandler {

    @BindView(R.id.search_results_list) RecyclerView searchRecyclerView;
    @BindView(R.id.empty_view_results) TextView emptyView;
    @BindView(R.id.toolbar_custom) Toolbar toolbar;

    RecipeListAdapter resultListAdapter;
    private Map<String,String> data;
    private ArrayList<String> labels;

    static final String TAG =ResultsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        ButterKnife.bind(this);

        Intent intent = getIntent();

        if (intent.getSerializableExtra(LABELS_INTENT)!= null) {
            labels = (ArrayList<String>) intent.getSerializableExtra(LABELS_INTENT);

            data = new HashMap<>();
            String[] healthLabels = getResources().getStringArray(R.array.health_categories);
            for (String s : labels) {
                if (Arrays.asList(healthLabels).contains(s)) {
                    data.put("health", s);
                } else {
                    data.put("diet", s);
                }
            }
        }

        if (data == null){
            data = Collections.EMPTY_MAP;
        }

        resultListAdapter = new RecipeListAdapter(this,this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,adjustGridColumns());
        searchRecyclerView.setLayoutManager(gridLayoutManager);
        searchRecyclerView.setHasFixedSize(true);

        String searchParam = intent.getStringExtra(SEARCH_TERM);

        RecipeInterface recipeInterface = RetrofitClientInstance.getInstance()
                .create(RecipeInterface.class);

        final Call<JsonResponse> jsonResponseResult = recipeInterface.getSearchResponse(searchParam,
                BuildConfig.app_id,BuildConfig.app_key , data);

        jsonResponseResult.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                ArrayList<Hit> hits = response.body().getHits();
                resultListAdapter.setRecipeData(hits,getApplicationContext());
                resultListAdapter.notifyDataSetChanged();

                if (hits.size() == 0){
                    emptyView.setVisibility(View.VISIBLE);
                }else {
                    emptyView.setVisibility(View.GONE);
                }
                Log.v(TAG , getString(R.string.successful_json_parsing) + response.body());
            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {
                Log.v(getString(R.string.failed_json_string) , t.toString());

            }
        });

        searchRecyclerView.setAdapter(resultListAdapter);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getApplicationInfo().labelRes);
    }

    @Override
    public void onClick(Recipe recipe, ImageView imageView) {
        Intent intent = new Intent(ResultsActivity.this,RecipeDetailActivity.class);
        intent.putExtra(RECIPE_DETAIL , recipe);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                imageView,getString(R.string.transition_name));
        startActivity(intent,optionsCompat.toBundle());
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
}
