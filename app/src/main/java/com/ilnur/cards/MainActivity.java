package com.ilnur.cards;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.PersistableBundle;
import android.os.StrictMode;
import android.util.Log;

import com.github.rubensousa.raiflatbutton.RaiflatButton;
import com.github.rubensousa.raiflatbutton.RaiflatImageButton;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.ilnur.cards.Fragments.SubjFragment;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import org.jsoup.Jsoup;

import java.io.IOException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static boolean exit = false;
    public static User user;
    public static boolean logged = false;
    public static boolean onCreate = true;
    public static boolean show = true;
    public static boolean addsub = false;
    public static boolean syncsub = false;
    Toolbar toolbar;
    AppBarLayout apbar;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onResume() {
        super.onResume();
        changeNavHead(navigationView, logged);
        Log.i("count  ", String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        apbar = findViewById(R.id.apbar);
        apbar.setExpanded(false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //ImageLoaderConfiguration conf = new ImageLoaderConfig
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getApplicationContext()));

        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        //Param main = new Param(getApplicationContext(), false, navigationView);

        //new AddingDB().execute(main);
        // init db
        MyDB db = new MyDB(this);
        MyDB.init(db, false);


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
            }
        }
        if (!logged)
            startActivity(new Intent(this, LoginActivity.class));


        if (!isNetworkConnected()) {
            Toast.makeText(getApplicationContext(), "Подключение к интернету отсутствует, скачивание данных невозможно",
                    Toast.LENGTH_LONG).show();
        } else {
            //add subjects
            Runnable addSubh = () -> MyDB.add();
            Thread add = new Thread(addSubh);
            add.setName("add");
            add.start();
        }


        SubjFragment subjFragment = new SubjFragment();
        subjFragment.setRetainInstance(true);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.parent, subjFragment)
                .addToBackStack(null)
                .commit();
        //show login activity if you're not logged
        /*if (!logged)
            startActivity(new Intent(this, LoginActivity.class));
        //change na header
        changeNavHead(navigationView, logged);
        //new addSubjs().execute();
        //show list of subjects
        SubjFragment subjFragment = new SubjFragment();
        subjFragment.setRetainInstance(true);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.parent, subjFragment)
                .addToBackStack(null)
                .commit();*/

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

    public void changeNavHead(NavigationView navigationView, boolean logged) {
        if (logged) {
            navigationView.removeHeaderView(navigationView.getHeaderView(0));
            View navheader = navigationView.inflateHeaderView(R.layout.nav_header_sucsess);
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
                changeNavHead(navigationView, false);
            });

        } else {
            navigationView.removeHeaderView(navigationView.getHeaderView(0));
            View navheader = navigationView.inflateHeaderView(R.layout.nav_header_login);
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
                Runnable sync = MyDB::syncSubj;
                new Thread(sync).start();
            } else if (logged && syncsub) {
                Toast.makeText(getApplicationContext(), "Карточку уже синхронизируются", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Для синхронизации необходима авторизация", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_save) {
            if (!addsub) {
                Runnable sync = MyDB::add;
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
