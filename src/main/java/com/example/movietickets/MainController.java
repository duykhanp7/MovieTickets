package com.example.movietickets;


import java.io.IOException;
import java.net.URL;
import java.util.*;

import com.example.movietickets.controller.ItemMovieController;
import com.example.movietickets.interf.OnItemClickedListener;
import com.example.movietickets.model.MovieObject;
import com.example.movietickets.interf.API;
import com.example.movietickets.utils.Utils;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainController implements Initializable, OnItemClickedListener {
    Boolean[] stateButtonClicked = new Boolean[]{false, false, false, false};
    Boolean[] stateButtonHovered = new Boolean[]{false, false, false, false};
    @FXML private Button buttonHome;
    @FXML private Button buttonAdd;
    @FXML private Button buttonMovieScreens;
    @FXML private Button buttonLogout;
    @FXML Button buttonMoviesRecently;

    @FXML HBox PaneHome;
    @FXML VBox loadingPane;

    @FXML Pane PaneAdd;
    @FXML Pane PaneMovieScreens;
    @FXML Pane PaneLogOut;

    @FXML GridPane gridAdapter;
    @FXML GridPane gridLayoutItemSearch;

    @FXML ScrollPane scrollPane;
    @FXML ScrollPane scrollPaneSearch;

    @FXML Button buttonSearch;
    @FXML TextField textFieldSearch;

    //
    String oldKeyword="";
    //DANH SÁCH CÁC PHIM ĐÃ BÁN GẦN ĐÂY
    //CHỈ THÊM VÀO MỖI KHI PHIM NÀY ĐƯỢC BÁN
    //DÙNG HASHTABLE ĐỂ TRÁNH INSERT CÁC PHẦN TỬ TRÙNG
    //KEY : LÀ ID CỦA BỘ PHIM ĐÓ, VALUES CHÍNH LÀ BỘ PHIM ĐÓ
    Map<String, MovieObject.Movie> moviesSoldRecently = new HashMap<>();

    //DANH SÁCH PHIM PHỔ BIẾN
    List<MovieObject> popularMovies = new ArrayList<>();
    Stage stageSellTickets;
    SoldTicketsController soldTicketsController;
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

    //CONTROLLER DANH SÁCH VÉ PHIM BÁN GẦN ĐÂY
    MovieSoldRecentlyController movieSoldRecentlyController;
    Stage stageMoviesRecently;
    Scene sceneMoviesRecently;


    public MainController() {}

    public void initialize(URL url, ResourceBundle resourceBundle) {
        //LẤY DANH SÁCH PHIM PHỔ BIẾN TỪ API TRẢ VỀ
        loadingPane.toFront();
        ProgressIndicator progressIndicator = new ProgressIndicator();
        loadingPane.getChildren().add(progressIndicator);

        try {
            getDataFromAPI(1);
        }
        catch (Exception exception){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Connection error, please check the network again");
        }

        InitStageMoviesRecently();
        InitLayoutSellTickets();

        this.stateButtonClicked[0] = true;
        this.buttonHome.setStyle("");


        //KHI SCROLL ĐẾN CUỐI THÌ BẮT ĐẦU LẤY DỮ LIỆU TỪ PAGE TIẾP THEO
        // ĐEM VỀ THEO VÀO GRIDLAYOUT
        //SCROLL PANE SHOW MOVIE
        scrollPane.vvalueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                if(newValue.equals(1.0)){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(pageMain < maxPageMain){
                                try {
                                    getDataFromAPI(++pageMain);
                                }
                                catch (Exception e){
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Error");
                                    alert.setHeaderText("Connection error, please check the network again");
                                }
                            }
                        }
                    }).start();
                }
            }
        });

        //SCROLL PANEL HIỂ THỊ DANH SÁCH PHIM TÌM KIẾM THEO TÊN
        scrollPaneSearch.vvalueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number newValue) {
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
                                                    InitLayoutContainMoviesResult(text,++pageSearch);
                                                }
                                            }).start();
                                        }
                                    }
                                    catch (Exception e){
                                        Alert alert = new Alert(Alert.AlertType.ERROR);
                                        alert.setTitle("Error");
                                        alert.setHeaderText("Connection error, please check the network again");
                                    }
                                }
                            }
                        }
                    }).start();
                }
            }
        });



    }

    //THAY ĐỔI BACKGROUND CỦA BUTTON MỖI KHI NHẤN
    @FXML
    private void onButtonClickedChangeBackgroundColor(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() == this.buttonHome) {
            this.changeStateButtonClicked(0);
        } else if (mouseEvent.getSource() == this.buttonAdd) {
            this.changeStateButtonClicked(1);
        } else if (mouseEvent.getSource() == this.buttonMovieScreens) {
            this.changeStateButtonClicked(2);
        } else if (mouseEvent.getSource() == this.buttonLogout) {
            this.changeStateButtonClicked(3);
        }

        this.buttonHome.setStyle(this.stateButtonClicked[0] ? "-fx-background-color:#4a4c4f" : "-fx-background-color:#616366");
        this.buttonAdd.setStyle(this.stateButtonClicked[1] ? "-fx-background-color:#4a4c4f" : "-fx-background-color:#616366");
        this.buttonMovieScreens.setStyle(this.stateButtonClicked[2] ? "-fx-background-color:#4a4c4f" : "-fx-background-color:#616366");
        this.buttonLogout.setStyle(this.stateButtonClicked[3] ? "-fx-background-color:#4a4c4f" : "-fx-background-color:#616366");


    }

    //THAY ĐỔI BACKGROUND MỖI KHI HOVERED
    @FXML
    private void onMouseHoveredOnButton(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() == this.buttonHome) {
            this.changeStateButtonHovered(0);
        } else if (mouseEvent.getSource() == this.buttonAdd) {
            this.changeStateButtonHovered(1);
        } else if (mouseEvent.getSource() == this.buttonMovieScreens) {
            this.changeStateButtonHovered(2);
        } else if (mouseEvent.getSource() == this.buttonLogout) {
            this.changeStateButtonHovered(3);
        }

        this.buttonHome.setStyle(this.stateButtonClicked[0] ? "-fx-background-color:#4a4c4f" : (this.stateButtonHovered[0] ? "-fx-background-color:#7c7f83" : "-fx-background-color:#616366"));
        this.buttonAdd.setStyle(this.stateButtonClicked[1] ? "-fx-background-color:#4a4c4f" : (this.stateButtonHovered[1] ? "-fx-background-color:#7c7f83" : "-fx-background-color:#616366"));
        this.buttonMovieScreens.setStyle(this.stateButtonClicked[2] ? "-fx-background-color:#4a4c4f" : (this.stateButtonHovered[2] ? "-fx-background-color:#7c7f83" : "-fx-background-color:#616366"));
        this.buttonLogout.setStyle(this.stateButtonClicked[3] ? "-fx-background-color:#4a4c4f" : (this.stateButtonHovered[3] ? "-fx-background-color:#7c7f83" : "-fx-background-color:#616366"));
    }


    //BẮT EVENT CLICKED
    @FXML
    private void onButtonClicked(ActionEvent actionEvent) {
        if (actionEvent.getSource() == this.buttonHome) {
            this.PaneHome.toFront();
            scrollPane.toFront();
        } else if (actionEvent.getSource() == this.buttonAdd) {
            this.PaneAdd.toFront();
        } else if (actionEvent.getSource() == this.buttonMovieScreens) {
            this.PaneMovieScreens.toFront();
        } else if (actionEvent.getSource() == this.buttonLogout) {
            this.PaneLogOut.toFront();
        } else {
            //NULL
        }

    }


    //THAY ĐỔI TRẠNG THÁI CLICKED OR NOT
    public void changeStateButtonClicked(int index) {
        for(int i = 0; i < this.stateButtonClicked.length; ++i) {
            if (index == i) {
                this.stateButtonClicked[i] = true;
            } else {
                this.stateButtonClicked[i] = false;
            }
        }

    }

    //THAY ĐỔI TRẠNG THÁI HOVERED
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
        API.api.getMoviesPopular(Utils.API_KEY,String.valueOf(page)).enqueue(new Callback<MovieObject>() {
            @Override
            public void onResponse(Call<MovieObject> call, Response<MovieObject> response) {
                //NẾU DỮ LIỆU TRẢ VỀ KHÔNG NULL THÌ UP DATA LÊN VIEWS
                if(response.body().getMovies().size() > 0){
                    popularMovies.add(response.body());
                    MovieObject movieObject = response.body();
                    maxPageMain = Integer.parseInt(movieObject.getTotal_pages());
                    for (int i = 0; i < movieObject.getMovies().size(); i++) {
                        MovieObject.Movie item = movieObject.getMovies().get(i);
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("view/item_movie.fxml"));
                        AnchorPane anchorPane = null;
                        try {
                            anchorPane = loader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //ĐƯA DỮ LIỆU VÀO VIEW
                        ItemMovieController itemController = loader.getController();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                itemController.setData(item,MainController.this);
                            }
                        }).start();

                        //LAYOUT ITEM
                        AnchorPane finalAnchorPane = anchorPane;

                        //CHO CHẠY TRONG HÀM RUN LATER ĐỂ TRÁNH CRASH LUỒNG CHÍNH
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {

                                //NẾU ĐỦ 6 ITEMS TRÊN 1 HÀNG THÌ
                                //CHUYỂN SANG HÀNG TIẾP THEO
                                if (column == 6) {
                                    column = 0;
                                    row++;
                                }

                                gridAdapter.add(finalAnchorPane, column++, row); //(child,column,row)
                            }
                        });
                        //set grid width
                        gridAdapter.setMinWidth(Region.USE_COMPUTED_SIZE);
                        gridAdapter.setPrefWidth(Region.USE_COMPUTED_SIZE);
                        gridAdapter.setMaxWidth(Region.USE_PREF_SIZE);

                        //set grid height
                        gridAdapter.setMinHeight(Region.USE_COMPUTED_SIZE);
                        gridAdapter.setPrefHeight(Region.USE_COMPUTED_SIZE);
                        gridAdapter.setMaxHeight(Region.USE_PREF_SIZE);

                        GridPane.setMargin(anchorPane, new Insets(10,0,20,20));
                    }
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("GET SUCCESSFULLY");
                            loadingPane.toBack();
                            PaneHome.toFront();
                        }
                    });
                }
                //NẾU NULL THÌ GỌI LẠI API LẤY DỮ LIỆU CỦA PAGE KHÁC ĐỔ VỀ
                else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(pageMain < maxPageMain){
                                try {
                                    getDataFromAPI(++pageMain);
                                }
                                catch (Exception e){
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Error");
                                    alert.setHeaderText("Connection error, please check the network again");
                                }
                            }
                        }
                    }).start();
                }
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

    @Override
    public void onButtonBuyClicked(MovieObject.Movie item) {
        if(!moviesSoldRecently.containsKey(item.getId())){
            System.out.println("ADD SUCCESSFULLY");
            moviesSoldRecently.put(item.getId(),item);
            System.out.println("ITEM BUY : "+item.getId()+" -- "+item.getTitle());
            //SAU KHI XUẤT VÉ THÀNH CÔNG THÌ MỚI THÊM PHIM VÀO DANH SÁCH BÁN GẦN ĐÂY
//            Platform.runLater(new Runnable() {
//                @Override
//                public void run() {
//                    movieSoldRecentlyController.addNewItem(item);
//                }
//            });

        }
        soldTicketsController.setTitle(item.getTitle());
        stageSellTickets.show();
    }


    //BẮT SỰ KIỆN CLICK TRÊN BUTTON SEARCH
    //NẾU RỖNG THÌ KHÔNG CHUYỂN LAYOUT, NGƯỢC LẠI THÌ CHUYỂN
    @FXML
    public void onClickSearch(ActionEvent actionEvent){
        SearchMovie();
    }


    //BẮT EVENT ENTER TRÊN TEXT FIELD FIND FILM BY TITLE
    public void onEnterPressed(KeyEvent keyEvent){
        if(keyEvent.getCode() == KeyCode.ENTER){
            System.out.println("ENTER PRESSED ON TEXTFIELD");
            SearchMovie();
        }
    }

    public synchronized void SearchMovie(){
        System.out.println("BUTTON SEARCH");
        String keyword = textFieldSearch.getText().trim().toString().toLowerCase();
        //NẾU KEYWORD MỚI VÀ CŨ TRÙNG NHAU THÌ KHÔNG CẦN LOAD LẠI
        if(!oldKeyword.equals(keyword)){
            loadingPane.toFront();
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
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            InitLayoutContainMoviesResult(keyword,pageSearch);
                        }
                    }).start();
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
    //LẤY DANH SÁCH PHIM DỰA THEO TÊN PHIM
    public synchronized void InitLayoutContainMoviesResult(String keyword,int page){
        API.api.getMoviesByKeyword(Utils.API_KEY,keyword,String.valueOf(page)).enqueue(new Callback<MovieObject>() {
            @Override
            public void onResponse(Call<MovieObject> call, Response<MovieObject> response) {
                System.out.println("PAGE SEARCH : "+page);
                if(response.body().getMovies().size() > 0){
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            loadingPane.toBack();
                        }
                    });
                    MovieObject movieObject = response.body();
                    maxPageSearch = Integer.parseInt(movieObject.getTotal_pages());
                    for (int i = 0; i < movieObject.getMovies().size(); i++) {
                        MovieObject.Movie item = movieObject.getMovies().get(i);

                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("view/item_movie.fxml"));
                        AnchorPane anchorPane = null;
                        try {
                            anchorPane = loader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //ĐƯA DỮ LIỆU VÀO VIEW
                        ItemMovieController itemController = loader.getController();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                itemController.setData(item, MainController.this);
                            }
                        }).start();

                        //LAYOUT ITEM
                        AnchorPane finalAnchorPane = anchorPane;

                        //CHO CHẠY TRONG HÀM RUN LATER ĐỂ TRÁNH CRASH LUỒNG CHÍNH
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {

                                //NẾU ĐỦ 6 ITEMS TRÊN 1 HÀNG THÌ
                                //CHUYỂN SANG HÀNG TIẾP THEO
                                if (columnSearch == 6) {
                                    columnSearch = 0;
                                    rowSearch++;
                                }
                                gridLayoutItemSearch.add(finalAnchorPane, columnSearch++, rowSearch); //(child,column,row)
                                //set grid width
                            }
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
                }
                else{
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            loadingPane.toBack();
                        }
                    });
                    pageSearch = 1;
                    maxPageSearch = 0;
                    //HIỂN THỊ THÔNG BÁO KHÔNG CÓ KẾT QUẢ NÀO TRÙNG KHỚP VỚI KEYWORD ĐÃ NHẬP
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Notification !");
                            alert.setHeaderText("There are no matching results !");
                            alert.showAndWait();
                        }
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

    public void InitLayoutSellTickets(){
        stageSellTickets = new Stage();
        stageSellTickets.setTitle("Sell Tickets");
        stageSellTickets.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("view/layout_sold_tickets.fxml"));
        sceneSellTickets = null;
        try {
            sceneSellTickets = new Scene(loader.load(),1300,600);
        } catch (IOException e) {
            e.printStackTrace();
        }
        soldTicketsController = loader.getController();
        stageSellTickets.setScene(sceneSellTickets);
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
}