package com.ilnur.cards;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.ilnur.cards.Json.Card;
import com.ilnur.cards.Json.Category;
import com.ilnur.cards.Json.StupCard;
import com.ilnur.cards.Json.Stupid;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MyDB extends SQLiteOpenHelper {
    private static String dbname = "jsondb.db";
    private static Context acontext;
    //public static String[] subjects = {"Русский язык", "Итоговое сочинение", "Математика"};
    //public static String[] style_names = {"rus","soch","math", "inf", "phys", "hist", "chem", "bio", "geo", "soc"};
    //public static String[] style_orig = {"Русский язык","Итоговое сочинение","Математика",
    //"Информатика", "Физика", "История", "Химия", "Биология", "География", "Обществознание"};
    private static String myPath;
    private static SQLiteDatabase sqliteDb;
    private static int version = 1;
    private static MyDB instance;
    private static ContentValues values;
    public static pair[] pairs = {new pair("rus", "Русский_язык"),
            new pair("phys", "Физика"), new pair("math", "Математика"),
            new pair("en", "Английский_язык"), new pair("hist", "История")};

    public static String[] style_names = {"en", "hist", "math", "phys", "rus"};
    public static String[] style_orig = {"Английский язык", "История", "Математика", "Физика", "Русский язык"};


    public MyDB(Context context) {
        super(context, dbname, null, version);
        acontext = context;
    }

    public static void init(MyDB db, boolean External) {
        Log.i("INIT", "START");
        instance = db;
        myPath = "/data/data/com.ilnur.cards/databases/";

        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 102400 * 1024);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!checkdb()) {
            Log.i("Copy", "START");
            copyDb();
        } else {
            sqliteDb = instance.getReadableDatabase();
            Log.i("VERSION", String.valueOf(sqliteDb.getVersion()) + " " + String.valueOf(version));
            if (sqliteDb.getVersion() != version) {
                copyDb();
            }
        }
        /*try {
            add();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    // to get Root names

    public static User getUser() {
        SQLiteDatabase sqdb = instance.getReadableDatabase();
        Cursor cursor = sqdb.rawQuery("SELECT login, password, session_id FROM user", null);
        if (cursor.moveToFirst()) {
            User user = new User();
            user.setLogin(cursor.getString(0));
            user.setPassword(cursor.getString(1));
            user.setSession_id(cursor.getString(2));
            cursor.close();
            return user;
        } else {
            return new User(null, null, null);
        }
    }

    public static void updateUser(String login, String password, String session_id) {
        SQLiteDatabase sqdb = instance.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("login", login);
        values.put("password", password);
        values.put("session_id", session_id);
        int id = sqdb.update("user", values, "login = ?", new String[]{login});
        if (id == 0)
            sqdb.insertWithOnConflict("user", null, values, SQLiteDatabase.CONFLICT_IGNORE);
        values.clear();
    }

    public static void removeUser(String old_login) {
        SQLiteDatabase sqdb = instance.getWritableDatabase();
        sqdb.delete("user", "login = ?", new String[]{old_login});

    }

    public static String[] getCatNames(String predmet) {
        SQLiteDatabase sqdb = instance.getReadableDatabase();

        if (predmet.contains(" "))
            predmet = predmet.replace(" ", "_");
        Cursor cursor = sqdb.rawQuery("SELECT title FROM " + predmet + "_cat" + " WHERE parent_id = ?", new String[]{"0"});
        cursor.moveToFirst();

        ArrayList<String> list = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            //Log.i("CURSOR", cursor.getString(0));
            cursor.moveToNext();
        }
        String[] mas = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            mas[i] = list.get(i);
            //Log.i("mas ", mas[i]);
        }
        //String[] s = cursor.getString(0).split("\n");
        cursor.close();
        return mas;
    }

    public static boolean checkRevers(String parent, String title) {
        SQLiteDatabase sqdb = instance.getReadableDatabase();
        if (parent.contains(" "))
            parent = parent.replace(" ", "_");
        Cursor cursor = sqdb.rawQuery("SELECT reversible FROM " + parent + "_cat" + " WHERE title = ?", new String[]{title});
        cursor.moveToFirst();
        int reversible = cursor.getInt(0);
        cursor.close();
        return reversible == 1;
    }

    //sync - done
    public static void updateWatchCard(String parent, int card_id, int result, int old_result) {
        //SQLiteDatabase sqdb = instance.getReadableDatabase();
        // 0 -dont; 1-know
        if (old_result == 4) {
            result = 0;
        } else if (old_result != 0) {
            result = old_result + result;
        }
        if (parent.contains(" "))
            parent = parent.replace(" ", "_");
        ContentValues values = new ContentValues();
        values.put("result", result);
        long current_time = Calendar.getInstance().getTimeInMillis();
        values.put("result_stamp", current_time);
        sqliteDb.update(parent + "_card", values, "id = ?", new String[]{String.valueOf(card_id)});
        values.clear();
        if (MainActivity.logged) {
            try {
                //https://hist-ege.sdamgia.ru/api?protocolVersion=1&type=card_result&id=463&result=3&session=xxxxxxxx
                Connection.Response response = Jsoup.connect("https://" + getLink(parent) +
                        "-ege.sdamgia.ru/api?protocolVersion=1&type=card_result&id=" + card_id + "&result=" +
                        result + "&session=" + MainActivity.user.getSession_id()).ignoreContentType(true)
                        .method(Connection.Method.GET).execute();
                Log.i("RESPONSE", response.statusMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //sync - done
    public static void updateCard(String parent, int card_id, int result, int old_result) {
        //SQLiteDatabase sqdb = instance.getReadableDatabase();
        // 0 -dont; 1-know
        if (old_result == 4) {
            result = 4;
        } else if (old_result != 0) {
            result = old_result + result;
        }
        if (parent.contains(" "))
            parent = parent.replace(" ", "_");
        ContentValues values = new ContentValues();
        values.put("result", result);
        long current_time = Calendar.getInstance().getTimeInMillis();
        values.put("result_stamp", current_time);
        sqliteDb.update(parent + "_card", values, "id = ?", new String[]{String.valueOf(card_id)});
        values.clear();
        if (MainActivity.logged) {
            try {
                //https://hist-ege.sdamgia.ru/api?protocolVersion=1&type=card_result&id=463&result=3&session=xxxxxxxx
                Connection.Response response = Jsoup.connect("https://" + getLink(parent) +
                        "-ege.sdamgia.ru/api?protocolVersion=1&type=card_result&id=" + card_id + "&result=" +
                        result + "&session=" + MainActivity.user.getSession_id()).ignoreContentType(true)
                        .method(Connection.Method.GET).execute();
                Log.i("RESPONSE", response.statusMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //to get parent id
    public static int getParentId(String parent, String title) {
        SQLiteDatabase sqdb = instance.getReadableDatabase();
        if (parent.contains(" "))
            parent = parent.replace(" ", "_");
        Cursor cursor = sqdb.rawQuery("SELECT id FROM " + parent + "_cat" + " WHERE title = ?",
                new String[]{title});
        cursor.moveToFirst();
        int id = cursor.getInt(0);
        cursor.close();
        return id;
    }

    public static ArrayList<Card> getParentCardsWatch(String parent, String title, int id) {
        SQLiteDatabase sqdb = instance.getReadableDatabase();
        if (parent.contains(" "))
            parent = parent.replace(" ", "_");
        //get child categories ids
        Cursor head = sqdb.rawQuery("SELECT id, avers, revers, result, result_stamp FROM " + parent +
                "_card " + "WHERE category_id = ?", new String[]{String.valueOf(id)});
        ArrayList<Card> list = new ArrayList<>();
        Card tmp;
        head.moveToFirst();
        for (int i = 0; i < head.getCount(); i++) {
            tmp = new Card();
            tmp.setId(head.getInt(0));
            tmp.setAvers(head.getString(1));
            tmp.setRevers(head.getString(2));
            tmp.setResult(head.getInt(3));
            tmp.setResult_stamp(head.getString(4));
            list.add(tmp);
            head.moveToNext();
        }
        head.close();

        Cursor cursor = sqdb.rawQuery("SELECT id FROM " + parent + "_cat" + " WHERE parent_id = ?",
                new String[]{String.valueOf(id)});
        cursor.moveToFirst();
        Cursor cursor1;

        for (int i = 0; i < cursor.getCount(); i++) {
            int ind = cursor.getInt(0);
            cursor1 = sqdb.rawQuery("SELECT id, avers, revers, result, result_stamp FROM " + parent + "_card" + " WHERE category_id = ?",
                    new String[]{String.valueOf(ind)});
            cursor1.moveToFirst();

            for (int j = 0; j < cursor1.getCount(); j++) {
                tmp = new Card();
                tmp.setId(cursor1.getInt(0));
                tmp.setAvers(cursor1.getString(1));
                tmp.setRevers(cursor1.getString(2));
                tmp.setResult(cursor1.getInt(3));
                tmp.setResult_stamp(cursor1.getString(4));
                list.add(tmp);
                cursor1.moveToNext();
            }
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    public static ArrayList<Card> getChildCardsWatch(String parent, String title, int id) {
        SQLiteDatabase sqdb = instance.getReadableDatabase();
        if (parent.contains(" "))
            parent = parent.replace(" ", "_");
        Cursor cursor = sqdb.rawQuery("SELECT id, avers, revers, result, result_stamp FROM " + parent + "_card" + " WHERE category_id = ?",
                new String[]{String.valueOf(id)});
        cursor.moveToFirst();
        ArrayList<Card> list = new ArrayList<>();

        Card tmp;
        for (int i = 0; i < cursor.getCount(); i++) {
            tmp = new Card();
            tmp.setId(cursor.getInt(0));
            tmp.setAvers(cursor.getString(1));
            tmp.setRevers(cursor.getString(2));
            tmp.setResult(cursor.getInt(3));
            tmp.setResult_stamp(cursor.getString(4));
            list.add(tmp);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public static ArrayList<Card> getParentCards(String parent, int id) {
        SQLiteDatabase sqdb = instance.getReadableDatabase();
        if (parent.contains(" "))
            parent = parent.replace(" ", "_");

        Cursor head = sqdb.rawQuery("SELECT id, avers, revers, result, result_stamp FROM " + parent + "_card " +
                "WHERE category_id = ?", new String[]{String.valueOf(id)});
        head.moveToFirst();
        ArrayList<Card> list = new ArrayList<>();
        Card tmp;
        long curTime = Calendar.getInstance().getTimeInMillis();

        for (int i = 0; i < head.getCount(); i++) {
            tmp = new Card();
            tmp.setId(head.getInt(0));
            tmp.setAvers(head.getString(1));
            tmp.setRevers(head.getString(2));
            tmp.setResult(head.getInt(3));
            tmp.setResult_stamp(head.getString(4));
            switch (tmp.getResult()) {
                case 0:
                    list.add(tmp);
                    break;
                case 1:
                    if (TimeUnit.MILLISECONDS.toDays(curTime - tmp.getDate()) > 1)
                        list.add(tmp);
                    break;
                case 2:
                    if (TimeUnit.MILLISECONDS.toDays(curTime - tmp.getDate()) > 7)
                        list.add(tmp);
                    break;
                case 3:
                    if (TimeUnit.MILLISECONDS.toDays(curTime - tmp.getDate()) > 30)
                        list.add(tmp);
                    break;
                case 4:
                    if (TimeUnit.MILLISECONDS.toDays(curTime - tmp.getDate()) > 365)
                        list.add(tmp);
                    break;
            }
            head.moveToNext();
        }

        Cursor cursor = sqdb.rawQuery("SELECT id FROM " + parent + "_cat" + " WHERE parent_id = ?",
                new String[]{String.valueOf(id)});
        cursor.moveToFirst();
        Cursor cursor1 = null;


        for (int i = 0; i < cursor.getCount(); i++) {
            int ind = cursor.getInt(0);
            cursor1 = sqdb.rawQuery("SELECT id, avers, revers, result, result_stamp FROM " + parent + "_card" + " WHERE category_id = ?",
                    new String[]{String.valueOf(ind)});
            cursor1.moveToFirst();

            for (int j = 0; j < cursor1.getCount(); j++) {
                tmp = new Card();
                tmp.setId(cursor1.getInt(0));
                tmp.setAvers(cursor1.getString(1));
                tmp.setRevers(cursor1.getString(2));
                tmp.setResult(cursor1.getInt(3));
                tmp.setResult_stamp(cursor1.getString(4));

                switch (tmp.getResult()) {
                    case 0:
                        list.add(tmp);
                        break;
                    case 1:
                        if (TimeUnit.MILLISECONDS.toDays(curTime - tmp.getDate()) > 1)
                            list.add(tmp);
                        break;
                    case 2:
                        if (TimeUnit.MILLISECONDS.toDays(curTime - tmp.getDate()) > 7)
                            list.add(tmp);
                        break;
                    case 3:
                        if (TimeUnit.MILLISECONDS.toDays(curTime - tmp.getDate()) > 30)
                            list.add(tmp);
                        break;
                    case 4:
                        if (TimeUnit.MILLISECONDS.toDays(curTime - tmp.getDate()) > 365)
                            list.add(tmp);
                        break;
                }
                cursor1.moveToNext();
            }
            cursor.moveToNext();
        }

        cursor.moveToFirst();
        if (list.size() < 20) {
            head.moveToFirst();
            for (int i = 0; i < head.getCount(); i++) {
                tmp = new Card();
                tmp.setId(head.getInt(0));
                tmp.setAvers(head.getString(1));
                tmp.setRevers(head.getString(2));
                tmp.setResult(head.getInt(3));
                tmp.setResult_stamp(head.getString(4));
                switch (tmp.getResult()) {
                    case 0:
                        break;
                    case 1:
                        if (!list.contains(tmp))
                            list.add(tmp);
                        break;
                    case 2:
                        if (!list.contains(tmp))
                            list.add(tmp);
                        break;
                    case 3:
                        if (!list.contains(tmp))
                            list.add(tmp);
                        break;
                    case 4:
                        if (!list.contains(tmp))
                            list.add(tmp);
                        break;
                }
                head.moveToNext();
            }

            for (int i = 0; i < cursor.getCount(); i++) {
                cursor1.moveToFirst();

                for (int j = 0; j < cursor1.getCount(); j++) {
                    tmp = new Card();
                    tmp.setId(cursor1.getInt(0));
                    tmp.setAvers(cursor1.getString(1));
                    tmp.setRevers(cursor1.getString(2));
                    tmp.setResult(cursor1.getInt(3));
                    tmp.setResult_stamp(cursor1.getString(4));

                    switch (tmp.getResult()) {
                        case 0:
                            break;
                        case 1:
                            if (!list.contains(tmp))
                                list.add(tmp);
                            break;
                        case 2:
                            if (!list.contains(tmp))
                                list.add(tmp);
                            break;
                        case 3:
                            if (!list.contains(tmp))
                                list.add(tmp);
                            break;
                        case 4:
                            if (!list.contains(tmp))
                                list.add(tmp);
                            break;
                    }
                    cursor1.moveToNext();
                }
                cursor.moveToNext();
            }
        }
        head.close();
        cursor.close();

        Random rnd = new Random();
        for (int i = 0; i < list.size(); i++) {
            int index = rnd.nextInt(i + 1);
            tmp = list.get(index);
            list.set(index, list.get(i));
            list.set(i, tmp);
        }
        Card t;
        ArrayList<Card> finished = new ArrayList<>();

        for (int i = 1; i <= 20; i++) {
            if (i == list.size()) {
                break;
            }
            else {
                t = list.get(i);
                String temp = t.getAvers();
                //String
                finished.add(list.get(i));
            }
        }

        return finished;
    }

    public static ArrayList<Card> getChildCards(String parent, String title, int id) {
        SQLiteDatabase sqdb = instance.getReadableDatabase();
        if (parent.contains(" "))
            parent = parent.replace(" ", "_");
        Cursor cursor = sqdb.rawQuery("SELECT id, avers, revers, result, result_stamp FROM " + parent + "_card" + " WHERE category_id = ?",
                new String[]{String.valueOf(id)});
        cursor.moveToFirst();
        ArrayList<Card> list = new ArrayList<>();

        long curTime = Calendar.getInstance().getTimeInMillis();
        Card tmp;
        for (int i = 0; i < cursor.getCount(); i++) {
            if (list.size() == 20)
                break;
            tmp = new Card();
            tmp.setId(cursor.getInt(0));
            tmp.setAvers(cursor.getString(1));
            tmp.setRevers(cursor.getString(2));
            tmp.setResult(cursor.getInt(3));
            tmp.setResult_stamp(cursor.getString(4));
            Log.i("date ", "" + curTime + " " + tmp.getDate());
            switch (tmp.getResult()) {
                case 0:
                    list.add(tmp);
                    break;
                case 1:
                    if (TimeUnit.MILLISECONDS.toDays(curTime - tmp.getDate()) > 1)
                        list.add(tmp);
                    break;
                case 2:
                    if (TimeUnit.MILLISECONDS.toDays(curTime - tmp.getDate()) > 7)
                        list.add(tmp);
                    break;
                case 3:
                    if (TimeUnit.MILLISECONDS.toDays(curTime - tmp.getDate()) > 30)
                        list.add(tmp);
                    break;
                case 4:
                    if (TimeUnit.MILLISECONDS.toDays(curTime - tmp.getDate()) > 365)
                        list.add(tmp);
                    break;
            }
            cursor.moveToNext();
        }

        cursor.moveToFirst();
        Log.i("CURScount", String.valueOf(cursor.getCount()));
        if (list.size() < 20) {
            for (int i = 0; i < cursor.getCount(); i++) {
                if (list.size() == 20)
                    break;
                if (list.size() == cursor.getCount())
                    break;
                tmp = new Card();
                tmp.setId(cursor.getInt(0));
                tmp.setAvers(cursor.getString(1));
                tmp.setRevers(cursor.getString(2));
                tmp.setResult(cursor.getInt(3));
                tmp.setResult_stamp(cursor.getString(4));

                switch (tmp.getResult()) {
                    case 0:
                        break;
                    case 1:
                        if (!list.contains(tmp))
                            list.add(tmp);
                        break;
                    case 2:
                        if (!list.contains(tmp))
                            list.add(tmp);
                        break;
                    case 3:
                        if (!list.contains(tmp))
                            list.add(tmp);
                        break;
                    case 4:
                        if (!list.contains(tmp))
                            list.add(tmp);
                        break;
                }
                cursor.moveToNext();
            }
        }

        cursor.close();
        Random rnd = new Random();
        for (int i = 0; i < list.size(); i++) {
            int index = rnd.nextInt(i + 1);
            tmp = list.get(index);
            list.set(index, list.get(i));
            list.set(i, tmp);
        }
        //Collections.shuffle(list);
        return list;
    }

    public static ArrayList<Category> getSubCatNames(String parent, String title) {
        SQLiteDatabase sqdb = instance.getReadableDatabase();
        if (parent.contains(" "))
            parent = parent.replace(" ", "_");
        Cursor cursor = sqdb.rawQuery("SELECT id FROM " + parent + "_cat" + " WHERE title = ?", new String[]{title});
        cursor.moveToFirst();
        int id = cursor.getInt(0);
        //Log.i("id", String.valueOf(id));
        cursor.close();
        //Log.i("id", parent);

        Cursor cursor1 = sqdb.rawQuery("SELECT id, title, reversible, \"order\" FROM " + parent + "_cat" + " WHERE parent_id = ?", new String[]{String.valueOf(id)});
        cursor1.moveToFirst();
        ArrayList<Category> list = new ArrayList<>();
        Category cat;
        while (!cursor1.isAfterLast()) {
            cat = new Category();
            cat.setId(cursor1.getInt(0));
            cat.setTitle(cursor1.getString(1));
            //Log.i("SUb", cat.getTitle());
            cat.setReversible(cursor1.getInt(2));
            cat.setOrder(cursor1.getInt(3));
            cat.setParent_id(id);
            list.add(cat);
            cursor1.moveToNext();
        }
        cursor1.close();
        return InsertionSort(list);
    }

    public static void updateStyle(String name, String style) {
        SQLiteDatabase sqdb = instance.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("data", style);
        int id = sqdb.update("style", values, "name = ?", new String[]{name});
        if (id == 0)
            sqdb.insertWithOnConflict("style", null, values, SQLiteDatabase.CONFLICT_IGNORE);
        values.clear();
        values = null;
    }

    public static String getStyle(String name) {
        SQLiteDatabase sqdb = instance.getReadableDatabase();
        if (name == null) {
            name = "1";
        } else {
            for (int i = 0; i < style_orig.length; i++) {
                if (style_orig[i].equals(name)) {
                    name = style_names[i];
                }
            }
        }
        Cursor cursor = sqdb.rawQuery("SELECT data FROM style WHERE name = ?", new String[]{name});
        cursor.moveToFirst();
        String i = cursor.getString(0);
        //Log.i("STRYLE",i);
        cursor.close();
        return i;
    }

    private static String getLink(String subj) {
        String ret = null;
        for (pair p : pairs) {
            if (p.subj_db.equals(subj)) {
                ret = p.link;
                break;
            }
        }
        return ret;
    }

    private static ArrayList<Category> InsertionSort(ArrayList<Category> list) {
        Comparator<Category> comparator = (o1, o2) -> {
            if (o1.getOrder() == 0 && o2.getOrder() == 0) {
                if (o1.getTitle().length() > o2.getTitle().length()) return 1;
                if (o1.getTitle().length() < o2.getTitle().length()) return -1;
            }
            String s1 = String.format(Locale.getDefault(), "%03d%s", o1.getOrder(), o1.getTitle());
            String s2 = String.format(Locale.getDefault(), "%03d%s", o2.getOrder(), o2.getTitle());
            return s1.compareTo(s2);
        };
        Category key;
        int i;
        //insertion sort itself
        for (int j = 1; j < list.size(); j++) {
            key = list.get(j);
            i = j - 1;
            while (i >= 0) {
                if (comparator.compare(key, list.get(i)) > 0) {
                    break;
                }
                list.set(i + 1, list.get(i));
                i--;
            }
            list.set(i + 1, key);
        }
        return list;
    }

    private static class pair {
        private String link;
        private String subj_db;

        public pair(String link, String subj_db) {
            this.link = link;
            this.subj_db = subj_db;
        }
    }

    //when you logged in & push sync button || after adding
    public static void syncSubj() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String link = "https://ege.sdamgia.ru/mobile_cards/";
                    String style = Jsoup.connect("https://ege.sdamgia.ru/mobile_cards/style.css?v="+String.valueOf(new Random().nextInt(9999999)))
                            .ignoreContentType(true).get().select("body").text();

                    try {
                        if (!getStyle(null).equals(style))
                            updateStyle("1", style);

                    } catch (IndexOutOfBoundsException e) {
                        updateStyle("1", style);
                    }

                    for (String s: style_names){
                        style = Jsoup.connect(link+"style_"+s+".css?v="+String.valueOf(new Random().nextInt(9999999))).ignoreContentType(true)
                                .get().select("body").text();
                        if (!style.equals(""))
                            updateStyle(s, style);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        MainActivity.syncsub = true;
        for (pair p : pairs) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String cards;
                    String cats = null;
                    Stupid cat;
                    Category[] mas;
                    Category temp;
                    StupCard tmp;
                    Connection.Response resp = null;
                    BufferedInputStream bis;
                    InputStreamReader isr;
                    JsonReader reader;
                    ContentValues values = new ContentValues();
                    SQLiteDatabase sqdb = instance.getReadableDatabase();
                    Cursor cursor = null;

                    cursor = sqdb.rawQuery("SELECT added_with_log FROM subj WHERE name = ?", new String[]{p.subj_db});
                    cursor.moveToFirst();
                    if (cursor.getInt(0) == 0) {
                        Log.i("Sync", p.subj_db);
                        cards = "https://" + p.link + "-ege.sdamgia.ru/api?protocolVersion=1&type=card&category_id=";
                        try {
                            cats = Jsoup.connect("https://" + p.link + "-ege.sdamgia.ru/api?protocolVersion=1&type=card_cat")
                                    .ignoreContentType(true).get().select("body").text();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        cat = new Gson().fromJson(cats, Stupid.class);
                        mas = cat.getData();

                        //Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                        for (int i = 0; i < mas.length; i++) {
                            temp = mas[i];
                            if (MainActivity.logged) {
                                Log.i("adding", "with logged");
                            }
                            //Log.i("mas[i]", temp.getTitle());
                            if (MainActivity.logged) {
                                try {
                                    resp = Jsoup.connect(cards + temp.getId() + "&session=" + MainActivity.user.getSession_id()).ignoreContentType(true)
                                            .maxBodySize(0).timeout(2000000).method(Connection.Method.GET).execute();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    resp = Jsoup.connect(cards + temp.getId()).ignoreContentType(true)
                                            .maxBodySize(0).timeout(2000000).method(Connection.Method.GET).execute();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            bis = resp.bodyStream();
                            isr = new InputStreamReader(bis);
                            reader = new JsonReader(isr);
                            reader.setLenient(true);
            /*resp = Jsoup.connect(cards+temp.getId()).ignoreContentType(true)
                    .maxBodySize(0).timeout(20000000).get().select("body").html();*/
                            //Log.i("data", data);
                            tmp = new Gson().fromJson(reader, StupCard.class);
                            updCat(temp, p.subj_db);
                            if (tmp.getData() != null) {
                                for (Card s : tmp.getData()) {
                                    updCard(s, p.subj_db);
                                }
                            }
                        }
                        Log.i(p.subj_db, "Added");
                        values.put("name", p.subj_db);
                        values.put("added", 1);
                        if (MainActivity.logged)
                            values.put("added_with_log", 1);
                        sqliteDb.update("subj", values, "name = ?", new String[]{p.subj_db});
                        values.clear();
                    }

                    cursor.close();
                }
            }).start();

        }
    }


    public static void add() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //String q = new Random(9999).toString();
                    String link = "https://ege.sdamgia.ru/mobile_cards/";
                    //Log.i("rand", new Random(9999999).toString());
                    String style = Jsoup.connect("https://ege.sdamgia.ru/mobile_cards/style.css?v="+String.valueOf(new Random().nextInt(9999999)))
                            .ignoreContentType(true).get().select("body").text();
                    Log.i("STYLE","-- "+ style);
                    try {
                        //Log.i("STYLE", style);
                        if (!getStyle(null).equals(style))
                            updateStyle("1", style);

                    } catch (IndexOutOfBoundsException e) {
                        updateStyle("1", style);
                    }

                    for (String s: style_names){
                        style = Jsoup.connect(link+"style_"+s+".css?v="+String.valueOf(new Random().nextInt(9999999))).ignoreContentType(true)
                                .get().select("body").text();
                        if (!style.equals(""))
                            updateStyle(s, style);
                        Log.i(s, "-- "+ style);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        MainActivity.addsub = true;
        for (pair p : pairs) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String cards;
                    String cats = null;
                    Stupid cat;
                    Category[] mas;
                    Category temp;
                    StupCard tmp;
                    Connection.Response resp = null;
                    BufferedInputStream bis;
                    InputStreamReader isr;
                    JsonReader reader;
                    ContentValues values = new ContentValues();
                    SQLiteDatabase sqdb = instance.getReadableDatabase();
                    Cursor cursor = null;
                    boolean log;

                    Log.i("adding", p.subj_db);
                    cursor = sqdb.rawQuery("SELECT added FROM subj WHERE name = ?", new String[]{p.subj_db});
                    cursor.moveToFirst();
                    if (cursor.getInt(0) == 0) {
                        log = false;
                        cards = "https://" + p.link + "-ege.sdamgia.ru/api?protocolVersion=1&type=card&category_id=";
                        try {
                            cats = Jsoup.connect("https://" + p.link + "-ege.sdamgia.ru/api?protocolVersion=1&type=card_cat")
                                    .ignoreContentType(true).get().select("body").text();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        cat = new Gson().fromJson(cats, Stupid.class);
                        mas = cat.getData();
                        if (!MainActivity.logged)
                            log = false;

                        //Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                        for (int i = 0; i < mas.length; i++) {
                            temp = mas[i];
                            if (MainActivity.logged) {
                                Log.i("adding", "with logged");
                                log = true;
                            } else {
                                log = false;
                            }
                            //Log.i("mas[i]", temp.getTitle());
                            if (MainActivity.logged) {
                                try {
                                    resp = Jsoup.connect(cards + temp.getId() + "&session=" + MainActivity.user.getSession_id()).ignoreContentType(true)
                                            .maxBodySize(0).timeout(2000000).method(Connection.Method.GET).execute();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    resp = Jsoup.connect(cards + temp.getId()).ignoreContentType(true)
                                            .maxBodySize(0).timeout(2000000).method(Connection.Method.GET).execute();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            bis = resp.bodyStream();
                            isr = new InputStreamReader(bis);
                            reader = new JsonReader(isr);
                            reader.setLenient(true);
            /*resp = Jsoup.connect(cards+temp.getId()).ignoreContentType(true)
                    .maxBodySize(0).timeout(20000000).get().select("body").html();*/
                            //Log.i("data", data);
                            tmp = new Gson().fromJson(reader, StupCard.class);
                            updCat(temp, p.subj_db);
                            if (tmp.getData() != null) {
                                for (Card s : tmp.getData()) {
                                    updCard(s, p.subj_db);
                                }
                            }
                        }
                        Log.i(p.subj_db, "Added");
                        values.put("name", p.subj_db);
                        values.put("added", 1);
                        if (log && MainActivity.logged)
                            values.put("added_with_log", 1);

                        sqliteDb.update("subj", values, "name = ?", new String[]{p.subj_db});
                        values.clear();
                    }

                    cursor.close();
                }
            }).start();


        }
    }

    public static boolean isSubjAdded(String parent) {
        SQLiteDatabase sqdb = instance.getReadableDatabase();
        if (parent.contains(" "))
            parent = parent.replace(" ", "_");
        Cursor cursor = sqdb.rawQuery("SELECT added FROM subj" + " WHERE name = ?", new String[]{parent});
        cursor.moveToFirst();
        int id = cursor.getInt(0);
        cursor.close();
        if (id == 1)
            return true;
        else
            return false;
    }

    public static void updCard(Card card, String table) {
        ContentValues values = new ContentValues();
        values.put("id", card.getId());
        values.put("avers", card.getAvers());

        values.put("revers", card.getRevers());
        values.put("category_id", card.getCategory_id());

        values.put("result", card.getResult());
        values.put("result_stamp", card.getResult_stamp());
        int id = sqliteDb.update(table + "_card", values, "id = ?", new String[]{String.valueOf(card.getId())});
        if (id == 0) {
            sqliteDb.insertWithOnConflict(table + "_card", null, values, SQLiteDatabase.CONFLICT_IGNORE);
        }
        //Log.i("Card", String.valueOf(card.getId())+" added");
        values.clear();
    }

    public static void updCat(Category cat, String table) {
        ContentValues values = new ContentValues();
        values.put("id", cat.getId());
        values.put("\"order\"", cat.getOrder());
        values.put("parent_id", cat.getParent_id());
        values.put("reversible", cat.getReversible());
        values.put("title", cat.getTitle());

        int id = sqliteDb.update(table + "_cat", values, "id = ?", new String[]{String.valueOf(cat.getId())});
        if (id == 0)
            sqliteDb.insertWithOnConflict(table + "_cat", null, values, SQLiteDatabase.CONFLICT_IGNORE);
        //Log.i("Cat", String.valueOf(cat.getId())+" added");
        values.clear();
    }

    private static void copyDb() {
        InputStream inputStream = null;
        try {
            inputStream = acontext.getAssets().open("database/" + dbname);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String outputfile = myPath + dbname;
        File f = new File(myPath);
        if (!f.exists()) {
            f.mkdir();
        }
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(outputfile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] buffer = new byte[4096];
        int length;
        try {
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sqliteDb = instance.getWritableDatabase();
        Log.i("Copy", "FINISH");
    }

    private static boolean checkdb() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(myPath + dbname, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null;
    }

    @Override
    public synchronized void close() {
        if (sqliteDb != null)
            sqliteDb.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
