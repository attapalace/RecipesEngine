package com.examples.apps.atta.recipesengine.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.examples.apps.atta.recipesengine.Model.Recipe;
import com.examples.apps.atta.recipesengine.R;

import java.util.ArrayList;

public class FavouriteListAdapter extends RecyclerView.Adapter<FavouriteListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Recipe> recipes = new ArrayList<>();

    private FavouriteListAdapter.FavouriteRecipeImageOnClickHandler onClickHandler;

    public FavouriteListAdapter(Context mContext, FavouriteRecipeImageOnClickHandler onClickHandler) {
        this.mContext = mContext;
        this.onClickHandler = onClickHandler;
    }

    public interface FavouriteRecipeImageOnClickHandler{
        void onClick(Recipe recipe, ImageView imageView);
    }

    public void setFavouriteRecipeData(ArrayList<Recipe> recipes , Context context){
        this.recipes = recipes;
        this.mContext = context;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.recipe_list_item,parent,false);
        view.setFocusable(true);
        return new FavouriteListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( FavouriteListAdapter.ViewHolder holder, int position) {
        if (recipes != null){
            Recipe recipe;
            recipe = recipes.get(position);
            String recipeImageUri = recipe.getImageUri();
            String recipeTitle = recipe.getRecipeLabel();
            Log.v("recipe is successful", recipeImageUri);

            if (recipeTitle != ""){
                holder.textView.setText(recipeTitle);
            }
            if (recipeImageUri != ""){
                Glide.with(mContext)
                        .load(recipeImageUri)
                        .into(holder.imageView);
            }

        }else {
            Log.v("recipe is unsuccessful","hits are null");
        }
    }

    @Override
    public int getItemCount() {
        if (recipes == null){
            return 0;
        }else {
            return recipes.size();
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        ImageView imageView;
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.recipe_image);
            textView = itemView.findViewById(R.id.recipe_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            onClickHandler.onClick(recipes.get(position),imageView);
        }
    }
}
