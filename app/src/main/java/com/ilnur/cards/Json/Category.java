package com.ilnur.cards.Json;

import com.google.gson.annotations.SerializedName;

public class Category {
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
    public Category(int id, String title, int parent_id, int reversible, int order){
        this.id = id;
        this.title = title;
        this.parent_id = parent_id;
        this.reversible = reversible;
        this.order = order;
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
}
