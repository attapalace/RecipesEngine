package com.examples.apps.atta.recipesengine.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.examples.apps.atta.recipesengine.Model.Ingredient;
import com.examples.apps.atta.recipesengine.R;

import java.util.List;

public class IngredientsListAdapter extends RecyclerView.Adapter<IngredientsListAdapter.ViewHolder> {

    private List<Ingredient> ingredients;
    private Context mContext;

    public IngredientsListAdapter(List<Ingredient> ingredients, Context mContext) {
        this.ingredients = ingredients;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_list_item
                ,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (ingredients != null){
            holder.ingredientTextView.setText("\u2022" + " " + ingredients.get(position).getText());
        }
    }

    @Override
    public int getItemCount() {
        if (ingredients != null){
            return ingredients.size();
        }else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView ingredientTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            ingredientTextView = itemView.findViewById(R.id.ingredient_textview);
        }
    }
}
