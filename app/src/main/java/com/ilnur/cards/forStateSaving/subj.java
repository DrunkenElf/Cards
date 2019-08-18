package com.ilnur.cards.forStateSaving;

import com.google.gson.annotations.SerializedName;

public class subj {
    @SerializedName("subjects")
    public String[] subjects;

    public subj(String[] subjects){
        this.subjects = subjects;
    }
}
