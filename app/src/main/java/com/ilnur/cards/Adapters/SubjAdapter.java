package com.ilnur.cards.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ilnur.cards.R;

public class SubjAdapter extends BaseAdapter {
    private final Context mContext;
    private final String[] mas;
    private final int[] imgs = {
            R.drawable.rus, R.drawable.phys,
            R.drawable.mathic, R.drawable.bio,
            R.drawable.hist
    };

    public SubjAdapter(Context mContext, String[] mas){
        this.mContext = mContext;
        this.mas = mas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridItem = convertView;
        if (gridItem == null)
            gridItem = LayoutInflater.from(mContext).inflate(R.layout.subj_grid_lay, parent, false);

        TextView text = gridItem.findViewById(R.id.grid_text);
        ImageView image = gridItem.findViewById(R.id.grid_image);
        text.setText(mas[position]);
        image.setImageResource(imgs[position]);
        return gridItem;
    }


    @Override
    public int getCount() {
        return mas.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
