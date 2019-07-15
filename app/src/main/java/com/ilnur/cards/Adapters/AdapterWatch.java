package com.ilnur.cards.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.text.Html;
import android.text.Spanned;
import android.util.Base64;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.ilnur.cards.Json.Card;
import com.ilnur.cards.MyDB;
import com.ilnur.cards.R;


import java.lang.ref.WeakReference;
import java.util.ArrayList;

import androidx.core.content.ContextCompat;

public class AdapterWatch extends BaseAdapter {
    private ArrayList<Card> list;
    private Context context;
    private String subj;
    private Spans spans;
    private boolean AvBig;


    public AdapterWatch(Context context, String subj, ArrayList<Card> list) {
        this.context = context;
        this.subj = subj;
        this.list = list;
        spans = new Spans(context);
        if (list.get(0).getAvers().length() > list.get(0).getRevers().length()){
            AvBig = true;
        } else
            AvBig = false;
    }

    @Override
    public int getCount() {
        return list.size();
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
        ViewHolder holder;

        Card card = list.get(position);


        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.watch_lay_item, parent, false);
            holder.lay = convertView.findViewById(R.id.obolochka);
            holder.web = convertView.findViewById(R.id.item_spisok);
            LinearLayout.LayoutParams layout = (LinearLayout.LayoutParams) holder.web.getLayoutParams();
            holder.web1 = convertView.findViewById(R.id.item_spisok1);
            LinearLayout.LayoutParams layout1 = (LinearLayout.LayoutParams) holder.web1.getLayoutParams();
            if (AvBig){
                layout.weight = 2;
                layout1.weight = 3;
            } else {
                layout.weight = 3;
                layout1.weight = 2;
            }
           /* holder.p1 = new Params(holder.web, card.getAvers());
            holder.p2 = new Params(holder.web1, card.getRevers());*/

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();

        }


        if (holder.web != null)
            spans.loadToView(card.getAvers(), holder.web);
        if (holder.web1 != null)
            spans.loadToView(card.getRevers(), holder.web1);

        switch (card.getResult()) {
            case 0:
                holder.lay.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                break;
            case 1:
                holder.lay.setBackgroundColor(ContextCompat.getColor(context, R.color.one));
                break;
            case 2:
                holder.lay.setBackgroundColor(ContextCompat.getColor(context, R.color.two));
                break;
            case 3:
                holder.lay.setBackgroundColor(ContextCompat.getColor(context, R.color.three));
                break;
            case 4:
                holder.lay.setBackgroundColor(ContextCompat.getColor(context, R.color.four));
                break;
        }
        //holder.p1 = new Params(holder.web, card.getAvers());
        //drawHtml w1 = new drawHtml();
        //w1.execute(holder.p1);


        //holder.p2 = new Params(holder.web1, card.getRevers());
        //drawHtml w2 = new drawHtml();
        //w2.execute(holder.p2);


        holder.lay.setOnClickListener(v -> {
            switch (card.getResult()) {
                case 0:
                    holder.lay.setBackgroundColor(ContextCompat.getColor(context, R.color.one));
                    card.setResult(1);
                    new updateCard().execute(new params(subj, card.getId(), 1, 0));
                    //MyDB.updateWatchCard(subj, card.getId(), 1, 0);
                    break;
                case 1:
                    holder.lay.setBackgroundColor(ContextCompat.getColor(context, R.color.two));
                    card.setResult(2);
                    new updateCard().execute(new params(subj, card.getId(), 1, 1));
                    //MyDB.updateWatchCard(subj, card.getId(), 1, 1);
                    break;
                case 2:
                    holder.lay.setBackgroundColor(ContextCompat.getColor(context, R.color.three));
                    card.setResult(3);
                    new updateCard().execute(new params(subj, card.getId(), 1, 2));
                    //MyDB.updateWatchCard(subj, card.getId(), 1, 2);
                    break;
                case 3:
                    holder.lay.setBackgroundColor(ContextCompat.getColor(context, R.color.four));
                    card.setResult(4);
                    new updateCard().execute(new params(subj, card.getId(), 1, 3));
                    break;
                case 4:
                    holder.lay.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                    card.setResult(0);
                    new updateCard().execute(new params(subj, card.getId(), 1, 4));
                    break;
            }
        });

        return convertView;
    }

    private class params {
        String subj;
        int id;
        int result;
        int old_result;

        public params(String subj, int id, int result, int old_result) {
            this.subj = subj;
            this.id = id;
            this.result = result;
            this.old_result = old_result;
        }
    }

    private class ViewHolder {
        TextView web;
        TextView web1;
        LinearLayout lay;

    }

    private static class Spans {
        //private Spans instance;
        private LruCache<String, Spanned> mMemoryCache = null;
        private int cacheSize = 1024 * 1024 * 10;
        private Context context;

        private Spans(Context context) {
            mMemoryCache = new LruCache<String, Spanned>(cacheSize);
            this.context = context;
        }
        /*public Spans getInstance(){
            if (instance == null){
                instance = new Spans();
            }
            return instance;
        }*/

        private class getSpanned extends AsyncTask<String, Void, Spanned> {
            private WeakReference<TextView> tv;
            private Context cont;

            public getSpanned(TextView tv, Context cont) {
                this.tv = new WeakReference<>(tv);
                this.cont = cont;
            }

            @Override
            protected void onPreExecute() {
                tv.get().setTag(this);
            }

            @Override
            protected Spanned doInBackground(String... strings) {
                Spanned s = Html.fromHtml(strings[0], source -> {
                    Bitmap bitmap;
                    if (source.contains("png")) {
                        byte[] encode = Base64.decode(source.split(",")[1], Base64.DEFAULT);
                        bitmap = BitmapFactory.decodeByteArray(encode, 0, encode.length);
                    } else {
                        bitmap = imageFromSVGString(source.split(",")[1]);
                    }
                    //Bitmap bitmap = imageFromString(source.split(",")[1]);
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(cont.getResources(), bitmap);
                    bitmapDrawable.setBounds(0, 0, bitmapDrawable.getIntrinsicWidth(), bitmapDrawable.getIntrinsicHeight());

                    //Log.i("HEITS", String.valueOf(bitmapDrawable.getIntrinsicWidth())+" "+String.valueOf(bitmapDrawable.getIntrinsicHeight()));
                    return bitmapDrawable;
                }, null);
                mMemoryCache.put(strings[0], s);
                return s;
            }

            @Override
            protected void onPostExecute(Spanned s) {
                try {
                    if (tv.get().getTag() == this) {
                        tv.get().setTag(null);

                        if (s != null)
                            tv.get().setText(s);
                    } else if (tv.get().getTag() != null) {

                        ((getSpanned) tv.get().getTag()).cancel(true);
                        tv.get().setTag(null);
                    }
                } catch (NullPointerException e){
                    tv.get().setTag(null);
                    e.printStackTrace();
                }
            }
        }


        public void loadToView(String source, TextView tv) {
            if (source == null || source.length() == 0)
                return;


            Spanned s = getFromCache(source);

            if (s == null) {

                final getSpanned task = (getSpanned) new getSpanned(tv, context);

                tv.setTag(task);

                task.execute(source);
            } else {
                tv.setText(s);
            }
        }


        public Spanned getFromCache(String s) {
            return mMemoryCache.get(s);
        }

    }

    /*class Params{
        TextView tv;
        String source;
        Spanned s;
        public Params(TextView tv, String source){
            this.tv = tv;
            this.source = source;
        }
        public Params(TextView tv, Spanned s){
            this.tv = tv;
            this.s = s;
        }
        public void setS(Spanned s){
            this.s = s;
        }
    }*/


    private class drawHtml extends AsyncTask<String, Void, Spanned> {
        WeakReference<TextView> reference;

        public drawHtml(TextView tv) {
            reference = new WeakReference<>(tv);
        }

        @Override
        protected Spanned doInBackground(String... params) {
            Spanned s = Html.fromHtml(params[0], source -> {
                Bitmap bitmap;
                if (source.contains("png")) {
                    byte[] encode = Base64.decode(source.split(",")[1], Base64.DEFAULT);
                    bitmap = BitmapFactory.decodeByteArray(encode, 0, encode.length);
                } else {
                    bitmap = AdapterWatch.this.imageFromSVGString(source.split(",")[1]);
                }
                //Bitmap bitmap = imageFromString(source.split(",")[1]);
                BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(), bitmap);
                bitmapDrawable.setBounds(0, 0, bitmapDrawable.getIntrinsicWidth(), bitmapDrawable.getIntrinsicHeight());

                //Log.i("HEITS", String.valueOf(bitmapDrawable.getIntrinsicWidth())+" "+String.valueOf(bitmapDrawable.getIntrinsicHeight()));
                return bitmapDrawable;
            }, null);
            return s;
        }

        @Override
        protected void onPostExecute(Spanned s) {
            super.onPostExecute(s);
            TextView tv = reference.get();
            tv.setText(s);
            //params.setS(params.s);
        }
    }

    public static Bitmap imageFromSVGString(String imageData) {
        //Log.i("base64", imageData);
        byte[] imageAsBytes = Base64.decode(imageData.getBytes(), Base64.DEFAULT);
        String svgAsString = new String(imageAsBytes);
        SVG svg = null;
        try {
            svg = SVG.getFromString(svgAsString);
        } catch (SVGParseException e) {
            e.printStackTrace();
        }

        // Create a bitmap and canvas to draw onto
        svg.setDocumentHeight(Math.round(svg.getDocumentHeight() * 2.7));
        svg.setDocumentWidth(Math.round(svg.getDocumentWidth() * 2.1));

        Bitmap newBM = Bitmap.createBitmap((int) Math.ceil(svg.getDocumentWidth()),
                (int) Math.ceil(svg.getDocumentHeight()),
                Bitmap.Config.ARGB_8888);

        Canvas bmcanvas = new Canvas(newBM);

        svg.renderToCanvas(bmcanvas);

        return newBM;
    }

    public class updateCard extends AsyncTask<params, Void, Void> {

        @Override
        protected Void doInBackground(params... params) {
            MyDB.updateWatchCard(params[0].subj, params[0].id, params[0].result, params[0].old_result);
            return null;
        }
    }

    public void clearCache(){
        spans.mMemoryCache = null;
        spans = null;
    }
}
