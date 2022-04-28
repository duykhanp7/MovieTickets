package com.example.movietickets.model;

import com.example.movietickets.utils.Utils;

public class Seat {

    String title;//TÊN GHẾ NGỒI
    boolean selected;//TRẠNG THÁI ĐƯỢC CHỌN HAY CHƯA
    String type;//LOẠI GHẾ (BÌNH THƯỜNG HOẶC CAO CẤP)
    TicketType ticketType;//VÉ
    boolean enable;//TRẠNG THÁI GHẾ ĐÃ ENABLE HAY CHƯA

    public Seat(){
        title = "";
        selected = false;
        type = Utils.TYPE_NORMAL;
        ticketType = new TicketType();
        enable = true;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public void setEnable(boolean enable){
        this.enable = enable;
    }

    public boolean isEnable(){return enable;}

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

}
