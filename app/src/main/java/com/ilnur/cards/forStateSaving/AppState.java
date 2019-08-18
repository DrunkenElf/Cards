package com.ilnur.cards.forStateSaving;

import android.database.sqlite.SQLiteException;

import com.google.gson.Gson;
import com.ilnur.cards.MyDB;

import java.util.ArrayList;

public class AppState {
    MyDB db;
    public ActivityState[] activities;

    public AppState(MyDB db) {
        this.db = db;
        //if first creation => empty activities
        fetchActs();
    }

    public void setMain(ActivityState main){
        activities[0] = main;
    }
    public void setLog(ActivityState log){
        activities[1] = log;
    }
    public void setReg(ActivityState reg){
        activities[2] = reg;
    }

    public mainActState getMainState() {
        if (activities[0] != null)
            return new Gson().fromJson(activities[0].data, mainActState.class);
        else
            return new mainActState();
    }

    public logActState getLogState() {
        if (activities[1] != null)
            return new Gson().fromJson(activities[1].data, logActState.class);
        else
            return null;
    }

    public regActState getRegState() {
        if (activities[2] != null)
            return new Gson().fromJson(activities[2].data, regActState.class);
        else
            return null;
    }

    public void fetchActs() {
        try {
            activities = db.fetchActs();
        } catch (SQLiteException e){
            activities = new ActivityState[3];
        }
    }
}
