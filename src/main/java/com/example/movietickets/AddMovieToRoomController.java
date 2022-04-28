package com.example.movietickets;

import com.example.movietickets.model.CinemaRoom;
import com.example.movietickets.model.MovieObject;
import com.example.movietickets.model.Seat;
import com.example.movietickets.utils.Utils;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import java.net.URL;
import java.util.*;

//CONTROLLER THÊM PHIM NÀO ĐÓ VÀO MỘT RẠP ĐANG TRỐNG HOẶC THAY THẾ PHIM HIỆN TẠI ĐANG CHIẾU Ở RẠP NÀO ĐÓ
public class AddMovieToRoomController implements Initializable {

    @FXML ImageView oldImage;
    @FXML ImageView newImage;
    @FXML Button textViewTitleOld;
    @FXML Button textViewTitleNew;
    @FXML Button buttonAgreeChangeMovies;
    @FXML ComboBox<String> comboBoxCinema;
    CinemaRoom roomOld;
    List<String> listCinemaName = new ArrayList<>();
    MovieObject.Movie movieOld;
    MovieObject.Movie movieNew;
    MainController mainController;


//     *HÀM INITIALIZE SẼ ĐƯỢC KHỞI TẠO KHI OBJECT NÀY ĐƯỢC KHỞI RẠO
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //SET EVENT MỖI KHI LỰA CHỌN THAY ĐỔI
        //MỖI KHI ITEM THAY ĐỔI THÌ LẤY RA BỘ PHIM TƯƠNG ỨNG VỚI RẠP CHIẾU ĐƯỢC CHỌN ĐÓ
        //GÁN CHO BIẾN movieOld TỪ DANH SÁCH CÁC BỘ PHIM ĐANG BÁN Ở RẠP TƯƠNG ỨNG
        comboBoxCinema.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                try {
                    movieOld = mainController.movieListSold.get(listCinemaName.indexOf(t1));
                    setOldImageToView(movieOld);
                }
                catch (Exception e){
                    movieOld = null;
                }
            }
        });
        //DO SOMETHING HERE
    }


    //SET NỘI DUNG LÊN VIEW PHIM CŨ
    public void setContentView(MovieObject.Movie movie){
        movieNew = movie;
        newImage.setImage(new Image(Utils.path_image_domain +movie.getPoster_path()));
        textViewTitleNew.setText(movie.getTitle());
    }



    //HIỂN THỊ ẢNH CỦA PHIM Ở RẠP CŨ
    public void setOldImageToView(MovieObject.Movie movieOld){
        oldImage.setImage(new Image(Utils.path_image_domain+movieOld.getPoster_path()));
        textViewTitleOld.setText(movieOld.getTitle());
    }

    //TRUYỀN VÀO OBJECT MAINCONTROLLER
    public void setMainController(MainController mainController){
        this.mainController = mainController;
        for (CinemaRoom room : mainController.mapRooms.values()){
            listCinemaName.add(room.nameRoom);
        }
        //SẮP XẾP TÊN CÁC RẠP CHIẾU
        listCinemaName.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Integer.parseInt(String.valueOf(o1.charAt(o1.length()-1))) - Integer.parseInt(String.valueOf(o2.charAt(o2.length()-1)));
            }
        });
        //kHỞI TẠO COMBOBOX CHỨA DANH SÁCH CÁC RẠP CHIẾU
        InitComboBox();
        movieOld = mainController.movieListSold.get(0);
        setOldImageToView(movieOld);
    }


    //kHỞI TẠO COMBOBOX CHỨA DANH SÁCH CÁC RẠP CHIẾU
    public void InitComboBox(){
        if(comboBoxCinema.getItems().isEmpty()){
            for (String item : listCinemaName){
                comboBoxCinema.getItems().add(item);
            }
            comboBoxCinema.getEditor().setFont(new Font("Agency FB",20));
            comboBoxCinema.getSelectionModel().selectFirst();
        }
        else{
            comboBoxCinema.getItems().clear();
            for (String item : listCinemaName){
                comboBoxCinema.getItems().add(item);
            }
        }
    }

    //THÊM MỘT ITEM (RẠP CHIẾU MỚI) VÀO COMBO BOX
    public void addNewItemToComboBox(String item){
        listCinemaName.add(item);
    }

    //XÓA MỘT ITEM Ở COMBO BOX
    public void removeItemOfComboBox(String str){
        listCinemaName.remove(str);
    }

    //BUTTON ĐỒNG Ý THAY ĐỔI PHIM CHIẾU TẠI RẠP i VỚI PHIM MỚI
    @FXML
    public void onButtonAgreeToChangeMovie(ActionEvent actionEvent){
        CinemaRoom cinemaRoom = null;
        CinemaRoom newCinemaRoom = null;
        if(movieOld != null){
            //NẾU RẠP CŨ ĐÃ CHIẾU PHIM TRƯỚC ĐÓ THÌ TA THAY THẾ PHIM CŨ VỚI PHIM MỚI ĐƯỢC CHỌN
            System.out.println("SIZE BEFORE CHANGED : "+mainController.mapRooms.keySet());
            cinemaRoom = mainController.mapRooms.get(movieOld.getId());
            newCinemaRoom = new CinemaRoom(cinemaRoom.idRoom,cinemaRoom.nameRoom);
            newCinemaRoom.setMovie(movieNew);
            newCinemaRoom.setMovieId(movieNew.getId());
            System.out.println("NEW MOVIE : "+newCinemaRoom.idMovie+" -- "+newCinemaRoom.idRoom+" -- "+newCinemaRoom.nameRoom);
            //THÊM CÁC KHUNG GIỜ CHIẾU VÀO RẠP
            for (String timeSlot : Utils.timeSlots) {
                mainController.addSeatsIntoRoom(newCinemaRoom,timeSlot);
            }
            //ĐẨY RẠP VỪA TẠO VÀO DANH SÁCH MAP CHỨA CÁC RẠP ĐANG KHẢ DỤNG
            mainController.mapRooms.put(movieNew.getId(),newCinemaRoom);
            mainController.movieListSold.set(mainController.movieListSold.indexOf(movieOld),movieNew);
            mainController.mapRooms.remove(movieOld.getId());
        }
        else{
            //CÒN NẾU RẠP ĐÓ CHƯA TỪNG CHIẾU PHIM THÌ THÊM PHIM MỚI VÀO RẠP ĐÓ
            System.out.println("MOVIE NEEEEEEEEEEEEEe : "+movieNew.getId());
            mainController.movieListSold.add(movieNew);
            cinemaRoom = mainController.mapRooms.get("TEMP");
            newCinemaRoom = new CinemaRoom(cinemaRoom.idRoom, cinemaRoom.nameRoom);
            newCinemaRoom.setMovie(movieNew);
            newCinemaRoom.setMovieId(movieNew.getId());
            //THÊM CÁC KHUNG GIỜ CHIẾU VÀO RẠP
            for (String timeSlot : Utils.timeSlots) {
                mainController.addSeatsIntoRoom(newCinemaRoom,timeSlot);
            }
            mainController.mapRooms.put(movieNew.getId(),newCinemaRoom);
            mainController.mapRooms.remove("TEMP");
        }
        mainController.hasSet = false;
        mainController.InitGridLayoutSoldMovies(mainController.movieListSold);

        //HIỂN THỊ THÔNG BÁO THÊM HOẶC THAY THẾ PHIM THÀNH CÔNG
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Change Movie");
        alert.setHeaderText("Successfully changed the movie showing at the theater.");
        alert.initOwner(buttonAgreeChangeMovies.getScene().getWindow());
        alert.showAndWait();
        mainController.stageAddToRoom.close();
    }

}
