package com.example.movietickets;

import com.example.movietickets.interf.OnSeatItemClickedListener;
import com.example.movietickets.model.Seat;
import com.example.movietickets.model.TicketType;
import com.example.movietickets.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;


//CONTROLLER CHO GHẾ NGỒI
public class SeatLayoutItemController implements Initializable {


    //BUTTON CHỌN GHẾ
    @FXML
    public Button selectSeatButton;

    //SỰ KIỆN MỖI KHI NHẤN CHỌN GHẾ
    OnSeatItemClickedListener onSeatItemClickedListener;
    Seat seat;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        seat = new Seat();
    }


    //XỬ LÝ SỰ KIỆN MỖI KHI NHẤN VÀO CHỌN GHẾ
    @FXML
    public void onButtonSelectSeatClicked(ActionEvent actionEvent){
        if(seat.isEnable()){
            if(!seat.getType().equals(Utils.TYPE_SELECTED)){
                System.out.println("NOT NULL");
                System.out.println("SEAT POSITION : "+seat.getTitle());
                seat.setSelected(!seat.isSelected());
                selectSeatButton.setStyle(seat.isSelected() ? "-fx-background-color:#e06b0b": seat.getType().equals(Utils.TYPE_NORMAL) ? "-fx-background-color:#72d684":"-fx-background-color:#077a33");
                onSeatItemClickedListener.onSeatItemClicked(seat);
            }
            else{
                System.out.println("NOT CLICKEDDDDDDDDDDD");
            }
        }
    }

    //BẮT SỰ KIỆN CLICK TRÊN MỖI GHẾ NGỒI TƯƠNG ỨNG
    public void setOnSeatItemClickedListener(OnSeatItemClickedListener onSeatItemClickedListener){
        this.onSeatItemClickedListener = onSeatItemClickedListener;
    }



    //SET LOẠI GHẾ CHO GHẾ : NORMAL, PRIME, SELECTED, SELECTING
    public void setType(String type) {
        seat.setType(type);
        //THAY ĐỔI MÀU GHẾ MỖI KHI THAY ĐỔI LOẠI GHẾ
        selectSeatButton.setStyle(type.equals(Utils.TYPE_NORMAL) ? "-fx-background-color:#72d684": type.equals(Utils.TYPE_PRIME) ? "-fx-background-color:#077a33": type.equals(Utils.TYPE_SELECTED) ? "-fx-background-color:#c20a04":"-fx-background-color:#e06b0b");
    }

    public void setSeat(Seat seat){
        this.seat = seat;
    }
}
