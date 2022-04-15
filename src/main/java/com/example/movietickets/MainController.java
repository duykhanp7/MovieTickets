package com.example.movietickets;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import com.example.movietickets.controller.ItemMovieController;
import com.example.movietickets.interf.OnItemClickedListener;
import com.example.movietickets.model.MovieObject;
import com.example.movietickets.interf.API;
import com.example.movietickets.utils.Utils;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
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

    @FXML HBox gridPaneHome;

    @FXML Pane gridPaneAdd;
    @FXML Pane gridPaneMovieScreens;
    @FXML Pane gridPaneLogOut;

    @FXML GridPane gridAdapter;
    @FXML GridPane gridLayoutItemSearch;

    @FXML ScrollPane scrollPane;
    @FXML ScrollPane scrollPaneSearch;

    @FXML Button buttonSearch;
    @FXML TextField textFieldSearch;
    @FXML Button buttonSearchSub;
    @FXML TextField textFieldSearchSub;

    //DANH SÁCH PHIM PHỔ BIẾN
    List<MovieObject> popularMovies = new ArrayList<>();

    int column = 0;
    int row = 2;
    int columnSearch = 0;
    int rowSearch = 1;

    int pageMain = 1;
    int maxPageMain = 0;
    int pageSearch = 1;
    int maxPageSearch = 0;

    public MainController() {}

    public void initialize(URL url, ResourceBundle resourceBundle) {
        //LẤY DANH SÁCH PHIM PHỔ BIẾN TỪ API TRẢ VỀ
        new Thread(new Runnable() {
            @Override
            public void run() {
                getDataFromAPI(pageMain);
            }
        }).start();


        this.stateButtonClicked[0] = true;
        this.buttonHome.setStyle("");


        //KHI SCROLL ĐẾN CUỐI THÌ BẮT ĐẦU LẤY DỮ LIỆU TỪ PAGE TIẾP THEO
        // ĐEM VỀ THEO VÀO GRIDLAYOUT
        //SCROLL PANE SHOW MOVIE
        scrollPane.vvalueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                System.out.println("NEW OLD : "+oldValue+" -- "+newValue);
                if(newValue.equals(1.0)){
                    System.out.println("SCROLL TO END");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(pageMain <= maxPageMain){
                                getDataFromAPI(++pageMain);
                            }
                        }
                    }).start();
                }
            }
        });

        //SCROLL PANE SHOW MOVIE SEARCH BY TITLE
        scrollPaneSearch.vvalueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number newValue) {
                if(newValue.equals(1.0)){
                    System.out.println("SCROLL TO END");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(pageSearch <= maxPageSearch){
                                String text = textFieldSearch.getText().trim().toString();
                                if(!text.isEmpty()){
                                    InitLayoutContainMoviesResult(text,++pageSearch);
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
            this.gridPaneHome.toFront();
            scrollPane.toFront();
            System.out.println("BUTTON HOME CLICKED");
        } else if (actionEvent.getSource() == this.buttonAdd) {
            this.gridPaneAdd.toFront();
            System.out.println("BUTTON ADD CLICKED");
        } else if (actionEvent.getSource() == this.buttonMovieScreens) {
            this.gridPaneMovieScreens.toFront();
            System.out.println("BUTTON MOVIE SCREENS CLICKED");
        } else if (actionEvent.getSource() == this.buttonLogout) {
            this.gridPaneLogOut.toFront();
            System.out.println("BUTTON LOG OUT CLICKED");
        } else {
            System.out.println("NHU CCCCCCCCCCCCCc");
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
                System.out.println("ON SUCCESSFULLY");
                //NẾU DỮ LIỆU TRẢ VỀ KHÔNG NULL THÌ UP DATA LÊN VIEWS
                if(response.body().getMovies().size() > 0){
                    popularMovies.add(response.body());
                    System.out.println("NOT NULL : "+response.body().movies.size());
                    MovieObject movieObject = response.body();
                    maxPageMain = Integer.parseInt(movieObject.getTotal_pages());
                    try {
                        for (int i = 0; i < movieObject.getMovies().size(); i++) {
                            MovieObject.Movie item = movieObject.getMovies().get(i);

                            System.out.println("ITEM GET IS : "+item.getId()+" -- "+item.getTitle());

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
                            itemController.setData(item,MainController.this);

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
                                    //set grid width
                                    gridAdapter.setMinWidth(Region.USE_COMPUTED_SIZE);
                                    gridAdapter.setPrefWidth(Region.USE_COMPUTED_SIZE);
                                    gridAdapter.setMaxWidth(Region.USE_PREF_SIZE);

                                    //set grid height
                                    gridAdapter.setMinHeight(Region.USE_COMPUTED_SIZE);
                                    gridAdapter.setPrefHeight(Region.USE_COMPUTED_SIZE);
                                    gridAdapter.setMaxHeight(Region.USE_PREF_SIZE);
                                }
                            });

                            GridPane.setMargin(anchorPane, new Insets(10,10,20,10));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //NẾU NULL THÌ GỌI LẠI API LẤY DỮ LIỆU CỦA PAGE KHÁC ĐỔ VỀ
                else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(pageMain <= maxPageMain){
                                getDataFromAPI(++pageMain);
                            }
                        }
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<MovieObject> call, Throwable throwable) {
                System.out.println("ON FAILED CONNECT TO SERVER");
            }
        });
    }


    //KHI MỖI ITEM PHIM ĐƯỢC NHẤN THÌ SẼ NHẢY VÀO FUNCTION NÀY
    @Override
    public void onClicked(MovieObject.Movie item) {
        System.out.println("ITEM CLICKED IS : "+item.getId()+" -- "+item.getTitle());
    }


    //BẮT SỰ KIỆN CLICK TRÊN BUTTON SEARCH
    //NẾU RỖNG THÌ KHÔNG CHUYỂN LAYOUT, NGƯỢC LẠI THÌ CHUYỂN
    @FXML
    public void onClickSearch(ActionEvent actionEvent){
        if(actionEvent.getSource() == buttonSearch){
            String keyword = textFieldSearch.getText().trim().toString();
            if(!keyword.isEmpty()){
                InitLayoutContainMoviesResult(keyword,pageSearch);
                scrollPaneSearch.toFront();
                textFieldSearchSub.setText(textFieldSearch.getText().trim().toString());
            }
        }
    }


    public synchronized void InitLayoutContainMoviesResult(String keyword,int page){
        API.api.getMoviesByKeyword(Utils.API_KEY,keyword,String.valueOf(page)).enqueue(new Callback<MovieObject>() {
            @Override
            public void onResponse(Call<MovieObject> call, Response<MovieObject> response) {

                if(response.body().getMovies().size() > 0){
                    System.out.println("NOT NULL : "+response.body().movies.size());
                    MovieObject movieObject = response.body();
                    maxPageSearch = Integer.parseInt(movieObject.getTotal_pages());
                    try {
                        for (int i = 0; i < movieObject.getMovies().size(); i++) {
                            MovieObject.Movie item = movieObject.getMovies().get(i);

                            System.out.println("ITEM GET IS : "+item.getId()+" -- "+item.getPoster_path());

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
                            itemController.setData(item,MainController.this);

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
                                    gridLayoutItemSearch.setMinWidth(Region.USE_COMPUTED_SIZE);
                                    gridLayoutItemSearch.setPrefWidth(Region.USE_COMPUTED_SIZE);
                                    gridLayoutItemSearch.setMaxWidth(Region.USE_PREF_SIZE);

                                    //set grid height
                                    gridLayoutItemSearch.setMinHeight(Region.USE_COMPUTED_SIZE);
                                    gridLayoutItemSearch.setPrefHeight(Region.USE_COMPUTED_SIZE);
                                    gridLayoutItemSearch.setMaxHeight(Region.USE_PREF_SIZE);
                                }
                            });

                            GridPane.setMargin(anchorPane, new Insets(10,0,5,10));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //NẾU NULL THÌ GỌI LẠI API LẤY DỮ LIỆU CỦA PAGE KHÁC ĐỔ VỀ
                else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<MovieObject> call, Throwable throwable) {
                System.out.println("ON GET MOVIES BY TITLE FAILED");
            }
        });
    }

}