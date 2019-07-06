package com.ilnur.cards.Category_Buttons;

import android.content.Context;

import com.ilnur.cards.R;

import tellh.com.recyclertreeview_lib.LayoutItemType;

public class Cat_butt_rev implements LayoutItemType {
    private String subj;
    private String title;
    private int id;
    private Context context;

    public Cat_butt_rev(String subj, String title, int id, Context context){
        this.title = title;
        this.subj = subj;
        this.id = id;
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public String getSubj() {
        return subj;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int getLayoutId() {
        return R.layout.cat_butt_rev_lay;
    }
}
