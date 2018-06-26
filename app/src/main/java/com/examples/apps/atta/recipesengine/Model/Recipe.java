package com.examples.apps.atta.recipesengine.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable {

    @SerializedName("uri")
    @Expose
    public String uri;
    @SerializedName("label")
    @Expose
    public String recipeLabel;
    @SerializedName("image")
    @Expose
    public String imageUri;
    @SerializedName("source")
    @Expose
    public String recipeSourceName;
    @SerializedName("url")
    @Expose
    public String recipeSourceUrl;
    @SerializedName("shareAs")
    @Expose
    public String shareAs;
    @SerializedName("yield")
    @Expose
    public Float yield;
    @SerializedName("dietLabels")
    @Expose
    public List<String> dietLabels = null;
    @SerializedName("healthLabels")
    @Expose
    public List<String> healthLabels = null;
    @SerializedName("cautions")
    @Expose
    public List<String> cautions = null;
    @SerializedName("ingredientLines")
    @Expose
    public List<String> ingredientLines = null;
    @SerializedName("ingredients")
    @Expose
    public ArrayList<Ingredient> ingredients = null;
    @SerializedName("calories")
    @Expose
    public Float calories;
    @SerializedName("totalWeight")
    @Expose
    public Float totalWeight;
    @SerializedName("totalTime")
    @Expose
    public Float totalTime;
    @SerializedName("totalNutrients")
    @Expose
    public TotalNutrients totalNutrients;
    @SerializedName("totalDaily")
    @Expose
    public TotalDaily totalDaily;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getRecipeLabel() {
        return recipeLabel;
    }

    public void setRecipeLabel(String label) {
        this.recipeLabel = label;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getRecipeSourceName() {
        return recipeSourceName;
    }

    public void setRecipeSourceName(String source) {
        this.recipeSourceName = source;
    }

    public String getRecipeSourceUrl() {
        return recipeSourceUrl;
    }

    public void setRecipeSourceUrl(String url) {
        this.recipeSourceUrl = url;
    }

    public String getShareAs() {
        return shareAs;
    }

    public void setShareAs(String shareAs) {
        this.shareAs = shareAs;
    }

    public Float getYield() {
        return yield;
    }

    public void setYield(Float yield) {
        this.yield = yield;
    }

    public List<String> getDietLabels() {
        return dietLabels;
    }

    public void setDietLabels(List<String> dietLabels) {
        this.dietLabels = dietLabels;
    }

    public List<String> getHealthLabels() {
        return healthLabels;
    }

    public void setHealthLabels(List<String> healthLabels) {
        this.healthLabels = healthLabels;
    }

    public List<String> getCautions() {
        return cautions;
    }

    public void setCautions(List<String> cautions) {
        this.cautions = cautions;
    }

    public List<String> getIngredientLines() {
        return ingredientLines;
    }

    public void setIngredientLines(List<String> ingredientLines) {
        this.ingredientLines = ingredientLines;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public Float getCalories() {
        return calories;
    }

    public void setCalories(Float calories) {
        this.calories = calories;
    }

    public Float getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Float totalWeight) {
        this.totalWeight = totalWeight;
    }

    public Float getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Float totalTime) {
        this.totalTime = totalTime;
    }

    public TotalNutrients getTotalNutrients() {
        return totalNutrients;
    }

    public void setTotalNutrients(TotalNutrients totalNutrients) {
        this.totalNutrients = totalNutrients;
    }

    public TotalDaily getTotalDaily() {
        return totalDaily;
    }

    public void setTotalDaily(TotalDaily totalDaily) {
        this.totalDaily = totalDaily;
    }

    public Recipe() {
    }

    protected Recipe(Parcel in) {
        uri = in.readString();
        recipeLabel = in.readString();
        imageUri = in.readString();
        recipeSourceName = in.readString();
        recipeSourceUrl = in.readString();
        shareAs = in.readString();
        yield = in.readByte() == 0x00 ? null : in.readFloat();
        if (in.readByte() == 0x01) {
            dietLabels = new ArrayList<String>();
            in.readList(dietLabels, String.class.getClassLoader());
        } else {
            dietLabels = null;
        }
        if (in.readByte() == 0x01) {
            healthLabels = new ArrayList<String>();
            in.readList(healthLabels, String.class.getClassLoader());
        } else {
            healthLabels = null;
        }
        if (in.readByte() == 0x01) {
            cautions = new ArrayList<String>();
            in.readList(cautions, String.class.getClassLoader());
        } else {
            cautions = null;
        }
        if (in.readByte() == 0x01) {
            ingredientLines = new ArrayList<>();
            in.readList(ingredientLines, String.class.getClassLoader());
        } else {
            ingredientLines = null;
        }
        if (in.readByte() == 0x01) {
            ingredients = new ArrayList<>();
            in.readList(ingredients, Ingredient.class.getClassLoader());
        } else {
            ingredients = null;
        }
        calories = in.readByte() == 0x00 ? null : in.readFloat();
        totalWeight = in.readByte() == 0x00 ? null : in.readFloat();
        totalTime = in.readByte() == 0x00 ? null : in.readFloat();
        totalNutrients = (TotalNutrients) in.readValue(TotalNutrients.class.getClassLoader());
        totalDaily = (TotalDaily) in.readValue(TotalDaily.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uri);
        dest.writeString(recipeLabel);
        dest.writeString(imageUri);
        dest.writeString(recipeSourceName);
        dest.writeString(recipeSourceUrl);
        dest.writeString(shareAs);
        if (yield == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeFloat(yield);
        }
        if (dietLabels == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(dietLabels);
        }
        if (healthLabels == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(healthLabels);
        }
        if (cautions == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(cautions);
        }
        if (ingredientLines == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(ingredientLines);
        }
        if (ingredients == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(ingredients);
        }
        if (calories == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeFloat(calories);
        }
        if (totalWeight == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeFloat(totalWeight);
        }
        if (totalTime == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeFloat(totalTime);
        }
        dest.writeValue(totalNutrients);
        dest.writeValue(totalDaily);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}
