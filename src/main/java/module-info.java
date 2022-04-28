module com.example.movietickets {
    requires javafx.controls;
    requires javafx.fxml;


    exports com.example.movietickets.interf;
    opens com.example.movietickets.interf to javafx.fxml;
    exports com.example.movietickets.controller;
    opens com.example.movietickets.controller to javafx.fxml;
    exports com.example.movietickets.utils;
    opens com.example.movietickets.utils to javafx.fxml;
    exports com.example.movietickets.model;
    opens com.example.movietickets.model to javafx.fxml;
    exports com.example.movietickets;
    opens com.example.movietickets to javafx.fxml;

    requires retrofit2;
    requires retrofit2.converter.gson;
    requires com.google.gson;
    requires java.mail;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires java.sql;
    requires mysql.connector.java;

}