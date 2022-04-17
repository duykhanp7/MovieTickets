package com.example.movietickets;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SoldTicketsController implements Initializable {


    @FXML Label textNameMovie;

    @FXML GridPane gridLayoutAllSeat;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        InitializeLayoutSeats();
    }


    //initialize layout seats
    public void InitializeLayoutSeats(){
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 21; j++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("view/seat_layout_item.fxml"));
                AnchorPane anchorPane = null;
                try {
                    anchorPane = fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(i == 0){
                    if(j != 0 && j != 1 && j != 19 && j != 20){
                        gridLayoutAllSeat.add(anchorPane,j,i+1);
                    }
                }
                else if(i == 1){
                    if(j != 0 && j != 20){
                        gridLayoutAllSeat.add(anchorPane,j,i+1);
                    }
                }
                else if(i == 2 || i == 3 || i ==4 || i == 6 || i == 7 || i ==8 || i == 9 || i == 10){
                    if(j != 10){
                        gridLayoutAllSeat.add(anchorPane,j,i+1);
                    }
                }
                gridLayoutAllSeat.setMinWidth(Region.USE_COMPUTED_SIZE);
                gridLayoutAllSeat.setPrefWidth(Region.USE_COMPUTED_SIZE);
                gridLayoutAllSeat.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                gridLayoutAllSeat.setMinHeight(Region.USE_COMPUTED_SIZE);
                gridLayoutAllSeat.setPrefHeight(Region.USE_COMPUTED_SIZE);
                gridLayoutAllSeat.setMaxHeight(Region.USE_PREF_SIZE);

                if(i == 4){
                    GridPane.setMargin(anchorPane, new Insets(3,0,25,5));
                }
                else{
                    GridPane.setMargin(anchorPane, new Insets(3,0,5,5));
                }
            }
        }
    }

    public void setTitle(String name){
        //textNameMovie.setText(name.t
        // rim());
    }

}
