package com.example.movietickets;

import com.example.movietickets.interf.OnSeatItemClickedListener;
import com.example.movietickets.model.GenresObject;
import com.example.movietickets.model.MovieObject;
import com.example.movietickets.utils.Utils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
    //MAP ĐỂ ĐÁNH DẤU GHẾ NÀO ĐÃ ĐƯỢC CHỌN VÀ GHẾ NÀO CHƯA
    Map<String,String> seatNumberString = new HashMap<>();
    //TỔNG GIÁ VÉ
    int priceTotal = 0;
    //MAP QUẢN LÝ DỰA VÀO TÊN PHIM VÀ CÁC KHUNG GIỜ CHIẾU;
    //ID PHIM, KHUNG GIỜ CHIẾU, TÊN GHẾ VÀ CONTROLLER
    //ID PHIM, KHUNG GIỜ CHIẾU, TÊN GHẾ VÀ LAYOUT
    Map<String,Map<String,Map<String,Boolean>>> mapParentStateSeat = new HashMap<>();
    Map<String,Map<String,Map<String,AnchorPane>>> mapParentLayout = new HashMap<>();
    Map<String,Map<String,Map<String,SeatLayoutItemController>>> mapParentController = new HashMap<>();

    Map<String,Map<String,Boolean>> mapState = new HashMap<>();
    Map<String,Map<String,AnchorPane>> mapLayout = new HashMap<>();
    Map<String,Map<String,SeatLayoutItemController>> mapController = new HashMap<>();
    //MỘT HASHMAP ĐỂ ÁNH XẠ TÊN GHẾ VÀ TRẠNG THÁI GHẾ ĐÓ ĐÃ CÓ AI ĐẶT CHƯA
    //Map<String,Boolean> listSeatsSelected = new HashMap<>();
    //MỘT LIST CHỨA CÁC SEAT CONTROLLER ĐỂ CHỈNH SỬA TYPE MỖI KHI ẤN NÚT BOOK TICKETS
    //MỖI KHI ĐỒNG Ý ĐẶT VÉ THÌ CÁC VỊ TRÍ ĐÓ SẼ CHUYỂN SANG ĐỎ
    Map<String,AnchorPane> listSeatsLayout = new HashMap<>();
    Map<String,SeatLayoutItemController> listSeatsController = new HashMap<>();
    Map<String,Boolean> listSeatsState = new HashMap<>();
    //STRING : KHUNG THỜI GIAN CHIẾU PHIM
    String timeSlot = "";
    //TRẠNG THÁI CLICKED CỦA CÁC TIME SLOT, MẶC ĐỊNH LÀ FALSE CHƯA CLICKED
    Boolean[] stateButtonTimeSlot = new Boolean[]{false,false,false,false,false};
    //NÚT ĐỒNG Ý ĐẶT VÉ THÌ THÊM PHIM VỪA ĐẶT VÀO DANH SÁCH PHIM VỪA BÁN
    MovieSoldRecentlyController movieSoldRecentlyController;
    MainController mainController;
    //PHIM ĐƯỢC XÁC NHẬN ĐỂ MUA
    MovieObject.Movie movie;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        InitializeLayoutSeats();
    }


    public void setMovie(MovieObject.Movie item){
        this.movie = item;
    }

    public void setMovieSoldRecentlyController(MovieSoldRecentlyController movieSoldRecentlyController){
        this.movieSoldRecentlyController = movieSoldRecentlyController;
    }

    public void setMainController(MainController mainController){
        this.mainController = mainController;
    }

    //KHỞI TẠO LAYOUT CÁC GHẾ CỦA PHÒNG CHIẾU
    public void InitializeLayoutSeats(){
        timeSlot = Utils.SLOT_9_45_AM;
        InitLayoutSeatsSelected(Utils.SLOT_9_45_AM);
    }

    public void InitLayoutSeatsSelected(String time){
        System.out.println("SIZE ALL : "+mapParentLayout.size());
        seatNumberString.clear();
        gridPaneLayoutSeats.getChildren().clear();
        mapLayout.clear();mapController.clear();mapState.clear();
        listSeatsController.clear();listSeatsLayout.clear();listSeatsState.clear();
        String[] verticalTitle = new String[]{"J","I","H","G","F","","E","D","C","B","A"};
        //NẾU MAP KHÔNG RỖNG THÌ RELOAD LẠI LAYOUT CŨ
        //KIỂM TRA NẾU MAP CHỨA CÁC ITEM THÌ KIỂM TRA XEM NÓ CÓ CHỨA PHIM ĐƯỢC MUA HIỆN TẠI HAY KHÔNG
        //VÀ CÓ CHỨA TIME SLOT HIỆN TẠI HAY CHƯA
        //NẾU CHƯA THÌ THÊM MỚI CHƯA THÌ LOAD TỪ MAP LÊN
        if(mapParentLayout.size() > 0 && mapParentLayout.containsKey(movie.getId()) && movie != null && mapParentLayout.get(movie.getId()).containsKey(time)){
            //NẾU CHỨA KEY THÌ LOAD LẠI LAYOUT CŨ THÔNG QUA 3 CÁI MAP
            //mapParentLayout, mapParentController, mapParentState
            //System.out.println("VALUES KEY 22222: "+mapParentLayout.keySet()+" -- "+mapParentLayout.get("634649").keySet()+" -- "+mapParentLayout.get("634649").get("09:45 AM").keySet());
            Map<String,Boolean> tempMapStateItems = new HashMap<>();
            Map<String,AnchorPane> tempMapLayoutItems = new HashMap<>();
            Map<String,SeatLayoutItemController> tempMapControllerItems = new HashMap<>();

            tempMapStateItems = mapParentStateSeat.get(movie.getId()).get(time);
            tempMapLayoutItems = mapParentLayout.get(movie.getId()).get(time);
            tempMapControllerItems = mapParentController.get(movie.getId()).get(time);

            //System.out.println("SIZE LOADING : "+tempMapStateItems.size()+" -- "+tempMapLayoutItems.size()+" -- "+tempMapControllerItems.size());


            //CONVERT MAP TO LIST
            //tempMapStateItems.
            for (int i = 0; i < 11; i++) {
                for (int j = 0; j < 21; j++) {

                    String title = verticalTitle[i] + (j + 1);
                    AnchorPane anchorPane = tempMapLayoutItems.get(title);
                    boolean state = tempMapStateItems.get(title);
                    SeatLayoutItemController controller = tempMapControllerItems.get(title);

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

                    if(!listSeatsState.containsKey(title)){
                        listSeatsState.put(title,state);
                    }
                    if(!listSeatsLayout.containsKey(title)){
                        listSeatsLayout.put(title,anchorPane);
                    }
                    if(!listSeatsController.containsKey(title)){
                        // listSeatsSelected.put(title,false);
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
                        GridPane.setMargin(tempMapLayoutItems.get(title), new Insets(3,0,25,5));
                    }
                    else{
                        GridPane.setMargin(tempMapLayoutItems.get(title), new Insets(3,0,5,5));
                    }
                }
            }
        }
        else{
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
                    //TỪ HÀNG 8 TRỞ ĐI SẼ LÀ GHẾ PRIME
                    if(i >= 8){
                        controller.setType(Utils.TYPE_PRIME);
                    }

                    String title = verticalTitle[i] + (j + 1);
                    controller.setSeatTitle(title);
                    controller.setOnSeatItemClickedListener(this);

                    //ADD NEW ITEM TO MAP
                    if(!listSeatsState.containsKey(title)){
                        listSeatsState.put(title,false);
                    }
                    if(!listSeatsLayout.containsKey(title)){
                        listSeatsLayout.put(title,anchorPane);
                    }
                    if(!listSeatsController.containsKey(title)){
                        // listSeatsSelected.put(title,false);
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
                System.out.println("AGREEEEEEEEEEEEEEEEEEE : "+timeSlot);
                alertConfirmation.close();
                if(seatNumberString.size() > 0){
                    for (String item : seatNumberString.keySet()){
                        listSeatsState.put(item,true);
                        listSeatsController.get(item).setType(Utils.TYPE_SELECTED);
                        System.out.println("TYPE CHANGE : "+listSeatsController.get(item).getType());
                    }
                    seatNumberString.clear();
                }

                if(!mainController.moviesSoldRecently.containsKey(movie.getId())){
                    System.out.println("ADD SUCCESSFULLY");
                    mainController.moviesSoldRecently.put(movie.getId(),movie);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            movieSoldRecentlyController.addNewItem(movie);
                        }
                    });
                }

                //HIỂN THỊ THÔNG BÁO ĐẶT VÉ THÀNH CÔNG
                Alert alertSuccessfully = new Alert(Alert.AlertType.CONFIRMATION);
                alertSuccessfully.setTitle("Successfully");
                alertSuccessfully.setHeaderText("Successful Ticket Booking");
                alertSuccessfully.showAndWait();

                mapState.put(timeSlot,new HashMap<String,Boolean>(listSeatsState));
                mapLayout.put(timeSlot,new HashMap<String,AnchorPane>(listSeatsLayout));
                mapController.put(timeSlot,new HashMap<String,SeatLayoutItemController>(listSeatsController));

                //Map<String,Map<String,Map<String,Boolean>>>
                //KIỂM TRA XEM PHIM NÀY ĐÃ TỒN TẠI CHƯA
                //NẾU CHƯA THÌ THÊM VÀO CÒN RỒI THÌ UPDATE KHUNG GIỜ CHIẾU
                if(!mapParentLayout.containsKey(movie.getId())){
                    System.out.println("EXISTEDDDDDDDDDDDDDDDDDD");
                    mapParentStateSeat.put(movie.getId(),new HashMap<String,Map<String,Boolean>>(mapState));
                    mapParentLayout.put(movie.getId(),new HashMap<String,Map<String,AnchorPane>>(mapLayout));
                    mapParentController.put(movie.getId(),new HashMap<String,Map<String,SeatLayoutItemController>>(mapController));
                }
                else {
                    System.out.println("NOTTTTTTTTTTTTTTTTTT EXISTEDDDDDDDDDDDDDDDDDD");
                    mapParentStateSeat.get(movie.getId()).put(timeSlot,new HashMap<String,Boolean>(listSeatsState));
                    mapParentLayout.get(movie.getId()).put(timeSlot,new HashMap<String,AnchorPane>(listSeatsLayout));
                    mapParentController.get(movie.getId()).put(timeSlot,new HashMap<String,SeatLayoutItemController>(listSeatsController));
                }

                System.out.println("SIZE AFTER ADD : "+mapParentLayout.size()+" -- "+mapParentLayout.get(movie.getId()).size());
                timeSlot = Utils.SLOT_9_45_AM;
                button945AM.setStyle("-fx-background-color: #d98609");
                button100PM.setStyle("-fx-background-color: #4d913d");
                button345PM.setStyle("-fx-background-color: #4d913d");
                button700PM.setStyle("-fx-background-color: #4d913d");
                button945PM.setStyle("-fx-background-color: #4d913d");
            }
            else if(optional.get() == cancel){
                System.out.println("DISSSSSSSSSSSSSSSSAGREEEEEEEEEEEEEEEEEEE");
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
            if(!timeSlot.equals(Utils.SLOT_9_45_AM)){
                timeSlot = Utils.SLOT_9_45_AM;
                InitLayoutSeatsSelected(timeSlot);
            }
        }
        else if(actionEvent.getSource() == button100PM){
            if(!timeSlot.equals(Utils.SLOT_1_00_PM)){
                timeSlot = Utils.SLOT_1_00_PM;
                InitLayoutSeatsSelected(timeSlot);
            }
        }
        else if(actionEvent.getSource() == button345PM){
            if(!timeSlot.equals(Utils.SLOT_3_45_PM)){
                timeSlot = Utils.SLOT_3_45_PM;
                InitLayoutSeatsSelected(timeSlot);
            }
        }
        else if(actionEvent.getSource() == button700PM){
            if(!timeSlot.equals(Utils.SLOT_7_00_PM)){
                timeSlot = Utils.SLOT_7_00_PM;
                InitLayoutSeatsSelected(timeSlot);
            }
        }
        else if(actionEvent.getSource() == button945PM){
            if(!timeSlot.equals(Utils.SLOT_9_45_PM)){
                timeSlot = Utils.SLOT_9_45_PM;
                InitLayoutSeatsSelected(timeSlot);
            }
        }
    }


    //UNCHECK CÁC SEAT ĐANG TRONG TRẠNG THÁI SELECTING CHUYỂN VỀ TRẠNG THÁI CŨ CỦA GHẾ
    //LÀ GHẾ NORMAL HOẶC PRIME
    public void unCheckSeatsWhenCloseNotBookingTickets(){
        if(seatNumberString.size() > 0){
            for (String item : seatNumberString.keySet()){
                System.out.println("TYPEEEEEEEEEE : "+item);
                //listSeatsSelected.put(item,false);
                listSeatsController.get(item).setType(seatNumberString.get(item));
            }
            seatNumberString.clear();
        }
    }

    //KIỂM TRA NGƯỜI DÙNG ĐÃ CHỌN KHUNG GIỜ CHIẾU VÀ CHỖ NGỒI CHƯA
    public boolean checkUserInputNotNull(){
        return timeSlot.trim().isEmpty() || seatNumberString.size() == 0;
    }


    //THAY ĐỔI BACKGROUND CỦA BUTTON MỖI KHI ĐƯỢC CHỌN
    //VÀ TRỞ VỀ MÀU BAN ĐẦU MỖI KHI NHẤN NÚT KHÁC
    @FXML
    public void onTimeSlotsClicked(MouseEvent mouseEvent){
        changeStateButtonTimeSlotToFalse();
        if(mouseEvent.getSource() == button945AM){
            stateButtonTimeSlot[0] = true;
        }
        else if(mouseEvent.getSource() == button100PM){
            stateButtonTimeSlot[1] = true;
        }
        else if(mouseEvent.getSource() == button345PM){
            stateButtonTimeSlot[2] = true;
        }
        else if(mouseEvent.getSource() == button700PM){
            stateButtonTimeSlot[3] = true;
        }
        else if(mouseEvent.getSource() == button945PM){
            stateButtonTimeSlot[4] = true;
        }
        button945AM.setStyle(stateButtonTimeSlot[0] ? "-fx-background-color: #d98609":"-fx-background-color: #4d913d");
        button100PM.setStyle(stateButtonTimeSlot[1] ? "-fx-background-color: #d98609":"-fx-background-color: #4d913d");
        button345PM.setStyle(stateButtonTimeSlot[2] ? "-fx-background-color: #d98609":"-fx-background-color: #4d913d");
        button700PM.setStyle(stateButtonTimeSlot[3] ? "-fx-background-color: #d98609":"-fx-background-color: #4d913d");
        button945PM.setStyle(stateButtonTimeSlot[4] ? "-fx-background-color: #d98609":"-fx-background-color: #4d913d");
    }

    //THAY ĐỔI TRẠNG THÁI CỦA CÁC BUTTON TIME SLOT VỀ FALSE
    public void changeStateButtonTimeSlotToFalse(){
        Arrays.fill(stateButtonTimeSlot, false);
    }

}
