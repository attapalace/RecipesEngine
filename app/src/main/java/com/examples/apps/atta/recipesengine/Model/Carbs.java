package com.examples.apps.atta.recipesengine.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Carbs implements Parcelable {

    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("quantity")
    @Expose
    private Float quantity;
    @SerializedName("unit")
    @Expose
    private String unit;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    protected Carbs(Parcel in) {
        label = in.readString();
        quantity = in.readByte() == 0x00 ? null : in.readFloat();
        unit = in.readString();
    }

    public Carbs() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(label);
        if (quantity == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeFloat(quantity);
        }
        dest.writeString(unit);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Carbs> CREATOR = new Parcelable.Creator<Carbs>() {
        @Override
        public Carbs createFromParcel(Parcel in) {
            return new Carbs(in);
        }

        @Override
        public Carbs[] newArray(int size) {
            return new Carbs[size];
        }
    };
}
