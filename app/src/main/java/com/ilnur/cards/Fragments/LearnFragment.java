package com.ilnur.cards.Fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.CursorIndexOutOfBoundsException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.rubensousa.raiflatbutton.RaiflatButton;
import com.github.rubensousa.raiflatbutton.RaiflatImageButton;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.ilnur.cards.Json.Card;
import com.ilnur.cards.LoginActivity;
import com.ilnur.cards.MainActivity;
import com.ilnur.cards.MyDB;
import com.ilnur.cards.R;
import com.ilnur.cards.forStateSaving.ActivityState;
import com.ilnur.cards.forStateSaving.FragmentState;
import com.ilnur.cards.forStateSaving.learn;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.Iterator;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class LearnFragment extends Fragment {

    private Card curr_card;
    private ArrayList<Card> list;
    private String c;
    private StringBuilder wrongs = new StringBuilder();
    private MyDB db;
    String strBody = "";
    public learn learn;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*if (savedInstanceState != null){
            //restore it
            subj = savedInstanceState.getString("subj");
            title = savedInstanceState.getString("title");
            id = savedInstanceState.getInt("id");
            learn.i = savedInstanceState.getInt("i");
            parent = savedInstanceState.getBoolean("parent");
            revers = savedInstanceState.getBoolean("revers");
            //list = savedInstanceState.getParcelableArrayList("learn");
            wrong = new StringBuilder();
            wrong.append("<table>").append(savedInstanceState.getString("sb"));
        }*/
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        /*if (savedInstanceState != null){
            //restore it
            subj = savedInstanceState.getString("subj");
            title = savedInstanceState.getString("title");
            id = savedInstanceState.getInt("id");
            learn.i = savedInstanceState.getInt("i");
            right = savedInstanceState.getInt("right");
            wrong = savedInstanceState.getInt("wrongs");
            parent = savedInstanceState.getBoolean("parent");
            revers = savedInstanceState.getBoolean("revers");
            //list = savedInstanceState.getParcelableArrayList("learn");
            wrong = new StringBuilder();
            wrong.append("<table>").append(savedInstanceState.getString("sb"));
        }*/
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //save fragment state
       /* outState.putCharSequence("subj", subj);
        outState.putCharSequence("title", title);
        outState.putInt("id", id);
        outState.putInt("i", learn.i);
        outState.putInt("right", right);
        outState.putInt("wrongs", wrong);
        outState.putBoolean("parent", parent);
        outState.putBoolean("revers", revers);
        //outState.putParcelableArrayList("learn", list);
        outState.putCharSequence("sb", wrong.toString());*/
        learn.wrongs = wrongs.toString();
        for (Iterator<FragmentState> iterator = MainActivity.main.fragments.iterator(); iterator.hasNext(); ) {
            FragmentState s = iterator.next();
            if (s.name.equals("learn")) {
                iterator.remove();
            }
        }
        MainActivity.main.addFragment(new FragmentState("learn", new Gson().toJson(learn)));
        db.updateActState(new ActivityState("main", new Gson().toJson(MainActivity.main)));
    }

    public void setLearnFragment(MyDB db, learn learn) {
        this.learn = learn;
        this.db = db;
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
            dontknow.setPadding(0, 0, 0, 50);
            know.setPadding(0, 0, 0, 50);
            learned.setPadding(0, 0, 0, 50);
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
            dontknow.setPadding(0, 0, 0, 250);
            know.setPadding(0, 0, 0, 250);
            learned.setPadding(0, 0, 0, 250);
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        for (Iterator<FragmentState> iterator = MainActivity.main.fragments.iterator(); iterator.hasNext(); ) {
            FragmentState s = iterator.next();
            if (s.name.equals("learn")) {
                iterator.remove();
            }
        }
        MainActivity.main.addFragment(new FragmentState("learn", new Gson().toJson(learn)));
        db.updateActState(new ActivityState("main", new Gson().toJson(MainActivity.main)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.learn_lay, container, false);
        setRetainInstance(true);
        //getActivity().setTitle(title);

        /*if (savedInstanceState != null){
            //restore it
            subj = savedInstanceState.getString("subj");
            title = savedInstanceState.getString("title");
            id = savedInstanceState.getInt("id");
            learn.i = savedInstanceState.getInt("i");
            parent = savedInstanceState.getBoolean("parent");
            revers = savedInstanceState.getBoolean("revers");
            //list = savedInstanceState.getParcelableArrayList("learn");

            right = savedInstanceState.getInt("right");
            wrong = savedInstanceState.getInt("wrongs");
            wrong = new StringBuilder();
            wrong.append("<table>").append(savedInstanceState.getString("sb"));
            if (parent)
                list = MyDB.getParentCards(subj, id);
            else
                list = MyDB.getChildCards(subj, title, id);
        }*/
        //if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT){
        //    strBody = "<html><head></head><body>";
        //} else {
        //SQLiteDatabase sqdb =
        try {
            if (!db.getStyle(null, db.getReadableDatabase(), MyDB.style_orig, MyDB.style_names)
                    .equals("") && db.getStyle(null, db.instance.getReadableDatabase(), MyDB.style_orig, MyDB.style_names) != null)
                //strBody = "<html><head><meta name=\\\"viewport\\\" content=\\\"width=device-width; user-scalable=no; initial-scale=7.0; minimum-scale=5.0; maximum-scale=7.0; target-densityDpi=device-dpi;\\\"/><style>" + MyDB.getStyle(null);
                strBody = "<html><head><meta name=\\\"viewport\\\" content=\\\"width=device-width\\\"/><style>" +
                        //strBody = "<html><head><style>" +
                        db.getStyle(null, db.instance.getReadableDatabase(), MyDB.style_orig, MyDB.style_names);
        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
            //strBody = "<html><head><meta name=\\\"viewport\\\" content=\\\"width=device-width; user-scalable=no; initial-scale=7.0; minimum-scale=5.0; maximum-scale=7.0; target-densityDpi=device-dpi;\\\"/><style>";
            strBody = "<html><head><meta name=\\\"viewport\\\" content=\\\"width=device-width\\\"/><style>";
            //strBody = "<html><head><style>";
        }
        try {
            if (!db.getStyle(learn.subj, db.instance.getReadableDatabase(), MyDB.style_orig, MyDB.style_names).equals("") &&
                    db.getStyle(learn.subj, db.instance.getReadableDatabase(), MyDB.style_orig, MyDB.style_names) != null)
                strBody = strBody + db.getStyle(learn.subj, db.instance.getReadableDatabase(), MyDB.style_orig, MyDB.style_names) +
                        "</style></head><body>";
        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
            strBody = strBody + "</style></head><body>";
        }
        //}
        Log.i("style", strBody);
        //Toolbar bar = Toolbar.class.cast(getActivity().findViewById(R.id.toolbar));
        CollapsingToolbarLayout col = CollapsingToolbarLayout.class.cast(getActivity().findViewById(R.id.collapsing_toolbar));
        col.setTitle(learn.title);
        col.setExpandedTitleMarginBottom((int) getContext().getResources().getDimension(R.dimen.margin_title_col));
        RaiflatButton learnBtn = col.findViewById(R.id.learn);
        RaiflatButton watchBtn = col.findViewById(R.id.watch);
        RaiflatImageButton rever = col.findViewById(R.id.rever);


        learnBtn.setVisibility(View.GONE);
        watchBtn.setVisibility(View.GONE);
        rever.setVisibility(View.GONE);
        AppBarLayout apbar = AppBarLayout.class.cast(getActivity().findViewById(R.id.apbar));
        apbar.setExpanded(false, false);
        apbar.setClickable(false);
        //StringBuilder wrong = new StringBuilder();
        //wrong.append("<table>");

        //if (savedInstanceState == null || !savedInstanceState.containsKey("learn")) {
        if (learn.cards != null && learn.cards.length>0){
            list = db.getCardsById(learn.subj, learn.cards);
        } else if (learn.parent) {
            list = db.getParentCards(learn.subj, learn.id);
        } else {
            list = db.getChildCards(learn.subj, learn.title, learn.id);
        }
        if (learn.cards == null || learn.cards.length == 0) {
            learn.cards = new int[list.size()];
            for (int j = 0; j < list.size(); j++) {
                learn.cards[j] = list.get(j).getId();
            }
        }
        //}
        /*for (Card c: list){

        }*/
        LinearLayout ll = rootview.findViewById(R.id.buttons);

        View include = rootview.findViewById(R.id.learn_inc_fr);
        WebView web = include.findViewById(R.id.web);
        //because in sdk version lower than 20 css doesn't supported
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            //web.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            //strBody = strBody.replace("absolute", "relative");
            //strBody = strBody.replace("left: 50%;", "left: 0%");
            //strBody = strBody.replace("top: 50%;", "top: 25%;");
            strBody = strBody.replace("translate(-50%, -50%);", "translate(-50%, -50%);" +
                    "-webkit-transform: translate(-50%, -50%);");
            strBody = strBody.replaceAll("max-width: 100%;", "max-width: 100%; height: 50%;");

            //ViewGroup.LayoutParams params = web.getLayoutParams();
            //ViewGroup.LayoutParams params1 = web.getLayoutParams();
            //params1.width =
            /*web.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));*/
            //web.getSettings().setSupportZoom(true);
            //web.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
            //web.setInitialScale(100);
            //web.getSettings().setDefaultFontSize(90);
            //web.getSettings().setLoadWithOverviewMode(false);
            //web.getSettings().setUseWideViewPort(true);
        } else
            strBody = strBody.replaceAll("max-width: 100%;", "max-width: 100%; height: inherit;");
        TextView count = rootview.findViewById(R.id.count);

        Button check = rootview.findViewById(R.id.check_but);
        RaiflatButton again = rootview.findViewById(R.id.again);
        again.setVisibility(View.GONE);

        Button dontknow = rootview.findViewById(R.id.dontkn);
        Button know = rootview.findViewById(R.id.know);
        Button learned = rootview.findViewById(R.id.learned);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            dontknow.setPadding(0, 0, 0, 250);
            know.setPadding(0, 0, 0, 250);
            learned.setPadding(0, 0, 0, 250);
        } else {
            dontknow.setPadding(0, 0, 0, 50);
            know.setPadding(0, 0, 0, 50);
            learned.setPadding(0, 0, 0, 50);
        }
        c = "/" + list.size();


        if (list.size() == 0) {
            check.setVisibility(View.GONE);
            ll.setVisibility(View.GONE);
            //dontknow.setVisibility(View.GONE);
            //know.setVisibility(View.GONE);
            //learned.setVisibility(View.GONE);
            count.setText("0" + c);
            web.loadDataWithBaseURL(null, append("Похоже, что на сегодня вы все повторили или выучили"), "text/html", "utf-8", "about:blank");
        } else {
            count.setText(learn.i + 1 + c);
            //int i = 0;

            curr_card = list.get(learn.i);
            Log.i("card", append(curr_card.getRevers()));
            if (learn.revers) {
                web.loadDataWithBaseURL(null, append(curr_card.getRevers()), "text/html",
                        "utf-8", "about:blank");
            } else
                web.loadDataWithBaseURL(null, append(curr_card.getAvers()), "text/html",
                        "utf-8", "about:blank");

        }
        check.setOnClickListener(v -> {
            Log.i("BUtton", "check");
            //animate(check);
            check.setVisibility(View.GONE);
            ll.setVisibility(View.VISIBLE);
            //dontknow.setVisibility(View.VISIBLE);
            //know.setVisibility(View.VISIBLE);
            //learned.setVisibility(View.VISIBLE);
            if (learn.revers) {
                web.loadDataWithBaseURL(null, append(curr_card.getAvers()), "text/html",
                        "utf-8", "about:blank");
            } else {
                web.loadDataWithBaseURL(null, append(checkCard(curr_card).getRevers()), "text/html",
                        "utf-8", "about:blank");
            }
            learn.i = learn.i + 1;

        });
        again.setOnClickListener(v -> {
            learn.i = 0;
            list = new ArrayList<>();
            if (learn.parent)
                list = db.getParentCards(learn.subj, learn.id);
            else
                list = db.getChildCards(learn.subj, learn.title, learn.id);
            curr_card = list.get(learn.i);
            if (learn.revers) {
                web.loadDataWithBaseURL(null, append(curr_card.getRevers()), "text/html",
                        "utf-8", "about:blank");
            } else
                web.loadDataWithBaseURL(null, append(curr_card.getAvers()), "text/html",
                        "utf-8", "about:blank");
            c = "/" + list.size();
            count.setVisibility(View.VISIBLE);
            count.setText(String.valueOf(learn.i + 1) + c);
            again.setVisibility(View.GONE);
            check.setVisibility(View.VISIBLE);
            ll.setVisibility(View.GONE);
        });

        dontknow.setOnClickListener(v -> {
            Log.i("BUtton", "dont");
            learn.wrong++;
            check.setVisibility(View.VISIBLE);
            //dontknow.setVisibility(View.GONE);
            //know.setVisibility(View.GONE);
            //learned.setVisibility(View.GONE);
            ll.setVisibility(View.GONE);
            if (curr_card.getRevers().length() == 1) {
                wrongs.append("<tr><td>" + checkCard(curr_card).getRevers() + "</td></tr><tr>\n" +
                        "        <td colspan=\"2\" class=\"divider\"><hr /></td>\n" +
                        "    </tr>");
            } else {
                wrongs.append("<tr><td>" + curr_card.getAvers() + "</td><td class=\"line\">" + curr_card.getRevers() + "</td></tr><tr>\n" +
                        "        <td colspan=\"2\" class=\"divider\"><hr /></td>\n" +
                        "    </tr>");
            }
            //web.setText(list.get(i).getAvers());
            if (learn.i == list.size()) {
                check.setVisibility(View.GONE);
                ll.setVisibility(View.GONE);
                count.setVisibility(View.GONE);
                again.setVisibility(View.VISIBLE);
                web.loadDataWithBaseURL(null, showWrong(wrongs.toString()), "text/html",
                        "utf-8", "about:blank");
                if (!MainActivity.main.logged && MainActivity.main.show) {
                    new AlertDialog.Builder(rootview.getContext())
                            .setTitle("Хотите войти или зарегистрироваться?")
                            .setMessage("Для сохранения статистики на сервере необходимо авторизироваться или зарегистрироваться")
                            .setPositiveButton("Да", (dialog, which) -> startActivity(new Intent(getActivity(), LoginActivity.class)))
                            .setNegativeButton("Позже", (dialog, which) -> MainActivity.main.show = false)
                            .show();
                }
            } else {
                curr_card = list.get(learn.i);
                if (learn.revers) {
                    web.loadDataWithBaseURL(null, append(curr_card.getRevers()), "text/html",
                            "utf-8", "about:blank");
                } else {
                    web.loadDataWithBaseURL(null, append(curr_card.getAvers()), "text/html",
                            "utf-8", "about:blank");
                }
                count.setText((learn.i + 1) + c);
            }
        });

        know.setOnClickListener(v -> {
            Log.i("BUtton", "know");
            learn.right++;
            check.setVisibility(View.VISIBLE);
            //dontknow.setVisibility(View.GONE);
            //know.setVisibility(View.GONE);
            //learned.setVisibility(View.GONE);
            ll.setVisibility(View.GONE);
            new updateCard().execute(new params(learn.subj, curr_card.getId(), 1, curr_card.getResult()));
            //MyDB.updateCard(subj, curr_card.getId(), 1, curr_card.getResult());
            if (learn.i == list.size()) {
                web.loadDataWithBaseURL(null, showWrong(wrongs.toString()), "text/html",
                        "utf-8", "about:blank");
                check.setVisibility(View.GONE);
                count.setVisibility(View.GONE);
                again.setVisibility(View.VISIBLE);
                if (!MainActivity.main.logged && MainActivity.main.show) {
                    new AlertDialog.Builder(rootview.getContext())
                            .setTitle("Хотите войти или зарегистрироваться?")
                            .setMessage("Для сохранения статистики на сервере необходимо авторизироваться или зарегистрироваться")
                            .setPositiveButton("Да", (dialog, which) -> startActivity(new Intent(getActivity(), LoginActivity.class)))
                            .setNegativeButton("Позже", (dialog, which) -> MainActivity.main.show = false)
                            .show();
                }
            } else {
                curr_card = list.get(learn.i);
                if (learn.revers) {
                    web.loadDataWithBaseURL(null, append(curr_card.getRevers()), "text/html",
                            "utf-8", "about:blank");
                } else {
                    web.loadDataWithBaseURL(null, append(curr_card.getAvers()), "text/html",
                            "utf-8", "about:blank");
                }
                count.setText((learn.i + 1) + c);
            }
        });

        learned.setOnClickListener(v -> {
            Log.i("BUtton", "learned");
            learn.right++;
            check.setVisibility(View.VISIBLE);
            //dontknow.setVisibility(View.GONE);
            //know.setVisibility(View.GONE);
            //learned.setVisibility(View.GONE);
            ll.setVisibility(View.GONE);
            new updateCard().execute(new params(learn.subj, curr_card.getId(), 4, curr_card.getResult()));
            //MyDB.updateCard(subj, curr_card.getId(), 4, curr_card.getResult());
            if (learn.i == list.size()) {
                web.loadDataWithBaseURL(null, showWrong(wrongs.toString()), "text/html",
                        "utf-8", "about:blank");
                check.setVisibility(View.GONE);
                count.setVisibility(View.GONE);
                again.setVisibility(View.VISIBLE);
                if (!MainActivity.main.logged && MainActivity.main.show) {
                    new AlertDialog.Builder(rootview.getContext())
                            .setTitle("Хотите войти или зарегистрироваться?")
                            .setMessage("Для сохранения статистики на сервере необходимо авторизироваться или зарегистрироваться")
                            .setPositiveButton("Да", (dialog, which) -> startActivity(new Intent(getActivity(), LoginActivity.class)))
                            .setNegativeButton("Позже", (dialog, which) -> MainActivity.main.show = false)
                            .show();
                }

            } else {
                curr_card = list.get(learn.i);
                if (learn.revers) {
                    web.loadDataWithBaseURL(null, append(curr_card.getRevers()), "text/html",
                            "utf-8", "about:blank");
                } else {
                    web.loadDataWithBaseURL(null, append(curr_card.getAvers()), "text/html",
                            "utf-8", "about:blank");
                }
                count.setText((learn.i + 1) + c);
            }
        });

        Log.i("count  ", String.valueOf(getFragmentManager().getBackStackEntryCount()));
        return rootview;
    }

    private String append(String s) {
        return strBody + s + "</body></html>";
    }

    private String showWrong(String table) {
        table = table + "</table>";
        String style = "<style> .line {\n" +
                "  border-left: thin solid #000000;\n" +
                "  padding: 2px;\n" +
                "}</style>";
        String html = "<html>" + style + "<body><div><p align='center'>Стопка закончилась</p><p></p><p>Верных ответов: " +
                learn.right + "</p>";
        if (learn.wrong != 0) {
            html = html + "<p>Неверных ответов: " + learn.wrong + "</p><br>";
            html = html + "<strong>Допущенные ошибки</strong>" + table + "</div></body></html>";
        }
        return html;
    }
    /*private String rev(String s){
        if (revers){

        }
    }*/


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

    public class updateCard extends AsyncTask<params, Void, Void> {
        @Override
        protected Void doInBackground(params... params) {
            db.updateCard(params[0].subj, params[0].id, params[0].result, params[0].old_result);
            return null;
        }
    }

    public Card checkCard(Card card) {
        String avers = card.getAvers()
                .replace("<div align=\"justify\" width=\"100%\" class=\"pbody\"><p class=\"left_margin\">", "")
                .replace("</div>", "");
        String revers = card.getRevers()
                .replace("<div align=\"justify\" width=\"100%\" class=\"pbody\"><p class=\"left_margin\">", "")
                .replace("</div>", "");
        revers = revers.toUpperCase();

        String fin = avers.replace("_", revers);
        //Log.i("fin", fin);
        if (revers.length() == 1) {
            card.setRevers(card.getAvers().replace(avers, fin));
        }
        //Log.i("BUKVA_ED", card.getRevers());
        return card;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
