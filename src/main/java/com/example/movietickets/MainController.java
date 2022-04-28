package com.example.movietickets;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import com.example.movietickets.controller.ItemMovieController;
import com.example.movietickets.interf.OnItemClickedListener;
import com.example.movietickets.model.*;
import com.example.movietickets.interf.API;
import com.example.movietickets.utils.Utils;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//CONTROLLER CHÍNH
public class MainController implements Initializable, OnItemClickedListener {
    //MẢNG CHỨA TRẠNG THÁI CLICKED VÀ HOVERED CỦA CÁC NÚT HOME,ADD,MOVIES SCREENS,LOGOUT
    Boolean[] stateButtonClicked = new Boolean[]{false, false, false, false};
    Boolean[] stateButtonHovered = new Boolean[]{false, false, false, false};

    //NÚT HOME
    @FXML private Button buttonHome;
    //NÚT MORE
    @FXML private Button buttonMore;
    //NÚT XEM DANH SÁCH CÁC PHÒNG CHIẾU
    @FXML private Button buttonMovieScreens;
    //NÚT ĐĂNG XUẤT
    @FXML private Button buttonLogout;
    //NÚT ĐỂ HIỆN THỊ DANH SÁCH PHIM BÁN GẦN ĐÂY
    @FXML Button buttonMoviesRecently;
    @FXML Button buttonReloadMovieScreen;


    @FXML HBox PaneHome;
    @FXML VBox loadingPane;

    @FXML HBox PaneMore;
    @FXML HBox PaneMovieScreens;
    @FXML HBox PaneLogOut;

    @FXML GridPane gridAdapter;
    @FXML GridPane gridLayoutItemSearch;
    @FXML GridPane gridLayoutSoldMovie;
    @FXML GridPane gridPaneMovieScreen;

    @FXML ScrollPane scrollPane;
    @FXML ScrollPane scrollPaneSearch;

    @FXML Button buttonSearch;
    @FXML TextField textFieldSearch;
    @FXML TextField idCinemaText;
    @FXML TextField nameCinemaText;

    @FXML Button buttonSignUp;
    @FXML Button buttonChangePassword;
    @FXML Button buttonLogOut;
    @FXML Button buttonDeleteCinema;
    @FXML Button buttonAddCinema;
    //COMBOBOX
    @FXML ComboBox<String> comboBoxCinemaMovieScreen;
    @FXML ComboBox<String> comboBoxTimeSlots;
    @FXML ComboBox<String> comboBoxDeleteCinema;

    //
    String oldKeyword="";
    //DANH SÁCH CÁC PHIM ĐÃ BÁN GẦN ĐÂY
    //CHỈ THÊM VÀO MỖI KHI PHIM NÀY ĐƯỢC BÁN
    //DÙNG HASHTABLE ĐỂ TRÁNH INSERT CÁC PHẦN TỬ TRÙNG
    //KEY : LÀ ID CỦA BỘ PHIM ĐÓ, VALUES CHÍNH LÀ BỘ PHIM ĐÓ
    Map<String, MovieObject.Movie> moviesSoldRecently = new HashMap<>();

    //DANH SÁCH PHIM PHỔ BIẾN
    List<MovieObject> popularMovies = new ArrayList<>();
    //CỬA SỐ BÁN VÉ
    Stage stageSellTickets;
    //CONTROLLER CỦA BÁN VÉ
    SoldTicketsController soldTicketsController;
    LoginActivityController loginActivityController;
    Scene sceneSellTickets;

    //BẢNG TÌM KIẾM
    int columnSearch = 0;
    int rowSearch = 1;
    int pageSearch = 1;
    int maxPageSearch = 0;

    //BẢNG HIỂN PHIM CHÍNH
    int column = 0;
    int row = 2;
    int pageMain = 0;
    int maxPageMain = 0;
    //BẢNG HIỂN THỊ PHIM BÁN
    int columnSold = 0;
    int rowSold = 2;
    //MAX SIZE CINEMA
    int maxCinema = 4;
    //CONTROLLER DANH SÁCH VÉ PHIM BÁN GẦN ĐÂY
    MovieSoldRecentlyController movieSoldRecentlyController;
    Stage stageMoviesRecently;
    Scene sceneMoviesRecently;
    //
    //DANH SÁCH CÁC PHÒNG CHIẾU PHIM
//    List<CinemaRoom> cinemaRooms = new ArrayList<>();
    Map<String, CinemaRoom> mapRooms = new HashMap<>();
    //MAP
    Map<String,Seat> listSeatsState = new HashMap<>();
    Map<String,AnchorPane> listSeatsLayout = new HashMap<>();
    Map<String,SeatLayoutItemController> listSeatsController = new HashMap<>();

    Map<String,AnchorPane> listSeatsLayoutMovieScreen = new HashMap<>();
    Map<String,SeatLayoutItemController> listSeatsControllerMovieScreen = new HashMap<>();

    Map<String,Map<String,Seat>> mapState = new HashMap<>();
    //Map<String,Map<String,AnchorPane>> mapLayout = new HashMap<>();
    //Map<String,Map<String,SeatLayoutItemController>> mapController = new HashMap<>();
//    String[] timeSlots = new String[]{Utils.SLOT_9_45_AM,Utils.SLOT_1_00_PM,Utils.SLOT_3_45_PM,Utils.SLOT_7_00_PM,Utils.SLOT_9_45_PM};
//    String[] verticalTitle = new String[]{"J","I","H","G","F","","E","D","C","B","A"};
    //PANE MORE
    boolean hasSet = false;
    boolean hasInitComboBox = false;
    //String Room name
    String ROOM_NAME = "CINEMA ";
    int room_id = 0;
    List<MovieObject.Movie> movieListSold = new ArrayList<>();

    ///
    Stage stageAddToRoom = new Stage();
    AddMovieToRoomController addMovieToRoomController;
    boolean hasInitViewAddToRoom = false;


    List<String> listCinemaName = new ArrayList<>();

    int posComboBoxCinema = 0;
    int posComboBoxTimeSlots = 0;

    @FXML Button buttonExcel;

    List<Customer> customers = new ArrayList<>();
    //TABLE VIEW STAFF's INFORMATION
    @FXML TableView<Customer> tableViewCustomerInformation;
    @FXML TableColumn<Customer,String> columnID;
    @FXML TableColumn<Customer,String> columnName;
    @FXML TableColumn<Customer,String> columnPhoneNumber;
    @FXML TableColumn<Customer,String> columnSex;
    @FXML TableColumn<Customer,String> columnTypeCustomer;
    @FXML TableColumn<Customer,String> columnPoint;
    ObservableList<Customer> observableList = FXCollections.observableArrayList();
    //TABLE VIEW CUSTOMER's BILL
    @FXML TableView<Bill> tableViewCustomerBill;
    @FXML TableColumn<Bill,String> columnIdBill;
    @FXML TableColumn<Bill,String> columnMovieName;
    @FXML TableColumn<Bill,String> columnCinemaName;
    @FXML TableColumn<Bill,String> columnTimeSlot;
    @FXML TableColumn<Bill,String> columnSeats;
    @FXML TableColumn<Bill,String> columnDateBought;
    @FXML TableColumn<Bill,String> columnPrice;
    @FXML TableColumn<Bill,String> columnDiscount;
    ObservableList<Bill> observableBill = FXCollections.observableArrayList();
    //DATABASE CONNECTION
    ConnectionToDatabaseController connectionToDatabaseController;
    Connection connection;
    //
    public static String customerCode = "KH";
    public static int posCustomer = 0;

    public MainController() {}


    public void initialize(URL url, ResourceBundle resourceBundle) {
        //LẤY DANH SÁCH PHIM PHỔ BIẾN TỪ API TRẢ VỀ
        //loadingPane.toFront();
        ProgressIndicator progressIndicator = new ProgressIndicator();
        loadingPane.getChildren().add(progressIndicator);

//        try {
        getDataFromAPI(1);
//        }
//        catch (Exception exception){
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Error");
//            alert.setHeaderText("Connection error, please check the network again");
//        }

        //KHỞI TẠO DANH SÁCH CÁC PHÒNG CHIẾU
        //KHỞI TẠO LAYOUT DANH SÁCH PHIM BÁN GẦN ĐÂY
        InitStageMoviesRecently();
        //KHỞI TẠO LAYOUT BÁN VÉ
        InitLayoutSellTickets();
        //InitCinemaRooms();
        InitLayoutAndControllerChooseSeats();
        InitLayoutMovieScreen();


        this.stateButtonClicked[0] = true;
        this.buttonHome.setStyle("");
        gridLayoutItemSearch.setStyle("-fx-background-color:#bda2dd");

        //KHI SCROLL ĐẾN CUỐI THÌ BẮT ĐẦU LẤY DỮ LIỆU TỪ PAGE TIẾP THEO
        // ĐEM VỀ THEO VÀO GRIDLAYOUT
        //SCROLL PANE SHOW MOVIE
        //MỖI KHI SCROLL ĐẾN CUỐI THÌ SẼ LẤY THÊM CÁC PHIM TỪ PAGE TIẾP THEO
        scrollPane.vvalueProperty().addListener((observableValue, oldValue, newValue) -> {
            if(newValue.equals(1.0)){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(pageMain < maxPageMain){
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    loadingPane.toFront();
                                }
                            });
                            try {
                                getDataFromAPI(++pageMain);
                            }
                            catch (Exception e){
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText("Connection error, please check the network again");
                                alert.initOwner(loadingPane.getScene().getWindow());
                            }
                        }
                    }
                }).start();
            }
        });

        //SCROLL PANEL HIỂ THỊ DANH SÁCH PHIM TÌM KIẾM THEO TÊN
        //MỖI KHI SCROLL ĐẾN CUỐI THÌ SẼ LẤY THÊM CÁC PHIM TỪ PAGE TIẾP THEO
        scrollPaneSearch.vvalueProperty().addListener((observableValue, number, newValue) -> {
            if(newValue.equals(1.0)){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(pageSearch < maxPageSearch){
                            String text = textFieldSearch.getText().trim().toString();
                            if(!text.isEmpty()){
                                //BẮT ĐẦU LẤY DỮ LIỆU
                                //NẾU LỖI THÌ SẼ THÔNG BÁO TRONG CATCH
                                try {
                                    if(pageSearch < maxPageSearch){
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Platform.runLater(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        loadingPane.toFront();
                                                    }
                                                });
                                                InitLayoutContainMoviesResult(text,++pageSearch);
                                            }
                                        }).start();
                                    }
                                }
                                catch (Exception e){
                                    //HIỆN THỊ THÔNG BÁO NẾU QUÁ TRÌNH KẾT NỐI ĐẾN API THẤT BẠI
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Error");
                                    alert.setHeaderText("Connection error, please check the network again");
                                }
                            }
                        }
                    }
                }).start();
            }
        });


        comboBoxTimeSlots.valueProperty().addListener((observableValue, oldValue, newValue) -> posComboBoxTimeSlots = Utils.getListTimeSlots().indexOf(newValue));

        comboBoxCinemaMovieScreen.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            for (String i : listCinemaName){
                System.out.println("NAMEEEE : "+i);
            }
            posComboBoxCinema = listCinemaName.indexOf(newValue);
        });

        connectionToDatabaseController = new ConnectionToDatabaseController();
        connection = connectionToDatabaseController.getConnection();
        try {
            loadCustomerInformationToTableView();
            loadCustomerBillInformationToTableView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        posCustomer = countItems();
    }


    //LOAD VÀ HIỂN THỊ THÔNG TIN KHÁCH HÀNG LÊN BẢNG TỪ DATABASE
    public void loadCustomerInformationToTableView() throws SQLException {
        //CONNECT TO DATABASE
        ResultSet resultSet;
        try {
             resultSet = connection.createStatement().executeQuery("SELECT * FROM CUSTOMER");
             while (resultSet.next()){
                 observableList.add(new Customer(resultSet.getString("ID"),resultSet.getString("NAME")
                                            ,resultSet.getString("PHONE_NUMBER"),resultSet.getString("SEX")
                                            ,resultSet.getString("TYPE_CUSTOMER"),resultSet.getString("TOTAL_POINT")));
             }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //id, name,phoneNumber,email,sex;
        columnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        columnSex.setCellValueFactory(new PropertyValueFactory<>("sex"));
        columnTypeCustomer.setCellValueFactory(new PropertyValueFactory<>("typeCustomer"));
        columnPoint.setCellValueFactory(new PropertyValueFactory<>("totalPoint"));
        columnID.setStyle( "-fx-alignment: CENTER;");
        columnName.setStyle( "-fx-alignment: CENTER;");
        columnPhoneNumber.setStyle( "-fx-alignment: CENTER;");
        columnTypeCustomer.setStyle( "-fx-alignment: CENTER;");
        columnSex.setStyle( "-fx-alignment: CENTER;");
        columnPoint.setStyle( "-fx-alignment: CENTER;");

        tableViewCustomerInformation.setItems(observableList);
    }


    //LOAD VÀ HIỂN THỊ THÔNG TIN HÓA ĐƠN LÊN BẢNG TỪ DATABASE
    public void loadCustomerBillInformationToTableView(){
        ResultSet resultSet;
        try {
            resultSet = connection.createStatement().executeQuery("SELECT * FROM BILL");
            while (resultSet.next()){
                System.out.println("NAME AND SEATS : "+resultSet.getString("MOVIE_NAME")+" -- "+resultSet.getString("SEATS"));
                observableBill.add(new Bill(resultSet.getString("ID_CUSTOMER"),resultSet.getString("MOVIE_NAME")
                        ,resultSet.getString("CINEMA_NAME"),resultSet.getString("TIME_SLOT")
                        ,resultSet.getString("SEATS"),resultSet.getString("DATE_BOUGHT")
                        ,resultSet.getString("PRICE"),resultSet.getString("DISCOUNT")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //ID_CUSTOMER,MOVIE_NAME,CINEMA_NAME,TIME_SLOT,SEATS,DATE_BOUGHT,PRICE,DISCOUNT
        columnIdBill.setCellValueFactory(new PropertyValueFactory<>("idCustomer"));
        columnMovieName.setCellValueFactory(new PropertyValueFactory<>("movieName"));
        columnCinemaName.setCellValueFactory(new PropertyValueFactory<>("cinemaName"));
        columnTimeSlot.setCellValueFactory(new PropertyValueFactory<>("timeSlot"));
        columnSeats.setCellValueFactory(new PropertyValueFactory<>("seatsText"));
        columnDateBought.setCellValueFactory(new PropertyValueFactory<>("dateBought"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        columnDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        //
        columnIdBill.setStyle( "-fx-alignment: CENTER;");
        columnMovieName.setStyle( "-fx-alignment: CENTER;");
        columnCinemaName.setStyle( "-fx-alignment: CENTER;");
        columnTimeSlot.setStyle( "-fx-alignment: CENTER;");
        columnSeats.setStyle( "-fx-alignment: CENTER;");
        columnDateBought.setStyle( "-fx-alignment: CENTER;");
        columnPrice.setStyle( "-fx-alignment: CENTER;");
        columnDiscount.setStyle( "-fx-alignment: CENTER;");

        tableViewCustomerBill.setItems(observableBill);
    }

    //KHỞI TẠO PHÒNG CHIẾU MỚI
    public void InitCinemaRooms(){
        if(!hasInitComboBox){
            new Thread(() -> {
                for (int i = 0; i < maxCinema; i++) {
                    int I = ++room_id;
                    MovieObject.Movie item = popularMovies.get(0).movies.get(i);
                    movieListSold.add(item);
                    CinemaRoom cinemaRoom = new CinemaRoom("R"+String.valueOf(I),ROOM_NAME+String.valueOf(I));
                    cinemaRoom.setMovieId(item.getId());
                    cinemaRoom.setMovie(item);
                    mapRooms.put(item.getId(),cinemaRoom);

                    for (String timeSlot : Utils.timeSlots) {
                        addSeatsIntoRoom(cinemaRoom,timeSlot);
                    }

                }
                soldTicketsController.setCinemaRooms(mapRooms);
                InitComboBoxCinemaAndTimeSlots();
                //InitializeViewAddMovieToRoom();
            }).start();
        }

    }


    //THÊM CÁC GHẾ NGỒI VÀO PHÒNG CHIẾU MỚI
    public void addSeatsIntoRoom(CinemaRoom cinemaRoom, String timeSlot){
        listSeatsState.clear();
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 21; j++) {
                //MẶC ĐỊNH CÁC GHẾ LÀ NORMAL
                Seat seat = new Seat();
                //TỪ HÀNG 8 TRỞ ĐI SẼ LÀ GHẾ PRIME
                seat.setType(Utils.TYPE_NORMAL);
                if (i >= 8) {
                    seat.setType(Utils.TYPE_PRIME);
                    seat.getTicketType().setType(Utils.TICKER_PRIME);
                }

                String title = Utils.verticalTitle[i] + (j + 1);
                seat.setTitle(title);
                //ADD NEW ITEM TO MAP
                if (!listSeatsState.containsKey(title)) {
                    listSeatsState.put(title, seat);
                }
            }
        }

        mapState.put(timeSlot, new HashMap<>(listSeatsState));
//                        mapLayout.put(timeSlot, new HashMap<String, AnchorPane>(listSeatsLayout));
//                        mapController.put(timeSlot, new HashMap<String, SeatLayoutItemController>(listSeatsController));

        //Map<String,Map<String,Map<String,Boolean>>>
        //KIỂM TRA XEM PHIM NÀY ĐÃ TỒN TẠI CHƯA
        //NẾU CHƯA THÌ THÊM VÀO CÒN RỒI THÌ UPDATE KHUNG GIỜ CHIẾU
        if(!cinemaRoom.mapParentStateSeat.containsKey(cinemaRoom.idMovie)){
            System.out.println("NOT CONTAIN");
            cinemaRoom.mapParentStateSeat.put(cinemaRoom.idMovie, new HashMap<>(mapState));
        }
        else {
            System.out.println("CONTAINNNNNNNNNNNNNNN");
            cinemaRoom.mapParentStateSeat.get(cinemaRoom.idMovie).put(timeSlot, new HashMap<>(listSeatsState));
        }

        mapState.clear();
        listSeatsState.clear();
        System.out.println("KEY SET AFTER CHANGED 2 : "+mapRooms.keySet());
    }

    //KHỞI TẠO LAYOUT VÀ CONTROLLER CHO STAGE LỰA CHỌN GHẾ NGỒI
    public void InitLayoutAndControllerChooseSeats(){
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

                    //controller.seat.getTicketType().setType(Utils.TICKER_NORMAL);
                    //TỪ HÀNG 8 TRỞ ĐI SẼ LÀ GHẾ PRIME
                    if(i >= 8){
                        controller.setType(Utils.TYPE_PRIME);
                        controller.seat.getTicketType().setType(Utils.TICKER_PRIME);
                    }

                    String title = Utils.verticalTitle[i] + (j + 1);
                    controller.seat.setTitle(title);
                    controller.setOnSeatItemClickedListener(soldTicketsController);


                    //ADD NEW ITEM TO MAP
                    if(!listSeatsLayout.containsKey(title)){
                        listSeatsLayout.put(title,anchorPane);
                    }
                    if(!listSeatsController.containsKey(title)){
                        // listSeatsSelected.put(title,false);
                        listSeatsController.put(title,controller);
                    }
                }
        }
        soldTicketsController.setListSeatsLayout(listSeatsLayout);
        soldTicketsController.setListSeatsController(listSeatsController);
        System.out.println("AFTER INITN : "+listSeatsController.size());
        System.out.println("AFTER INITN : "+listSeatsLayout.size());
    }


    //KHỞI TẠO LAYOUT BÁN VÉ
    public void InitGridLayoutSoldMovies(List<MovieObject.Movie> list){
        if(!hasSet){
            columnSold = 0;
            rowSold = 2;
            System.out.println("DO ITTTTTTT");
            hasSet = true;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    gridLayoutSoldMovie.getChildren().clear();
                    for (int i = 0; i < maxCinema; i++) {
                        try {
                            MovieObject.Movie item = list.get(i);
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("view/item_movie_sell.fxml"));
                            AnchorPane anchorPane = null;
                            try {
                                anchorPane = loader.load();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            //ĐƯA DỮ LIỆU VÀO VIEW
                            ItemMovieController itemController = loader.getController();
                            new Thread(() -> itemController.setData(item,MainController.this)).start();

                            //LAYOUT ITEM
                            AnchorPane finalAnchorPane = anchorPane;

                            //CHO CHẠY TRONG HÀM RUN LATER ĐỂ TRÁNH CRASH LUỒNG CHÍNH
                            Platform.runLater(() -> {

                                //NẾU ĐỦ 6 ITEMS TRÊN 1 HÀNG THÌ
                                //CHUYỂN SANG HÀNG TIẾP THEO
                                if (columnSold == 6) {
                                    columnSold = 0;
                                    rowSold++;
                                }

                                gridLayoutSoldMovie.add(finalAnchorPane, columnSold++, rowSold); //(child,column,row)
                            });
                            //set grid width
                            gridLayoutSoldMovie.setMinWidth(Region.USE_COMPUTED_SIZE);
                            gridLayoutSoldMovie.setPrefWidth(Region.USE_COMPUTED_SIZE);
                            gridLayoutSoldMovie.setMaxWidth(Region.USE_PREF_SIZE);

                            //set grid height
                            gridLayoutSoldMovie.setMinHeight(Region.USE_COMPUTED_SIZE);
                            gridLayoutSoldMovie.setPrefHeight(Region.USE_COMPUTED_SIZE);
                            gridLayoutSoldMovie.setMaxHeight(Region.USE_PREF_SIZE);

                            GridPane.setMargin(anchorPane, new Insets(10,0,20,20));
                        }
                        catch (Exception e){

                        }
                    }
                }
            });
        }
    }


    public void setLoginActivityController(LoginActivityController loginActivityController){
        this.loginActivityController = loginActivityController;
    }

    //THAY ĐỔI BACKGROUND CỦA BUTTON MỖI KHI NHẤN VÀO CÁC NÚT HOME, MOVIE SCREEN, MORE...
    @FXML
    private void onButtonClickedChangeBackgroundColor(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() == this.buttonHome) {
            this.changeStateButtonClicked(0);
        } else if (mouseEvent.getSource() == this.buttonMore) {
            this.changeStateButtonClicked(1);
        } else if (mouseEvent.getSource() == this.buttonMovieScreens) {
            this.changeStateButtonClicked(2);
        } else if (mouseEvent.getSource() == this.buttonLogout) {
            this.changeStateButtonClicked(3);
        }

        this.buttonHome.setStyle(this.stateButtonClicked[0] ? "-fx-background-color:#311b4b" : "-fx-background-color:#4a2970");
        this.buttonMore.setStyle(this.stateButtonClicked[1] ? "-fx-background-color:#311b4b" : "-fx-background-color:#4a2970");
        this.buttonMovieScreens.setStyle(this.stateButtonClicked[2] ? "-fx-background-color:#311b4b" : "-fx-background-color:#4a2970");
        this.buttonLogout.setStyle(this.stateButtonClicked[3] ? "-fx-background-color:#311b4b" : "-fx-background-color:#4a2970");


    }

    //THAY ĐỔI BACKGROUND MỖI KHI HOVERED CHO BUTTON HOME,MORE,LOGOUT,MOVIES SCREENS
    @FXML
    private void onMouseHoveredOnButton(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() == this.buttonHome) {
            this.changeStateButtonHovered(0);
        } else if (mouseEvent.getSource() == this.buttonMore) {
            this.changeStateButtonHovered(1);
        } else if (mouseEvent.getSource() == this.buttonMovieScreens) {
            this.changeStateButtonHovered(2);
        } else if (mouseEvent.getSource() == this.buttonLogout) {
            this.changeStateButtonHovered(3);
        }

        this.buttonHome.setStyle(this.stateButtonClicked[0] ? "-fx-background-color:#311b4b" : (this.stateButtonHovered[0] ? "-fx-background-color:#633795" : "-fx-background-color:#4a2970"));
        this.buttonMore.setStyle(this.stateButtonClicked[1] ? "-fx-background-color:#311b4b" : (this.stateButtonHovered[1] ? "-fx-background-color:#633795" : "-fx-background-color:#4a2970"));
        this.buttonMovieScreens.setStyle(this.stateButtonClicked[2] ? "-fx-background-color:#311b4b" : (this.stateButtonHovered[2] ? "-fx-background-color:#633795" : "-fx-background-color:#4a2970"));
        this.buttonLogout.setStyle(this.stateButtonClicked[3] ? "-fx-background-color:#311b4b" : (this.stateButtonHovered[3] ? "-fx-background-color:#633795" : "-fx-background-color:#4a2970"));
    }


    //BẮT EVENT CHO CÁC NÚT HOME,MORE,LOGOUT,MOVIE SCREENS
    @FXML
    private void onButtonClicked(ActionEvent actionEvent) {
        if (actionEvent.getSource() == this.buttonHome) {
            this.PaneHome.toFront();
        } else if (actionEvent.getSource() == this.buttonMore) {
            scrollPane.toFront();
            this.PaneMore.toFront();
        } else if (actionEvent.getSource() == this.buttonMovieScreens) {
            this.PaneMovieScreens.toFront();
        } else if (actionEvent.getSource() == this.buttonLogout) {
            this.PaneLogOut.toFront();
        }  //NULL


    }


    //THAY ĐỔI TRẠNG THÁI CLICKED OR NOT CỦA CÁC NÚT HOME,MORE,LOGOUT,MOVIE SCREENS
    public void changeStateButtonClicked(int index) {
        for(int i = 0; i < this.stateButtonClicked.length; ++i) {
            if (index == i) {
                this.stateButtonClicked[i] = true;
            } else {
                this.stateButtonClicked[i] = false;
            }
        }

    }

    //THAY ĐỔI TRẠNG THÁI HOVERED CỦA CÁC NÚT HOME,MORE,LOGOUT,MOVIE SCREENS
    public void changeStateButtonHovered(int index) {
        for(int i = 0; i < this.stateButtonHovered.length; ++i) {
            if (index == i) {
                this.stateButtonHovered[i] = true;
            } else {
                this.stateButtonHovered[i] = false;
            }
        }

    }

    //FUNCTIONS
    //LẤY DỮ LIỆU TỪ API
    //VÀ ĐỒNG THỜI ĐỒNG BỘ CÁC THREAD CÙNG TRUY XUẤT ĐẾN HÀM NÀY
    public synchronized void getDataFromAPI(int page) {
        API.api.getMoviesPopular(Utils.API_KEY,String.valueOf(page)).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<MovieObject> call, Response<MovieObject> response) {
                //NẾU DỮ LIỆU TRẢ VỀ KHÔNG NULL THÌ UP DATA LÊN VIEWS
                if (response.body().getMovies().size() > 0) {
                    Platform.runLater(() -> loadingPane.toBack());
                    popularMovies.add(response.body());
                    MovieObject movieObject = response.body();
                    maxPageMain = Integer.parseInt(movieObject.getTotal_pages());
                    for (int i = 0; i < movieObject.getMovies().size(); i++) {
                        MovieObject.Movie item = movieObject.getMovies().get(i);
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("view/item_movie_add_for_sell.fxml"));
                        AnchorPane anchorPane = null;
                        try {
                            anchorPane = loader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //ĐƯA DỮ LIỆU VÀO VIEW
                        ItemMovieAddForSellController itemController = loader.getController();
                        new Thread(() -> itemController.setData(item, MainController.this)).start();

                        //LAYOUT ITEM
                        AnchorPane finalAnchorPane = anchorPane;

                        //CHO CHẠY TRONG HÀM RUN LATER ĐỂ TRÁNH CRASH LUỒNG CHÍNH
                        Platform.runLater(() -> {

                            //NẾU ĐỦ 6 ITEMS TRÊN 1 HÀNG THÌ
                            //CHUYỂN SANG HÀNG TIẾP THEO
                            if (column == 6) {
                                column = 0;
                                row++;
                            }

                            gridAdapter.add(finalAnchorPane, column++, row); //(child,column,row)
                        });
                        //set grid width
                        gridAdapter.setMinWidth(Region.USE_COMPUTED_SIZE);
                        gridAdapter.setPrefWidth(Region.USE_COMPUTED_SIZE);
                        gridAdapter.setMaxWidth(Region.USE_PREF_SIZE);

                        //set grid height
                        gridAdapter.setMinHeight(Region.USE_COMPUTED_SIZE);
                        gridAdapter.setPrefHeight(Region.USE_COMPUTED_SIZE);
                        gridAdapter.setMaxHeight(Region.USE_PREF_SIZE);

                        GridPane.setMargin(anchorPane, new Insets(10, 0, 20, 20));
                    }
                    //ĐẨY MÀN HÌNH LOADING VỀ CUỐI VÀ ĐƯA MÀN HÌNH HIỂN THỊ CÁC PHIM LÊN ĐẦU
                    Platform.runLater(() -> {
                        System.out.println("GET SUCCESSFULLY");
                        loadingPane.toBack();
                    });
                }
                //NẾU NULL THÌ GỌI LẠI API LẤY DỮ LIỆU CỦA PAGE KHÁC ĐỔ VỀ
                else {
                    new Thread(() -> {
                        if (pageMain < maxPageMain) {
                            try {
                                getDataFromAPI(++pageMain);
                            } catch (Exception e) {
                                //HIỂN THỊ THÔNG BÁO NẾU QUÁ TRÌNH KẾT NỐI API THẤT BẠI
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText("Connection error, please check the network again");
                            }
                        }
                    }).start();
                }
                InitGridLayoutSoldMovies(popularMovies.get(0).getMovies());
                InitCinemaRooms();
                Platform.runLater(() -> InitializeViewAddMovieToRoom());
            }

            @Override
            public void onFailure(Call<MovieObject> call, Throwable throwable) {

            }
        });
    }


    //KHI MỖI ITEM PHIM ĐƯỢC NHẤN THÌ SẼ NHẢY VÀO FUNCTION NÀY
    @Override
    public void onClicked(MovieObject.Movie item) {

    }

    //NÚT MUA VÉ XEM PHIM DƯỚI MỖI ITEM PHIM
    @Override
    public void onButtonBuyClicked(MovieObject.Movie item) {
        if(soldTicketsController.mapRooms.containsKey(item.getId())){
            System.out.println("PHIM DANG MO BAN");
            if(soldTicketsController.mainController == null && soldTicketsController.movieSoldRecentlyController == null){
                soldTicketsController.setMainController(this);
                soldTicketsController.setCinemaRooms(mapRooms);
                soldTicketsController.setMovieSoldRecentlyController(movieSoldRecentlyController);
            }

            //MÓC API ĐỂ LẤY THÔNG TIN CHI TIẾT CỦA PHIM
            API.api.getDetailMovie(item.getId(),Utils.API_KEY).enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<TempObject> call, Response<TempObject> response) {
                    TempObject tempObject = response.body();
                    item.setGenres(tempObject.getGenres());
                    item.setRelease_date(tempObject.getRelease_date());
                    item.setRuntime(tempObject.getRuntime());
                    item.setTagline(tempObject.getTagline());
                    System.out.println("LOAD MOVIE IDDDDDDDDDDDDDDDDDDDDDDDDD : " + item.getId());
                    soldTicketsController.setMovie(item);
                    soldTicketsController.setContent(item);
                    soldTicketsController.InitLayoutSeatsSelected(Utils.SLOT_9_45_AM);
                    Platform.runLater(() -> {
                        soldTicketsController.customer = new Customer();
                        soldTicketsController.customer.getBill().setMovie(item);
                        stageSellTickets.show();
                    });
                }

                @Override
                public void onFailure(Call<TempObject> call, Throwable throwable) {

                }
            });
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Add Movie To Room");
            alert.setHeaderText("The movie is not open for sale yet, please choose another movie.");
            alert.initOwner(buttonSearch.getScene().getWindow());
            alert.showAndWait();
        }
    }

    //BẮT SỰ KIỆN BUTTON THÊM PHIM VÀO RẠP CHIẾU NÀO ĐÓ
    @Override
    public void onButtonAddToRoom(MovieObject.Movie item) {
        if(!hasInitViewAddToRoom){
            System.out.println("DO ONECE AGAIN");
            InitializeViewAddMovieToRoom();
        }

        if(!movieListSold.contains(item)){
            addMovieToRoomController.setContentView(item);
            addMovieToRoomController.InitComboBox();
            stageAddToRoom.show();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Add Movie To Room");
            alert.setHeaderText("The movie has been added please choose another movie.");
            alert.initOwner(buttonSearch.getScene().getWindow());
            alert.showAndWait();
        }
    }

    public void InitializeViewAddMovieToRoom(){
        hasInitViewAddToRoom = true;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("view/layout_add_movie_to_room.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(loader.load(),640,450);
        } catch (IOException e) {
            e.printStackTrace();
        }
        addMovieToRoomController = loader.getController();
        addMovieToRoomController.setMainController(this);
        try {
            stageAddToRoom.initModality(Modality.APPLICATION_MODAL);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        stageAddToRoom.setScene(scene);
        try {
            stageAddToRoom.getIcons().add(new Image(new FileInputStream("C:\\Users\\duy khan\\IdeaProjects\\MovieTickets\\src\\main\\resources\\com\\example\\movietickets\\image\\background_login.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("END DDDDDDDDDDINITTTTTTTTTTTTTTT");
    }


    //BẮT SỰ KIỆN CLICK TRÊN BUTTON SEARCH
    //NẾU RỖNG THÌ KHÔNG CHUYỂN LAYOUT, NGƯỢC LẠI THÌ CHUYỂN
    @FXML
    public void onClickSearch(ActionEvent actionEvent){
        System.out.println("SIZE LIST MAP : "+mapRooms.size());
        SearchMovie();
    }


    //BẮT SỰ ENTER TRÊN TEXT FIELD FIND FILM BY TITLE ĐỂ BẮT ĐẦU TÌM KIẾM PHIM
    //THAY VÌ PHẢI NHẤN NÚT SEARCH
    public void onEnterPressed(KeyEvent keyEvent){
        if(keyEvent.getCode() == KeyCode.ENTER){
            System.out.println("ENTER PRESSED ON TEXTFIELD");
            SearchMovie();
        }
    }

    //HÀM KẾT NỐI ĐẾN API LẤY DANH SÁCH PHIM THEO TÊN ĐÃ NHẬP
    public synchronized void SearchMovie(){
        System.out.println("BUTTON SEARCH");
        String keyword = textFieldSearch.getText().trim().toString().toLowerCase();
        //NẾU KEYWORD MỚI VÀ CŨ TRÙNG NHAU THÌ KHÔNG CẦN LOAD LẠI
        if(!oldKeyword.equals(keyword)){
            Platform.runLater(() -> loadingPane.toFront());
            System.out.println("DIFFERENT KEYWORD");
            oldKeyword = keyword;
            if(!keyword.isEmpty()){
                System.out.println("NOT NULLLLLLLL KEYWORD");
                pageSearch = 1;
                columnSearch = 0;
                rowSearch = 1;
                if(gridLayoutItemSearch.getChildren().size() > 0){
                    gridLayoutItemSearch.getChildren().clear();
                }
                //BẮT ĐẦU LẤY DỮ LIỆU TỪ API
                try {
                    new Thread(() -> InitLayoutContainMoviesResult(keyword,pageSearch)).start();
                }
                catch (Exception e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Connection error, please check the network again");
                }
            }
            else{
                System.out.println("NULLLLLLLLLLLLLLLLLLLL");
            }
        }
        if(!keyword.trim().isEmpty()){
            scrollPaneSearch.toFront();
        }

    }


    //KHỞI TẠO LAYOUT HIỂN THỊ DANH SÁCH PHIM TÌM KIẾM
    public synchronized void InitLayoutContainMoviesResult(String keyword,int page){
        API.api.getMoviesByKeyword(Utils.API_KEY,keyword,String.valueOf(page)).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<MovieObject> call, Response<MovieObject> response) {
                System.out.println("PAGE SEARCH : " + page);
                if (response.body().getMovies().size() > 0) {
                    Platform.runLater(() -> loadingPane.toBack());
                    MovieObject movieObject = response.body();
                    maxPageSearch = Integer.parseInt(movieObject.getTotal_pages());
                    for (int i = 0; i < movieObject.getMovies().size(); i++) {
                        MovieObject.Movie item = movieObject.getMovies().get(i);

                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("view/item_movie_add_for_sell.fxml"));
                        AnchorPane anchorPane = null;
                        try {
                            anchorPane = loader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //ĐƯA DỮ LIỆU VÀO VIEW
                        ItemMovieAddForSellController itemController = loader.getController();
                        new Thread(() -> itemController.setData(item, MainController.this)).start();

                        //LAYOUT ITEM
                        AnchorPane finalAnchorPane = anchorPane;

                        //CHO CHẠY TRONG HÀM RUN LATER ĐỂ TRÁNH CRASH LUỒNG CHÍNH
                        Platform.runLater(() -> {

                            //NẾU ĐỦ 6 ITEMS TRÊN 1 HÀNG THÌ
                            //CHUYỂN SANG HÀNG TIẾP THEO
                            if (columnSearch == 6) {
                                columnSearch = 0;
                                rowSearch++;
                            }
                            gridLayoutItemSearch.add(finalAnchorPane, columnSearch++, rowSearch); //(child,column,row)
                            //set grid width
                        });

                        gridLayoutItemSearch.setMinWidth(Region.USE_COMPUTED_SIZE);
                        gridLayoutItemSearch.setPrefWidth(Region.USE_COMPUTED_SIZE);
                        gridLayoutItemSearch.setMaxWidth(Region.USE_PREF_SIZE);

                        //set grid height
                        gridLayoutItemSearch.setMinHeight(Region.USE_COMPUTED_SIZE);
                        gridLayoutItemSearch.setPrefHeight(Region.USE_COMPUTED_SIZE);
                        gridLayoutItemSearch.setMaxHeight(Region.USE_PREF_SIZE);
                        GridPane.setMargin(anchorPane, new Insets(10, 0, 10, 20));
                    }
                } else {
                    Platform.runLater(() -> loadingPane.toBack());
                    pageSearch = 1;
                    maxPageSearch = 0;
                    //HIỂN THỊ THÔNG BÁO KHÔNG CÓ KẾT QUẢ NÀO TRÙNG KHỚP VỚI KEYWORD ĐÃ NHẬP
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Notification !");
                        alert.setHeaderText("There are no matching results !");
                        alert.initOwner(gridLayoutItemSearch.getScene().getWindow());
                        alert.showAndWait();
                    });
                }
            }

            @Override
            public void onFailure(Call<MovieObject> call, Throwable throwable) {

            }
        });
    }


    //HIỂN THỊ DANH SÁCH CÁC PHIM TÌM KIẾM GẦN ĐÂY
    @FXML
    public void showMoviesTicketsSoldRecently(ActionEvent actionEvent){
        stageMoviesRecently.show();
    }

    //KHỞI TẠO VIEWS DANH SÁCH VÉ PHIM BÁN GẦN ĐÂY
    public void InitStageMoviesRecently(){
        stageMoviesRecently = new Stage();
        stageMoviesRecently.setResizable(false);
        stageMoviesRecently.setTitle("Recently Sold Movie Tickets");
        //CHẶN VIỆC CLOSE PARENT STAGE CHO ĐẾN KHI CHILDREN STAGE CLOSED
        stageMoviesRecently.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("view/movie_sold_recently.fxml"));
        try {
            sceneMoviesRecently = new Scene(loader.load(),850,650);
        } catch (IOException e) {
            e.printStackTrace();
        }
        movieSoldRecentlyController = loader.getController();
        movieSoldRecentlyController.setOnItemClickedListener(this);
        movieSoldRecentlyController.setMovies(getMoviesSoldRecently());
        stageMoviesRecently.setScene(sceneMoviesRecently);
    }


    //KHỞI TẠO LAYOUT BÁN VÉ
    public void InitLayoutSellTickets(){
        stageSellTickets = new Stage();
        stageSellTickets.setTitle("Sell Tickets");
        stageSellTickets.initModality(Modality.APPLICATION_MODAL);
        try {
            stageSellTickets.getIcons().add(new Image(new FileInputStream("C:\\Users\\duy khan\\IdeaProjects\\MovieTickets\\src\\main\\resources\\com\\example\\movietickets\\image\\background_login.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("view/layout_sold_tickets.fxml"));
        sceneSellTickets = null;
        try {
            sceneSellTickets = new Scene(loader.load(),1300,650);
        } catch (IOException e) {
            e.printStackTrace();
        }
        soldTicketsController = loader.getController();
        stageSellTickets.setScene(sceneSellTickets);
        stageSellTickets.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                System.out.println("ON STAGE SELL TICKETS CLOSE");
                soldTicketsController.unCheckSeatsWhenCloseNotBookingTickets();
                soldTicketsController.seatsSelected.setText("");
                soldTicketsController.seatNumber.setText("");
                soldTicketsController.price.setText("");
                soldTicketsController.button945AM.setStyle("-fx-background-color: #d98609");
                soldTicketsController.button100PM.setStyle("-fx-background-color: #4d913d");
                soldTicketsController.button345PM.setStyle("-fx-background-color: #4d913d");
                soldTicketsController.button700PM.setStyle("-fx-background-color: #4d913d");
                soldTicketsController.button945PM.setStyle("-fx-background-color: #4d913d");
                soldTicketsController.timeSlot = Utils.SLOT_9_45_AM;
            }
        });
    }


    //TRẢ VỀ DANH SÁCH VÉ PHIM BÁN GẦN ĐÂY TỪ HASHTABLE
    public List<MovieObject.Movie> getMoviesSoldRecently(){
        if(moviesSoldRecently.size() > 0){
            List<MovieObject.Movie> list = new ArrayList<>();
            for (String key : moviesSoldRecently.keySet()){
                list.add(moviesSoldRecently.get(key));
            }
            return list;
        }
        return null;
    }



    //BẮT SỰ KIỆN BUTTON ĐĂNG KÍ
    @FXML
    public void onButtonSignUp(ActionEvent actionEvent){
        Stage stageSignUp = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("view/sign_up_layout.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 430, 530);
        } catch (IOException e) {
            e.printStackTrace();
        }
        stageSignUp.setScene(scene);
        stageSignUp.initModality(Modality.APPLICATION_MODAL);
        stageSignUp.setResizable(false);
        stageSignUp.show();
    }

    //BẮT SỰ KIỆN BUTTON ĐỔI MẬT KHẨU
    @FXML
    public void onButtonChangePassword(ActionEvent actionEvent){
        Stage stageChangePassword = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("view/forgot_password_layout.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(),500,450);
        } catch (IOException e) {
            e.printStackTrace();
        }
        stageChangePassword.setScene(scene);
        stageChangePassword.initModality(Modality.APPLICATION_MODAL);
        stageChangePassword.setResizable(false);
        stageChangePassword.show();
    }


    //BẮT SỰ KIỆN BUTTON ĐĂNG XUẤT
    @FXML
    public void onButtonLogout(ActionEvent actionEvent){
        ButtonType Ok = new ButtonType("OK");
        ButtonType Cancel = new ButtonType("Cancel");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getButtonTypes().clear();
        alert.getButtonTypes().add(Ok);
        alert.getButtonTypes().add(Cancel);
        alert.setTitle("Log Out");
        alert.setHeaderText("Are you sure to exit the session ?");
        alert.initOwner(buttonLogout.getScene().getWindow());
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == Ok){
            Platform.exit();
        }
        else if(result.get() == Cancel){
            alert.close();
        }
    }


    //KHỞI TẠO DANH SÁCH GHẾ MOVIE SCREEN
    public void InitLayoutMovieScreen(){
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
                            gridPaneMovieScreen.add(anchorPane,j+3,i+1);
                        }
                    }
                    else if(i == 1){
                        if(j != 0 && j != 20){
                            gridPaneMovieScreen.add(anchorPane,j+3,i+1);
                        }
                    }
                    else if(i == 2 || i == 3 || i ==4 || i == 6 || i == 7 || i ==8 || i == 9 || i == 10){
                        if(j != 10){
                            gridPaneMovieScreen.add(anchorPane,j+3,i+1);
                        }
                    }
                    //TỪ HÀNG 8 TRỞ ĐI SẼ LÀ GHẾ PRIME
                    if(i >= 8){
                        controller.setType(Utils.TYPE_PRIME);
                        controller.seat.getTicketType().setType(Utils.TICKER_PRIME);
                    }

                    String title = Utils.verticalTitle[i] + (j + 1);

                    controller.seat.setTitle(title);
                    controller.seat.setEnable(false);
                    controller.setOnSeatItemClickedListener(soldTicketsController);


                    //ADD NEW ITEM TO MAP
                    if(!listSeatsLayoutMovieScreen.containsKey(title)){
                        listSeatsLayoutMovieScreen.put(title,anchorPane);
                    }
                    if(!listSeatsControllerMovieScreen.containsKey(title)){
                        // listSeatsSelected.put(title,false);
                        listSeatsControllerMovieScreen.put(title,controller);
                    }

                    gridPaneMovieScreen.setMinWidth(Region.USE_COMPUTED_SIZE);
                    gridPaneMovieScreen.setPrefWidth(Region.USE_COMPUTED_SIZE);
                    gridPaneMovieScreen.setMaxWidth(Region.USE_PREF_SIZE);

                    //set grid height
                    gridPaneMovieScreen.setMinHeight(Region.USE_COMPUTED_SIZE);
                    gridPaneMovieScreen.setPrefHeight(Region.USE_COMPUTED_SIZE);
                    gridPaneMovieScreen.setMaxHeight(Region.USE_PREF_SIZE);

                    if(i == 4){
                        GridPane.setMargin(anchorPane, new Insets(3,0,25,5));
                    }
                    else{
                        GridPane.setMargin(anchorPane, new Insets(3,0,5,5));
                    }
                }
            }
        addVerticalAndHorizontalAxisTitle(gridPaneMovieScreen);
    }

    //KHỞI TẠO COMBOBOX CÁC DANH SÁCH CÁC PHÒNG CHIẾU
    //KHỞI TẠO COMBOBOX CÁC KHUNG GIỜ CHIẾU
    public void InitComboBoxCinemaAndTimeSlots(){
        //COMBOBOX TIMESLOTS
        if(!hasInitComboBox){
            hasInitComboBox = true;
            for (String i : Utils.timeSlots){
                comboBoxTimeSlots.getItems().add(i);
            }
            for (CinemaRoom room : mapRooms.values()){
                listCinemaName.add(room.nameRoom);
            }
            listCinemaName.sort(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return Integer.parseInt(String.valueOf(o1.charAt(o1.length()-1))) - Integer.parseInt(String.valueOf(o2.charAt(o2.length()-1)));
                }
            });
            for (String item : listCinemaName){
                comboBoxCinemaMovieScreen.getItems().add(item);
                comboBoxDeleteCinema.getItems().add(item);
            }
            comboBoxCinemaMovieScreen.getEditor().setFont(new Font("Agency FB",20));
            Platform.runLater(() -> {
                comboBoxTimeSlots.getSelectionModel().selectFirst();
                comboBoxCinemaMovieScreen.getSelectionModel().selectFirst();
                comboBoxDeleteCinema.getSelectionModel().selectFirst();
            });
        }
    }



    public void addVerticalAndHorizontalAxisTitle(GridPane grid){
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
                grid.add(anchorPaneLeft,0,i+2);
                grid.add(anchorPaneRight,28,i+2);
            }
            else{
                grid.add(anchorPaneLeft,0,i+1);
                grid.add(anchorPaneRight,28,i+1);
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

                grid.add(anchorPane,i+3,12);
                GridPane.setMargin(anchorPane, new Insets(10,0,5,5));
            }
        }

    }

    ///////BẮT SỰ KIỆN NÚT RELOAD


    @FXML
    public void onButtonReload(ActionEvent actionEvent){
        RefreshStateLayoutSeatsMovieScreen();
    }

    public void RefreshStateLayoutSeatsMovieScreen(){
        if(posComboBoxCinema != -1){

            System.out.println("SIZEEEEEEEEEEEEEEEE : "+movieListSold.size()+" -- "+posComboBoxCinema);
            for (MovieObject.Movie a : movieListSold){
                System.out.println("MOVIEEEEEEEEEEEEEEEE : "+a.getId()+" -- "+a.getTitle());
            }
            String id = "0";
            try {
                 id = movieListSold.get(posComboBoxCinema).getId();
            }
            catch (Exception e){

            }
            //String timeSlot = comboBoxTimeSlots.getSelectionModel().getSelectedIndex();
            listSeatsState = mapRooms.get(id).mapParentStateSeat.get(id).get(Utils.timeSlots[posComboBoxTimeSlots]);

            System.out.println("SIZE LIST SEATS : "+listSeatsState.size());
            System.out.println("SIZE LIST SEATS : "+mapRooms.get(id).mapParentStateSeat.get(id).get(Utils.timeSlots[posComboBoxTimeSlots]).size());
            for (String key : listSeatsState.keySet()){
                System.out.println("KEY : "+key);
                Seat seat = listSeatsState.get(key);
                listSeatsControllerMovieScreen.get(key).setSeat(seat);
                listSeatsControllerMovieScreen.get(key).setType(seat.getType());
            }
            System.out.println("ON RELOADDDDDDDDDDDDDDDDDDD");
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Add Cinema");
            alert.setHeaderText("Please select the correct cinema and timeslot !");
            alert.initOwner(buttonAddCinema.getScene().getWindow());
            alert.showAndWait();
        }
    }



    //BẮT SỰ KIỆN TRÊN BUTTON THÊM PHÒNG CHIẾU PHIM

    @FXML
    public void onButtonAddNewCinema(ActionEvent actionEvent){
        addNewCinema();
    }


    //THÊM RẠP CHIẾU MỚI
    public void addNewCinema(){
        String idText = idCinemaText.getText().trim();
        String nameText = nameCinemaText.getText().trim();
        boolean idCinemaTextBoolean = idText.isEmpty();
        boolean nameCinemaTextBoolean = nameText.isEmpty();
        if(!listCinemaName.contains(nameText)){
            listCinemaName.add(nameText);
            if(!idCinemaTextBoolean && !nameCinemaTextBoolean){
                ++maxCinema;
                CinemaRoom cinemaRoom = new CinemaRoom(idText,nameText);
                cinemaRoom.setMovieId("TEMP");
                cinemaRoom.setMovie(null);
                mapRooms.put("TEMP",cinemaRoom);

                for (String timeSlot : Utils.timeSlots) {
                    addSeatsIntoRoom(cinemaRoom,timeSlot);
                }

                Platform.runLater(() -> {
                    comboBoxCinemaMovieScreen.getItems().add(nameText);
                    comboBoxDeleteCinema.getItems().add(nameText);
                    addMovieToRoomController.addNewItemToComboBox(nameText);
                });
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Add Cinema");
                alert.setHeaderText("Congratulation! Successfully add cinema.");
                alert.initOwner(buttonAddCinema.getScene().getWindow());
                alert.showAndWait();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Add Cinema");
                alert.setHeaderText("Please enter full cinema information !");
                alert.initOwner(buttonAddCinema.getScene().getWindow());
                alert.showAndWait();
            }
        }
        else if(idCinemaTextBoolean || nameCinemaTextBoolean){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Add Cinema");
            alert.setHeaderText("Please fill out all fields !");
            alert.initOwner(buttonAddCinema.getScene().getWindow());
            alert.showAndWait();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Add Cinema");
            alert.setHeaderText("This cinema already exists, please choose another id and name !");
            alert.initOwner(buttonAddCinema.getScene().getWindow());
            alert.showAndWait();
        }
    }


    //BẮT SỰ KIỆN XÓA 1 PHÒNG CHIẾU NÀO ĐÓ
    @FXML
    public void onButtonDeleteCinema(ActionEvent actionEvent){
        deleteCinema();
    }

    //XÓA MỘT RẠP CHIẾU NÀO ĐÓ
    public void deleteCinema(){
        //ĐẦU TIÊN CHỌN RẠP CHIẾU
        //SAU ĐÓ HIỂN THỊ THÔNG BÁO CHẮC CHẮC MUỐN XÓA RẠP NÀY KHÔNG
        ButtonType agreeDelete = new ButtonType("Sure");
        ButtonType disAgreeDelete = new ButtonType("Cancel");
        Alert alertConfirm = new Alert(Alert.AlertType.WARNING);
        alertConfirm.setTitle("Delete Cinema");
        alertConfirm.setHeaderText("Do you sure to delete this cinema ?");
        alertConfirm.initOwner(buttonDeleteCinema.getScene().getWindow());
        alertConfirm.getButtonTypes().clear();
        alertConfirm.getButtonTypes().addAll(agreeDelete,disAgreeDelete);

        Optional<ButtonType> result = alertConfirm.showAndWait();
        //NẾU ĐỒNG Ý THÌ XÓA, KHÔNG THÌ BỎ QUA
        if(result.get() == agreeDelete){
            String itemDelete = comboBoxDeleteCinema.getValue();
            int indexDelete = comboBoxDeleteCinema.getSelectionModel().getSelectedIndex();
            if( indexDelete != -1){
                try {
                    mapRooms.remove(itemDelete);
                    listCinemaName.remove(itemDelete);
                    movieListSold.remove(indexDelete);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                hasSet = false;
                Platform.runLater(() -> InitGridLayoutSoldMovies(movieListSold));
                Platform.runLater(() -> {
                    comboBoxCinemaMovieScreen.getItems().remove(indexDelete);
                    comboBoxDeleteCinema.getItems().remove(indexDelete);
                    addMovieToRoomController.removeItemOfComboBox(itemDelete);
                });
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Delete Cinema");
                alert.setHeaderText("Remove the cinema successfully !");
                alert.initOwner(buttonDeleteCinema.getScene().getWindow());
                alert.showAndWait();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Delete Cinema");
                alert.setHeaderText("Please select the correct cinema !");
                alert.initOwner(buttonDeleteCinema.getScene().getWindow());
                alert.showAndWait();
            }
        }
    }

    // BUTTON XUẤT DANH SÁCH KHÁCH HÀNG VÀ HÓA ĐƠN KHÁCH HÀNG RA FILE EXCEL
    @FXML
    public void onButtonExcel(ActionEvent actionEvent){
        List<Customer> customers = new ArrayList<>(tableViewCustomerInformation.getItems());
        List<Bill> bills = new ArrayList<>(tableViewCustomerBill.getItems());
        exportCustomerInformationToExcel(customers,bills);
    }

    //HÀM XUẤT DANH SÁCH KHÁCH HÀNG VÀ HÓA ĐƠN KHÁCH HÀNG RA FILE EXCEL
    public void exportCustomerInformationToExcel(List<Customer> customers,List<Bill> bills ){
        //MỞ BẢNG CHỌN NƠI LƯU FILE
        FileChooser fileChooser = new FileChooser();
        WriteFileExcel writeFileExcel = new WriteFileExcel();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel","*.xlsx"));
        File file = fileChooser.showSaveDialog(null);
        //NẾU TRẢ VỀ FILE HỢP LỆ THÌ LƯU KHÔNG THÌ THÔNG BÁO CHƯA CHỌN NƠI LƯU FILE
        if(file != null){
            writeFileExcel.createFileExcelCustomerInformation(file.getAbsolutePath(),customers,bills);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Save Excel File");
            alert.setHeaderText("Export excel file successfully !");
            alert.initOwner(buttonExcel.getScene().getWindow());
            alert.showAndWait();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Save Excel File");
            alert.setHeaderText("Please select the appropriate file location !");
            alert.initOwner(buttonExcel.getScene().getWindow());
            alert.showAndWait();
        }
    }

    public int countItems(){
        String query = "SELECT COUNT(*) AS COUNT FROM CUSTOMER";
        ResultSet resultSet = null ;
        int count = 0;
        try {
            resultSet = connection.createStatement().executeQuery(query);
            resultSet.next();
            count = Integer.parseInt(resultSet.getString("COUNT"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

}