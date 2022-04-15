package com.example.movietickets.controller;

import com.example.movietickets.interf.OnItemClickedListener;
import com.example.movietickets.model.MovieObject;
import com.example.movietickets.utils.Utils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class ItemMovieController {

    @FXML ImageView itemPosterMovie;

    @FXML Label itemTitleMovie;

    @FXML AnchorPane itemLayout;

    @FXML VBox vBoxItem;

    @FXML Button buyTicketButton;


    private MovieObject.Movie movie;
    public OnItemClickedListener onItemClickedListener;

    public void setData(MovieObject.Movie item,OnItemClickedListener onItemClickedListenerTemp){
        movie = item;
        onItemClickedListener = onItemClickedListenerTemp;
        if(item.getPoster_path() != null){
            itemPosterMovie.setImage(new Image(Utils.path_image_domain.trim()+item.getPoster_path().trim()));
        }
        else{
            //CREATE A IMAGE WITH BACKGROUND COLOR IS BROWN TO SET WHEN POSTER PATH IS NULL
        }
        itemTitleMovie.setText(item.getTitle().trim());
    }


    //SET EVENT CHO MỖI KHI NHẤN VÀO ITEM
    @FXML
    public void onClick(MouseEvent mouseEvent){
        onItemClickedListener.onClicked(movie);
    }

    //HIỂN THỊ TOOLTIP SHOW TÊN CỦA BỘ PHIM ĐƯỢC HOVERED
    @FXML
    public void mouseHovered(MouseEvent mouseEvent){
        Tooltip tooltip = new Tooltip(movie.getTitle());
        Tooltip.install(itemLayout,tooltip);
        System.out.println("MOUSE HOVEREDDDDDDDDDDDDDDDDD");
    }

    @FXML
    public void mouseGone(MouseEvent mouseEvent){
        System.out.println("MOUSE GONEEEEEEEEEEEEEEEEEEEE");
    }

    //THAY ĐỔI BACKGROUND BUTTON "BUY TICKETS" KHI ĐƯỢC HOVERED VÀ NHẤN 2 MÀU KHÁC NHAU
    //HOVERED : MÀU NHẠT HƠN MÀU BACKGOUND DEFAULT
    //CLICKED : MÀU ĐẬM HƠN BACKGROUND DEFAULT
    //HOVERED
    @FXML
    public void hoveredOnButtonBuyTickets(MouseEvent mouseEvent){
        buyTicketButton.setStyle("-fx-background-color: #e8c78e");
    }

    //CLICKED
    @FXML
    public void clickedOnButtonBuyTickets(MouseEvent mouseEvent){
        buyTicketButton.setStyle("-fx-background-color: #cc7604");
    }

    //EXIT
    @FXML
    public void exitOnButtonBuyTickets(MouseEvent mouseEvent){
        buyTicketButton.setStyle("-fx-background-color: #e6ae5c");
    }

}
