package com.example.movietickets.model;

import com.example.movietickets.utils.Utils;

public class TicketType {

    String type;
    int price;

    public TicketType() {
        this.type = Utils.TICKER_NORMAL;
        this.price = Utils.PRICE_NORMAL;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        if(type.equals(Utils.TICKER_NORMAL)){
            this.price = Utils.PRICE_NORMAL;
        }
        else {
            this.price = Utils.PRICE_PRIME;
        }
    }

    public int getPrice() {
        return price;
    }

}
