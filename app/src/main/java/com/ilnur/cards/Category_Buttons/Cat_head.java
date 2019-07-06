package com.ilnur.cards.Category_Buttons;

import com.ilnur.cards.R;

import tellh.com.recyclertreeview_lib.LayoutItemType;

public class Cat_head implements LayoutItemType {
    private String title;

    public Cat_head(String title){
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int getLayoutId() {
        return R.layout.cat_head_lay;
    }

}
