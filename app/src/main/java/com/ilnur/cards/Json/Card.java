package com.ilnur.cards.Json;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Card {
    @SerializedName("id")
    private int id;
    @SerializedName("avers")
    private String avers;
    @SerializedName("revers")
    private String revers;
    @SerializedName("category_id")
    private int category_id;
    @SerializedName("result")
    private int result;
    @SerializedName("result_stamp")
    private String result_stamp;

    public Card(){}
    public Card(int id,String avers,String revers, int category_id, int result, String result_stamp){
        this.id = id;
        this.avers = avers;
        this.result = result;
        this.revers = revers;
        this.category_id = category_id;
        this.result_stamp = result_stamp;
    }
    public long getDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        long date = 0;
        try {
            date = sdf.parse(result_stamp).getTime();
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        } catch (NullPointerException n){
            n.printStackTrace();
            return date;
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAvers(String avers) {
        this.avers = avers;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public void setResult_stamp(String result_stamp) {
        this.result_stamp = result_stamp;
    }

    public void setRevers(String revers) {
        this.revers = revers;
    }

    public int getId() {
        return id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public int getResult() {
        return result;
    }

    public String getAvers() {
        return avers;
    }

    public String getResult_stamp() {
        return result_stamp;
    }

    public String getRevers() {
        return revers;
    }
}
