package com.example.movietickets.utils;

import com.example.movietickets.model.ConnectionToDatabaseController;
import javafx.scene.paint.ImagePattern;

import java.util.*;

public class Utils {
    public static final String API_MOVIE_DOMAIN = "https://api.themoviedb.org";
    public static final String API_KEY = "904b3059ddd54e71c45dc72502d59375";
    public static final String path_image_domain = "https://image.tmdb.org/t/p/w500";

    public static String TYPE_NORMAL = "NORMAL";
    public static String TYPE_PRIME = "PRIME";
    public static String TYPE_SELECTED = "SELECTED";
    public static String TYPE_SELECTING= "SELECTING";
    public static String TYPE_RESTRICT= "RESTRICT";

    //TIME SLOT
    public static String SLOT_9_45_AM = "09:45 AM";
    public static String SLOT_1_00_PM = "01:00 PM";
    public static String SLOT_3_45_PM = "03:45 PM";
    public static String SLOT_7_00_PM = "07:00 PM";
    public static String SLOT_9_45_PM = "09:45 PM";

    //TICKET TYPE
    public static String TICKER_NORMAL = "TICKER_NORMAL";
    public static String TICKER_PRIME = "TICKER_PRIME";
    //PRICE FOR EACH TYPE
    public static int PRICE_NORMAL = 65000;
    public static int PRICE_PRIME = 85000;
    //EMAIL AUTHENTICATE
    public static final String EMAIL = "duykhangp7@gmail.com";
    public static final String PASSWORD = "0397439979adAD";
    //KHUNG THỜI GIAN
    public static final String[] timeSlots = new String[]{Utils.SLOT_9_45_AM,Utils.SLOT_1_00_PM,Utils.SLOT_3_45_PM,Utils.SLOT_7_00_PM,Utils.SLOT_9_45_PM};
    public static final String[] verticalTitle = new String[]{"J","I","H","G","F","","E","D","C","B","A"};

    public static List<String> getListTimeSlots(){
        return Arrays.asList(timeSlots);
    }
    //TYPE CUSTOMER - LOẠI KHÁCH HÀNG
    public static final String CUSTOMER_NORMAL = "NORMAL";
    public static final String CUSTOMER_PRIME = "PRIME";

}

