package com.ilnur.cards.forStateSaving;

import com.google.gson.annotations.SerializedName;
import com.ilnur.cards.Json.Category;

import java.util.ArrayList;

public class learn {
    @SerializedName("subj")
    public String subj;
    @SerializedName("id")
    public int id;
    @SerializedName("title")
    public String title;
    @SerializedName("revers")
    public boolean revers;
    @SerializedName("parent")
    public boolean parent;
    @SerializedName("i")
    public int i = 0;
    @SerializedName("wrongs")
    public String wrongs = "";
    @SerializedName("right")
    public int right = 0;
    @SerializedName("wrong")
    public int wrong = 0;
    @SerializedName("cards")
    public int[] cards;

    public learn(String subj, int id, String title, boolean revers, boolean parent, int i, String wrongs){
        this.subj = subj;
        this.id = id;
        this.title = title;
        this.revers = revers;
        this.parent = parent;
        this.i = i;
        this.wrongs = wrongs;
    }
    public learn(String subj, int id, String title, boolean revers, boolean parent){
        this.subj = subj;
        this.id = id;
        this.title = title;
        this.revers = revers;
        this.parent = parent;
    }

}
