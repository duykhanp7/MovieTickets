package com.example.movietickets;

import com.example.movietickets.MainController;
import com.example.movietickets.controller.ItemMovieController;
import com.example.movietickets.interf.OnItemClickedListener;
import com.example.movietickets.model.MovieObject;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MovieSoldRecentlyController implements Initializable {

    @FXML GridPane gridLayoutMoviesSoldRecently;

    int column = 0;
    int row = 1;

    List<MovieObject.Movie> movies = new ArrayList<>();
    OnItemClickedListener onItemClickedListener;
    FXMLLoader loader;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setMovies(List<MovieObject.Movie> items){
        if(items != null){
            movies.addAll(items);
            InitGridView(items);
        }
    }

    public void setOnItemClickedListener(OnItemClickedListener itemClickedListener){
        this.onItemClickedListener = itemClickedListener;
    }

    //KHỞI TẠO GRID VIEW DANH SÁCH VÉ PHIM BÁN GẦN ĐÂY
    public void InitGridView(List<MovieObject.Movie> a){
        for (MovieObject.Movie item : a) {
                addNewItem(item);
//            AnchorPane anchorPane = null;
//            try {
//                anchorPane = loader.load();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            //ĐƯA DỮ LIỆU VÀO VIEW
//            ItemMovieController itemController = loader.getController();
//            itemController.setData(item, onItemClickedListener);
//
//            //LAYOUT ITEM
//            AnchorPane finalAnchorPane = anchorPane;
//
//            //CHO CHẠY TRONG HÀM RUN LATER ĐỂ TRÁNH CRASH LUỒNG CHÍNH
//            Platform.runLater(new Runnable() {
//                @Override
//                public void run() {
//
//                    //NẾU ĐỦ 6 ITEMS TRÊN 1 HÀNG THÌ
//                    //CHUYỂN SANG HÀNG TIẾP THEO
//                    if (column == 5) {
//                        column = 0;
//                        row++;
//                    }
//
//                    gridLayoutMoviesSoldRecently.add(finalAnchorPane, column++, row); //(child,column,row)
//                }
//            });
//            //set grid width
//            gridLayoutMoviesSoldRecently.setMinWidth(Region.USE_COMPUTED_SIZE);
//            gridLayoutMoviesSoldRecently.setPrefWidth(Region.USE_COMPUTED_SIZE);
//            gridLayoutMoviesSoldRecently.setMaxWidth(Region.USE_PREF_SIZE);
//
//            //set grid height
//            gridLayoutMoviesSoldRecently.setMinHeight(Region.USE_COMPUTED_SIZE);
//            gridLayoutMoviesSoldRecently.setPrefHeight(Region.USE_COMPUTED_SIZE);
//            gridLayoutMoviesSoldRecently.setMaxHeight(Region.USE_PREF_SIZE);
//
//            GridPane.setMargin(anchorPane, new Insets(10));
        }
    }

    public synchronized void addNewItem(MovieObject.Movie item){

        loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("view/item_movie.fxml"));

        AnchorPane anchorPane = null;

        try {
            anchorPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //ĐƯA DỮ LIỆU VÀO VIEW
        ItemMovieController itemController = loader.getController();

        new Thread(new Runnable() {
            @Override
            public void run() {
                itemController.setData(item, onItemClickedListener);
            }
        }).start();

        //LAYOUT ITEM
        AnchorPane finalAnchorPane = anchorPane;

        //CHO CHẠY TRONG HÀM RUN LATER ĐỂ TRÁNH CRASH LUỒNG CHÍNH
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                //NẾU ĐỦ 6 ITEMS TRÊN 1 HÀNG THÌ
                //CHUYỂN SANG HÀNG TIẾP THEO
                if (column == 5) {
                    column = 0;
                    row++;
                }

                //set grid width
                gridLayoutMoviesSoldRecently.add(finalAnchorPane, column++, row); //(child,column,row)
                gridLayoutMoviesSoldRecently.setMinWidth(Region.USE_COMPUTED_SIZE);
                gridLayoutMoviesSoldRecently.setPrefWidth(Region.USE_COMPUTED_SIZE);
                gridLayoutMoviesSoldRecently.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                gridLayoutMoviesSoldRecently.setMinHeight(Region.USE_COMPUTED_SIZE);
                gridLayoutMoviesSoldRecently.setPrefHeight(Region.USE_COMPUTED_SIZE);
                gridLayoutMoviesSoldRecently.setMaxHeight(Region.USE_PREF_SIZE);

            }
        });
        GridPane.setMargin(anchorPane, new Insets(10));
    }

}
