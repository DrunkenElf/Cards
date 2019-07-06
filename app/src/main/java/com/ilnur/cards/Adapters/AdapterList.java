package com.ilnur.cards.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ilnur.cards.R;

public class AdapterList extends BaseAdapter {
    private String[] mas;
    private Context context;

    public AdapterList(Context context, String[] mas){
        this.context = context;
        this.mas = mas;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final AdapterList.ViewHolder holder;
        String title = mas[position];

        if (convertView==null) {
            holder = new AdapterList.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_text_item, null, true);

            holder.tv = convertView.findViewById(R.id.textView);
            //holder.learn = convertView.findViewById(R.id.but_learn);
            //holder.watch = convertView.findViewById(R.id.but_watch);
            //tv.setText(cat.getTitle());
            convertView.setTag(holder);
        } else {
            holder = (AdapterList.ViewHolder) convertView.getTag();
        }
        holder.tv.setText(title);
        /*holder.learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(view.getContext(), cat.getTitle()+" Learn", Toast.LENGTH_SHORT).show();
            }
        });

        holder.watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(view.getContext(), cat.getTitle()+" Watch", Toast.LENGTH_SHORT).show();

            }
        });*/
        return convertView;
    }

    private class ViewHolder{
        TextView tv;
        //Button learn, watch;
    }
}
