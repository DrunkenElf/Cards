package com.ilnur.cards;

import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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

import com.github.rubensousa.raiflatbutton.RaiflatButton;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.ilnur.cards.Fragments.SubjFragment;

import org.jsoup.Jsoup;

import java.io.IOException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ComponentCallbacks2 {
    public static boolean exit = false;
    public static User user;
    public static boolean logged = false;
    public static boolean onCreate = true;
    public static boolean show = true;
    public static boolean addsub = false;
    public static boolean syncsub = false;
    public static Fragment current;
    public static String current_tag;
    public Context context;
    public static MyDB db;
    public static boolean isRegistered = false;
    static Toolbar toolbar;
    static ActionBar bar;
    AppBarLayout apbar;
    static DrawerLayout drawer;
    static ActionBarDrawerToggle toggle;
    NavigationView navigationView;

    //public static SharedPreferences msettings;

    @Override
    protected void onStart() {
        Log.i("Start", "Created");
        super.onStart();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        Log.i("onRstoreInst", "Created");
        super.onRestoreInstanceState(savedInstanceState, persistentState);
    }

    @Override
    public void onStateNotSaved() {
        Log.i("StateNotSaved", "Created");
        super.onStateNotSaved();
    }

    @Override
    public void onLowMemory() {
        Log.i("onLowMem", "main");
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        Log.i("onTrimMem", "main");
        switch (level) {
            case ComponentCallbacks2.TRIM_MEMORY_BACKGROUND:
                Log.i("memBack", "asd");
                break;
            case ComponentCallbacks2.TRIM_MEMORY_COMPLETE:
                Log.i("memCom", "asd");
                break;
            case ComponentCallbacks2.TRIM_MEMORY_MODERATE:
                Log.i("memMod", "asd");
                break;
            case ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN:
                Log.i("memUiHid", "asd");
                System.gc();
                Runtime.getRuntime().gc();
                break;
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL:
                Log.i("memRunCrit", "asd");
                break;
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW:
                Log.i("memRunLow", "asd");
                break;
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE:
                Log.i("memRunMod", "asd");
                break;
        }
        super.onTrimMemory(level);
    }

    ///onSa

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Pause", "Created");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Stop", "Created");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Ressume", "Created");
        changeNavHead(logged);
        Log.i("count  ", String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("Ressume", "Created");

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        Log.i("SaveInstanceMain", "Created");
        super.onSaveInstanceState(outState);
        //if (current_tag!=null && current!=null)
        //    getSupportFragmentManager().putFragment(outState, current_tag, current);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("MainActivity", "Created");
        setContentView(R.layout.activity_main);

        /*msettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if (msettings.contains(PREFERENCES_LOG) && msettings.contains(PREFERENCES_PAS) &&
                msettings.contains(PREFERENCES_SES)) {
            user.setLogin(msettings.getString(PREFERENCES_LOG, ""));
            user.setSession_id(msettings.getString(PREFERENCES_SES, ""));
            user.setPassword(msettings.getString(PREFERENCES_PAS, ""));
            logged = true;
        }*/


        if (savedInstanceState != null) {
            Log.i("saved", "not null");
            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);


            apbar = findViewById(R.id.apbar);
            apbar.setExpanded(false);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //ImageLoaderConfiguration conf = new ImageLoaderConfig
            //ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getApplicationContext()));

            drawer = findViewById(R.id.drawer_layout);
            toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();


            navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            changeNavHead(logged);


            // init db
            db = new MyDB(this);
            db.init(db, false);
            if (!isNetworkConnected()) {
                Toast.makeText(getApplicationContext(), "Подключение к интернету отсутствует, скачивание данных невозможно",
                        Toast.LENGTH_LONG).show();
            } else {
                //add subjects
                if (!logged) {
                    Log.i("not logged", "start adding");
                    /*Runnable addSubh = () -> db.add();
                    Thread add = new Thread(addSubh);
                    add.setName("add");
                    add.start();*/
                    db.add();
                }
            }
            //current = getSupportFragmentManager().getFragment(savedInstanceState, current_tag);


        } else {

            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            apbar = findViewById(R.id.apbar);
            apbar.setExpanded(false);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //ImageLoaderConfiguration conf = new ImageLoaderConfig
            //ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getApplicationContext()));

            drawer = findViewById(R.id.drawer_layout);
            toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();


            navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            db = new MyDB(this);
            db.init(db, false);

            user = MyDB.getUser();
            if (user.getLogin() == null) {
                logged = false;
            } else {
                Log.i("OLD_SESSION", user.getSession_id());
                if (checkValid(false)) {
                    //значит все валидно
                    //запуск добавления предметов
                    Toast.makeText(getApplicationContext(), "Вы вошли под логином " + user.getLogin(), Toast.LENGTH_SHORT).show();
                    logged = true;
                } else {
                    logged = false;
                    if (user.getLogin() != null && user.getPassword() != null && user.getSession_id() != null)
                        logged = true;
                    /*if (msettings.contains(PREFERENCES_LOG))
                        logged = true;*/
                }
            }
            changeNavHead(logged);
            if (!logged)
                startActivity(new Intent(this, LoginActivity.class));


            if (!isNetworkConnected()) {
                Toast.makeText(getApplicationContext(), "Подключение к интернету отсутствует, скачивание данных невозможно",
                        Toast.LENGTH_LONG).show();
            } else {
                //add subjects
                if (!logged) {
                    Log.i("add subj", "started");
                    Runnable addSubh = () -> db.add();
                    Thread add = new Thread(addSubh);
                    add.setName("add");
                    add.start();
                    //MyDB.add();
                    /*MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });*/
                }
            }


            SubjFragment subjFragment = new SubjFragment(db);
            subjFragment.setRetainInstance(true);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.parent, subjFragment)
                    .addToBackStack("subj")
                    .commit();
            //show login activity if you're not logged
        }
        bar = getSupportActionBar();

    }

    public void enableBackBtn(boolean enable) {
        if (enable) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            toggle.setDrawerIndicatorEnabled(false);
            bar.setDisplayHomeAsUpEnabled(true);
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (!isRegistered) {
                toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
                isRegistered = true;
            }
        } else {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            bar.setDisplayHomeAsUpEnabled(false);
            //getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.setToolbarNavigationClickListener(null);
            isRegistered = false;
        }
    }


    class Param {
        Context context;
        boolean logged;
        NavigationView nav;

        public Param(Context context, boolean logged, NavigationView nav) {
            this.context = context;
            this.logged = logged;
            this.nav = nav;
        }
    }

    public void changeNavHead(boolean logged) {
        Log.i("CCCC", "" + navigationView.getHeaderCount());
        if (logged) {

           /* navigationView.removeHeaderView(navigationView.getHeaderView(0));
            if (navigationView.getHeaderCount()>0)
                navigationView.removeHeaderView(navigationView.getHeaderView(0));*/
            navigationView.removeHeaderView(navigationView.getHeaderView(0));
            //navigationView;
            View navheader = navigationView.inflateHeaderView(R.layout.nav_header_sucsess);
            navigationView.removeHeaderView(navigationView.getHeaderView(0));
            navigationView.addHeaderView(navheader);
            ImageView iv = navheader.findViewById(R.id.nav_icon);
            TextView title = navheader.findViewById(R.id.nav_title);

            TextView info = navheader.findViewById(R.id.info);
            AppCompatButton logout = navheader.findViewById(R.id.log_out);
            info.setText("Вы вошли под логином " + user.getLogin());
            logout.setOnClickListener(v -> {
                MainActivity.logged = false;
                navigationView.removeHeaderView(navheader);
                MyDB.removeUser(user.getLogin());
                user.setLogin(null);
                user.setPassword(null);
                user.setSession_id(null);
                changeNavHead(false);
            });

        } else {

            /*navigationView.removeHeaderView(navigationView.getHeaderView(0));
            if (navigationView.getHeaderCount()>0)
                navigationView.removeHeaderView(navigationView.getHeaderView(0));*/
            navigationView.removeHeaderView(navigationView.getHeaderView(0));
            View navheader = navigationView.inflateHeaderView(R.layout.nav_header_login);
            navigationView.removeHeaderView(navigationView.getHeaderView(0));
            navigationView.addHeaderView(navheader);
            RaiflatButton enter = navheader.findViewById(R.id.enter);

            enter.setOnClickListener(v -> {
                //user.setLogin(login.getText().toString());
                //user.setPassword(password.getText().toString());
                startActivity(new Intent(this, LoginActivity.class));

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
            /*SharedPreferences.Editor edit = msettings.edit();
            edit.putString(PREFERENCES_LOG, user.getLogin());
            edit.putString(PREFERENCES_PAS, user.getPassword());
            edit.putString(PREFERENCES_SES, user.getSession_id());
            edit.apply();*/
            MyDB.updateUser(user.getLogin(), user.getPassword(), user.getSession_id());
            Log.i("Session_ID", session_id);
            return true;
        }
        if (toast)
            Toast.makeText(getApplicationContext(), "Введенные вами логин и пароль не" +
                    " являются действительными", Toast.LENGTH_SHORT).show();
        return false;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            this.finish();
        } else {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            //Log.i("COUNT", String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                Toast.makeText(this, "Нажмите ещё раз, чтобы выйти", Toast.LENGTH_SHORT).show();
                exit = true;
            } else {
                if (isRegistered && getSupportFragmentManager().getBackStackEntryCount() == 2)
                    enableBackBtn(false);
                super.onBackPressed();
            }
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       *//* if (id == R.id.action_settings) {
            return true;
        }*//*

        return super.onOptionsItemSelected(item);
    }*/


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        item.setCheckable(false);
        if (id == R.id.nav_sync) {
            if (logged && !syncsub) {
                Runnable sync = db::syncSubj;
                new Thread(sync).start();
            } else if (logged && syncsub) {
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
