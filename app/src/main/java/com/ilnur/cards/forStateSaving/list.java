package com.ilnur.cards.forStateSaving;

import com.google.gson.annotations.SerializedName;

public class list {
    @SerializedName("title")
    public String title;
    @SerializedName("mas")
    public String[] mas;

    public list(String title, String[] mas){
        this.title = title;
        this.mas = mas;
    }
}
