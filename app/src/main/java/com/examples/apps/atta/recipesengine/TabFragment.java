package com.examples.apps.atta.recipesengine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.examples.apps.atta.recipesengine.Model.Ingredient;
import com.examples.apps.atta.recipesengine.Model.TotalDaily;
import com.examples.apps.atta.recipesengine.Model.TotalNutrients;
import com.examples.apps.atta.recipesengine.adapters.IngredientsListAdapter;
import com.examples.apps.atta.recipesengine.adapters.NutrientListAdapter;

import java.util.ArrayList;

public class TabFragment extends Fragment {

    int position;
    ArrayList<Ingredient> ingredients;
    TotalNutrients totalNutrients;
    TotalDaily totalDaily;
    private RecyclerView recyclerView;
    IngredientsListAdapter ingredientsAdapter;
    NutrientListAdapter nutrientListAdapter;

    public static final String INGREDIENTS_LIST = "ingredients";
    public static final String TOTAL_NUT = "total nutrients";
    public static final String TOTAL_DAILY = "total_daily";

    public static Fragment getInstance(int position , ArrayList<Ingredient> ingredients , TotalNutrients totalNutrients
            , TotalDaily totalDaily ) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        bundle.putParcelableArrayList(INGREDIENTS_LIST , ingredients);
        bundle.putParcelable(TOTAL_NUT , totalNutrients);
        bundle.putParcelable(TOTAL_DAILY , totalDaily);
        TabFragment tabFragment = new TabFragment();
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("pos");
        ingredients = getArguments().getParcelableArrayList(INGREDIENTS_LIST);
        totalNutrients = getArguments().getParcelable(TOTAL_NUT);
        totalDaily = getArguments().getParcelable(TOTAL_DAILY);

        ingredientsAdapter = new IngredientsListAdapter(ingredients,getContext());
        nutrientListAdapter = new NutrientListAdapter(totalNutrients,totalDaily,getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView =  view.findViewById(R.id.tab_recyclerView);

        if (position == 0){
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(ingredientsAdapter);
        }else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(nutrientListAdapter);
        }

    }
}
