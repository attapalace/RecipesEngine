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
import com.examples.apps.atta.recipesengine.Model.Hit;
import com.examples.apps.atta.recipesengine.Model.Recipe;
import com.examples.apps.atta.recipesengine.R;

import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {

    private Context mContext;
    private Recipe recipe = new Recipe();
    private List<Hit> hits;

    private recipeImageOnClickHandler onClickHandler;

    public RecipeListAdapter(Context mContext, recipeImageOnClickHandler onClickHandler) {
        this.mContext = mContext;
        this.onClickHandler = onClickHandler;
    }

    public interface recipeImageOnClickHandler{
        void onClick(Recipe recipe, ImageView imageView);
    }

    public void setRecipeData(List<Hit> hits , Context context){
        this.hits = hits;
        this.mContext = context;
        notifyDataSetChanged();
    }


    @Override
    public RecipeViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.recipe_list_item,parent,false);
        view.setFocusable(true);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder( RecipeListAdapter.RecipeViewHolder holder, int position) {
        if (hits != null){
            recipe = hits.get(position).getRecipe();
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
        if (hits == null){
            return 0;
        }else {
            return hits.size();
        }

    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        ImageView imageView;
        TextView textView;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.recipe_image);
            textView = itemView.findViewById(R.id.recipe_title);
            //set content description for the recipe
            imageView.setContentDescription(recipe.getRecipeLabel());
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();
            onClickHandler.onClick(hits.get(position).getRecipe(),imageView);
        }
    }
}
