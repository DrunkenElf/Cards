package com.ilnur.cards.Json;
import com.google.gson.annotations.SerializedName;
import com.ilnur.cards.Json.Category;

public class Stupid {
    @SerializedName("data")
    private Category[] data;

    public Stupid(Category[] data){
        this.data = data;
    }

    public void setData(Category[] data) {
        this.data = data;
    }

    public Category[] getData() {
        return data;
    }

}
