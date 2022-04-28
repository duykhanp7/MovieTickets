package com.example.movietickets.model;

import java.util.ArrayList;
import java.util.List;

public class Bill {

    String idCustomer; //ID CỦA KHÁCH HÀNG SỞ HỮU ĐƠN HÀNG NÀY
    int discount;       //TIỀN GIẢM GIÁ ĐƯỢC ĐỔI TỪ ĐIỂM TÍCH LŨY CÁC LẦN MUA TRƯỚC
    int totalPrice;     //TỔNG TIỀN CHO VÉ XEM PHIM NÀY
    MovieObject.Movie movie;    //BỘ PHIM ĐANG MUA
    List<Seat> seats;       // DANH SÁCH CÁC CHỖ NGỒI ĐÃ CHỌN
    String timeSlot;        //KHUNG GIỜ CHIẾU PHIM
    String cinemaName;      //TÊN RẠP CHIẾU
    String dateBought;      //NGÀY MUA VÉ
    String movieName;       //TÊN PHIM
    String seatsText;       //DANH SÁCH GHẾ NGỒI (DẠNG TEXT)

    //CONSTRUCTOR
    public Bill(){
        this.discount = 0;
        this.totalPrice = 0;
        this.movie = new MovieObject.Movie();
        this.seats = new ArrayList<>();
        this.timeSlot = "";
        this.cinemaName = "";
        this.dateBought = "";
    }

    ////ID_CUSTOMER,MOVIE_NAME,CINEMA_NAME,TIME_SLOT,SEATS,DATE_BOUGHT,PRICE,DISCOUNT
    public Bill(String idCustomer,String movieNameText, String cinemaName, String timeSlot, String seatsText, String dateBought, String price, String discount) {
        this.idCustomer = idCustomer;
        this.movieName = movieNameText;
        this.cinemaName = cinemaName;
        this.timeSlot = timeSlot;
        this.seatsText = seatsText;
        this.dateBought = dateBought;
        this.totalPrice = Integer.parseInt(price);
        this.discount = Integer.parseInt(discount);
    }


    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getSeatsText() {
        return seatsText;
    }

    public void setSeatsText(String seatsText) {
        this.seatsText = seatsText;
    }

    public String getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(String idCustomer) {
        this.idCustomer = idCustomer;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public MovieObject.Movie getMovie() {
        return movie;
    }

    public void setMovie(MovieObject.Movie movie) {
        this.movie = movie;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public void calculateTotalPrice(){
        this.totalPrice -= this.discount;
    }

    public void plusPrice(int price){
        this.totalPrice += price;
    }

    public void addSeat(Seat seat){
        this.seats.add(seat);
        plusPrice(seat.getTicketType().getPrice());
    }

    public void removeSeat(Seat seat){
        this.seats.remove(seat);
        subtractPrice(seat.getTicketType().getPrice());
    }

    public void subtractPrice(int price){
        this.totalPrice -= price;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }

    public String getDateBought() {
        return dateBought;
    }

    public void setDateBought(String dateBought) {
        this.dateBought = dateBought;
    }
}
