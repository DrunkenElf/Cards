package com.ilnur.cards.forStateSaving;

import com.google.gson.annotations.SerializedName;

public class watch {
    @SerializedName("subj")
    public String subj;
    @SerializedName("title")
    public String title;
    @SerializedName("id")
    public int id;
    @SerializedName("parent")
    public boolean parent;

    public watch(String subj, String title, int id, boolean parent){
        this.subj = subj;
        this.title = title;
        this.id = id;
        this.parent = parent;
    }
}
