package com.ilnur.cards.Fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.rubensousa.raiflatbutton.RaiflatButton;
import com.github.rubensousa.raiflatbutton.RaiflatImageButton;
import com.google.android.material.appbar.AppBarLayout;
import com.ilnur.cards.CustomWeb;
import com.ilnur.cards.Json.Card;
import com.ilnur.cards.MyDB;
import com.ilnur.cards.R;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import java.util.ArrayList;

public class WatchFragment1 extends Fragment {
    private String subj;
    private String title;
    private int id;
    private boolean parent;
    private ArrayList<Card> list;
    //String strBody = "";
    private CustomWeb web;
    private CustomWeb web1;
    String head;
    String secondpart = null;
    int index;


    public void setWatchFragment(String subj, String title, int id, boolean parent) {
        this.subj = subj;
        this.title = title;
        this.id = id;
        this.parent = parent;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("Watch", "saved");
        outState.putCharSequence("subj", subj);
        outState.putCharSequence("title", title);
        outState.putInt("id", id);
        outState.putBoolean("parent", parent);
        //outState.putParcelableArrayList("watch", list);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("Watch", "created");
        if (savedInstanceState != null) {
            subj = savedInstanceState.getString("subj");
            title = savedInstanceState.getString("title");
            id = savedInstanceState.getInt("id");
            parent = savedInstanceState.getBoolean("parent");
            //list = savedInstanceState.getParcelableArrayList("watch");
        }
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.i("Watch", "restored");
        if (savedInstanceState != null) {
            subj = savedInstanceState.getString("subj");
            title = savedInstanceState.getString("title");
            id = savedInstanceState.getInt("id");
            parent = savedInstanceState.getBoolean("parent");
            //list = savedInstanceState.getParcelableArrayList("watch");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*if (secondpart != null && web1 != null){
            web1.loadDataWithBaseURL(null, secondpart, "text/html", "utf-8", "auto:black");
        }*/
        /*if (secondpart != null && web1 != null) {
            web1.post(new Runnable() {
                @Override
                public void run() {
                    web1.loadDataWithBaseURL(null, secondpart, "text/html", "utf-8", "auto:black");
                }
            });
        }*/
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.watch_lay, container, false);
        Log.i("CREATE", "START");
        if (savedInstanceState != null) {
            subj = savedInstanceState.getString("subj");
            title = savedInstanceState.getString("title");
            id = savedInstanceState.getInt("id");
            parent = savedInstanceState.getBoolean("parent");
            //list = savedInstanceState.getParcelableArrayList("watch");
            if (parent)
                list = MyDB.getParentCardsWatch(subj, title, id);
            else
                list = MyDB.getChildCardsWatch(subj, title, id);
        }

        modifyToolbar();

        if (savedInstanceState == null || !savedInstanceState.containsKey("watch")) {
            if (parent)
                list = MyDB.getParentCardsWatch(subj, title, id);
            else
                list = MyDB.getChildCardsWatch(subj, title, id);
        }

        if (parent && list.size() > 50 && subj.equals("История") && (list.get(0).getAvers().getBytes().length > 90000 ||
                list.get(0).getRevers().getBytes().length > 90000))
            initTwoWeb(rootview, true);
        else
            initTwoWeb(rootview, false);


        return rootview;
    }

    private void initTwoWeb(View rootview, boolean flag) {
        web = rootview.findViewById(R.id.watch_list1);
        web.first = true;
        web.several = flag;
        setupWeb(web, rootview.getContext());
        Log.i("FLAG", "" + flag);
        if (flag) {
            web1 = rootview.findViewById(R.id.watch_list2);
            web1.several = flag;
            web1.first = false;
            web1.second = web;
            web.second = web1;
            setupWeb(web1, rootview.getContext());
        }

        secondpart = addContent(flag);
        if (secondpart != null) {
            web1.loadDataWithBaseURL(null, secondpart, "text/html", "utf-8", "auto:black");

        }
    }

    private String addContent(boolean flag) {
        StringBuilder s = new StringBuilder();
        s.append(getHead());
        s.append("<table>");
        if (flag) {
            Card c;
            s.append("<caption>Карточки, отмеченные зеленым, не показываются</caption><tr><th><br></th></tr>");
            for (int i = 0; i < list.size() / 2; i++) {
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
                s.append("<tr onclick=\"javascript:return uped(this.id)\" id=\"");
                s.append(subj).append("/").append(c.getId()).append("/").append(c.getResult()).append("\" bgcolor=\"")
                        .append(color).append("\" class='clickable-row' data-href='").append(subj)
                        .append("/").append(c.getId()).append("/").append(c.getResult()).append("'>");
                s.append("<td class=\"left\">").append(c.getAvers()).append("</td><td class=\"line\">")
                        .append(c.getRevers()).append("</td></tr>");
            }
            s.append("</table></body></html>");
            web.loadDataWithBaseURL(null, s.toString(), "text/html", "utf-8", "auto:black");

            s = new StringBuilder();
            s.append(getHead()).append("<table>");
            for (int i = list.size() / 2; i < list.size(); i++) {
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
                s.append("<tr onclick=\"javascript:return uped(this.id)\" id=\"");
                s.append(subj).append("/").append(c.getId()).append("/").append(c.getResult()).append("\" bgcolor=\"")
                        .append(color).append("\" class='clickable-row' data-href='").append(subj)
                        .append("/").append(c.getId()).append("/").append(c.getResult()).append("'>");
                s.append("<td class=\"left\">").append(c.getAvers()).append("</td><td class=\"line\">")
                        .append(c.getRevers()).append("</td></tr>");
            }
            s.append("</table></body></html>");
            return s.toString();
        } else {
            Card c;
            s.append("<caption>Карточки, отмеченные зеленым, не показываются</caption><tr><th><br></th></tr>");
            for (int i = 0; i < list.size(); i++) {
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
                s.append("<tr onclick=\"javascript:return uped(this.id)\" id=\"");
                s.append(subj).append("/").append(c.getId()).append("/").append(c.getResult()).append("\" bgcolor=\"")
                        .append(color).append("\" class='clickable-row' data-href='").append(subj)
                        .append("/").append(c.getId()).append("/").append(c.getResult()).append("'>");
                s.append("<td class=\"left\">").append(c.getAvers()).append("</td><td class=\"line\">")
                        .append(c.getRevers()).append("</td></tr>");
            }
            s.append("</table></body></html>");
            web.loadDataWithBaseURL(null, s.toString(), "text/html", "utf-8", "auto:black");
            return null;
        }
    }

    //closed with </body>
    private String getHead() {
        return "<html><head>" +
                "<meta name=\"viewport\" content=\"width=device-width\"/>" + "\n" +
                " <script type=\"text/javascript\">\n" +
                "           function uped(data)\n" +
                "               {" +
                "                   AndroidFunction.update(data);" +
                "               };" +
                "               function changecol(data, color, edited){" +
                "                   document.getElementById(data).style.background = color;  " +
                "                   document.getElementById(data).id = edited;  " +
                "               };" +
                "        </script><style>" +
                "table, tr {border-collapse: collapse;}" +
                "td.left {" +
                "   padding-right: 5px;" +
                "   padding-left: 6px;" +
                "   width: 50%;" +
                "   text-align: left;" +
                "   vertical-align: top;" +
                "   horizontal-align: left;" +
                "   -moz-hyphens: auto;" +
                "   -webkit-hyphens: auto;" +
                "   -ms-hyphens: auto;" +
                "}" +
                "table, tr, td.left, td.line{" +
                "   -moz-hyphens: auto;" +
                "   -webkit-hyphens: auto;" +
                "   -ms-hyphens: auto;" +
                "}" +
                " td.line {" +
                "       border-left: 1px solid #000000;" +
                "       padding-left: 6px;" +
                "       padding-right: 1em;" +
                "       width: 50%;" +
                "       text-align: left;" +
                "       vertical-align: top;" +
                "       horizontal-align: right;" +
                "       -moz-hyphens: auto;" +
                "       -webkit-hyphens: auto;" +
                "       -ms-hyphens: auto;" +
                "  }" +
                "   p {" +
                "  width: 100%;" +
                "  margin-top: 1em;" +
                "  margin-bottom: 1em;" +
                "  margin-left: 2;" +
                "  margin-right: 2;" +
                "  padding: 8px" +
                "  align: center;" +
                "  horizontal-align: center;" +
                "}" +
                "td {border-bottom: 2px solid black;}" +
                //"th {width: 100%;}"
                "</style></head><body>";
    }

    private void setupWeb(CustomWeb web, Context con) {
        web.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        web.getSettings().setDomStorageEnabled(false);
        web.setWebChromeClient(new WebChromeClient());
        //web.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        //web.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        web.getSettings().setEnableSmoothTransition(true);
        web.getSettings().setJavaScriptEnabled(true);
        //web.setWebViewClient(new WebViewClient());
        web.setVerticalScrollBarEnabled(true);
        if (subj.equals("История"))
            web.getSettings().setDefaultFontSize(16);
        else
            web.getSettings().setDefaultFontSize(20);
        web.getSettings().setLoadWithOverviewMode(true);
        web.getSettings().setUseWideViewPort(true);
        web.getSettings().setAppCacheEnabled(true);
        web.getSettings().setAppCachePath(con.getCacheDir().getPath());
        web.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        web.addJavascriptInterface(new JSinterface(con, web), "AndroidFunction");
    }

    private void modifyToolbar() {
        CollapsingToolbarLayout col = CollapsingToolbarLayout.class.cast(getActivity().findViewById(R.id.collapsing_toolbar));
        col.setTitle(title);
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
            MyDB.updateWatchCard(params[0].subj, params[0].id, params[0].result, params[0].old_result);
            return null;
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
}
