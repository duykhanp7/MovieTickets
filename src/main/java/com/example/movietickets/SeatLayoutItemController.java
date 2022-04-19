package com.example.movietickets;

import com.example.movietickets.interf.OnSeatItemClickedListener;
import com.example.movietickets.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class SeatLayoutItemController implements Initializable {


    //BUTTON CHỌN GHẾ
    @FXML
    public Button selectSeatButton;

    //TÊN GHẾ HIỆN TẠI : E1,E2....
    @FXML
    public Label idLabelButton;

    //GÁN LOẠI VÉ PHIM MẶC ĐỊNH VÀ NORMAL(BÌNH THƯỜNG)
    String type = Utils.TYPE_NORMAL;
    String seatTitle;
    //TRẠNG THÁI GHẾ CHƯA ĐƯỢC CHỌN, MẶC ĐỊNH LÀ FALSE
    Boolean stateSelected = false;
    //SỰ KIỆN MỖI KHI NHẤN CHỌN GHẾ
    OnSeatItemClickedListener onSeatItemClickedListener;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //selectSeatButton.setStyle( type.equals(Utils.TYPE_NORMAL) ?  "-fx-background-color:#72d684": type.equals(Utils.TYPE_PRIME) ? "-fx-background-color:#077a33":"-fx-background-color:#c20a04");
    }


    //XỬ LÝ SỰ KIỆN MỖI KHI NHẤN VÀO CHỌN GHẾ
    @FXML
    public void onButtonSelectSeatClicked(ActionEvent actionEvent){
        if(!type.equals(Utils.TYPE_SELECTED)){
            System.out.println("NOT NULL");
            System.out.println("SEAT POSITION : "+seatTitle);
            stateSelected = !stateSelected;
            selectSeatButton.setStyle(stateSelected ? "-fx-background-color:#e06b0b": type.equals(Utils.TYPE_NORMAL) ? "-fx-background-color:#72d684":"-fx-background-color:#077a33");
            onSeatItemClickedListener.onSeatItemClicked(seatTitle,stateSelected,type);
        }
        else{
            System.out.println("NOT CLICKEDDDDDDDDDDD");
        }
    }

    public void setOnSeatItemClickedListener(OnSeatItemClickedListener onSeatItemClickedListener){
        this.onSeatItemClickedListener = onSeatItemClickedListener;
    }

    public String getType() {
        return type;
    }

    //SET LOẠI GHẾ CHO GHẾ : NORMAL, PRIME, SELECTED, SELECTING
    public void setType(String type) {
        this.type = type;
        //THAY ĐỔI MÀU GHẾ MỖI KHI THAY ĐỔI LOẠI GHẾ
        selectSeatButton.setStyle( type.equals(Utils.TYPE_NORMAL) ? "-fx-background-color:#72d684": type.equals(Utils.TYPE_PRIME) ? "-fx-background-color:#077a33": type.equals(Utils.TYPE_SELECTED) ? "-fx-background-color:#c20a04":"-fx-background-color:#e06b0b");
        //System.out.println("CHANGE COLOR");
    }

    //ĐẶT TÊN GHẾ : E1, E2...
    public void setSeatTitle(String str){
        seatTitle = str;
    }
}
