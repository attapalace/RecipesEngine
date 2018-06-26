package com.examples.apps.atta.recipesengine.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Hit implements Parcelable {

    @SerializedName("recipe")
    @Expose
    private Recipe recipe;
    @SerializedName("bookmarked")
    @Expose
    private Boolean bookmarked;
    @SerializedName("bought")
    @Expose
    private Boolean bought;

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Boolean getBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(Boolean bookmarked) {
        this.bookmarked = bookmarked;
    }

    public Boolean getBought() {
        return bought;
    }

    public void setBought(Boolean bought) {
        this.bought = bought;
    }

    protected Hit(Parcel in) {
        recipe = (Recipe) in.readValue(Recipe.class.getClassLoader());
        byte bookmarkedVal = in.readByte();
        bookmarked = bookmarkedVal == 0x02 ? null : bookmarkedVal != 0x00;
        byte boughtVal = in.readByte();
        bought = boughtVal == 0x02 ? null : boughtVal != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(recipe);
        if (bookmarked == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (bookmarked ? 0x01 : 0x00));
        }
        if (bought == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (bought ? 0x01 : 0x00));
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Hit> CREATOR = new Parcelable.Creator<Hit>() {
        @Override
        public Hit createFromParcel(Parcel in) {
            return new Hit(in);
        }

        @Override
        public Hit[] newArray(int size) {
            return new Hit[size];
        }
    };
}
