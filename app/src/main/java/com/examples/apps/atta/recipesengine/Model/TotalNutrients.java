package com.examples.apps.atta.recipesengine.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TotalNutrients implements Parcelable {

    @SerializedName("FAT")
    @Expose
    private Fat fat;

    @SerializedName("CHOCDF")
    @Expose
    private Carbs carbs;

    @SerializedName("FIBTG")
    @Expose
    private Fiber fiber;

    @SerializedName("SUGAR")
    @Expose
    private Sugar sUGAR;

    @SerializedName("PROCNT")
    @Expose
    private Protein pROCNT;

    @SerializedName("CHOLE")
    @Expose
    private Cholesterol cHOLE;

    public Fat getFAT() {
        return fat;
    }

    public void setFAT(Fat fAT) {
        this.fat = fAT;
    }

    public Carbs getCarbs() {
        return carbs;
    }

    public void setCarbs(Carbs carbs) {
        this.carbs = carbs;
    }

    public Fiber getFiber() {
        return fiber;
    }

    public void setFiber(Fiber fIBTG) {
        this.fiber = fIBTG;
    }

    public Sugar getSUGAR() {
        return sUGAR;
    }

    public void setSUGAR(Sugar sUGAR) {
        this.sUGAR = sUGAR;
    }

    public Protein getProtein() {
        return pROCNT;
    }

    public void setProtein(Protein pROCNT) {
        this.pROCNT = pROCNT;
    }

    public Cholesterol getCHOLE() {
        return cHOLE;
    }

    public void setCHOLE(Cholesterol cHOLE) {
        this.cHOLE = cHOLE;
    }

    protected TotalNutrients(Parcel in) {
        fat = (Fat) in.readValue(Fat.class.getClassLoader());
        carbs = (Carbs) in.readValue(Carbs.class.getClassLoader());
        fiber = (Fiber) in.readValue(Fiber.class.getClassLoader());
        sUGAR = (Sugar) in.readValue(Sugar.class.getClassLoader());
        pROCNT = (Protein) in.readValue(Protein.class.getClassLoader());
        cHOLE = (Cholesterol) in.readValue(Cholesterol.class.getClassLoader());
    }

    public TotalNutrients() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(fat);
        dest.writeValue(carbs);
        dest.writeValue(fiber);
        dest.writeValue(sUGAR);
        dest.writeValue(pROCNT);
        dest.writeValue(cHOLE);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TotalNutrients> CREATOR = new Parcelable.Creator<TotalNutrients>() {
        @Override
        public TotalNutrients createFromParcel(Parcel in) {
            return new TotalNutrients(in);
        }

        @Override
        public TotalNutrients[] newArray(int size) {
            return new TotalNutrients[size];
        }
    };
}