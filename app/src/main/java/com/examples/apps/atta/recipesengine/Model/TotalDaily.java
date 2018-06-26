package com.examples.apps.atta.recipesengine.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class TotalDaily implements Parcelable {

    @SerializedName("FAT")
    @Expose
    private Fat fAT;

    @SerializedName("CHOCDF")
    @Expose
    private Carbs cHOCDF;

    @SerializedName("FIBTG")
    @Expose
    private Fiber fIBTG;

    @SerializedName("PROCNT")
    @Expose
    private Protein pROCNT;

    @SerializedName("CHOLE")
    @Expose
    private Cholesterol cHOLE;

    public Fat getFAT() {
        return fAT;
    }

    public void setFAT(Fat fAT) {
        this.fAT = fAT;
    }

    public Carbs getCarbs() {
        return cHOCDF;
    }

    public void setCarbs(Carbs cHOCDF) {
        this.cHOCDF = cHOCDF;
    }

    public Fiber getFiber() {
        return fIBTG;
    }

    public void setFiber(Fiber fIBTG) {
        this.fIBTG = fIBTG;
    }

    public Protein getProtein() {
        return pROCNT;
    }

    public void setProtein(Protein pROCNT) {
        this.pROCNT = pROCNT;
    }

    public Cholesterol getCholesterol() {
        return cHOLE;
    }

    public void setCholesterol(Cholesterol cHOLE) {
        this.cHOLE = cHOLE;
    }

    protected TotalDaily(Parcel in) {
        fAT = (Fat) in.readValue(Fat.class.getClassLoader());
        cHOCDF = (Carbs) in.readValue(Carbs.class.getClassLoader());
        fIBTG = (Fiber) in.readValue(Fiber.class.getClassLoader());
        pROCNT = (Protein) in.readValue(Protein.class.getClassLoader());
        cHOLE = (Cholesterol) in.readValue(Cholesterol.class.getClassLoader());
    }

    public TotalDaily() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(fAT);
        dest.writeValue(cHOCDF);
        dest.writeValue(fIBTG);
        dest.writeValue(pROCNT);
        dest.writeValue(cHOLE);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TotalDaily> CREATOR = new Parcelable.Creator<TotalDaily>() {
        @Override
        public TotalDaily createFromParcel(Parcel in) {
            return new TotalDaily(in);
        }

        @Override
        public TotalDaily[] newArray(int size) {
            return new TotalDaily[size];
        }
    };
}