package com.example.movietickets.model;

import com.example.movietickets.SeatLayoutItemController;
import javafx.scene.layout.AnchorPane;

import java.util.HashMap;
import java.util.Map;

public class CinemaRoom {

    public String idRoom, nameRoom; //ID VÀ TÊN PHÒNG CHIẾU
    public MovieObject.Movie movie; //PHIM ĐANG CHIẾU TẠI PHÒNG CHIẾU NÀY
    public String idMovie;      //ID CỦA BỘ PHIM

    //THAM SỐ MAP
    // 1 : ID CỦA PHIM
    // 2 : KHUNG GIỜ CHIẾU PHIM
    // 3 : DANH SÁCH GHẾ CỦA KHUNG GIỜ CHIẾU PHIM ĐÓ
    public Map<String,Map<String, Map<String,Seat>>> mapParentStateSeat = new HashMap<>();
//    public Map<String,Map<String,Map<String, AnchorPane>>> mapParentLayout = new HashMap<>();
//    public Map<String,Map<String,Map<String, SeatLayoutItemController>>> mapParentController = new HashMap<>();


    //CONSTRUCTOR
    public CinemaRoom(String id, String nameRoom){
        idMovie = "";
        this.idRoom = id;
        this.nameRoom = nameRoom;
    }

    //ĐƯA PHIM VÀO PHÒNG CHIẾU NÀY
    public void setMovie(MovieObject.Movie movie){
        this.movie = movie;
    }

    public void setMovieId(String id){
        this.idMovie = id;
    }
}
