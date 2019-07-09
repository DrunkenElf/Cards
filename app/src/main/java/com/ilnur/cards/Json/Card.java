package com.ilnur.cards.Json;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Card implements Parcelable {
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

    private Card(Parcel in){
        id = in.readInt();
        avers = in.readString();
        revers = in.readString();
        result_stamp = in.readString();
        category_id = in.readInt();
        result = in.readInt();
        //dest.writeInt(id);
        //        dest.writeString(avers);
        //        dest.writeString(revers);
        //        dest.writeString(result_stamp);
        //        dest.writeInt(category_id);
        //        dest.writeInt(result);
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //int id,String avers,String revers, int category_id, int result, String result_stamp
        dest.writeInt(id);
        dest.writeString(avers);
        dest.writeString(revers);
        dest.writeString(result_stamp);
        dest.writeInt(category_id);
        dest.writeInt(result);
    }

    public static final Parcelable.Creator<Card> CREATOR = new Parcelable.Creator<Card>() {
        public Card createFromParcel(Parcel in) {
            return new Card(in);
        }

        public Card[] newArray(int size) {
            return new Card[size];
        }
    };
}
