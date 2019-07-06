package com.ilnur.cards.Category_Buttons;

import android.content.Context;

import com.ilnur.cards.R;

import tellh.com.recyclertreeview_lib.LayoutItemType;

public class Cat_butt implements LayoutItemType {
    private String subj;
    private String title;
    private int id;
    private Context context;

    public Cat_butt(String subj, String title, int id, Context context){
        this.title = title;
        this.subj = subj;
        this.id = id;
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public String getSubj() {
        return subj;
    }

    @Override
    public int getLayoutId() {
        return R.layout.cat_butt_lay;
    }
}
