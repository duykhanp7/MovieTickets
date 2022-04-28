package com.example.movietickets.controller;

import com.example.movietickets.interf.OnItemClickedListener;
import com.example.movietickets.model.MovieObject;
import com.example.movietickets.utils.Utils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;





//CONTROLLER CHO MỖI ITEM PHIM
public class ItemMovieController {

    //IMAGEVIEW HIỂN THỊ POSTER CỦA PHIM
    @FXML ImageView itemPosterMovie;

    //LABEL HIỂN THỊ TÊN PHIM
    @FXML Label itemTitleMovie;

    //LAYOUT CHÍNH CHỨA HÌNH ẢNH VÀ TÊN PHIM
    @FXML AnchorPane itemLayout;

    //BUTTON MUA VÉ
    @FXML Button buyTicketButton;

    private MovieObject.Movie movie;

    //OBJECT BẮT SỰ KIỆN MỖI KHI NHẤN VÀO TỪNG BỘ PHIM
    public OnItemClickedListener onItemClickedListener;

    //ĐEM DỮ LIỆU ĐẨY LÊN VIEW - SET HÌNH ẢNH VÀ TÊN PHIM
    public void setData(MovieObject.Movie item,OnItemClickedListener onItemClickedListenerTemp){
        movie = item;
        onItemClickedListener = onItemClickedListenerTemp;
        //CHO CHẠY LUỒNG RIÊNG TRÁNH ẢNH HƯỞNG ĐẾN LUỒNG CHÍNH
        Platform.runLater(() -> itemTitleMovie.setText(item.getTitle().trim()));
        //LẤY ẢNH TỬ URL ĐẨY VÀO IMAGE VIEW
        new Thread(() -> {
            if(item.getPoster_path() != null){
                itemPosterMovie.setImage(new Image(Utils.path_image_domain.trim()+item.getPoster_path().trim()));
            }
        }).start();
    }


    //SET EVENT CHO MỖI KHI NHẤN VÀO BỘ PHIM
    @FXML
    public void onClick(MouseEvent mouseEvent){
        onItemClickedListener.onClicked(movie);
    }


    //HIỂN THỊ TOOLTIP SHOW TÊN CỦA BỘ PHIM ĐƯỢC HOVERED
    @FXML
    public void mouseHovered(MouseEvent mouseEvent){
        Tooltip tooltip = new Tooltip(movie.getTitle());
        Tooltip.install(itemLayout,tooltip);
    }

    //THAY ĐỔI BACKGROUND BUTTON "BUY TICKETS" KHI ĐƯỢC HOVERED VÀ NHẤN 2 MÀU KHÁC NHAU
    //HOVERED : MÀU NHẠT HƠN MÀU BACKGOUND DEFAULT
    //CLICKED : MÀU ĐẬM HƠN BACKGROUND DEFAULT
    //HOVERED1
    //SET EVENT CHO BUTTON MUA VÉ
    @FXML
    public void onButtonBuyTicketClicked(MouseEvent mouseEvent){
        onItemClickedListener.onButtonBuyClicked(movie);
        buyTicketButton.setStyle("-fx-background-color: #fa9805");
    }
    @FXML
    public void hoveredOnButtonBuyTickets(MouseEvent mouseEvent){
        buyTicketButton.setStyle("-fx-background-color: #d98e48");
    }

    //THAY ĐỔI BACKGROUND CỦA BUTTON MỖI KHI HOVERED TRÊN BỘ PHIM ĐÓ MẤT ĐI
    @FXML
    public void exitOnButtonBuyTickets(MouseEvent mouseEvent){
        buyTicketButton.setStyle("-fx-background-color: #d98609");
    }


}
