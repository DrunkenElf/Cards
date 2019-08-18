package com.ilnur.cards.forStateSaving;

import com.google.gson.annotations.SerializedName;

public class logActState {
    @SerializedName("login")
    public String login = "";
    @SerializedName("password")
    public String password = "";

    public logActState(){}

    public logActState(String login, String password){
        this.login = login;
        this.password = password;
    }
}
