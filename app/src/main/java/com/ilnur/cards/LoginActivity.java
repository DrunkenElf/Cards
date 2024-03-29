package com.ilnur.cards;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.telecom.Call;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;

import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.github.rubensousa.raiflatbutton.RaiflatButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.ilnur.cards.forStateSaving.ActivityState;
import com.ilnur.cards.forStateSaving.AppState;
import com.ilnur.cards.forStateSaving.logActState;

import org.jsoup.Jsoup;

import java.io.IOException;

import static com.ilnur.cards.MainActivity.db;
import static com.ilnur.cards.MainActivity.user;

public class LoginActivity extends AppCompatActivity {
    logActState logState;
    ActivityOptions options = null;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        ActivityState activityState = new ActivityState("log", new Gson().toJson(logState));
        MainActivity.appState.activities[1] = activityState;
        db.updateActState(activityState);
    }

    void saveAct() {
        ActivityState activityState = new ActivityState("log", new Gson().toJson(logState));
        MainActivity.appState.activities[1] = activityState;
        db.updateActState(activityState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i("onBACKPRESS", "DONE");
        Runnable sync = () -> db.syncSubj();
        new Thread(sync).start();
        MainActivity.appState.activities[1] = null;
        db.deleteActState("log");
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

            //WebView.enableSlowWholeDocumentDraw();
            //когда переходишь на новую
            getWindow().setExitTransition(toLeft);
            getWindow().setEnterTransition(toRight);

            //когда нажимаешь с другого назад и открываешь со старого
            getWindow().setReturnTransition(toRight);
            getWindow().setReenterTransition(toRight);

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        Explode explodeAnimation = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            explodeAnimation = new Explode();
            explodeAnimation.setDuration(1000);
            getWindow().setEnterTransition(explodeAnimation);
            getWindow().setReenterTransition(explodeAnimation);  // When MainActivity Re-enter the Screen
            getWindow().setExitTransition(explodeAnimation);
            getWindow().setAllowReturnTransitionOverlap(false);
        }*/
        setupAnim();
        setContentView(R.layout.login_activity);
        if (MainActivity.appState.activities[1] == null) {
            logState = new logActState(" ", " ");
            MainActivity.appState.activities[1] = new ActivityState("log", new Gson().toJson(logState));
            MainActivity.db.updateActState(MainActivity.appState.activities[1]);
        } else {
            logState = new Gson().fromJson(MainActivity.appState.activities[1].data, logActState.class);
        }


        AppCompatButton close = findViewById(R.id.not_now);
        ImageView close_kr = findViewById(R.id.close_krest);
        close_kr.setOnClickListener(v -> onBackPressed()/*finish()*/);
        close.setOnClickListener(v -> {
            //this.setVisible(false);
            /*MainActivity.appState.activities[1] = null;
            db.deleteActState("log");
            finish();*/
            onBackPressed();
        });
        TextView tv = findViewById(R.id.resh_kart);
        ImageView iv = findViewById(R.id.lable);
        iv.setImageResource(R.drawable.logo_trans);
        EditText login = findViewById(R.id.login_login);
        EditText password = findViewById(R.id.login_password);

        RaiflatButton signIn = findViewById(R.id.sign_in);
        AppCompatButton register = findViewById(R.id.register);

        //register.setAlpha(0.0f);
        Log.i("LOGIN", logState.login);
        Log.i("PASSWORD", logState.password);
        if (logState.login.length() > 1)
            login.setText(logState.login);
        if (logState.password.length() > 1)
            login.setText(logState.password);

        login.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                user.setLogin(login.getText().toString());
                logState.login = login.getText().toString();
                return true;
            }
            return false;
        });
        login.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                user.setLogin(login.getText().toString());
                logState.login = login.getText().toString();
                //signIn.performClick();
                return true;
            }
            return false;
        });

        /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            options = ActivityOptions.makeSceneTransitionAnimation(this);
        }*/

        register.setOnClickListener(v -> {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                options = ActivityOptions.makeSceneTransitionAnimation(this);
            }
            finish();
            startActivity(new Intent(this, RegisterActivity.class), options.toBundle());
                //finish();
        });


        password.setOnKeyListener((View v, int keyCode, KeyEvent event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                user.setPassword(password.getText().toString());
                logState.password = password.getText().toString();
                //saveAct();
                return true;
            }
            return false;
        });
        password.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                user.setPassword(password.getText().toString());
                logState.password = password.getText().toString();
                //saveAct();
                signIn.performClick();
                return true;
            }
            return false;
        });

        signIn.setOnClickListener(v -> {
            user.setLogin(login.getText().toString());
            logState.login = login.getText().toString();
            user.setPassword(password.getText().toString());
            logState.password = password.getText().toString();
            TextInputLayout logg = findViewById(R.id.login_lay);
            TextInputLayout pass = findViewById(R.id.password_lay);
            if ((user.getLogin().equals("") || user.getLogin() == null) &&
                    (user.getPassword().equals("") || user.getPassword() == null)) {
                logg.setError("Вы не ввели логин");
                pass.setError("Вы не ввели пароль");
                login.requestFocus();
            } else if (user.getLogin().equals("") || user.getLogin() == null) {
                String er = "Вы не ввели логин";
                pass.setError(null);
                logg.setError(er);
                login.requestFocus();
            } else if (user.getPassword().equals("") || user.getPassword() == null) {
                String er = "Вы не ввели пароль";
                logg.setError(null);
                pass.setError(er);
                password.requestFocus();
            } else if (checkValid(true)) {
                MainActivity.main.logged = true;
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Toast.makeText(getApplicationContext(), "Авторизация прошла успешно", Toast.LENGTH_SHORT).show();
                Runnable sync = MainActivity.db::syncSubj;
                new Thread(sync).start();
                MainActivity.appState.activities[1] = null;
                db.deleteActState("log");
                finish();
                //overridePendingTransition(R.anim.exit_to_left, R.anim.enter_from_right);
            }

        });


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
            db.updateUser(user.getLogin(), user.getPassword(), user.getSession_id());
            Log.i("Session_ID", session_id);
            return true;
        }
        if (toast)
            Toast.makeText(getApplicationContext(), "Введенные вами логин и пароль не" +
                    " являются действительными", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void finish() {
        super.finish();
        // //super.onBackPressed();
        //overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }
}
