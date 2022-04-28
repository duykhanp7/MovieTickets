package com.example.movietickets;

import com.example.movietickets.interf.OnSeatItemClickedListener;
import com.example.movietickets.model.*;
import com.example.movietickets.utils.Utils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
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
    Map<String,Seat> listSeatsState = new HashMap<>();

    Map<String,AnchorPane> listSeatsLayout = new HashMap<>();
    Map<String,SeatLayoutItemController> listSeatsController = new HashMap<>();

    //Map<String,Map<String,Seat>> mapState = new HashMap<>();

//    Map<String,Map<String,AnchorPane>> mapLayout = new HashMap<>();
//    Map<String,Map<String,SeatLayoutItemController>> mapController = new HashMap<>();
    //MỘT HASHMAP ĐỂ ÁNH XẠ TÊN GHẾ VÀ TRẠNG THÁI GHẾ ĐÓ ĐÃ CÓ AI ĐẶT CHƯA
    //Map<String,Boolean> listSeatsSelected = new HashMap<>();
    //MỘT LIST CHỨA CÁC SEAT CONTROLLER ĐỂ CHỈNH SỬA TYPE MỖI KHI ẤN NÚT BOOK TICKETS
    //MỖI KHI ĐỒNG Ý ĐẶT VÉ THÌ CÁC VỊ TRÍ ĐÓ SẼ CHUYỂN SANG ĐỎ
    //STRING : KHUNG THỜI GIAN CHIẾU PHIM
    String timeSlot = Utils.SLOT_9_45_AM;
    //TRẠNG THÁI CLICKED CỦA CÁC TIME SLOT, MẶC ĐỊNH LÀ FALSE CHƯA CLICKED
    Boolean[] stateButtonTimeSlot = new Boolean[]{false,false,false,false,false};
    //NÚT ĐỒNG Ý ĐẶT VÉ THÌ THÊM PHIM VỪA ĐẶT VÀO DANH SÁCH PHIM VỪA BÁN
    MovieSoldRecentlyController movieSoldRecentlyController;
    MainController mainController;
    //PHIM ĐƯỢC XÁC NHẬN ĐỂ MUA
    MovieObject.Movie movie;
    //
    int row = 0;
    int column = 0;
    //DANH SÁCH CÁC PHÒNG CHIẾU PHIM
    Map<String, CinemaRoom> mapRooms = new HashMap<>();
    @FXML Label textViewRoomName;
    //STAGE INFORMATION CUSTOMER
    Stage stageGetInformation ;
    LayoutInputCustomerInfoController layoutInputCustomerInfoController;
    boolean valid = false;
    //CUSTOMER
    Customer customer = new Customer();
    ConnectionToDatabaseController controller;
    Connection connection;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //InitializeLayoutSeats();
        controller = new ConnectionToDatabaseController();
        connection = controller.getConnection();
    }


    public void setMovie(MovieObject.Movie item){
        this.movie = item;
        //cinemaRoom.setMovie(item);
    }

    public void setMovieSoldRecentlyController(MovieSoldRecentlyController movieSoldRecentlyController){
        this.movieSoldRecentlyController = movieSoldRecentlyController;
    }

    public void setMainController(MainController mainController){
        this.mainController = mainController;
    }

    public void setCinemaRooms(Map<String, CinemaRoom> mapRooms){
        this.mapRooms = mapRooms;
        //cinemaRoom = mapRooms.get(0);
    }

    public void setListSeatsLayout(Map<String,AnchorPane> listSeatsLayout){
        this.listSeatsLayout = listSeatsLayout;
    }

    public void setListSeatsController(Map<String,SeatLayoutItemController> listSeatsController){
        this.listSeatsController = listSeatsController;
    }



    public void RefreshStateLayoutSeats(String time, MovieObject.Movie movie){
        this.movie = movie;
        seatsSelected.setText("");
        seatNumber.setText("");
        price.setText("");
        System.out.println("KEY SETTTTTTTTTTTTTTTTTT : "+mapRooms.keySet()+" -- "+movie.id);
        listSeatsState = mapRooms.get(movie.getId()).mapParentStateSeat.get(movie.getId()).get(time);
        for (String key : listSeatsState.keySet()){
            Seat seat = listSeatsState.get(key);
            System.out.println("KEY : "+key+" -- "+seat.getType());
            listSeatsController.get(key).setSeat(seat);
            listSeatsController.get(key).setType(seat.getType());
        }
    }

    public void InitLayoutSeatsSelected(String time){
        listSeatsState = mapRooms.get(movie.getId()).mapParentStateSeat.get(movie.getId()).get(time);
        System.out.println("INITTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT : "+movie.getId()+" -- "+listSeatsState.size());
        CinemaRoom cinemaRoom = mapRooms.get(movie.getId());
        seatNumberString.clear();
        gridPaneLayoutSeats.getChildren().clear();
        String[] verticalTitle = new String[]{"J","I","H","G","F","","E","D","C","B","A"};
        //NẾU MAP KHÔNG RỖNG THÌ RELOAD LẠI LAYOUT CŨ
        //KIỂM TRA NẾU MAP CHỨA CÁC ITEM THÌ KIỂM TRA XEM NÓ CÓ CHỨA PHIM ĐƯỢC MUA HIỆN TẠI HAY KHÔNG
        //VÀ CÓ CHỨA TIME SLOT HIỆN TẠI HAY CHƯA
        //NẾU CHƯA THÌ THÊM MỚI CHƯA THÌ LOAD TỪ MAP LÊN
        for (Seat seat : listSeatsState.values()){
            System.out.println("SETAEEEEEEEEEEEEEEE : "+seat.getType());
        }
        if(cinemaRoom.mapParentStateSeat.size() > 0){
            System.out.println("NOT NULLLLLLLLLL  SEATSSSSSSSSSSSSSSSSSSSSSSSS");
            //NẾU CHỨA KEY THÌ LOAD LẠI LAYOUT CŨ THÔNG QUA 3 CÁI MAP
            //mapParentLayout, mapParentController, mapParentState
            //System.out.println("VALUES KEY 22222: "+mapParentLayout.keySet()+" -- "+mapParentLayout.get("634649").keySet()+" -- "+mapParentLayout.get("634649").get("09:45 AM").keySet());

            //System.out.println("SIZE LOADING : "+tempMapStateItems.size()+" -- "+tempMapLayoutItems.size()+" -- "+tempMapControllerItems.size());
            //CONVERT MAP TO LIST
            for (int i = 0; i < 11; i++) {
                for (int j = 0; j < 21; j++) {

                    String title = verticalTitle[i] + (j + 1);
                    AnchorPane anchorPane = listSeatsLayout.get(title);
//                    boolean state = tempMapStateItems.get();
                    boolean state = false;
                    listSeatsController.get(title).setSeat(listSeatsState.get(title));
                    listSeatsController.get(title).setType(listSeatsState.get(title).getType());
                    System.out.println("TITLEEEEEEEEEEEEEEee : "+title);
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
            mainController.addVerticalAndHorizontalAxisTitle(gridPaneLayoutSeats);
        }
        new Thread(() -> Platform.runLater(() -> initLayoutGetInformationCustomers())).start();
    }

    //THÊM TÊN CHO TRỤC NGANG VÀ TRỤ DỌC ĐỂ ĐÁNH DẤU TÊN GHẾ


    //ININT LAYOUT NHAP THONG TIN KHACH HANG
    public void initLayoutGetInformationCustomers(){
        stageGetInformation = new Stage();
        stageGetInformation.setTitle("Customer's Information");
        Scene scene = null;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("view/layout_input_customer_info.fxml"));
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        layoutInputCustomerInfoController = loader.getController();
        layoutInputCustomerInfoController.setSoldTicketsController(this);
        stageGetInformation.setScene(scene);
        try {
            stageGetInformation.getIcons().add(new Image(new FileInputStream("C:\\Users\\duy khan\\IdeaProjects\\MovieTickets\\src\\main\\resources\\com\\example\\movietickets\\image\\background_login.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("WATINGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
        stageGetInformation.setOnHidden(windowEvent -> {
            if(valid){
                System.out.println("NOTTTTTTTTWAIINGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
                //NẾU NHẬP THÀNH CÔNG THÌ XUẤT VÉ THÀNH CÔNG
                priceTotal = 0;
                if(seatNumberString.size() > 0){
                    for (String item : seatNumberString.keySet()){
                        //listSeatsState.put(item,);
                        listSeatsController.get(item).setType(Utils.TYPE_SELECTED);
                        listSeatsState.get(item).setType(Utils.TYPE_SELECTED);
                        listSeatsController.get(item).seat.setSelected(true);
                        System.out.println("TYPE CHANGE : "+listSeatsController.get(item).seat.getType());

                    }
                    seatNumberString.clear();
                }

                if(!mainController.moviesSoldRecently.containsKey(movie.getId())){
                    System.out.println("ADD SUCCESSFULLY");
                    mainController.moviesSoldRecently.put(movie.getId(),movie);
                    Platform.runLater(() -> movieSoldRecentlyController.addNewItem(movie));
                }

                //HIỂN THỊ THÔNG BÁO ĐẶT VÉ THÀNH CÔNG
                Alert alertSuccessfully = new Alert(Alert.AlertType.CONFIRMATION);
                alertSuccessfully.setTitle("Successfully");
                alertSuccessfully.setHeaderText("Successful Ticket Booking");
                alertSuccessfully.initOwner(buttonAgreeBuyTickets.getScene().getWindow());
                alertSuccessfully.showAndWait();
                //THÊM GIỜ XUẤT VÉ CHO BILL
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                customer.getBill().setDateBought(simpleDateFormat.format(calendar.getTime()));
                new Thread(() -> {
                    //KIỂM TRA XEM PHIM NÀY ĐÃ TỒN TẠI CHƯA
                    //NẾU CHƯA THÌ THÊM VÀO CÒN RỒI THÌ UPDATE KHUNG GIỜ CHIẾU
                    if(!mapRooms.get(movie.getId()).mapParentStateSeat.containsKey(movie.getId())){
                        System.out.println("NOTTTTTTTTTTTTTTTTTT EXISTEDDDDDDDDDDDDDDDDDD");
                    }
                    else {
                        System.out.println("EXISTEDDDDDDDDDDDDDDDDDD");
                        mapRooms.get(movie.getId()).mapParentStateSeat.get(movie.getId()).put(timeSlot,new HashMap<String,Seat>(listSeatsState));
                    }
                }).start();
                System.out.println("INFORMATION CUSTOMTERRRRRRRRRRRRRRRRR");
                System.out.println("ID : "+customer.getId());
                System.out.println("NAME : "+customer.getName());
                System.out.println("SEX : "+customer.getSex());
                System.out.println("PHONE : "+customer.getPhoneNumber());
                System.out.println("TYPE : "+customer.getTypeCustomer());
                System.out.println("TOTAL POINT  : "+customer.getTotalPoint());
                System.out.println("BILL TOTAL PRICE : "+customer.getBill().getTotalPrice());
                System.out.println("BILL MOVIE NAME : "+customer.getBill().getMovie().getTitle());
                System.out.println("BILL DISCOUNT : "+customer.getBill().getDiscount());
                System.out.println("TIME SLOTS : "+customer.getBill().getTimeSlot()+" -- "+customer.getBill().getCinemaName());
                System.out.println("TIME BOUGHT: "+customer.getBill().getDateBought());
                customer.getBill().getSeats().forEach(item-> System.out.println("SEAT NAME : "+item.getTitle()));
                //CẦN THÊM VÀO VÉ PHIM CHIẾU Ở RẠP NÀO MẤY GIỜ
                //THÊM TÊN RẠP VÀO VÉ, THỜI GIAN CHIẾU BAO LÂU
                mainController.customers.add(customer);
                //THÊM KHÁCH HÀNH VÀ BILL VÀO TABLE
                System.out.println("AGAIN CHECK  : "+customer.getName());
                //INSERT INTO DATABASE
                try {
                    connection = controller.getConnection();
                    assert connection != null;
                    //SAVE TO CUSTOMER's INFORMATION TABLE
                    System.out.println("IDDDDDDDDDD NGU : "+customer.getId()+" -- "+customer.getName()+" -- "+customer.getTotalPoint());
                    String query = String.format("INSERT INTO CUSTOMER (ID, NAME, PHONE_NUMBER, SEX, TYPE_CUSTOMER, TOTAL_POINT) VALUES ('%s', '%s', '%s', '%s', '%s', '%s')"
                            ,customer.getId(), customer.getName(), customer.getPhoneNumber().isEmpty() ? "":customer.getPhoneNumber(), customer.getSex(), customer.getTypeCustomer(), String.valueOf(customer.getTotalPoint()));
                    //SAVE TO CUSTOMER's BILL TABLE
                    Bill bill = customer.getBill();
                    List<String> stringSeats = new ArrayList<>();
                    customer.getBill().getSeats().forEach(item->stringSeats.add(item.getTitle()));
                    String queryBill = String.format("INSERT INTO BILL(ID_CUSTOMER,MOVIE_NAME,CINEMA_NAME,TIME_SLOT,SEATS,DATE_BOUGHT,PRICE,DISCOUNT)\n" +
                                                    "VALUES\t('%s','%s','%s','%s','%s','%s','%s','%s')",
                                                    customer.getId(),bill.getMovie().getTitle(),
                                                    bill.getCinemaName(),bill.getTimeSlot(),String.join(", ",stringSeats),
                                                    bill.getDateBought(),bill.getTotalPrice(),bill.getDiscount());
                    Objects.requireNonNull(connection).createStatement().
                            execute(query);
                    connection.createStatement().execute(queryBill);
                } catch (Exception e) {
                    System.out.println("ERROR SAVE DATABASEEEEEEEEEEEEEEEEEEEEEE");
                    e.printStackTrace();
                }
                Platform.runLater(() -> {
                    System.out.println("CUSTOMER INFOR :"+customer.getName()+" -- "+customer.getBill().getCinemaName());
                    List<String> seatsTxt = new ArrayList<>();
                    customer.getBill().setIdCustomer(customer.getId());
                    customer.getBill().setMovieName(customer.getBill().getMovie().getTitle());
                    customer.getBill().getSeats().forEach(item->seatsTxt.add(item.getTitle()));
                    customer.getBill().setSeatsText(String.join(", ",seatsTxt));
                    mainController.tableViewCustomerInformation.getItems().add(customer);
                    mainController.tableViewCustomerBill.getItems().add(customer.getBill());
                    customer = new Customer();
                });
            }
            else {
                System.out.println("FALSEEEEEEEEEEEEEEE");
            }
            layoutInputCustomerInfoController.usingVipAccount = false;
            layoutInputCustomerInfoController.updateToVIP = false;
        });
    }

    //ĐẨY DỮ LIỆU CỦA PHIM LÊN VIEWS
    public void setContent(MovieObject.Movie item){
        //countSeatsSelected = 0;
        seatNumberString.clear();
        priceTotal = 0;
        Platform.runLater(() -> gridGenres.getChildren().clear());
        gridGenres.setAlignment(Pos.CENTER);
        new Thread(() -> Platform.runLater(() -> {
            textViewRoomName.setText("Ticket Details : "+mapRooms.get(movie.getId()).nameRoom);
            imageMovie.setImage(new Image(Utils.path_image_domain+item.getPoster_path()));
            titleMovie.setText(item.getTitle());
            runtime.setText("Runtime : "+item.getRuntime());
            tagline.setText(item.getTagline());
            release_date.setText("Release Date : "+item.getRelease_date());
        })).start();
        row = 0;
        column = 0;
        for (GenresObject.Genres a : item.getGenres()){
            AnchorPane anchorPane = new AnchorPane();
            Label label = new Label(a.getName());
            label.setAlignment(Pos.CENTER);
            label.setContentDisplay(ContentDisplay.CENTER);
            label.setFont(new Font("Agency FB",18));
            anchorPane.getChildren().add(label);

            Platform.runLater(() -> {
                if(column == 2){
                    ++row;
                    column = 0;
                }
                gridGenres.add(anchorPane,++column,row);
            });

            GridPane.setMargin(anchorPane, new Insets(5,0,10,10));
        }
    }

    //BẮT SỰ KIỆN MỖI KHI NHẤN VÀO CHỌN GHẾ NÀO ĐÓ
    //UPDATE SỐ LƯỢNG GHẾ ĐÃ CHỌN, TÊN GHẾ ĐÃ CHỌN
    @Override
    public void onSeatItemClicked(Seat seat) {
        if(seat.isSelected()){
            //THÊM TÊN GHẾ VÀO DANH SÁCH ĐANG CHỌN
            priceTotal+=seat.getTicketType().getPrice();
            customer.getBill().addSeat(seat);
            seatNumberString.put(seat.getTitle(), seat.getType());
        }
        else {
            //XÓA TÊN GHẾ KHỎI DANH SÁCH ĐẶT
            priceTotal-=seat.getTicketType().getPrice();
            seatNumberString.remove(seat.getTitle());
            customer.getBill().removeSeat(seat);
        }
        //UPDATE SỐ LƯỢNG VÀ TÊN GHẾ ĐÃ CHỌN
        seatsSelected.setText(String.valueOf(seatNumberString.size()));
        seatNumber.setText(String.join(", ",seatNumberString.keySet()));
        price.setText(formatStringToVNDCurrency(priceTotal));
    }

    public String formatStringToVNDCurrency(int priceTotal){
        Locale locale = new Locale("vn","VN");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        return numberFormat.format(priceTotal);
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
            alertConfirmation.initOwner(buttonAgreeBuyTickets.getScene().getWindow());

            Optional<ButtonType> optional = alertConfirmation.showAndWait();

            if(optional.get() == agree){
                alertConfirmation.close();
                //MỞ HỘP THOẠI NHẬP THÔNG TIN KHÁCH HÀNG
                if(layoutInputCustomerInfoController != null){
                    layoutInputCustomerInfoController.fullNameField.clear();
                    layoutInputCustomerInfoController.phoneNumberField.clear();
                }
                customer.getBill().setTimeSlot(timeSlot);
                customer.getBill().setMovie(movie);
                customer.getBill().setCinemaName(mapRooms.get(movie.getId()).nameRoom);
                stageGetInformation.show();
            }
            else {
                System.out.println("NOTTTTTTTT WAIINGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
            }


        }
        else{
            //HIỂN THỊ THÔNG BÁO NẾU CHƯA CHỌN GHẾ VÀ KHUNG GIỜ CHIẾU
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setTitle("Booking Warning");
            alertWarning.setHeaderText("Please Choose Your Seats And Time Slot !");
            alertWarning.initOwner(buttonAgreeBuyTickets.getScene().getWindow());
            alertWarning.showAndWait();
        }
    }

    //BẮT SỰ KIỆN BUTTON CHỌN KHUNG THỜI GIAN XEM PHIM
    @FXML
    public void onButtonChooseTimeSlot(ActionEvent actionEvent){
        if(actionEvent.getSource() == button945AM){
            if(!timeSlot.equals(Utils.SLOT_9_45_AM)){
                timeSlot = Utils.SLOT_9_45_AM;
                RefreshStateLayoutSeats(timeSlot,movie);
                //InitLayoutSeatsSelected(timeSlot);
            }
        }
        else if(actionEvent.getSource() == button100PM){
            if(!timeSlot.equals(Utils.SLOT_1_00_PM)){
                timeSlot = Utils.SLOT_1_00_PM;
                //InitLayoutSeatsSelected(timeSlot);
                RefreshStateLayoutSeats(timeSlot,movie);
            }
        }
        else if(actionEvent.getSource() == button345PM){
            if(!timeSlot.equals(Utils.SLOT_3_45_PM)){
                timeSlot = Utils.SLOT_3_45_PM;
                //InitLayoutSeatsSelected(timeSlot);
                RefreshStateLayoutSeats(timeSlot,movie);
            }
        }
        else if(actionEvent.getSource() == button700PM){
            if(!timeSlot.equals(Utils.SLOT_7_00_PM)){
                timeSlot = Utils.SLOT_7_00_PM;
                //InitLayoutSeatsSelected(timeSlot);
                RefreshStateLayoutSeats(timeSlot,movie);
            }
        }
        else if(actionEvent.getSource() == button945PM){
            if(!timeSlot.equals(Utils.SLOT_9_45_PM)){
                timeSlot = Utils.SLOT_9_45_PM;
                //InitLayoutSeatsSelected(timeSlot);
                RefreshStateLayoutSeats(timeSlot,movie);
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
