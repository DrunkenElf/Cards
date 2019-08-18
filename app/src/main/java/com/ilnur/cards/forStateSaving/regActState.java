package com.ilnur.cards.forStateSaving;

import com.google.gson.annotations.SerializedName;

public class regActState {
    @SerializedName("username")
    public String username = null;
    @SerializedName("name1")
    public String name1 = null;
    @SerializedName("surname1")
    public String surname1 = null;
    @SerializedName("password")
    public String password = null;
    @SerializedName("password1")
    public String password1 = null;
    @SerializedName("day")
    public String day = null;
    @SerializedName("month")
    public String month = null;
    @SerializedName("year")
    public String year = null;
    @SerializedName("status")
    public String status = null;
    @SerializedName("valid")
    public boolean valid = true;

    public regActState(){}

    public regActState(
            String username,
            String name1,
            String surname1,
            String password,
            String password1,
            String day,
            String month,
            String year,
            String status,
            boolean valid) {
        this.username = username;
        this.name1 = name1;
        this.surname1 = surname1;
        this.password = password;
        this.password1 = password1;
        this.day = day;
        this.month = month;
        this.year = year;
        this.status = status;
        this.valid = valid;
    }
}
