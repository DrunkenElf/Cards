package com.ilnur.cards.Fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.github.rubensousa.raiflatbutton.RaiflatButton;
import com.github.rubensousa.raiflatbutton.RaiflatImageButton;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.ilnur.cards.CustomWeb;
import com.ilnur.cards.Json.Card;
import com.ilnur.cards.MainActivity;
import com.ilnur.cards.MyDB;
import com.ilnur.cards.R;
import com.ilnur.cards.forStateSaving.ActivityState;
import com.ilnur.cards.forStateSaving.FragmentState;
import com.ilnur.cards.forStateSaving.watch;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class WatchFragment1 extends Fragment {

    //private ArrayList<Card> list;
    private SparseArray<Card> list;
    //String strBody = "";
    private CustomWeb web;
    private CustomWeb web1;
    private int width;
    int load = 0;
    boolean flag;
    private StringBuilder s;
    watch watch;
    MyDB db;


    public void setWatchFragment(MyDB db, watch watch) {
        this.watch = watch;
        this.db = db;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i("onCreate", "yesy");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onDestroyView() {
        Log.i("onDestroy", "yesy");
        super.onDestroyView();
    }

    @Override
    public void onLowMemory() {
        Log.d("onLowMem", "yesy");
        Log.v("onLowMem", "yesy");
        Log.wtf("onLowMem", "yesy");
        Log.w("onLowMem", "yesy");
        if (flag){
            web.clearCache(false);
            web1.clearCache(false);
        } else {
            web.clearCache(false);
            web.destroyDrawingCache();
        }
        super.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("Watch", "saved");
        //MainActivity.main.fragments.remove(MainActivity.main.fragments.size()-1);
        /*outState.putCharSequence("subj", subj);
        outState.putCharSequence("title", title);
        outState.putInt("id", id);
        outState.putBoolean("parent", parent);*/
        /*if (flag){
            web.saveState(outState);
            web1.saveState(outState);
        } else {
            web.saveState(outState);
        }*/
        //outState.putParcelableArrayList("watch", list);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("Watch", "actcreated");
       /* if (savedInstanceState != null) {
            subj = savedInstanceState.getString("subj");
            title = savedInstanceState.getString("title");
            id = savedInstanceState.getInt("id");
            parent = savedInstanceState.getBoolean("parent");
            *//*if (flag){
                web.restoreState(savedInstanceState);
                web1.restoreState(savedInstanceState);
            } else
                web.restoreState(savedInstanceState);*//*
            //list = savedInstanceState.getParcelableArrayList("watch");
        }*/
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.i("Watch", "restored");
       /* if (savedInstanceState != null) {
            subj = savedInstanceState.getString("subj");
            title = savedInstanceState.getString("title");
            id = savedInstanceState.getInt("id");
            parent = savedInstanceState.getBoolean("parent");
            //list = savedInstanceState.getParcelableArrayList("watch");
            *//*if (flag){
                web.restoreState(savedInstanceState);
                web1.restoreState(savedInstanceState);
            } else
                web.restoreState(savedInstanceState);*//*
        }*/
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        ViewGroup rootview = (ViewGroup) getView();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            /*ScrollView sv = rootview.findViewById(R.id.scroll);
            ViewGroup.LayoutParams params = sv.getLayoutParams();
            ViewGroup.LayoutParams params1 = web.getLayoutParams();
            //width = params1.height;
            //web.
            web.setLayoutParams(params1);*/
            /*int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            CustomWeb web = rootview.findViewById(R.id.watch_list1);
            web.setLayoutParams(new TableRow.LayoutParams(width, height));*/
            Log.i("ORIENTATION", "landscape");
            Log.i("WIDTH", "landscape " + width);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
           /* int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            CustomWeb web = rootview.findViewById(R.id.watch_list1);
            web.setLayoutParams(new TableRow.LayoutParams(width, height));*/
            ViewGroup.LayoutParams params = web.getLayoutParams();
            params.width = width;
            float density = getResources().getDisplayMetrics().density;
            float dpwidth = getResources().getDisplayMetrics().widthPixels / density;
            params.width = (int) dpwidth;
            web.setLayoutParams(params);
            if (flag) {
                ViewGroup.LayoutParams params1 = web1.getLayoutParams();
                params1.width = (int) dpwidth;
                web1.setLayoutParams(params1);
            }
            Log.i("ORIENTATION", "portarait");
            Log.i("WIDTH", "portarait " + params.width);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("Watch", "viewcreated");
        addContent(flag);
        for (Iterator<FragmentState> iterator = MainActivity.main.fragments.iterator(); iterator.hasNext(); ) {
            FragmentState s = iterator.next();
            if (s.name.equals("watch")) {
                iterator.remove();
            }
        }
        MainActivity.main.addFragment(new FragmentState("watch", new Gson().toJson(watch)));
        db.updateActState(new ActivityState("main", new Gson().toJson(MainActivity.main)));
        //MainActivity.main.fragments.remove(MainActivity.main.fragments.size()-1);
        //MainActivity.main.addFragment(new FragmentState("watch", new Gson().toJson(watch)));
        //db.updateActState(new ActivityState("main", new Gson().toJson(MainActivity.main)));
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.watch_lay1, container, false);
        Log.i("Watch", "create");
        //getActivity().setContentView(R.layout.watch_lay1);

        setRetainInstance(true);
       /* if (savedInstanceState != null) {
            subj = savedInstanceState.getString("subj");
            title = savedInstanceState.getString("title");
            id = savedInstanceState.getInt("id");
            parent = savedInstanceState.getBoolean("parent");
            //list = savedInstanceState.getParcelableArrayList("watch");

            *//*if (flag){
                web.restoreState(savedInstanceState);
                web1.restoreState(savedInstanceState);
            } else
                web.restoreState(savedInstanceState);*//*
            if (parent) {
               *//* if (MyDB.hugePages.containsKey(subj+"//"+id)) {
                    list = MyDB.hugePages.get(subj + "//" + id);
                    Log.i("page", " contains");
                    Log.i("page", list.get(0).getAvers());
                }
                else*//*
                list = MyDB.getParentCardsWatch(subj, id);
            } else
                list = MyDB.getChildCardsWatch(subj, title, id);
        }*/

        /*if (!MyDB.hugePages.containsKey(subj+"//"+id) && parent)
            new loadHugePageWatch().execute();*/

        modifyToolbar();
        Glide.with(this)
                .load(R.drawable.ball)
                .timeout(100)
                .into((ImageView) rootview.findViewById(R.id.packman));


        if (savedInstanceState == null || !savedInstanceState.containsKey("watch")) {
            if (watch.parent) {
                /*if (MyDB.hugePages.containsKey(subj + "//" + id)) {
                    list = MyDB.hugePages.get(subj + "//" + id);
                    Log.i("page", " contains");
                    Log.i("page", list.get(0).getAvers());
                } else*/
                    list = db.getParentCardsWatch(watch.subj, watch.id);
            } else
                list = db.getChildCardsWatch(watch.subj, watch.title, watch.id);
        }

        if (watch.parent && list.size() > 50 && watch.subj.equals("История") && (list.get(0).getAvers().getBytes().length > 90000 ||
                list.get(0).getRevers().getBytes().length > 90000))
            initTwoWeb(rootview, true);
        else
            initTwoWeb(rootview, false);


        return rootview;
    }

    private void initTwoWeb(View rootview, boolean flag) {
        ScrollView sv = rootview.findViewById(R.id.scroll);


        web = rootview.findViewById(R.id.watch_list1);
        web.first = true;
        web.several = flag;
        //web.sv = sv;
        setupWeb(web, rootview.getContext(), rootview);
        Log.i("FLAG", "" + flag);
        if (flag) {
            web1 = rootview.findViewById(R.id.watch_list2);
            web1.several = flag;
            web1.first = false;
            //web1.sv = sv;
            web1.second = web;
            web.second = web1;
            setupWeb(web1, rootview.getContext(), rootview);
        }
        this.flag = flag;
        //addContent(flag);
        //secondpart = addContent(flag);


        /*if (secondpart != null) {
            web1.loadDataWithBaseURL(null, secondpart, "text/html", "utf-8", "auto:black");

        }*/
    }

    private void addContent(boolean flag) {
        if (flag) {
            new loadToWeb(web, true, false).execute();
            new loadToWeb(web1, false, false).execute();
            //loadData(web,true, false);
            //loadData(web1, false, false);
        } else {
            //loadData(web, false, true);
            new loadToWeb(web, false, true).execute();
        }
    }

    //closed with </body>
    private String getHead(boolean big) {
        String size;
        if (watch.subj.equals("Математика"))
            size = "20pt;";
        else if (big)
            size = "15pt;";
        else
            size = "10pt;";
        StringBuilder sb = new StringBuilder(2000);
        sb.append("<html><head>")
                .append("<meta name=\"viewport\" content=\"width=device-width\"/>\n")
                //.append("<meta name=\"viewport\" content=\"width=device-width; user-scalable=no; initial-scale=1.0; minimum-scale=1.0; maximum-scale=1.0; target-densityDpi=device-dpi;\\\"/>")
                .append(" <script type=\"text/javascript\">\n"
                        +"function uped(data)\n"
                        +"{AndroidFunction.update(data);};"
                        +"function changecol(data, color, edited){"
                        +"document.getElementById(data).style.background = color;  "
                        +"document.getElementById(data).id = edited;};"
                        +"</script><style>"
                        +"@media screen and (orientation:portrait) {"
                        +"img{width: 100%;}}"
                        +"@media screen and (orientation:landscape) {"
                        +"img{width: 100%;}}"
                        +"table, tr {border-collapse: collapse; width: 100%;}"+"td.left {"
                        +"   padding-right: 7px;"
                        +"   padding-left: 7px;"
                        +"   padding-top: 7px;"+"   padding-bottom: 7px;");
        /*if (subj.equals("Физика")) {
            sb.append("     width: 119;");
        } else*/
            sb.append("   width: 50%;");
        sb.append("   min-width: 30%;"+"   font-size: "+size
                +"   text-align: left;"
                +"   vertical-align: top;"
                +"   horizontal-align: left;}"
                +" td.line {"
                +"       border-left: 1px solid #000000;"
                +"       padding-left: 7px;"
                +"       padding-right: 7px;"
                +"       padding-top: 7px;"
                +"       padding-bottom: 7px;"
                +"       width: 50%;"
                +"       max-width: 50%;"
                +"       font-size: "+size
                +"       text-align: left;"
                +"       vertical-align: top;"
                +"       horizontal-align: right;}"
                +"   p {padding-top: 2px; padding-bottom: 2px;}"
                +" img {width: 100%; height: auto;}"
                +"td.left:nth-child(1){min-width: 119; width: fit-content;}"
                +"td.left div.pbody {width: unset;}"
                +"td.line div.pbody {width: fir-content;}"
                +"* {font-size: "+size+"}"
                +"td {border-bottom: 2px solid black;}"+"</style></head><body>");
        return sb.toString();
    }

    private void setupWeb(CustomWeb web, Context con, View rootView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //WebView.enableSlowWholeDocumentDraw();
        }
        web.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        web.getSettings().setDomStorageEnabled(false);
        web.getSettings().setSupportZoom(false);
        //web.setInitialScale(5);
        //web.setWebChromeClient(new WebChromeClient());
        web.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.i("page", "finished");
                load++;
                if (watch.parent && load == 2) {
                    ImageView gif = rootView.findViewById(R.id.packman);
                    gif.setVisibility(View.GONE);
                    s = null;
                    width = web.getWidth();
                    Log.i("widthLoaded", "" + width);
                    System.gc();
                    Runtime.getRuntime().gc();
                } else if (!watch.parent && load == 1) {
                    ImageView gif = rootView.findViewById(R.id.packman);
                    //gif.animate().alpha(0.0f);
                    s = null;
                    width = web.getWidth();
                    gif.setVisibility(View.GONE);
                    System.gc();
                    Runtime.getRuntime().gc();
                }
            }
        });

        //web.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        web.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        web.getSettings().setEnableSmoothTransition(true);
        web.getSettings().setJavaScriptEnabled(true);
        //web.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        web.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        //web.setWebViewClient(new WebViewClient());
        //web.setVerticalScrollBarEnabled(true);
        if (watch.subj.equals("История"))
            web.getSettings().setDefaultFontSize(16);
        else
            web.getSettings().setDefaultFontSize(20);
        web.getSettings().setLoadWithOverviewMode(true);
        web.getSettings().setUseWideViewPort(true);
        //web.getSettings().setAppCacheEnabled(true);
        //web.getSettings().setAppCachePath(con.getCacheDir().getPath());

        web.addJavascriptInterface(new JSinterface(con, web), "AndroidFunction");
    }

    private void modifyToolbar() {
        Log.i("MODIFY", watch.title);
        CollapsingToolbarLayout col = CollapsingToolbarLayout.class.cast(getActivity().findViewById(R.id.collapsing_toolbar));
        col.setTitle(watch.title);
        col.setExpandedTitleMarginBottom((int) getContext().getResources().getDimension(R.dimen.margin_title_col));
        RaiflatButton learn = col.findViewById(R.id.learn);
        RaiflatButton watch = col.findViewById(R.id.watch);
        RaiflatImageButton rever = col.findViewById(R.id.rever);
        learn.setVisibility(View.GONE);
        watch.setVisibility(View.GONE);
        rever.setVisibility(View.GONE);
        AppBarLayout apbar = AppBarLayout.class.cast(getActivity().findViewById(R.id.apbar));
        apbar.setExpanded(false);
        apbar.setClickable(false);
    }

    public class updateCard extends AsyncTask<params, Void, Void> {

        @Override
        protected Void doInBackground(params... params) {
            db.updateWatchCard(params[0].subj, params[0].id, params[0].result, params[0].old_result);
            return null;
        }
    }



    private class loadToWeb extends AsyncTask<String, Void, String> {
        CustomWeb web;
        boolean isFirstPart;
        boolean onlyOne;

        public loadToWeb(CustomWeb web, boolean isFirstPart, boolean onlyOne) {
            this.web = web;
            this.isFirstPart = isFirstPart;
            this.onlyOne = onlyOne;
        }

        @Override
        protected String doInBackground(String... strings) {
            if (watch.parent) {
                try {
                    if (watch.subj.equals("История"))
                        Thread.sleep(1200);
                    else
                        Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (watch.subj.equals("Истроия")) {
                try {
                    Thread.sleep(70);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            s = new StringBuilder();
            System.out.println(getHead(false));
            if (watch.subj.equals("История"))
                s.append(getHead(false)).append("<table>");
            else
                s.append(getHead(true)).append("<table>");
            Card c;
            int begin;
            int end;
            if (isFirstPart) {
                begin = 0;
                end = list.size() / 2;
                Log.i("first", "async");
                s.append("<caption>Карточки, отмеченные зеленым, не показываются</caption><tr><th><br></th></tr>");
            } else {
                begin = list.size() / 2;
                end = list.size();
                Log.i("second", "async");
            }
            if (onlyOne) {
                begin = 0;
                end = list.size();
                Log.i("only", "async");
                s.append("<caption>Карточки, отмеченные зеленым, не показываются</caption><tr><th><br></th></tr>");
            }
            for (int i = begin; i < end; i++) {
                c = list.get(i);
                String color = "";
                switch (c.getResult()) {
                    case 0:
                        color = "#ffffff";
                        break;
                    case 1:
                        color = getString(R.string.onee);
                        break;
                    case 2:
                        color = getString(R.string.twoo);
                        break;
                    case 3:
                        color = getString(R.string.three);
                        break;
                    case 4:
                        color = getString(R.string.fourr);
                        break;
                }
                s.append("<tr onclick=\"javascript:return uped(this.id)\" id=\"" + watch.subj + "/" + c.getId() + "/");
                //s.append(subj);
                //s.append("/");
                //s.append(c.getId());
                //s.append("/");
                //s.append(c.getResult());
                s.append(c.getResult() + "\" bgcolor=\"" + color);
                //s.append(color);
                s.append("\" class='clickable-row' data-href='" + watch.subj + "/" + c.getId() + "/" + c.getResult() + "'>");
                //s.append(subj);
                //s.append("/");
                //s.append(c.getId());
                //s.append("/");
                //s.append(c.getResult());
                //s.append("'>");
                s.append("<td class=\"left\">" + c.getAvers() + "</td><td class=\"line\">" + c.getRevers() + "</td></tr>");
                //s.append(c.getAvers());
                //s.append("</td><td class=\"line\">" + c.getRevers());
                //s.append(c.getRevers());
                //s.append("</td></tr>");
            }
            s.append("</table></body></html>");
            /*File f = new File("/data/data/" + getContext().getPackageName() + "/file/");
            if (!f.exists())
                f.mkdir();

            try {
                OutputStream os = new FileOutputStream("/data/data/" + getContext().getPackageName() + "/file/" + subj + id + ".html");
                os.write(s.toString().getBytes());
                os.flush();
                os.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            return s.toString();
        }


        @Override
        protected void onPostExecute(String data) {
            //super.onPostExecute(data);
            Log.i("POST", "started");
            /*web.post(new Runnable() {
                @Override
                public void run() {
                    web.loadDataWithBaseURL(null, data, "text/html", "utf-8", "auto:black");
                }
            });*/
            /*getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    web.loadDataWithBaseURL(null, data, "text/html", "utf-8", "auto:black");
                }
            });*/
            //s = null;
            web.loadDataWithBaseURL(null, data, "text/html", "utf-8", "auto:black");
            /*if (isFirstPart && !onlyOne)
                w = data;
            else if (!isFirstPart && !onlyOne)
                w1 = data;*/
            web.freeMemory();
            //System.gc();
            //Runtime.getRuntime().gc();
        }
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

    public class JSinterface {
        Context context;
        CustomWeb web;

        public JSinterface(Context context, CustomWeb web) {
            this.context = context;
            this.web = web;
        }

        @JavascriptInterface
        public void update(String data) {
            String subj = data.split("/")[0];
            //Log.i("upade", "yes");
            int id = Integer.parseInt(data.split("/")[1]);
            int old_result = Integer.parseInt(data.split("/")[2]);
            new updateCard().execute(new params(subj, id, 1, old_result));

            web.post(new Runnable() {
                @Override
                public void run() {
                    String color = "";
                    switch (old_result) {
                        case 0:
                            color = getString(R.string.onee);
                            web.loadUrl("javascript:changecol(\"" + data + "\",\"" + color + "\", \"" + subj + "/" + id + "/" + 1 + "\")");
                            break;
                        case 1:
                            color = getString(R.string.twoo);
                            web.loadUrl("javascript:changecol(\"" + data + "\",\"" + color + "\", \"" + subj + "/" + id + "/" + 2 + "\")");
                            break;
                        case 2:
                            color = getString(R.string.three);
                            web.loadUrl("javascript:changecol(\"" + data + "\",\"" + color + "\", \"" + subj + "/" + id + "/" + 3 + "\")");
                            break;
                        case 3:
                            color = getString(R.string.fourr);
                            web.loadUrl("javascript:changecol(\"" + data + "\",\"" + color + "\", \"" + subj + "/" + id + "/" + 4 + "\")");
                            break;
                        case 4:
                            color = "#ffffff";
                            web.loadUrl("javascript:changecol(\"" + data + "\",\"" + color + "\", \"" + subj + "/" + id + "/" + 0 + "\")");
                            break;
                    }
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        //System.gc();
        //Runtime.getRuntime().gc();
        super.onDestroy();
    }

}
