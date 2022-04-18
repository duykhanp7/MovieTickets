package com.example.movietickets;

import com.example.movietickets.interf.OnSeatItemClickedListener;
import com.example.movietickets.model.GenresObject;
import com.example.movietickets.model.MovieObject;
import com.example.movietickets.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class SoldTicketsController implements Initializable, OnSeatItemClickedListener {


    @FXML Label textNameMovie;

    //LAYOUT HIỂN THỊ CHỖ NGỖI
    @FXML GridPane gridPaneLayoutSeats;

    //NÚT MUA VÉ
    @FXML Button buttonAgreeBuyTickets;
    //LABEL HIỂN THỊ SỐ CHỖ NGỒI ĐÃ CHỌN
    @FXML Label seatsSelected;
    //HIỂN THỊ TÊN CỦA CÁC GHẾ ĐÃ CHỌN
    @FXML Label seatNumber;
    //LABEL HIỂN THỊ TỔNG GIÁ VÉ
    @FXML Label price;
    //LABEL HIỂN THỊ THỜI GIAN TỔNG CỦA BỘ PHIM
    @FXML Label runtime;
    //LABEL HIỂN THỊ TAGLINE
    @FXML Label tagline;
    //LABLE HIỂN THỊ NGÀY RA RẠP
    @FXML Label release_date;
    //LABEL HIỂN THỊ TÊN PHIM
    @FXML Label titleMovie;
    //IMAGEVIEW HIỂN THỊ ẢNH POSTER CỦA BỘ PHIM
    @FXML ImageView imageMovie;
    //Grid LAYOUT HIỂN THỊ THỂ LOẠI CỦA PHIM
    @FXML GridPane gridGenres;
    //BUTTON CHỌN KHUNG GIỜ CHIẾU LÚC 9:45AM
    @FXML Button button945AM;
    //BUTTON CHỌN KHUNG GIỜ CHIẾU LÚC 1:00PM
    @FXML Button button100PM;
    //BUTTON CHỌN KHUNG GIỜ CHIẾU LÚC 3:45PM
    @FXML Button button345PM;
    //BUTTON CHỌN KHUNG GIỜ CHIẾU LÚC 7:00PM
    @FXML Button button700PM;
    //BUTTON CHỌN KHUNG GIỜ CHIẾU LÚC 9:45PM
    @FXML Button button945PM;
    //int countSeatsSelected = 0;
    //MAP ĐỂ ĐÁNH DẤU GHẾ NÀO ĐÃ ĐƯỢC CHỌN VÀ GHẾ NÀO CHƯA
    Map<String,String> seatNumberString = new HashMap<>();
    //TỔNG GIÁ VÉ
    int priceTotal = 0;

    //MỘT HASHMAP ĐỂ ÁNH XẠ TÊN GHẾ VÀ TRẠNG THÁI GHẾ ĐÓ ĐÃ CÓ AI ĐẶT CHƯA
    Map<String,Boolean> listSeatsSelected = new HashMap<>();
    //MỘT LIST CHỨA CÁC SEAT CONTROLLER ĐỂ CHỈNH SỬA TYPE MỖI KHI ẤN NÚT BOOK TICKETS
    //MỖI KHI ĐỒNG Ý ĐẶT VÉ THÌ CÁC VỊ TRÍ ĐÓ SẼ CHUYỂN SANG ĐỎ
    Map<String,SeatLayoutItemController> listSeatsController = new HashMap<>();
    //STRING : KHUNG THỜI GIAN CHIẾU PHIM
    String timeSlot = "";


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        InitializeLayoutSeats();
    }


    //KHỞI TẠO LAYOUT CÁC GHẾ CỦA PHÒNG CHIẾU
    public void InitializeLayoutSeats(){
        String[] verticalTitle = new String[]{"J","I","H","G","F","","E","D","C","B","A"};
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
                SeatLayoutItemController controller = fxmlLoader.getController();
                if(i == 0){
                    if(j != 0 && j != 1 && j != 19 && j != 20){
                        gridPaneLayoutSeats.add(anchorPane,j+3,i+1);
                    }
                }
                else if(i == 1){
                    if(j != 0 && j != 20){
                        gridPaneLayoutSeats.add(anchorPane,j+3,i+1);
                    }
                }
                else if(i == 2 || i == 3 || i ==4 || i == 6 || i == 7 || i ==8 || i == 9 || i == 10){
                    if(j != 10){
                        gridPaneLayoutSeats.add(anchorPane,j+3,i+1);
                    }
                }
                if(i >= 8){
                    controller.setType(Utils.TYPE_PRIME);
                }

                String title = verticalTitle[i] + (j + 1);
                controller.setSeatTitle(title);
                controller.setOnSeatItemClickedListener(this);

                if(!listSeatsSelected.containsKey(title) && !listSeatsController.containsKey(title)){
                    listSeatsSelected.put(title,false);
                    listSeatsController.put(title,controller);
                }

                gridPaneLayoutSeats.setMinWidth(Region.USE_COMPUTED_SIZE);
                gridPaneLayoutSeats.setPrefWidth(Region.USE_COMPUTED_SIZE);
                gridPaneLayoutSeats.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                gridPaneLayoutSeats.setMinHeight(Region.USE_COMPUTED_SIZE);
                gridPaneLayoutSeats.setPrefHeight(Region.USE_COMPUTED_SIZE);
                gridPaneLayoutSeats.setMaxHeight(Region.USE_PREF_SIZE);

                if(i == 4){
                    GridPane.setMargin(anchorPane, new Insets(3,0,25,5));
                }
                else{
                    GridPane.setMargin(anchorPane, new Insets(3,0,5,5));
                }
            }
        }
        addVerticalAndHorizontalAxisTitle();
    }

    //THÊM TÊN CHO TRỤC NGANG VÀ TRỤ DỌC ĐỂ ĐÁNH DẤU TÊN GHẾ
    public void addVerticalAndHorizontalAxisTitle(){
        String[] verticalTitle = new String[]{"J","I","H","G","F","E","D","C","B","A"};

        //THÊM TÊN CHO TRỤC Y BÊN TRÁI VÀ PHẢI
        for (int i = 0; i < 10; i++) {
            FXMLLoader loaderLeft = new FXMLLoader();
            loaderLeft.setLocation(getClass().getResource("view/title_seats.fxml"));
            FXMLLoader loaderRight = new FXMLLoader();
            loaderRight.setLocation(getClass().getResource("view/title_seats.fxml"));
            AnchorPane anchorPaneLeft = null;
            AnchorPane anchorPaneRight = null;
            try {
                anchorPaneLeft = loaderLeft.load();
                anchorPaneRight = loaderRight.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            TitleSeatsController controller = loaderLeft.getController();
            TitleSeatsController controller1 = loaderRight.getController();
            controller.setTitle(verticalTitle[i]);
            controller1.setTitle(verticalTitle[i]);
            if( i > 4){
                gridPaneLayoutSeats.add(anchorPaneLeft,0,i+2);
                gridPaneLayoutSeats.add(anchorPaneRight,28,i+2);
            }
            else{
                gridPaneLayoutSeats.add(anchorPaneLeft,0,i+1);
                gridPaneLayoutSeats.add(anchorPaneRight,28,i+1);
            }

            if(i == 4){
                GridPane.setMargin(anchorPaneLeft, new Insets(5,0,25,5));
                GridPane.setMargin(anchorPaneRight, new Insets(5,0,25,5));
            }
            else{
                GridPane.setMargin(anchorPaneLeft, new Insets(5,0,5,5));
                GridPane.setMargin(anchorPaneRight, new Insets(5,0,5,5));
            }
        }

        //THÊM TÊN CHO TRỤC X CÁC CHỈ SỐ THỨ TỰ GHẾ 1->20
        for (int i = 0; i < 21; i++) {
            if(i != 10){
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("view/title_seats.fxml"));
                AnchorPane anchorPane = null;
                try {
                    anchorPane = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                TitleSeatsController controller = loader.getController();
                controller.setTitle(String.valueOf(i+1));

                gridPaneLayoutSeats.add(anchorPane,i+3,12);
                GridPane.setMargin(anchorPane, new Insets(10,0,5,5));
            }
        }

    }


    //ĐẨY DỮ LIỆU CỦA PHIM LÊN VIEWS
    public void setContent(MovieObject.Movie item){
        //countSeatsSelected = 0;
        seatNumberString.clear();
        priceTotal = 0;
        gridGenres.getChildren().clear();
        gridGenres.setAlignment(Pos.CENTER);
        imageMovie.setImage(new Image(Utils.path_image_domain+item.getPoster_path()));
        titleMovie.setText(item.getTitle());
        runtime.setText("Runtime : "+item.getRuntime());
        tagline.setText(item.getTagline());
        release_date.setText("Release Date : "+item.getRelease_date());
        int row = 0;
        int column = 0;
        for (GenresObject.Genres a : item.getGenres()){
            AnchorPane anchorPane = new AnchorPane();
            Label label = new Label(a.getName());
            label.setAlignment(Pos.CENTER);
            label.setContentDisplay(ContentDisplay.CENTER);
            label.setFont(new Font("Agency FB",18));
            anchorPane.getChildren().add(label);
            if(column == 2){
                ++row;
                column = 0;
            }
            gridGenres.add(anchorPane,++column,row);

            GridPane.setMargin(anchorPane, new Insets(5,0,10,10));
        }
    }

    //BẮT SỰ KIỆN MỖI KHI NHẤN VÀO CHỌN GHẾ NÀO ĐÓ
    //UPDATE SỐ LƯỢNG GHẾ ĐÃ CHỌN, TÊN GHẾ ĐÃ CHỌN
    @Override
    public void onSeatItemClicked(String text,boolean state,String type) {
        if(state){
            //THÊM TÊN GHẾ VÀO DANH SÁCH ĐANG CHỌN
            seatNumberString.put(text,type);
        }
        else {
            //XÓA TÊN GHẾ KHỎI DANH SÁCH ĐẶT
            seatNumberString.remove(text);
        }
        //UPDATE SỐ LƯỢNG VÀ TÊN GHẾ ĐÃ CHỌN
        seatsSelected.setText(String.valueOf(seatNumberString.size()));
        seatNumber.setText(String.join(", ",seatNumberString.keySet()));
    }


    //BẮT ĐẦU ĐẶT VÉ
    //NÚT ĐẶT VÉ
    @FXML
    public void onButtonBookTicketsClick(ActionEvent actionEvent){
        //KIỂM TRA NẾU CHƯA CHỌN GHẾ NGỒI VÀ KHUNG GIỜ CHIẾU
        //NẾU ĐÃ ĐỦ ĐIỀU KIỆN TRÊN THÌ BẮT ĐẦU ĐẶT VÉ
        //KHÔNG THÌ HIỂN THỊ THÔNG BÁO CHO NGƯỜI DÙNG BIẾT ĐỂ CHỌN GHẾ VÀ KHUNG GIỜ CHIẾU
        if(!checkUserInputNotNull()){
            ButtonType agree = new ButtonType("Agree");
            ButtonType cancel = new ButtonType("Cancel");
            Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
            alertConfirmation.setTitle("Booking Confirmation");
            alertConfirmation.setHeaderText("Are You Sure To Book Tickets?");
            alertConfirmation.getButtonTypes().clear();
            alertConfirmation.getButtonTypes().addAll(agree,cancel);

            Optional<ButtonType> optional = alertConfirmation.showAndWait();

            if(optional.get() == agree){
                alertConfirmation.close();
                if(seatNumberString.size() > 0){
                    for (String item : seatNumberString.keySet()){
                        listSeatsSelected.put(item,true);
                        listSeatsController.get(item).setType(Utils.TYPE_SELECTED);
                    }
                    seatNumberString.clear();
                }
                //HIỂN THỊ THÔNG BÁO ĐẶT VÉ THÀNH CÔNG
                Alert alertSuccessfully = new Alert(Alert.AlertType.CONFIRMATION);
                alertSuccessfully.setTitle("Successfully");
                alertSuccessfully.setHeaderText("Successful Ticket Booking");
                alertSuccessfully.showAndWait();
            }
            else if(optional.get() == cancel){
                alertConfirmation.close();
            }

        }
        else{
            //HIỂN THỊ THÔNG BÁO NẾU CHƯA CHỌN GHẾ VÀ KHUNG GIỜ CHIẾU
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setTitle("Booking Warning");
            alertWarning.setHeaderText("Please Choose Your Seats And Time Slot !");
            alertWarning.showAndWait();
        }
    }

    //BẮT SỰ KIỆN BUTTON CHỌN KHUNG THỜI GIAN XEM PHIM
    @FXML
    public void onButtonChooseTimeSlot(ActionEvent actionEvent){
        if(actionEvent.getSource() == button945AM){
            timeSlot = Utils.SLOT_9_45_AM;
        }
        else if(actionEvent.getSource() == button100PM){
            timeSlot = Utils.SLOT_1_00_PM;
        }
        else if(actionEvent.getSource() == button345PM){
            timeSlot = Utils.SLOT_3_45_PM;
        }
        else if(actionEvent.getSource() == button700PM){
            timeSlot = Utils.SLOT_7_00_PM;
        }
        else if(actionEvent.getSource() == button945PM){
            timeSlot = Utils.SLOT_9_45_PM;
        }
    }


    //UNCHECK CÁC SEAT ĐANG TRONG TRẠNG THÁI SELECTING CHUYỂN VỀ TRẠNG THÁI CŨ CỦA GHẾ
    //LÀ GHẾ NORMAL HOẶC PRIME
    public void unCheckSeatsWhenCloseNotBookingTickets(){
        if(seatNumberString.size() > 0){
            for (String item : seatNumberString.keySet()){
                System.out.println("TYPEEEEEEEEEE : "+item);
                listSeatsSelected.put(item,false);
                listSeatsController.get(item).setType(seatNumberString.get(item));
            }
            seatNumberString.clear();
        }
    }

    public boolean checkUserInputNotNull(){
        return timeSlot.trim().isEmpty() && seatNumberString.size() == 0;
    }

}
