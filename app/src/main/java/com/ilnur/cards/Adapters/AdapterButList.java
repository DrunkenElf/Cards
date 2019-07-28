package com.ilnur.cards.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ilnur.cards.Json.Category;
import com.ilnur.cards.R;
import com.robertlevonyan.views.expandable.Expandable;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class AdapterButList extends BaseAdapter {
    private ArrayList<Category> mas;
    private Context context;
    private int layout;
    private String subj;

    public AdapterButList(Context context, ArrayList<Category> mas, int layout, String subj){
        this.context = context;
        this.mas = mas;
        this.layout = layout;
        this.subj = subj;
    }


    @Override
    public int getCount() {
        return mas.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        Category cat = mas.get(position);
        FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();

        if (cat.getReversible()==1)
            layout = R.layout.expan_lay_rever_item;
        else
            layout = R.layout.expan_lay_item;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(layout, null, true);


        if (convertView==null) {
            holder = new ViewHolder();

            Expandable expandable = convertView.findViewById(layout);



            holder.tv = expandable.findViewById(R.id.textView);
            holder.learn = expandable.findViewById(R.id.but_learn);
            holder.watch = expandable.findViewById(R.id.but_watch);
            if (cat.getReversible()==1){
                holder.revers = expandable.findViewById(R.id.but_rev);
            }
            //tv.setText(cat.getTitle());
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
       /* if (cat.getReversible()==1)
            layout = R.layout.list_item_rever_lay;
        else
            layout = R.layout.list_item_lay;

        if (convertView==null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null, true);

            holder.tv = convertView.findViewById(R.id.headerIndicator_text);
            holder.learn = convertView.findViewById(R.id.but_learn);
            holder.watch = convertView.findViewById(R.id.but_watch);
            if (cat.getReversible()==1){
                holder.revers = convertView.findViewById(R.id.but_rev);
            }
            //tv.setText(cat.getTitle());
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //String s;
        //String.format(s, cat.getOrder(), cat.getTitle());

        //учить перевернутые
        if (cat.getReversible()==1){
            holder.revers.setOnClickListener(v ->{
                LearnFragment lf = new LearnFragment();
                lf.setLearnFragment(subj, cat.getTitle(), cat.getId(), true, false);
                manager.beginTransaction()
                        .replace(R.id.parent, lf)
                        .addToBackStack(null)
                        .commit();
            });
        }

        holder.tv.setText(cat.getTitle());
        //учить
        holder.learn.setOnClickListener(v -> {
            LearnFragment lf = new LearnFragment();
            lf.setLearnFragment(subj, cat.getTitle(), cat.getId(), false, false);
            manager.beginTransaction()
                    .replace(R.id.parent, lf)
                    .addToBackStack(null)
                    .commit();
        });
        //список
        holder.watch.setOnClickListener(v -> {
            int id = cat.getId();
            WatchFragment wf = new WatchFragment();
            wf.setWatchFragment(subj,cat.getTitle(), id, false);
            manager.beginTransaction()
                    .replace(R.id.parent, wf)
                    .addToBackStack(null)
                    .commit();
        });
        return convertView;*/
    }

    private class ViewHolder{
        TextView tv;
        Button learn, watch;
        ImageButton revers;
    }
}
