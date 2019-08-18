package com.ilnur.cards.forStateSaving;

import com.google.gson.annotations.SerializedName;
import com.ilnur.cards.Json.Category;

import java.util.ArrayList;

public class btn {
    @SerializedName("subj")
    public String subj;
    @SerializedName("id")
    public int id;
    @SerializedName("title")
    public String title;
    @SerializedName("list")
    public ArrayList<Category> list;
    @SerializedName("checkRevers")
    public boolean checkRevers;


    public btn(String subj, int id, String title, ArrayList<Category> list, boolean checkRevers){
        this.subj = subj;
        this.id = id;
        this.title = title;
        this.list = list;
        this.checkRevers = checkRevers;
    }
}
