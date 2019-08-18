package com.ilnur.cards.forStateSaving;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ActivityState {
    @SerializedName("activity")
    public String activity;
    @SerializedName("data")
    public String data;

    public ActivityState(String activity, String data){
        this.activity = activity;
        this.data = data;
    }
    /**
     * firstActivity is always MainAct
     * activity: Main, Login, Register (main->login, main->(login->reg)
     * data: [0] is always main, [1] (login | reg)
     */
}
