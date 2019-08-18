package com.ilnur.cards.forStateSaving;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.ilnur.cards.User;

import java.util.ArrayList;

public class mainActState {
    @SerializedName("logged")
    public boolean logged = false;
    @SerializedName("isRegistered")
    public boolean isRegistered = false;
    @SerializedName("show")
    public boolean show = true;
    @SerializedName("exit")
    public boolean exit = false;
    @SerializedName("fragments")
    public ArrayList<FragmentState> fragments;

    public mainActState(){
        if (fragments == null)
            fragments = new ArrayList<>();
    }

    public mainActState(boolean logged,
                        boolean isRegistered,
                        boolean show,
                        boolean exit,
                        ArrayList<FragmentState> fragments){
        this.logged = logged;
        this.isRegistered = isRegistered;
        this.show = show;
        this.exit = exit;
        this.fragments = fragments;
    }

    public void setParams(boolean logged,
                   boolean isRegistered,
                   boolean show,
                   boolean exit){
        this.logged = logged;
        this.isRegistered = isRegistered;
        this.show = show;
        this.exit = exit;
    }

    public void addFragment(FragmentState fr){
        Log.i("addFragment", fr.name);
        fragments.add(fr);
    }

    public void deleteFragment() {
        Log.i("removeFrag", fragments.get(fragments.size()-1).name);
        fragments.remove(fragments.size() -1);
    }

}
