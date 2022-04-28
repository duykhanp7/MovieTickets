package com.example.movietickets.model;


import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionToDatabaseController {

    //CÁC YẾU TỐ CẦN THIẾT ĐỂ KẾT NỐI ĐẾN DATABASE
    private final String databaseName = "moviedb"; //TÊN DATABASE
    private final String username = "root"; //USERNAME
    private final String password = "190401";//PASSWORD
    private final String url = "jdbc:mysql://localhost:3306/" + databaseName;//URL LOCALHOST
    private Connection connection;

    public Connection getConnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url,username,password);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return connection;
    }

}
