package com.example.movietickets.controller;

import com.example.movietickets.interf.OnItemClickedListener;
import com.example.movietickets.model.MovieObject;
import com.example.movietickets.utils.Utils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class ItemMovieController {

    @FXML
    ImageView itemPosterMovie;

    @FXML
    Label itemTitleMovie;

    private MovieObject.Movie movie;
    public OnItemClickedListener onItemClickedListener;

    public void setData(MovieObject.Movie item,OnItemClickedListener onItemClickedListenerTemp){
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//            }
//        });
        movie = item;
        onItemClickedListener = onItemClickedListenerTemp;
        itemPosterMovie.setImage(new Image(Utils.path_image_domain+item.getPoster_path()));
        itemTitleMovie.setText(item.getTitle());
    }


    //SET EVENT CHO MỖI KHI NHẤN VÀO ITEM
    @FXML
    public void onClick(MouseEvent mouseEvent){
        onItemClickedListener.onClicked(movie);
    }

}
