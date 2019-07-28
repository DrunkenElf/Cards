package com.ilnur.cards.Fragments;

import android.animation.Animator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.CursorIndexOutOfBoundsException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    private Card curr_card;
    private int right = 0;
    private int wrongs = 0;
    private ArrayList<Card> list;
    private String c;
    private StringBuilder wrong = new StringBuilder();
    String strBody = "";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null){
            //restore it
            subj = savedInstanceState.getString("subj");
            title = savedInstanceState.getString("title");
            id = savedInstanceState.getInt("id");
            i = savedInstanceState.getInt("i");
            parent = savedInstanceState.getBoolean("parent");
            revers = savedInstanceState.getBoolean("revers");
            //list = savedInstanceState.getParcelableArrayList("learn");
            wrong = new StringBuilder();
            wrong.append("<table>").append(savedInstanceState.getString("sb"));
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null){
            //restore it
            subj = savedInstanceState.getString("subj");
            title = savedInstanceState.getString("title");
            id = savedInstanceState.getInt("id");
            i = savedInstanceState.getInt("i");
            right = savedInstanceState.getInt("right");
            wrongs = savedInstanceState.getInt("wrongs");
            parent = savedInstanceState.getBoolean("parent");
            revers = savedInstanceState.getBoolean("revers");
            //list = savedInstanceState.getParcelableArrayList("learn");
            wrong = new StringBuilder();
            wrong.append("<table>").append(savedInstanceState.getString("sb"));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //save fragment state
        outState.putCharSequence("subj", subj);
        outState.putCharSequence("title", title);
        outState.putInt("id", id);
        outState.putInt("i", i);
        outState.putInt("right", right);
        outState.putInt("wrongs", wrongs);
        outState.putBoolean("parent", parent);
        outState.putBoolean("revers", revers);
        //outState.putParcelableArrayList("learn", list);
        outState.putCharSequence("sb", wrong.toString());
    }

    public void setLearnFragment(String subj, String title, int id, boolean revers, boolean parent){
        this.subj = subj;
        this.title = title;
        this.id = id;
        i = 0;
        this.revers = revers;
        this.parent = parent;
    }

    /*@Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }*/

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View view = inflater.inflate(R.layout.learn_lay, null);
        ViewGroup rootview = (ViewGroup) getView();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //Toast.makeText(this.getContext(), "landscape", Toast.LENGTH_SHORT).show();
            Button dontknow = rootview.findViewById(R.id.dontkn);
            Button know = rootview.findViewById(R.id.know);
            Button learned = rootview.findViewById(R.id.learned);
            dontknow.setPadding(0,0,0,50);
            know.setPadding(0,0,0,50);
            learned.setPadding(0,0,0,50);
            /*LinearLayout.LayoutParams d = (LinearLayout.LayoutParams) dontknow.getLayoutParams();
            LinearLayout.LayoutParams k = (LinearLayout.LayoutParams) know.getLayoutParams();
            LinearLayout.LayoutParams l = (LinearLayout.LayoutParams) learned.getLayoutParams();

            d.bottomMargin = 50;
            k.bottomMargin = 50;
            l.bottomMargin = 50;*/
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Button dontknow = rootview.findViewById(R.id.dontkn);
            Button know = rootview.findViewById(R.id.know);
            Button learned = rootview.findViewById(R.id.learned);
            dontknow.setPadding(0,0,0,250);
            know.setPadding(0,0,0,250);
            learned.setPadding(0,0,0,250);
            /*LinearLayout.LayoutParams d = (LinearLayout.LayoutParams) dontknow.getLayoutParams();
            LinearLayout.LayoutParams k = (LinearLayout.LayoutParams) know.getLayoutParams();
            LinearLayout.LayoutParams l = (LinearLayout.LayoutParams) learned.getLayoutParams();
            d.bottomMargin = 100;
            k.bottomMargin = 100;
            l.bottomMargin = 100;*/
            //Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.learn_lay, container, false);

        //getActivity().setTitle(title);

        if (savedInstanceState != null){
            //restore it
            subj = savedInstanceState.getString("subj");
            title = savedInstanceState.getString("title");
            id = savedInstanceState.getInt("id");
            i = savedInstanceState.getInt("i");
            parent = savedInstanceState.getBoolean("parent");
            revers = savedInstanceState.getBoolean("revers");
            //list = savedInstanceState.getParcelableArrayList("learn");

            right = savedInstanceState.getInt("right");
            wrongs = savedInstanceState.getInt("wrongs");
            wrong = new StringBuilder();
            wrong.append("<table>").append(savedInstanceState.getString("sb"));
            if (parent)
                list = MyDB.getParentCards(subj, id);
            else
                list = MyDB.getChildCards(subj, title, id);
        }
        try {
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
        //Toolbar bar = Toolbar.class.cast(getActivity().findViewById(R.id.toolbar));
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
        apbar.setExpanded(false, false);
        apbar.setClickable(false);
        //StringBuilder wrong = new StringBuilder();
        //wrong.append("<table>");

        if (savedInstanceState == null || !savedInstanceState.containsKey("learn")) {
            if (parent) {
                list = MyDB.getParentCards(subj, id);
            }
            else {
                list = MyDB.getChildCards(subj, title, id);
            }
        }
        /*for (Card c: list){

        }*/
        LinearLayout ll = rootview.findViewById(R.id.buttons);

        View include = rootview.findViewById(R.id.learn_inc_fr);
        WebView web = include.findViewById(R.id.web);
        TextView count = rootview.findViewById(R.id.count);

        Button check = rootview.findViewById(R.id.check_but);
        RaiflatButton again = rootview.findViewById(R.id.again);
        again.setVisibility(View.GONE);

        Button dontknow = rootview.findViewById(R.id.dontkn);
        Button know = rootview.findViewById(R.id.know);
        Button learned = rootview.findViewById(R.id.learned);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT){
            dontknow.setPadding(0,0,0,250);
            know.setPadding(0,0,0,250);
            learned.setPadding(0,0,0,250);
        } else {
            dontknow.setPadding(0,0,0,50);
            know.setPadding(0,0,0,50);
            learned.setPadding(0,0,0,50);
        }
        c = "/"+list.size();



        if (list.size()==0){
            check.setVisibility(View.GONE);
            dontknow.setVisibility(View.GONE);
            know.setVisibility(View.GONE);
            learned.setVisibility(View.GONE);
            count.setText("0"+c);
            web.loadDataWithBaseURL(null, append("Похоже, что на сегодня вы все повторили или выучили"),"text/html", "utf-8", "about:blank");
        } else {
            count.setText(i + 1 + c);
            //int i = 0;

            curr_card = list.get(i);
            if (revers) {
                web.loadDataWithBaseURL(null, append(curr_card.getRevers()), "text/html",
                        "utf-8", "about:blank");
            } else
                web.loadDataWithBaseURL(null, append(curr_card.getAvers()), "text/html",
                        "utf-8", "about:blank");

        }
        check.setOnClickListener(v -> {
            Log.i("BUtton","check");
            //animate(check);
            check.setVisibility(View.GONE);
            ll.setVisibility(View.VISIBLE);
            dontknow.setVisibility(View.VISIBLE);
            know.setVisibility(View.VISIBLE);
            learned.setVisibility(View.VISIBLE);
            if (revers) {
                web.loadDataWithBaseURL(null, append(curr_card.getAvers()), "text/html",
                        "utf-8", "about:blank");
            } else {
                web.loadDataWithBaseURL(null, append(checkCard(curr_card).getRevers()), "text/html",
                        "utf-8", "about:blank");
            }
            i = i +1;

        });
        again.setOnClickListener(v -> {
            i = 0;
            list = new ArrayList<>();
            if (parent)
                list = MyDB.getParentCards(subj, id);
            else
                list = MyDB.getChildCards(subj, title, id);
            curr_card = list.get(i);
            if (revers) {
                web.loadDataWithBaseURL(null, append(curr_card.getRevers()), "text/html",
                        "utf-8", "about:blank");
            } else
                web.loadDataWithBaseURL(null, append(curr_card.getAvers()), "text/html",
                        "utf-8", "about:blank");
            c = "/"+list.size();
            count.setVisibility(View.VISIBLE);
            count.setText(String.valueOf(i + 1) + c);
            again.setVisibility(View.GONE);
            check.setVisibility(View.VISIBLE);
            ll.setVisibility(View.GONE);
        });

        dontknow.setOnClickListener(v -> {
            Log.i("BUtton","dont");
            wrongs++;
            check.setVisibility(View.VISIBLE);
            dontknow.setVisibility(View.GONE);
            know.setVisibility(View.GONE);
            learned.setVisibility(View.GONE);
            ll.setVisibility(View.GONE);
            if (curr_card.getRevers().length() == 1){
                wrong.append("<tr><td>"+checkCard(curr_card).getRevers()+"</td></tr><tr>\n" +
                        "        <td colspan=\"2\" class=\"divider\"><hr /></td>\n" +
                        "    </tr>");
            } else {
                wrong.append("<tr><td>"+curr_card.getAvers()+"</td><td class=\"line\">"+curr_card.getRevers()+"</td></tr><tr>\n" +
                        "        <td colspan=\"2\" class=\"divider\"><hr /></td>\n" +
                        "    </tr>");
            }
            //web.setText(list.get(i).getAvers());
            if (i == list.size()){
                check.setVisibility(View.GONE);
                ll.setVisibility(View.GONE);
                count.setVisibility(View.GONE);
                again.setVisibility(View.VISIBLE);
                web.loadDataWithBaseURL(null, showWrong(wrong.toString()), "text/html",
                        "utf-8", "about:blank");
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
                    web.loadDataWithBaseURL(null, append(curr_card.getRevers()), "text/html",
                            "utf-8", "about:blank");
                } else {
                    web.loadDataWithBaseURL(null, append(curr_card.getAvers()), "text/html",
                            "utf-8", "about:blank");
                }
                count.setText((i + 1) +c);
            }
        });

        know.setOnClickListener(v -> {
            Log.i("BUtton","know");
            right++;
            check.setVisibility(View.VISIBLE);
            dontknow.setVisibility(View.GONE);
            know.setVisibility(View.GONE);
            learned.setVisibility(View.GONE);
            ll.setVisibility(View.GONE);
            new updateCard().execute(new params(subj, curr_card.getId(), 1, curr_card.getResult()));
            //MyDB.updateCard(subj, curr_card.getId(), 1, curr_card.getResult());
            if (i == list.size()){
                web.loadDataWithBaseURL(null, showWrong(wrong.toString()), "text/html",
                        "utf-8", "about:blank");
                check.setVisibility(View.GONE);
                count.setVisibility(View.GONE);
                again.setVisibility(View.VISIBLE);
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
                    web.loadDataWithBaseURL(null, append(curr_card.getRevers()), "text/html",
                            "utf-8", "about:blank");
                } else {
                    web.loadDataWithBaseURL(null, append(curr_card.getAvers()), "text/html",
                            "utf-8", "about:blank");
                }
                count.setText((i + 1) +c);
            }
        });

        learned.setOnClickListener(v -> {
            Log.i("BUtton","learned");
            right++;
            check.setVisibility(View.VISIBLE);
            dontknow.setVisibility(View.GONE);
            know.setVisibility(View.GONE);
            learned.setVisibility(View.GONE);
            ll.setVisibility(View.GONE);
            new updateCard().execute(new params(subj, curr_card.getId(), 4, curr_card.getResult()));
            //MyDB.updateCard(subj, curr_card.getId(), 4, curr_card.getResult());
            if (i == list.size()){
                web.loadDataWithBaseURL(null, showWrong(wrong.toString()), "text/html",
                        "utf-8", "about:blank");
                check.setVisibility(View.GONE);
                count.setVisibility(View.GONE);
                again.setVisibility(View.VISIBLE);
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
                    web.loadDataWithBaseURL(null, append(curr_card.getRevers()), "text/html",
                            "utf-8", "about:blank");
                } else {
                    web.loadDataWithBaseURL(null, append(curr_card.getAvers()), "text/html",
                            "utf-8", "about:blank");
                }
                count.setText((i + 1) +c);
            }
        });

        Log.i("count  ",String.valueOf(getFragmentManager().getBackStackEntryCount()));
        return rootview;
    }
    private String append(String s){
       return strBody+s+"</body></html>";
    }
    private String showWrong(String table){
        table = table+"</table>";
        String style = "<style> .line {\n" +
                "  border-left: thin solid #000000;\n" +
                "  padding: 2px;\n" +
                "}</style>";
        String html = "<html>"+style+"<body><div><p align='center'>Стопка закончилась</p><p></p><p>Верных ответов: "+
                right+"</p>";
        if (wrongs != 0){
            html = html +"<p>Неверных ответов: "+ wrongs+"</p><br>";
            html = html +"<strong>Допущенные ошибки</strong>"+table + "</div></body></html>";
        }
        return html;
    }
    /*private String rev(String s){
        if (revers){

        }
    }*/

    public void animate(View v){
        v.animate()
                .alpha(1.0f)
                .setDuration(200)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        v.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }

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
            if (params[0].old_result == 4)


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
