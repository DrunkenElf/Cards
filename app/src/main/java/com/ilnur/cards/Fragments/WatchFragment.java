package com.ilnur.cards.Fragments;

import android.content.Context;
import android.database.CursorIndexOutOfBoundsException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rubensousa.raiflatbutton.RaiflatButton;
import com.github.rubensousa.raiflatbutton.RaiflatImageButton;
import com.google.android.material.appbar.AppBarLayout;
import com.ilnur.cards.Adapters.AdapterWatch;
import com.ilnur.cards.Json.Card;
import com.ilnur.cards.MyDB;
import com.ilnur.cards.R;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class WatchFragment extends Fragment {
    private String subj;
    private String title;
    private int id;
    private boolean parent;
    private ArrayList<Card> list;
    //String strBody = "";
    private WebView web;
    private WebView web1;
    String head;
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
        outState.putParcelableArrayList("watch", list);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("Watch", "created");
        if (savedInstanceState != null) {
            subj = savedInstanceState.getString("subj");
            title = savedInstanceState.getString("title");
            id = savedInstanceState.getInt("id");
            parent = savedInstanceState.getBoolean("parent");
            list = savedInstanceState.getParcelableArrayList("watch");
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.i("Watch", "restored");
        if (savedInstanceState != null) {
            subj = savedInstanceState.getString("subj");
            title = savedInstanceState.getString("title");
            id = savedInstanceState.getInt("id");
            parent = savedInstanceState.getBoolean("parent");
            list = savedInstanceState.getParcelableArrayList("watch");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Card c;
        StringBuilder sb = new StringBuilder();
        sb.append(head);
        for (int i = index; i < list.size(); i++) {
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
            sb.append("<tr onclick=\"javascript:return uped(this.id)\" id=\"");
            sb.append(subj).append("/").append(c.getId()).append("/").append(c.getResult()).append("\" bgcolor=\"")
                    .append(color).append("\" class='clickable-row' data-href='").append(subj)
                    .append("/").append(c.getId()).append("/").append(c.getResult()).append("'>");
            sb.append("<td class=\"left\">").append(c.getAvers()).append("</td><td class=\"line\">")
                    .append(c.getRevers()).append("</td></tr>");
        }
        sb.append("</table></body></html>");

        web1.loadDataWithBaseURL(null, sb.toString(), "text/html", "UTF-8", "auto:blank");
}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.watch_lay, container, false);
        /*if (savedInstanceState !=null){
            onViewStateRestored(savedInstanceState);
        }*/
        //getActivity().setTitle(title);
        if (savedInstanceState != null) {
            subj = savedInstanceState.getString("subj");
            title = savedInstanceState.getString("title");
            id = savedInstanceState.getInt("id");
            parent = savedInstanceState.getBoolean("parent");
            list = savedInstanceState.getParcelableArrayList("watch");
        }
        /*try {
            if (!MyDB.getStyle(null).equals("") && MyDB.getStyle(null) != null)
                strBody = "<html><head><style>" + MyDB.getStyle(null);
        } catch (CursorIndexOutOfBoundsException e){
            e.printStackTrace();
            strBody = "<html><head><style>";
        }
        try {
            if (!MyDB.getStyle(subj).equals("") && MyDB.getStyle(subj) != null)
                strBody = strBody + MyDB.getStyle(subj) + "</style></head><body>";
        } catch (CursorIndexOutOfBoundsException e){
            e.printStackTrace();
            strBody = strBody + "</style></head><body>";
        }
*/
        //TextView info = rootview.findViewById(R.id.show_info);
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

        if (savedInstanceState == null || !savedInstanceState.containsKey("watch")) {
            if (parent)
                list = MyDB.getParentCardsWatch(subj, title, id);
            else
                list = MyDB.getChildCardsWatch(subj, title, id);
        }

        //web = rootview.findViewById(R.id.watch_list);
        //web.getSettings().setJavaScriptEnabled(true);
        setupWeb(rootview.getContext());
       /* web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        web.getSettings().setDomStorageEnabled(true);
        web.setWebChromeClient(new WebChromeClient());
        web.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        web.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        web.getSettings().setEnableSmoothTransition(true);
        //web.setWebViewClient(new WebViewClient());
        web.setVerticalScrollBarEnabled(true);
        if (subj.equals("История"))
            web.getSettings().setDefaultFontSize(16);
        else
            web.getSettings().setDefaultFontSize(20);
        web.getSettings().setLoadWithOverviewMode(true);
        web.getSettings().setUseWideViewPort(true);
        web.getSettings().setAppCacheEnabled(true);
        web.getSettings().setAppCachePath(getContext().getCacheDir().getPath());*/
        //generate(list);
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head>" +
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
                "</style></head><body>");
        //sb.append("<p> Карточки, отмеченные зеленым, не показываются</p>");
        sb.append("<table>");
        head = sb.toString();
        sb.append("<caption>Карточки, отмеченные зеленым, не показываются</caption><tr><th><br></th></tr>");
        Card c;
        index = list.size() / 2;
        for (int i = 0; i < list.size() / 2; i++) {
            //subj, card.getId(), 1, 4
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
            sb.append("<tr onclick=\"javascript:return uped(this.id)\" id=\"");
            sb.append(subj).append("/").append(c.getId()).append("/").append(c.getResult()).append("\" bgcolor=\"")
                    .append(color).append("\" class='clickable-row' data-href='").append(subj)
                    .append("/").append(c.getId()).append("/").append(c.getResult()).append("'>");
            sb.append("<td class=\"left\">").append(c.getAvers()).append("</td><td class=\"line\">")
                    .append(c.getRevers()).append("</td></tr>");
        }
        sb.append("</table></body></html>");
        String s = sb.toString();
        Log.i("SIZE", "" + s.getBytes().length);
        sb = null;

        //Log.i("size", ""+sb.length());
        //web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        //web.addJavascriptInterface(new JSinterface(rootview.getContext()), "AndroidFunction");
        /*web.post(new Runnable() {
            @Override
            public void run() {
                web.loadDataWithBaseURL(null, sb.toString(), "text/html", "utf-8", "about:blank");
            }
        });*/
        //about:blank
        //String s = sb.toString();
        //sb = null;
        /*File f = new File("/data/data/" + getContext().getPackageName() + "/file/");
        if (!f.exists())
            f.mkdir();
        web.loadUrl("file:///data/data/com.ilnur.cards/file/large.html");*/
        /*File f = new File("/data/data/" + getContext().getPackageName() + "/file/");
        if (!f.exists())
            f.mkdir();
        if (s.getBytes().length > 5000000) {
            try {
                OutputStream os = new FileOutputStream("/data/data/" + getContext().getPackageName() + "/file/" + subj + id + ".html");
                os.write(s.getBytes());
                os.flush();
                os.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            web.loadUrl("file:///");
        }*/
        //web.loadDataWithBaseURL(null, s, "text/html", "utf-8", "about:blank");
        //web.loadData(s, "text/html", "utf-8");
        //web.reload();
        //Runtime.getRuntime().gc();
        //System.gc();
        //web.stopLoading();
        /*ListView lw = rootview.findViewById(R.id.watch_list);
        //AdapterWatchList adapter = new AdapterWatchList(rootview.getContext(), list, subj);
        AdapterWatch adapter = new AdapterWatch(rootview.getContext(), subj, list, strBody);
        lw.setAdapter(adapter);
        lw.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0){
                    View v = lw.getChildAt(0);
                    int offset = (v == null) ? 0 : v.getTop();
                    if (offset == 0) {
                        info.setVisibility(View.VISIBLE);
                    }
                } else if (totalItemCount - visibleItemCount > firstVisibleItem){
                    info.setVisibility(View.GONE);
                }
            }
        });*/
        //Log.i("count  ",String.valueOf(getFragmentManager().getBackStackEntryCount()));
        return rootview;
    }

public class JSinterface {
    Context context;

    public JSinterface(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public void update(String data) {
        String subj = data.split("/")[0];
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

public class updateCard extends AsyncTask<params, Void, Void> {

    @Override
    protected Void doInBackground(params... params) {
        MyDB.updateWatchCard(params[0].subj, params[0].id, params[0].result, params[0].old_result);
        return null;
    }
}

    public void generate(ArrayList<Card> list) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuilder sb = new StringBuilder();
                sb.append("<html><head>" +
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
                        "</style></head><body>");
                //sb.append("<p> Карточки, отмеченные зеленым, не показываются</p>");
                sb.append("<table>");
                sb.append("<caption>Карточки, отмеченные зеленым, не показываются</caption><tr><th><br></th></tr>");
                for (Card c : list) {
                    //subj, card.getId(), 1, 4
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
                    sb.append("<tr onclick=\"javascript:return uped(this.id)\" id=\"");
                    sb.append(subj).append("/").append(c.getId()).append("/").append(c.getResult()).append("\" bgcolor=\"")
                            .append(color).append("\" class='clickable-row' data-href='").append(subj)
                            .append("/").append(c.getId()).append("/").append(c.getResult()).append("'>");
                    sb.append("<td class=\"left\">").append(c.getAvers()).append("</td><td class=\"line\">")
                            .append(c.getRevers()).append("</td></tr>");
                }
                sb.append("</table></body></html>");
                String s = sb.toString();
                Log.i("SIZE", "" + s.getBytes().length);
                sb = null;
                web.post(new Runnable() {
                    @Override
                    public void run() {
                        web.loadDataWithBaseURL(null, s, "text/html", "utf-8", "about:blank");
                    }
                    // 4681840
                    // 4685941
                    //23579338
                    //23601178
                    //
                });
            }
        }).start();
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

    private void setupWeb(Context con) {
        web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        web.getSettings().setDomStorageEnabled(true);
        //web.setWebChromeClient(new WebChromeClient().o);

        web.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.i("page", "finished");
            }

        });



        web.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        web.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        web.getSettings().setEnableSmoothTransition(true);
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
        web.addJavascriptInterface(new JSinterface(con), "AndroidFunction");



        web1.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        web1.getSettings().setDomStorageEnabled(true);
        web1.setWebChromeClient(new WebChromeClient());
        web1.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        web1.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        web1.getSettings().setEnableSmoothTransition(true);
        //web.setWebViewClient(new WebViewClient());
        web1.setVerticalScrollBarEnabled(true);
        if (subj.equals("История"))
            web1.getSettings().setDefaultFontSize(16);
        else
            web1.getSettings().setDefaultFontSize(20);
        web1.getSettings().setLoadWithOverviewMode(true);
        web1.getSettings().setUseWideViewPort(true);
        web1.getSettings().setAppCacheEnabled(true);
        web1.getSettings().setAppCachePath(con.getCacheDir().getPath());
        web.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        web.addJavascriptInterface(new JSinterface(con), "AndroidFunction");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i("onDestroy", " 2");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("onDestroyView", " 2");
    }

}
