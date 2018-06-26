package com.examples.apps.atta.recipesengine.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecipeDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1 ;

    static final String DATABASE_NAME = "recipe.db";

    public RecipeDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_RECIPE_TABLE = "CREATE TABLE " + RecipeContract.RecipeEntry.TABLE_NAME + " (" +

                RecipeContract.RecipeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RecipeContract.RecipeEntry.COLUMN_RECIPE_LABEL+ " TEXT NOT NULL, " +
                RecipeContract.RecipeEntry.COLUMN_RECIPE_IMAGE + " TEXT, " +
                RecipeContract.RecipeEntry.COLUMN_CALORIES + " FLOAT," +
                RecipeContract.RecipeEntry.COLUMN_SOURCE_LINK + " TEXT," +
                RecipeContract.RecipeEntry.COLUMN_SOURCE_NAME + " TEXT," +
                RecipeContract.RecipeEntry.COLUMN_HEALTH_LABELS + " TEXT, " +
                RecipeContract.RecipeEntry.COLUMN_DIET_LABELS + " TEXT, "+
                RecipeContract.RecipeEntry.COLUMN_INGREDIENTS + " TEXT, "+
                RecipeContract.RecipeEntry.COLUMN_FAT_NUTRIENT + " TEXT, " +
                RecipeContract.RecipeEntry.COLUMN_CARBS_NUTRIENT + " TEXT, " +
                RecipeContract.RecipeEntry.COLUMN_CHOLESTROL_NUTRIENT + " TEXT, " +
                RecipeContract.RecipeEntry.COLUMN_FIBER_NUTRIENT + " TEXT, " +
                RecipeContract.RecipeEntry.COLUMN_PROTEIN_NUTRIENT + " TEXT, " +
                RecipeContract.RecipeEntry.COLUMN_SUGAR_NUTRIENT + " TEXT);";


        sqLiteDatabase.execSQL(SQL_CREATE_RECIPE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipeContract.RecipeEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
