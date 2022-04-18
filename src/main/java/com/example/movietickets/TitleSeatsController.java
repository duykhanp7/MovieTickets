package com.example.movietickets;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TitleSeatsController {

    @FXML
    Label title;


    public void setTitle(String str){
        title.setText(str);
    }

}
