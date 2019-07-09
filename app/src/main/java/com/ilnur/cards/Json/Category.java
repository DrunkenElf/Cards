package com.ilnur.cards.Json;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Category implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("parent_id")
    private int parent_id;
    @SerializedName("reversible")
    private int reversible;
    @SerializedName("order")
    private int order;

    public Category(){}

    private Category(Parcel in){
        id = in.readInt();
        title = in.readString();
        parent_id = in.readInt();
        reversible = in.readInt();
        order = in.readInt();
    }

    public Category(int id, String title, int parent_id, int reversible, int order){
        this.id = id;
        this.title = title;
        this.parent_id = parent_id;
        this.reversible = reversible;
        this.order = order;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeInt(parent_id);
        dest.writeInt(reversible);
        dest.writeInt(order);
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public void setReversible(int reversibale) {
        this.reversible = reversibale;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public int getParent_id() {
        return parent_id;
    }

    public int getOrder() {
        return order;
    }

    public int getReversible() {
        return reversible;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

}
