package com.examples.apps.atta.recipesengine.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.examples.apps.atta.recipesengine.Model.Recipe;
import com.examples.apps.atta.recipesengine.TabFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private String[] titles = {"Ingredients" , "Nutrition"};
    private Recipe recipe = new Recipe();

    public ViewPagerAdapter(FragmentManager fm,Recipe recipe) {
        super(fm);
        this.recipe = recipe;
    }

    @Override
    public Fragment getItem(int position) {
        return TabFragment.getInstance(position
                ,recipe.getIngredients(),recipe.getTotalNutrients(),recipe.getTotalDaily());
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    


}
