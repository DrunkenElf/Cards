package com.ilnur.cards;

import android.app.ActivityOptions;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;



import com.github.rubensousa.raiflatbutton.RaiflatButton;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.ilnur.cards.Fragments.ButListFragment;
import com.ilnur.cards.Fragments.LearnFragment;
import com.ilnur.cards.Fragments.ListFragment;
import com.ilnur.cards.Fragments.SubjFragment;
import com.ilnur.cards.Fragments.WatchFragment1;
import com.ilnur.cards.forStateSaving.ActivityState;
import com.ilnur.cards.forStateSaving.AppState;
import com.ilnur.cards.forStateSaving.FragmentState;
import com.ilnur.cards.forStateSaving.btn;
import com.ilnur.cards.forStateSaving.learn;
import com.ilnur.cards.forStateSaving.list;
import com.ilnur.cards.forStateSaving.logActState;
import com.ilnur.cards.forStateSaving.mainActState;
import com.ilnur.cards.forStateSaving.subj;
import com.ilnur.cards.forStateSaving.watch;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ComponentCallbacks2 {
    public static User user;
    public static boolean addsub = false;
    public static boolean syncsub = false;
    public Context context;
    public static MyDB db;
    static Toolbar toolbar;
    static ActionBar bar;
    AppBarLayout apbar;
    static DrawerLayout drawer;
    static ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    public static AppState appState;
    public static mainActState main;


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        //outState.putBoolean("a",);
        Log.i("onSAvem", "Created");
        super.onSaveInstanceState(outState);
    }


    public void addAllFrag(Bundle savedInstanceState) {
        FragmentTransaction transaction;
        for (Iterator<FragmentState> iterator = MainActivity.main.fragments.iterator(); iterator.hasNext(); ) {
            FragmentState state = iterator.next();
            transaction = getSupportFragmentManager().beginTransaction();
            Log.i("NAME", state.name);
            switch (state.name) {
                case "subj":
                    //if (getSupportFragmentManager().getFragment(savedInstanceState, "subj") == null) {
                    subj subj = new Gson().fromJson(state.data, com.ilnur.cards.forStateSaving.subj.class);
                    Log.i("addAll subj", subj.subjects.length + "");
                    SubjFragment subjFragment = new SubjFragment();
                    subjFragment.setState(db, subj);
                    subjFragment.setRetainInstance(true);
                    subjFragment.setArguments(savedInstanceState);
                    transaction
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.from_left, R.anim.to_right)
                            .replace(R.id.parent, subjFragment)
                            .addToBackStack("subj")
                            .commit();
                    //}
                    break;
                case "list":
                    //if (getSupportFragmentManager().getFragment(savedInstanceState, "list") == null) {
                    list list1 = new Gson().fromJson(state.data, list.class);
                    ListFragment listFragment = new ListFragment();
                    listFragment.setTitle(db, list1);
                    listFragment.setArguments(savedInstanceState);
                    transaction
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.from_left, R.anim.to_right)
                            .replace(R.id.parent, listFragment)
                            .addToBackStack("list")
                            .commit();
                    //}
                    break;
                case "btn":
                    //if (getSupportFragmentManager().getFragment(savedInstanceState, "btn") == null) {
                    btn button = new Gson().fromJson(state.data, btn.class);
                    ButListFragment btnFragment = new ButListFragment();
                    btnFragment.setBtn(db, button);
                    btnFragment.setArguments(savedInstanceState);
                    transaction
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.from_left, R.anim.to_right)
                            .replace(R.id.parent, btnFragment)
                            .addToBackStack("btn").commit();
                    //}
                    break;
                case "learn":
                    //if (getSupportFragmentManager().getFragment(savedInstanceState, "learn") == null) {
                    LearnFragment lf = new LearnFragment();
                    learn learnState = new Gson().fromJson(state.data, learn.class);
                    lf.setLearnFragment(db, learnState);
                    lf.setArguments(savedInstanceState);
                    transaction
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.from_left, R.anim.to_right)
                            .replace(R.id.parent, lf)
                            .addToBackStack("learn").commit();
                    //}
                    break;
                case "watch":
                    //if (getSupportFragmentManager().getFragment(savedInstanceState, "watch") == null) {
                    WatchFragment1 wf = new WatchFragment1();
                    watch watch = new Gson().fromJson(state.data, com.ilnur.cards.forStateSaving.watch.class);
                    wf.setWatchFragment(db, watch);
                    wf.setArguments(savedInstanceState);
                    transaction
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.from_left, R.anim.to_right)
                            .replace(R.id.parent, wf).addToBackStack("watch").commit();
                    //}
                    break;
            }
            Log.d("addALLCUR", state.name);
        }
        //transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Ressume", "Created");
        changeNavHead(main.logged);
    }

    private void setupAnim(){
        if (Build.VERSION.SDK_INT >= 21) {
            Slide toRight = new Slide();
            toRight.setSlideEdge(Gravity.RIGHT);
            toRight.setDuration(500);

            Slide toLeft = new Slide();
            toLeft.setSlideEdge(Gravity.LEFT);
            toLeft.setDuration(500);

            Slide toTop = new Slide();
            toTop.setSlideEdge(Gravity.TOP);
            toTop.setDuration(500);

            Slide toBot = new Slide();
            toBot.setSlideEdge(Gravity.BOTTOM);
            toBot.setDuration(500);

            getWindow().setExitTransition(toRight);
            getWindow().setEnterTransition(toRight);
            getWindow().setReturnTransition(toRight);
            getWindow().setReenterTransition(toRight);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("onCreate", "Main");
        setupAnim();
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        apbar = findViewById(R.id.apbar);
        apbar.setExpanded(false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        bar = getSupportActionBar();

        if (appState != null && appState.activities[0] != null && db != null &&
                getSupportFragmentManager().getBackStackEntryCount() > 1)
            restoreState(savedInstanceState);
        else
            firstCreate(savedInstanceState);


        if (user.getLogin() == null) {
            main.logged = false;
        } else {
            Log.i("OLD_SESSION", user.getSession_id());
            if (checkValid(false)) {
                Toast.makeText(getApplicationContext(), "Вы вошли под логином " + user.getLogin(), Toast.LENGTH_SHORT).show();
                main.logged = true;
            } else {
                main.logged = false;
                if (user.getLogin() != null && user.getPassword() != null && user.getSession_id() != null) {
                    main.logged = true;
                }
                    /*if (msettings.contains(PREFERENCES_LOG))
                        logged = true;*/
            }
        }


        changeNavHead(main.logged);

        if (isNetworkAvailable()) {
            Log.i("add subj", "started");
            db.add();
        } else
            Toast.makeText(getApplicationContext(), "Подключение к интернету отсутствует, скачивание данных невозможно",
                    Toast.LENGTH_LONG).show();


        Log.i("STACK", getSupportFragmentManager().getBackStackEntryCount() + "");

        if (main.fragments.size() > 1)
            enableBackBtn(true);
        else
            enableBackBtn(false);
    }

    private void firstCreate(Bundle savedInstanceState) {
        Log.i("firstCreate", "start");
        if (db == null) {
            db = new MyDB(this);
            db.init(db, false);
            user = db.getUser();
        }

        if (appState == null)
            appState = new AppState(db);

        if (appState.activities[2] != null) {
            main = new mainActState();
            SubjFragment subjFragment = new SubjFragment();
            subj subj = new subj(new String[]{"Русский язык", "Физика", "Математика", "Английский язык", "История"});
            subjFragment.setState(db, subj);
            main.addFragment(new FragmentState("subj", new Gson().toJson(subj)));
            subjFragment.setRetainInstance(true);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.parent, subjFragment)
                    .addToBackStack("subj")
                    .commit();
            Log.i("STACK ELSE", getSupportFragmentManager().getBackStackEntryCount() + "");
            db.updateActState(new ActivityState("main", new Gson().toJson(main)));
            if (!main.logged) {
                startActivity(new Intent(this, RegisterActivity.class));
            }
        } else if (appState.activities[1] != null) {
            main = new mainActState();
            SubjFragment subjFragment = new SubjFragment();
            subj subj = new subj(new String[]{"Русский язык", "Физика", "Математика", "Английский язык", "История"});
            subjFragment.setState(db, subj);
            main.addFragment(new FragmentState("subj", new Gson().toJson(subj)));
            subjFragment.setRetainInstance(true);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.parent, subjFragment)
                    .addToBackStack("subj")
                    .commit();
            Log.i("STACK ELSE", getSupportFragmentManager().getBackStackEntryCount() + "");
            db.updateActState(new ActivityState("main", new Gson().toJson(main)));
            if (!main.logged)
                startActivity(new Intent(this, LoginActivity.class));

        } else if (appState.activities[0] != null) {
            main = new Gson().fromJson(appState.activities[0].data, mainActState.class);
            Log.d("AddAll", "ELSE");
            addAllFrag(savedInstanceState);
        } else {
            main = new mainActState();
            SubjFragment subjFragment = new SubjFragment();
            subj subj = new subj(new String[]{"Русский язык", "Физика", "Математика", "Английский язык", "История"});
            subjFragment.setState(db, subj);
            main.addFragment(new FragmentState("subj", new Gson().toJson(subj)));
            subjFragment.setRetainInstance(true);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.parent, subjFragment)
                    .addToBackStack("subj")
                    .commit();
            Log.i("STACK ELSE", getSupportFragmentManager().getBackStackEntryCount() + "");
            db.updateActState(new ActivityState("main", new Gson().toJson(main)));
            if (!main.logged)
                startActivity(new Intent(this, LoginActivity.class));

        }
    }

    private void restoreState(Bundle savedInstanceState) {
        Log.i("RESTORE", "onCreate");
        main = new Gson().fromJson(appState.activities[0].data, mainActState.class);

        changeNavHead(main.logged);

        if (main.fragments.size() > 1 && appState.activities[1] == null) {
            Log.d("addAll", "in RESTORE");
            addAllFrag(savedInstanceState);
        } else if (appState.activities[1] != null) {
            addAllFrag(savedInstanceState);
            if (!main.logged)
                startActivity(new Intent(this, LoginActivity.class)/*, options.toBundle()*/);
        } else if (appState.activities[2] != null)
            startActivity(new Intent(this, RegisterActivity.class)/*, options.toBundle()*/);

    }

    public void enableBackBtn(boolean enable) {
        if (enable) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            toggle.setDrawerIndicatorEnabled(false);
            bar.setDisplayHomeAsUpEnabled(true);

            toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        } else {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            bar.setDisplayHomeAsUpEnabled(false);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.setToolbarNavigationClickListener(null);
        }
    }

    public void changeNavHead(boolean logged) {
        Log.i("CCCC", "" + navigationView.getHeaderCount());
        if (logged) {
            navigationView.removeHeaderView(navigationView.getHeaderView(0));
            View navheader = navigationView.inflateHeaderView(R.layout.nav_header_sucsess);
            navigationView.removeHeaderView(navigationView.getHeaderView(0));
            navigationView.addHeaderView(navheader);

            TextView info = navheader.findViewById(R.id.info);
            AppCompatButton logout = navheader.findViewById(R.id.log_out);
            info.setText("Вы вошли под логином " + user.getLogin());
            logout.setOnClickListener(v -> {
                main.logged = false;
                navigationView.removeHeaderView(navheader);
                db.removeUser(user.getLogin());
                user.setLogin(null);
                user.setPassword(null);
                user.setSession_id(null);
                changeNavHead(false);
            });

        } else {
            navigationView.removeHeaderView(navigationView.getHeaderView(0));
            View navheader = navigationView.inflateHeaderView(R.layout.nav_header_login);
            navigationView.removeHeaderView(navigationView.getHeaderView(0));
            navigationView.addHeaderView(navheader);
            RaiflatButton enter = navheader.findViewById(R.id.enter);

            enter.setOnClickListener(v -> {
                //user.setLogin(login.getText().toString());
                //user.setPassword(password.getText().toString());
                ActivityOptions options = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    options = ActivityOptions.makeSceneTransitionAnimation(this);
                }
                startActivity(new Intent(this, LoginActivity.class), options.toBundle());

            });

        }

    }


    public boolean checkValid(boolean toast) {
        String link = "https://ege.sdamgia.ru/api?protocolVersion=1&type=login&user="
                + user.getLogin() + "&password=" + user.getPassword();
        String resp = null;
        try {
            resp = Jsoup.connect(link).ignoreContentType(true).get().text();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //{"error": "Wrong login or password"}
        //{"data": {"session": "136d8b66ffd93b20b5dffbfe0864e77f"}}
        if (resp == null) {
            if (toast)
                Toast.makeText(getApplicationContext(), "Проверьте подключение к интернету", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!resp.contains("error")) {
            String session_id = resp.split(" ")[2];
            session_id = session_id.replaceAll("\"", "").replace("}}", "");
            user.setSession_id(session_id);
            db.updateUser(user.getLogin(), user.getPassword(), user.getSession_id());
            Log.i("Session_ID", session_id);
            return true;
        }
        if (toast)
            Toast.makeText(getApplicationContext(), "Введенные вами логин и пароль не" +
                    " являются действительными", Toast.LENGTH_SHORT).show();
        return false;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        } else
            return false;
    }

    // subj, learn, watch, btn, list
    @Override
    public void onBackPressed() {
        if (main.exit) {
            db.deleteActState("main");
            this.finish();
        } else {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                Toast.makeText(this, "Нажмите ещё раз, чтобы выйти", Toast.LENGTH_SHORT).show();
                main.exit = true;
            } else {
                if (getSupportFragmentManager().getBackStackEntryCount() == 2)
                    enableBackBtn(false);
                FragmentManager manager = getSupportFragmentManager();
                Log.d("BACK COUNt", getSupportFragmentManager().getBackStackEntryCount() + "");
                if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                    String tag = manager.getBackStackEntryAt(manager.getBackStackEntryCount() - 1).getName();
                    Log.d("BACK REMOVE", tag);
                    manager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    main.deleteFragment();
                    db.updateActState(new ActivityState("main", new Gson().toJson(main)));
                }
            }
        }
    }



    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        item.setCheckable(false);
        if (id == R.id.nav_sync) {
            if (main.logged && !syncsub) {
                Runnable sync = db::syncSubj;
                new Thread(sync).start();
            } else if (main.logged && syncsub) {
                Toast.makeText(getApplicationContext(), "Карточку уже синхронизируются", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Для синхронизации необходима авторизация", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_save) {
            if (!db.isAlladded()) {
                Runnable sync = () -> db.add();
                new Thread(sync).start();
            } else {
                Toast.makeText(getApplicationContext(), "Предметы уже добавляются", Toast.LENGTH_SHORT).show();
            }
        }

        /*if (id == R.id.nav_stats) {

        } else if (id == R.id.nav_fave) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
