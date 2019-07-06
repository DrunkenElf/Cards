package com.ilnur.cards.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.github.rubensousa.raiflatbutton.RaiflatButton;
import com.github.rubensousa.raiflatbutton.RaiflatImageButton;
import com.google.android.material.appbar.AppBarLayout;
import com.ilnur.cards.Adapters.AdapterWatch;
import com.ilnur.cards.Json.Card;
import com.ilnur.cards.LoginActivity;
import com.ilnur.cards.MainActivity;
import com.ilnur.cards.MyDB;
import com.ilnur.cards.R;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import org.jsoup.Jsoup;

import java.util.ArrayList;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class LearnFragment extends Fragment {
    private String subj;
    private String title;
    private int id;
    private static int i = 0;
    private boolean revers;
    private boolean parent;
    private String strBody = "<html>"
            + "<head>"
            + "     <style type=\"text/css\">"
            + "         .center {"
            + "             padding: 70px 0;"
            + "             text-align: center;"
            + "         }"
            + "     </style>"
            + ""
            + "</head>"
            + "<body> <div class=\"center\"> <p>";
    private Card curr_card;
    public void setLearnFragment(String subj, String title, int id, boolean revers, boolean parent){
        this.subj = subj;
        this.title = title;
        this.id = id;
        i = 0;
        this.revers = revers;
        this.parent = parent;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.learn_lay, container, false);
        //getActivity().setTitle(title);
        Toolbar bar = Toolbar.class.cast(getActivity().findViewById(R.id.toolbar));
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
        ArrayList<Card> list;
        if (parent)
            list = MyDB.getParentCards(subj, id);
        else
            list = MyDB.getChildCards(subj, title, id);

        /*for (Card c: list){

        }*/

        View include = rootview.findViewById(R.id.learn_inc_fr);
        WebView web = include.findViewById(R.id.web);
        TextView count = rootview.findViewById(R.id.count);

        Button check = rootview.findViewById(R.id.check_but);

        Button dontknow = rootview.findViewById(R.id.dontkn);
        Button know = rootview.findViewById(R.id.know);
        Button learned = rootview.findViewById(R.id.learned);

        String c = "/"+String.valueOf(list.size());



        if (list.size()==0){
            check.setVisibility(View.GONE);
            dontknow.setVisibility(View.GONE);
            know.setVisibility(View.GONE);
            learned.setVisibility(View.GONE);
            count.setText("0"+c);
            web.loadDataWithBaseURL(null, append("Похоже, что на сегодня вы все повторили или выучили"),"text/html", "utf-8", "about:blank");
        } else {
            count.setText(String.valueOf(i + 1) + c);
            //int i = 0;

            curr_card = list.get(i);
            if (revers) {
                web.loadDataWithBaseURL(null, curr_card.getRevers(), "text/html",
                        "utf-8", "about:blank");
            } else
                web.loadDataWithBaseURL(null, curr_card.getAvers(), "text/html",
                        "utf-8", "about:blank");

        }
        check.setOnClickListener(v -> {
            Log.i("BUtton","check");
            check.setVisibility(View.GONE);
            dontknow.setVisibility(View.VISIBLE);
            know.setVisibility(View.VISIBLE);
            learned.setVisibility(View.VISIBLE);
            if (revers) {
                web.loadDataWithBaseURL(null, curr_card.getAvers(), "text/html",
                        "utf-8", "about:blank");
            } else {
                web.loadDataWithBaseURL(null, checkCard(curr_card).getRevers(), "text/html",
                        "utf-8", "about:blank");
            }
            i = i +1;

        });

        dontknow.setOnClickListener(v -> {
            Log.i("BUtton","dont");
            check.setVisibility(View.VISIBLE);
            dontknow.setVisibility(View.GONE);
            know.setVisibility(View.GONE);
            learned.setVisibility(View.GONE);

            //web.setText(list.get(i).getAvers());
            if (i == list.size()){
                web.loadDataWithBaseURL(null, append("Конец"), "text/html",
                        "utf-8", "about:blank");
                check.setVisibility(View.GONE);
                if (!MainActivity.logged && MainActivity.show) {
                    new AlertDialog.Builder(rootview.getContext())
                            .setTitle("Хотите войти или зарегистрироваться?")
                            .setMessage("Для сохранения статистики на сервере необходимо авторизироваться или зарегистрироваться")
                            .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(getActivity(), LoginActivity.class));
                                }
                            })
                            .setNegativeButton("Позже", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    MainActivity.show = false;
                                }
                            })
                            .show();
                }
            } else {
                curr_card = list.get(i);
                if (revers) {
                    web.loadDataWithBaseURL(null, curr_card.getRevers(), "text/html",
                            "utf-8", "about:blank");
                } else {
                    web.loadDataWithBaseURL(null, curr_card.getAvers(), "text/html",
                            "utf-8", "about:blank");
                }
                count.setText((i + 1) +c);
            }
        });

        know.setOnClickListener(v -> {
            Log.i("BUtton","know");
            check.setVisibility(View.VISIBLE);
            dontknow.setVisibility(View.GONE);
            know.setVisibility(View.GONE);
            learned.setVisibility(View.GONE);
            new updateCard().execute(new params(subj, curr_card.getId(), 1, curr_card.getResult()));
            //MyDB.updateCard(subj, curr_card.getId(), 1, curr_card.getResult());
            if (i == list.size()){
                web.loadDataWithBaseURL(null, append("Стопка закончилась"), "text/html",
                        "utf-8", "about:blank");
                check.setVisibility(View.GONE);
                if (!MainActivity.logged && MainActivity.show) {
                    new AlertDialog.Builder(rootview.getContext())
                            .setTitle("Хотите войти или зарегистрироваться?")
                            .setMessage("Для сохранения статистики на сервере необходимо авторизироваться или зарегистрироваться")
                            .setPositiveButton("Да", (dialog, which) -> startActivity(new Intent(getActivity(), LoginActivity.class)))
                            .setNegativeButton("Позже", (dialog, which) -> MainActivity.show = false)
                            .show();
                }
            } else {
                curr_card = list.get(i);
                if (revers) {
                    web.loadDataWithBaseURL(null, curr_card.getRevers(), "text/html",
                            "utf-8", "about:blank");
                } else {
                    web.loadDataWithBaseURL(null, curr_card.getAvers(), "text/html",
                            "utf-8", "about:blank");
                }
                count.setText(String.valueOf(i+1)+c);
            }
        });

        learned.setOnClickListener(v -> {
            Log.i("BUtton","learned");
            check.setVisibility(View.VISIBLE);
            dontknow.setVisibility(View.GONE);
            know.setVisibility(View.GONE);
            learned.setVisibility(View.GONE);
            new updateCard().execute(new params(subj, curr_card.getId(), 4, curr_card.getResult()));
            //MyDB.updateCard(subj, curr_card.getId(), 4, curr_card.getResult());
            if (i == list.size()){
                web.loadDataWithBaseURL(null, append("Конец"), "text/html",
                        "utf-8", "about:blank");
                check.setVisibility(View.GONE);
                if (!MainActivity.logged && MainActivity.show) {
                    new AlertDialog.Builder(rootview.getContext())
                            .setTitle("Хотите войти или зарегистрироваться?")
                            .setMessage("Для сохранения статистики на сервере необходимо авторизироваться или зарегистрироваться")
                            .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(getActivity(), LoginActivity.class));
                                }
                            })
                            .setNegativeButton("Позже", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    MainActivity.show = false;
                                }
                            })
                            .show();
                }

            } else {
                curr_card = list.get(i);
                if (revers) {
                    web.loadDataWithBaseURL(null, curr_card.getRevers(), "text/html",
                            "utf-8", "about:blank");
                } else {
                    web.loadDataWithBaseURL(null, curr_card.getAvers(), "text/html",
                            "utf-8", "about:blank");
                }
                count.setText(String.valueOf(i+1)+c);
            }
        });

        Log.i("count  ",String.valueOf(getFragmentManager().getBackStackEntryCount()));
        return rootview;
    }
    private String append(String s){
        return strBody+s+"</p> </div> </body></html>";
    }
    /*private String rev(String s){
        if (revers){

        }
    }*/
    private class params{
        String subj;
        int id;
        int result;
        int old_result;
        public params(String subj, int id, int result, int old_result){
            this.subj = subj;
            this.id = id;
            this.result = result;
            this.old_result = old_result;
        }
    }

    public class updateCard extends AsyncTask<params, Void, Void> {
        @Override
        protected Void doInBackground(params... params) {
            MyDB.updateCard(params[0].subj, params[0].id, params[0].result, params[0].old_result);
            return null;
        }
    }

    public Card checkCard(Card card){
        String avers = card.getAvers()
                .replace("<div align=\"justify\" width=\"100%\" class=\"pbody\"><p class=\"left_margin\">","")
                .replace("</div>","");
        String revers = card.getRevers()
                .replace("<div align=\"justify\" width=\"100%\" class=\"pbody\"><p class=\"left_margin\">","")
                .replace("</div>","");
        revers = revers.toUpperCase();

        String fin = avers.replace("_", revers);
        //Log.i("fin", fin);
        if (revers.length()==1) {
            card.setRevers(card.getAvers().replace(avers, fin));
        }
        //Log.i("BUKVA_ED", card.getRevers());
        return card;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AppBarLayout apbar = AppBarLayout.class.cast(getActivity().findViewById(R.id.apbar));
        apbar.setExpanded(false);
    }
}
