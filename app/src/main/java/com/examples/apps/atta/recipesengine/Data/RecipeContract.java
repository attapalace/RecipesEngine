package com.examples.apps.atta.recipesengine.Data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class RecipeContract {

    public static final String CONTENT_AUTHORITY = "com.examples.apps.atta.recipesengine";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String RECIPE_PATH = "recipe";

    public static final class RecipeEntry implements BaseColumns {
        // table name
        public static final String TABLE_NAME = "recipe";
        // columns
//        public static final String COLUMN_RECIPE_ID = "recipe_id";
        public static final String COLUMN_RECIPE_LABEL = "label";
        public static final String COLUMN_RECIPE_IMAGE = "image";
        public static final String COLUMN_CALORIES = "calories";
        public static final String COLUMN_SOURCE_LINK = "source";
        public static final String COLUMN_SOURCE_NAME = "sourceName";
        public static final String COLUMN_HEALTH_LABELS = "healthLabels";
        public static final String COLUMN_DIET_LABELS = "dietLabels";
        public static final String COLUMN_INGREDIENTS = "ingredients";
        public static final String COLUMN_FAT_NUTRIENT = "fat_nutrient";
        public static final String COLUMN_CARBS_NUTRIENT = "carbs";
        public static final String COLUMN_CHOLESTROL_NUTRIENT = "cholesterol";
        public static final String COLUMN_FIBER_NUTRIENT = "fiber";
        public static final String COLUMN_PROTEIN_NUTRIENT = "protein";
        public static final String COLUMN_SUGAR_NUTRIENT = "sugar";

        // create content uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(RECIPE_PATH).build();
        // create cursor of base type directory for multiple entries
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        // create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +"/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        // for building URIs on insertion
        public static Uri buildRecipessUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
