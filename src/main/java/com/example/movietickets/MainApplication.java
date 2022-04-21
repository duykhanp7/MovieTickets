package com.example.movietickets;

import java.io.IOException;

import com.example.movietickets.interf.API;
import com.example.movietickets.model.MovieObject;
import com.example.movietickets.utils.Utils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainApplication extends Application {


    public MainApplication(){}


    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(MainApplication.class.getResource("view/login_activity.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 850, 600);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.setTitle("Cinema");
        stage.setScene(scene);
        stage.show();
    }

    //MAIN
    public static void main(String[] args) {
        launch();
    }


}