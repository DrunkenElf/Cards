package com.ilnur.cards.forStateSaving;



import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FragmentState {
    //subj, list, btn, watch, learn
    @SerializedName("name")
    public String name;
    // data in json
    @SerializedName("data")
    public String data;

    public FragmentState(String name, String data){
        this.name = name;
        this.data = data;
    }
}
