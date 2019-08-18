package com.ilnur.cards;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("login")
    private String login;
    @SerializedName("password")
    private String password;
    @SerializedName("session_id")
    private String session_id;

    public User(){}

    public User(String login, String password, String session_id){
        this.login = login;
        this.password = password;
        this.session_id = session_id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getSession_id() {
        return session_id;
    }
}
