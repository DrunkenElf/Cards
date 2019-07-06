package com.ilnur.cards;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;

import com.google.android.material.textfield.TextInputLayout;

import org.jsoup.Jsoup;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import static com.ilnur.cards.MainActivity.user;

public class RegisterActivity extends AppCompatActivity {
    public final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private final String hash = "quaeweSio7aingoo6wa1xoochethieJ6eishieph1eishai6Gi";
    private String link = "https://ege.sdamgia.ru/api?type=register";
    private String username = null;
    private String name1 = null;
    private String surname1 = null;
    private String password = null;
    private String password1 = null;
    private String day = null;
    private String month = null;
    private String year = null;
    private String status = null;
    private static boolean valid = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_lay);
        setTitle("Решу ЕГЭ. Карточки");
        TextView tv = findViewById(R.id.spinner_hint);

        //init view elements
        TextInputLayout mailLay = findViewById(R.id.reg_lay_mail);
        EditText mail = findViewById(R.id.reg_mail);

        TextInputLayout pasLay = findViewById(R.id.reg_lay_pass);
        EditText pas = findViewById(R.id.reg_pass);

        TextInputLayout pasAgainLay = findViewById(R.id.reg_lay_pass_again);
        EditText pasAgain = findViewById(R.id.reg_pass_again);

        TextInputLayout nameLay = findViewById(R.id.reg_lay_name);
        EditText name = findViewById(R.id.reg_name);

        TextInputLayout dateLay = findViewById(R.id.reg_lay_date);
        EditText date = findViewById(R.id.reg_date);

        TextInputLayout surnameLay = findViewById(R.id.reg_lay_surname);
        EditText surname = findViewById(R.id.reg_surname);

        TextInputLayout typeLay = findViewById(R.id.reg_type);
        AppCompatSpinner spinner = findViewById(R.id.spinner);

        AppCompatButton enter = findViewById(R.id.reg_but);


        //adding listeners to editTexts
        //listener for mail
        mail.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                username = mail.getText().toString();
                return true;
            }
            return false;
        });
        mail.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                username = mail.getText().toString();
                //signIn.performClick();
                return true;
            }
            return false;
        });

        //listener for name
        name.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                name1 = name.getText().toString();
                return true;
            }
            return false;
        });
        name.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                name1 = name.getText().toString();
                //signIn.performClick();
                return true;
            }
            return false;
        });

        //listeners for surname
        surname.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                surname1 = surname.getText().toString();
                return true;
            }
            return false;
        });
        surname.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                surname1 = surname.getText().toString();
                //signIn.performClick();
                return true;
            }
            return false;
        });

        //listeners for password
        pas.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                password = pas.getText().toString();
                return true;
            }
            return false;
        });
        pas.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                password = pas.getText().toString();
                //signIn.performClick();
                return true;
            }
            return false;
        });

        //listeners for password again
        pasAgain.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                password1 = pasAgain.getText().toString();
                return true;
            }
            return false;
        });
        pasAgain.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                password1 = pasAgain.getText().toString();
                //signIn.performClick();
                return true;
            }
            return false;
        });


        //adding vars to spinner & creating adapter for it & adding listeners
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.types,
                R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setPrompt("Кто вы?");
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status = getResources().getStringArray(R.array.types)[position];
                switch (status) {
                    case "Ученик":
                        status = "student";
                        break;
                    case "Учитель":
                        status = "teacher";
                        break;
                    case "Родитель":
                        status = "parent";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //creating datepicker to pick date
        final Calendar calendar = Calendar.getInstance();
        date.setInputType(InputType.TYPE_NULL);
        DatePickerDialog.OnDateSetListener dateSetter = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            this.day = String.valueOf(dayOfMonth);
            this.month = String.valueOf(month);
            this.year = String.valueOf(year);
            updateLable(date, calendar);
        };
        date.setOnClickListener(v -> {
            new DatePickerDialog(RegisterActivity.this, dateSetter, calendar
                    .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
            String myFormat = "dd/MM/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
            date.setText(sdf.format(calendar.getTime()));
            try {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    date.requestFocus();
                    new DatePickerDialog(RegisterActivity.this, dateSetter, calendar
                            .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)).show();
                    try {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        //listener for signup button
        enter.setOnClickListener(v -> {
            username = mail.getText().toString();
            name1 = name.getText().toString();
            surname1 = surname.getText().toString();
            password = pas.getText().toString();
            password1 = pasAgain.getText().toString();

            //check validity of mail
            if (mail.getText() == null || mail.getText().toString().equals("")) {
                mailLay.setError("Вы не ввели почту");
                valid = false;
            } else if (!validateMail(mail.getText().toString(), EMAIL_PATTERN)) {
                mailLay.setError("Введите корректную почту");
                valid = false;
            } else
                mailLay.setError(null);

            //validity of name
            if (name.getText() == null || name.getText().toString().equals("")) {
                nameLay.setError("Вы не ввели свое имя");
                valid = false;
            } else
                nameLay.setError(null);

            //validity of surname
            if (surname.getText() == null || surname.getText().toString().equals("")) {
                surnameLay.setError("Вы не ввели свою фамилию");
                valid = false;
            } else
                surnameLay.setError(null);

            //validity of password
            if (pas.getText() == null || pas.getText().toString().equals("")) {
                pasLay.setError("Вы не ввели пароль");
                valid = false;
            } else if (pasAgain.getText() == null || pasAgain.getText().toString().equals("")) {
                pasAgainLay.setError("Введите пароль еще раз");
                valid = false;
            } else if (!pas.getText().toString().equals(pasAgain.getText().toString())) {
                pasLay.setError("Пароли не совпадают");
                pasAgainLay.setError("Пароли не совпадают");
                valid = false;
            } else {
                pasLay.setError(null);
                pasAgainLay.setError(null);
            }

            if (valid) {
               new signUpIn(RegisterActivity.this).execute();
            }

            valid = true;
        });

    }

    private class signUpIn extends AsyncTask<Void, Void, String> {
        private ProgressDialog dialog;

        public signUpIn(Context context){
            dialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Регистрация...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String result = null;
            try {
                result = signUp();
            } catch (IOException e) {
                e.printStackTrace();
                return "conError";
            }
            if (result == null)
                return "conError";
            if (result.equals("wrong email"))
                return "wrongMail";
            if (result.contains("duplicate email"))
                return "dupMail";
            if (result.contains("error"))
                return "servError";
            if (result.contains("data"))
                return "succes";
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //dialog.cancel();
            switch (s){
                case "connError":
                    dialog.cancel();
                    Toast.makeText(getApplicationContext(), "Проверьте подключение к интернету", Toast.LENGTH_LONG).show();
                    break;
                case "wrongMail":
                    dialog.cancel();
                    Toast.makeText(getApplicationContext(), "Такой почты не существует", Toast.LENGTH_LONG).show();
                    break;
                case "dupMail":
                    dialog.cancel();
                    Toast.makeText(getApplicationContext(), "Данный e-mail уже занят, попробуйте другой", Toast.LENGTH_LONG).show();
                    break;
                case "servError":
                    dialog.cancel();
                    Toast.makeText(getApplicationContext(), "Сервер РЕШУ ЕГЭ временно недоступен", Toast.LENGTH_LONG).show();
                    break;
                case "succes":
                    Toast.makeText(getApplicationContext(), "Регистрация прошла успешно", Toast.LENGTH_LONG).show();
                    dialog.setMessage("Авторизация...");
                    user.setLogin(username);
                    user.setPassword(password);
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
                        Toast.makeText(getApplicationContext(), "Проверьте подключение к интернету", Toast.LENGTH_SHORT).show();
                    }
                    else if (!resp.contains("error")) {
                        String session_id = resp.split(" ")[2];
                        session_id = session_id.replaceAll("\"", "").replace("}}", "");
                        user.setSession_id(session_id);
                        MyDB.updateUser(user.getLogin(), user.getPassword(), user.getSession_id());
                        Log.i("Session_ID", session_id);
                    }
                    dialog.cancel();
                    MainActivity.logged = true;
                    Toast.makeText(getApplicationContext(), "Вы успешно авторизованы", Toast.LENGTH_LONG).show();
                    Runnable sync = MyDB::syncSubj;
                    new Thread(sync).start();
                    finish();
            }
        }
    }

    public String getDate() {
        return day + "/" + month + "/" + year;
    }

    //setting formatted date
    private void updateLable(EditText date, Calendar calendar) {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        date.setText(sdf.format(calendar.getTime()));
    }


    //signing up
    private String signUp() throws IOException {
        //String hash = "quaeweSio7aingoo6wa1xoochethieJ6eishieph1eishai6Gi";
        String parameters = "username=" + username + "&password=" +
                password + "&name=" + name1 +
                "&sname=" + surname1 + "&hash=" +
                MD5(hash + username) + "&protocolVersion=1";
        /*if (getDate() !=null){
            parameters = parameters.concat("&birthdate=" + getDate());
        }
        if (status != null) {
            parameters = parameters.concat("&status=" + status);
        }*/
        HttpsURLConnection connection = (HttpsURLConnection) new URL(link).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);

        byte[] data = null;
        InputStream is = null;

        connection.setRequestProperty("Content-Length", "" + parameters.getBytes().length);
        OutputStream os = connection.getOutputStream();
        data = parameters.getBytes("UTF-8");
        os.write(data);
        data = null;
        connection.connect();
        int responseCode = connection.getResponseCode();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String str = null;
        if (responseCode == 200) {
            is = connection.getInputStream();

            byte[] buffer = new byte[8192]; // Такого вот размера буфер
            // Далее, например, вот так читаем ответ
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            data = baos.toByteArray();
            str = new String(data, "UTF-8");
        }
        //{"data": {"id": 4717053}}
        Log.i("resp", str);
        return str;
    }

    private boolean validateMail(String current, String pattern) {
        return Pattern
                .compile(pattern)
                .matcher(current)
                .matches();
    }

    public static String MD5(String md5) {
        MessageDigest messageDigest = null;
        byte[] digest = new byte[0];

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(md5.getBytes());
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            // тут можно обработать ошибку
            // возникает она если в передаваемый алгоритм в getInstance(,,,) не существует
            e.printStackTrace();
        }

        BigInteger bigInt = new BigInteger(1, digest);
        String md5Hex = bigInt.toString(16);

        while (md5Hex.length() < 32) {
            md5Hex = "0" + md5Hex;
        }

        return md5Hex;
    }

}
