package com.ilnur.cards.Json;

import com.google.gson.annotations.SerializedName;
import com.ilnur.cards.Json.Card;

public class StupCard {
    @SerializedName("data")
    private Card[] data;

    public StupCard(Card[] data){
        this.data = data;
    }

    public void setData(Card[] data) {
        this.data = data;
    }

    public Card[] getData() {
        return data;
    }
}
