package com.example.movietickets;

import com.example.movietickets.model.ConnectionToDatabaseController;
import com.example.movietickets.model.Customer;
import com.example.movietickets.utils.Utils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.movietickets.MainController.customerCode;
import static com.example.movietickets.MainController.posCustomer;


//CONTROLLER ĐỂ ĐIỀN THÔNG TIN KHÁCH HÀNG
public class LayoutInputCustomerInfoController implements Initializable {


    @FXML TextField fullNameField;
    @FXML ComboBox<String> comboBoxSex;
    @FXML TextField phoneNumberField;
    @FXML Button buttonAgreeInfor;
    @FXML Button buttonUpToVip;
    @FXML Button buttonVipCustomer;

    @FXML Label textViewInvalidPhoneNumber;
    @FXML Label textViewInvalidName;
    @FXML CheckBox checkBoxUsingDiscount;

    SoldTicketsController soldTicketsController;
    boolean updateToVIP = false;
    boolean usingVipAccount = false;
    ConnectionToDatabaseController controller;
    Connection connection;


    public void setSoldTicketsController(SoldTicketsController soldTicketsController){
        this.soldTicketsController = soldTicketsController;
    }

    //KIỂM TRA EMAIL ĐÃ ĐÚNG FORMAT CHƯA
    public boolean regexFullName(String fullName){
        Pattern pattern = Pattern.compile("^[a-zA-Z\\s]*$");
        return pattern.matcher(fullName).matches();
    }

    //KIỂM TRA SĐT ĐÃ ĐÚNG FORMAT CHƯA : TOÀN LÀ CHỮ SỐ, BẮT ĐẦU BẰNG 0 vÀ TỐI ĐA 10 SỐ
    public boolean regexPhoneNumber(String phone){
        Pattern pattern = Pattern.compile("0+[0-9]{9}");
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    //BẮT SỰ KIỆN HOÀN THÀNH NHẬP THÔNG TIN KHÁCH HÀNG
    @FXML
    public void OnButtonCompleteInformationCustomer(ActionEvent actionEvent){
        completeInputCustomerInformation();
    }


    //BẮT SỰ KIỆN UPGRADED LÊN VIP
    @FXML
    public void OnButtonUpgradeToVIP(ActionEvent actionEvent){
        updateToVIP();
    }


    //BẮT SỰ KIỆN ON BUTTON VIP CUSTOMER
    @FXML
    public void OnButtonVipCustomer(ActionEvent actionEvent){
        Platform.runLater(() -> {
            soldTicketsController.customer.setTypeCustomer(Utils.CUSTOMER_PRIME);
            usingVipAccount = true;
            checkBoxUsingDiscount.setVisible(true);
            phoneNumberField.setDisable(false);
            fullNameField.setDisable(true);
            comboBoxSex.setDisable(true);
            updateToVIP = false;
            buttonUpToVip.setText("NORMAL");
            textViewInvalidName.setVisible(false);
            textViewInvalidPhoneNumber.setVisible(false);
        });

    }

    //HÀM ĐĂNG KÍ KHÁCH HÀNG VIP
    public void updateToVIP(){
        fullNameField.setDisable(false);
        comboBoxSex.setDisable(false);
        if(usingVipAccount){
            Platform.runLater(() -> {
                updateToVIP = false;
                soldTicketsController.customer.setTypeCustomer(Utils.CUSTOMER_NORMAL);
                phoneNumberField.setDisable(true);
                buttonUpToVip.setText("VIP");
                checkBoxUsingDiscount.setVisible(false);
                textViewInvalidName.setVisible(false);
                textViewInvalidPhoneNumber.setVisible(false);
            });
        }
        else{
            updateToVIP = !updateToVIP;
            Platform.runLater(() -> {
                soldTicketsController.customer.setTypeCustomer(updateToVIP ? Utils.CUSTOMER_PRIME:Utils.CUSTOMER_NORMAL);
                phoneNumberField.setDisable(!updateToVIP);
                buttonUpToVip.setText(updateToVIP ? "NORMAL":"VIP");
                checkBoxUsingDiscount.setVisible(false);
                textViewInvalidName.setVisible(false);
                textViewInvalidPhoneNumber.setVisible(false);
            });
        }
        usingVipAccount = false;
    }


    //KIỂM TRA HOÀN TẤT THÔNG TIN KHÁC HÀNG
    public void completeInputCustomerInformation(){
        String fullName = fullNameField.getText().trim();
        boolean validFullName = regexFullName(fullName);
        String sexText = comboBoxSex.getValue();
        System.out.println("NAME SEX : "+validFullName+" -- "+sexText);
        //TRƯỜNG HỢP NẾU KHÁCH HÀNG NÂNG VIP
        if(updateToVIP){
            String phoneNumber = phoneNumberField.getText().trim();
            boolean validPhoneNumber = regexPhoneNumber(phoneNumber);
            if(validFullName && validPhoneNumber && !sexText.isEmpty()){
                if(!checkPhoneNumberForUpdateVipExist(phoneNumber)){
                    soldTicketsController.valid = true;
                    updateToVIP = false;
                    phoneNumberField.setDisable(true);
                    buttonUpToVip.setText("VIP");
                    textViewInvalidName.setVisible(false);
                    textViewInvalidPhoneNumber.setVisible(false);
                    //GHI THÔNG TIN KHÁCH HÀNH VIP VÀO DATABASE
                    //
                    soldTicketsController.customer.setTypeCustomer(Utils.CUSTOMER_PRIME);
                    int pos = ++posCustomer;
                    soldTicketsController.customer.setId(String.valueOf(customerCode+(pos > 10 ? pos:"0"+pos)));
                    soldTicketsController.customer.setName(fullName);
                    soldTicketsController.customer.setSex(sexText);
                    soldTicketsController.customer.setTotalPoint(soldTicketsController.customer.getBill().getSeats().size()*100);
                    soldTicketsController.customer.setPhoneNumber(phoneNumber);
                    returnDefaultUI();
                    soldTicketsController.stageGetInformation.close();

                    //SAVE NEW CUSTOMER VIP TO DATABASE
                    String query = String.format("INSERT INTO VIPCUSTOMER(PHONE_NUMBER,FULL_NAME,SEX)\n" +
                                                "VALUES('%s','%s','%s')",phoneNumber,fullName,sexText);
                    try {
                        connection.createStatement().execute(query);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Alert alertSuccessfully = new Alert(Alert.AlertType.CONFIRMATION);
                    alertSuccessfully.setTitle("Customer Information");
                    alertSuccessfully.setHeaderText("This phone number is already in use, please choose another phone number");
                    alertSuccessfully.initOwner(buttonAgreeInfor.getScene().getWindow());
                    alertSuccessfully.showAndWait();
                }
                //
            }
            else{
                soldTicketsController.valid = false;
                //LỖI FIELD NAME TRỐNG
                if(fullName.isEmpty() || phoneNumber.isEmpty() || !regexPhoneNumber(phoneNumber)){
                    Alert alertSuccessfully = new Alert(Alert.AlertType.CONFIRMATION);
                    alertSuccessfully.setTitle("Customer Information");
                    alertSuccessfully.setHeaderText("Please enter the full information");
                    alertSuccessfully.initOwner(buttonAgreeInfor.getScene().getWindow());
                    alertSuccessfully.showAndWait();
                }
                textViewInvalidName.setVisible(!validFullName);
                textViewInvalidPhoneNumber.setVisible(!validPhoneNumber);
            }
        }
        //KHÁCH HÀNG BÌNH THƯỜNG VÀ KHÁC DÙNG ACCOUNT VIP
        else{
            //NẾU KHÁCH HÀNG SỬ DỤNG TÀI KHOẢN VIP ĐÃ CÓ
            if(usingVipAccount){
//                    soldTicketsController.customer.setTotalPoint(20000);
                String phoneNumber = phoneNumberField.getText().trim();
                if(!phoneNumber.trim().isEmpty() && regexPhoneNumber(phoneNumber)){
                    if(checkPhoneNumberForUpdateVipExist(phoneNumber)){
                        System.out.println("ALOOOOOOOOOOOOOOOOOOO #3333333333333333333333");
                        System.out.println("VALIDDDDĐ");
                        String[] result = returnFullNameAndSexIfAccountExist(phoneNumber).split("@");
                        System.out.println("RESULT : "+result[0]);
                        soldTicketsController.customer.setTotalPoint(Integer.parseInt(result[2]));
                        Platform.runLater(() -> {
                            fullNameField.setText(result[0]);
                            comboBoxSex.getSelectionModel().select(result[1]);
                            System.out.println("TOTAL POINT : "+result[2]);
                            //soldTicketsController.customer.setTotalPoint(Integer.parseInt(result[2]));
                        });
                        updateToVIP = false;
                        buttonUpToVip.setText("VIP");
                        phoneNumberField.setDisable(true);
                        soldTicketsController.valid = true;
                        textViewInvalidName.setVisible(false);
                        textViewInvalidPhoneNumber.setVisible(false);
                        //NẾU CHECKBOX USING DISCOUNT CHECKED THÌ DÙNG ĐIỂM QUY ĐỔI
                        //KHÔNG THÌ CỘNG ĐIỂM CŨ VỚI SỐ ĐIỂM HIỆN TẠI
                        //NẾU KHÁCH HÀNG TICK VÀO Ô SỬ DỤNG ĐIỂM ĐỂ QUY ĐỔI TIỀN GIẢM GIÁ
                        //KIỂM TRA ĐIỂM TÍCH LŨY ĐỦ ÍT NHẤT 1000 CHƯA
                        //NẾU CHƯA THÔNG BÁO KHÔNG THỂ SỬ DỤNG GIẢM GIÁ BÂY GIỜ
                        //CÒN ĐỦ THÌ QUY ĐỔI CỨ 1000 ĐIỂM THÌ ĐƯỢC 10.000VNĐ GIẢM GIÁ
                        //NẾU SỐ TIỀN GIẢM GIÁ LỚN HƠN GIÁ TIỀN VÉ PHẢI TRẢ THÌ SỐ ĐIỂM DƯ CÒN LẠI SẼ ĐƯỢC LƯU LẠI
                        //NẾU KHÔNG SỬ DỤNG ĐIỂM TÍCH LŨY THÌ ĐIỂM ĐƯỢC CỘNG DỒN
                        if(checkBoxUsingDiscount.isSelected()){
                            if(soldTicketsController.customer.checkCanUsingDiscount()){
                                soldTicketsController.customer.usingDiscount();
                                updatePoints(phoneNumber, String.valueOf(soldTicketsController.customer.getTotalPoint()));
                            }
                            //THÔNG BÁO KHÔNG ĐỦ ĐIỂM TÍCH LŨY ÍT NHẤT ĐỂ QUY ĐỔI
                            else {
                                Alert alertSuccessfully = new Alert(Alert.AlertType.CONFIRMATION);
                                alertSuccessfully.setTitle("Customer Information");
                                alertSuccessfully.setHeaderText("Not enough accumulated points, switch to default payment");
                                alertSuccessfully.initOwner(buttonAgreeInfor.getScene().getWindow());
                                alertSuccessfully.showAndWait();
                            }
                        }
                        else {
                            //UPDATE ĐIỂM NẾU KHÔNG SỬ DỤNG ĐIỂM QUY ĐỔI SANG TIỀN THƯỞNGduykhan
                            Customer customerTemp = soldTicketsController.customer;
                            soldTicketsController.customer.setTotalPoint(Integer.parseInt(String.valueOf(Integer.parseInt(result[2])+(customerTemp.getBill().getSeats().size()*100))));
                            System.out.println("POINT ADD : "+Integer.parseInt(String.valueOf(Integer.parseInt(result[2])+(customerTemp.getBill().getSeats().size()*100))));
                            updatePoints(phoneNumber, String.valueOf(soldTicketsController.customer.getTotalPoint()));
                        }
                        //GHI THÔNG TIN KHÁCH HÀNH VÀO DATABASE
                        int pos = ++posCustomer;
                        soldTicketsController.customer.setId(customerCode+(pos > 10 ? pos:"0"+pos));
                        soldTicketsController.customer.setName(result[0]);
                        soldTicketsController.customer.setSex(sexText);
                        soldTicketsController.customer.setPhoneNumber(phoneNumber);
                        //
                        returnDefaultUI();
                        soldTicketsController.stageGetInformation.close();
                    }
                    else{
                        //THÔNG BÁO TÀI KHOẢN CHƯA ĐĂNG KÍ VIP
                        Alert alertSuccessfully = new Alert(Alert.AlertType.CONFIRMATION);
                        alertSuccessfully.setTitle("Customer Information");
                        alertSuccessfully.setHeaderText("This phone number is not registered vip.");
                        alertSuccessfully.initOwner(buttonAgreeInfor.getScene().getWindow());
                        alertSuccessfully.showAndWait();
                    }
                }
                else{
                    textViewInvalidPhoneNumber.setVisible(true);
                }
            }
            else{
                //NẾU ĐẦY ĐỦ THÔNG TIN VÀ HỢP LỆ THÌ XUẤT VÉ THÀNH CÔNG
                //TÊN HỢP LỆ, GIỚI TÍNH HỢP LỆ
                if(!fullName.trim().isEmpty() && validFullName && !sexText.isEmpty()){
                    System.out.println("ALOOOOOOOOOOOOOOOOOOO #3333333333333333333333");
                    System.out.println("VALIDDDDĐ");
                    updateToVIP = false;
                    buttonUpToVip.setText("VIP");
                    phoneNumberField.setDisable(true);
                    soldTicketsController.valid = true;
                    textViewInvalidName.setVisible(false);
                    textViewInvalidPhoneNumber.setVisible(false);
                    //GHI THÔNG TIN KHÁCH HÀNH VÀO DATABASE
                    soldTicketsController.customer.setTypeCustomer(Utils.CUSTOMER_NORMAL);
                    int pos = ++posCustomer;
                    soldTicketsController.customer.setId(customerCode+(pos > 10 ? pos:"0"+pos));
                    soldTicketsController.customer.setName(fullName);
                    soldTicketsController.customer.setSex(sexText);
                    soldTicketsController.customer.setTotalPoint(soldTicketsController.customer.getBill().getSeats().size()*100);
                    //
                    returnDefaultUI();
                    soldTicketsController.stageGetInformation.close();
                }
                //THÔNG BÁO NẾU CÓ FIELDS NÀO ĐÓ RỖNG
                else{
                    System.out.println("ALOOOOOOOOOOOOOOOOOOO 444444444444444444444");
                    System.out.println("NOT VALIDDDDDD");
                    //LỖI FIELD NAME TRỐNG
                    soldTicketsController.valid = false;
                    if(fullName.trim().isEmpty()){
                        Alert alertSuccessfully = new Alert(Alert.AlertType.CONFIRMATION);
                        alertSuccessfully.setTitle("Customer Information");
                        alertSuccessfully.setHeaderText("Please enter the full information");
                        alertSuccessfully.initOwner(buttonAgreeInfor.getScene().getWindow());
                        alertSuccessfully.showAndWait();
                    }
                    textViewInvalidName.setVisible(!validFullName);
                }
            }
        }
    }


    //TRẢ VỀ GIAO DIỆN BAN ĐẦU SAU KHI SỬ DỤNG TÀI KHOẢN VIP HOẶC NÂNG VIP
    public void returnDefaultUI(){
        updateToVIP = false;
        usingVipAccount = false;
        phoneNumberField.setDisable(true);
        fullNameField.setDisable(false);
        comboBoxSex.setDisable(false);
        checkBoxUsingDiscount.setVisible(false);
        Platform.runLater(() -> buttonUpToVip.setText("VIP"));
    }

    //KIỂM TRA THỬ SỐ ĐIỆN THOẠI NÂNG VIP ĐÃ ĐƯỢC SỬ DỤNG CHƯA
    public boolean checkPhoneNumberForUpdateVipExist(String phone){
        String query = String.format("SELECT PHONE_NUMBER FROM VIPCUSTOMER WHERE PHONE_NUMBER='%s'",phone);
        ResultSet resultSet;
        boolean check = false;
        try {
            resultSet = connection.createStatement().executeQuery(query);
            check = resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return check;
    }

    //TRẢ VỀ VÀ HIỂN THỊ TÊN VÀ GIỚI TÍNH KHÁCH HÀNG VIP NẾU TÀI KHOẢN TỒN TẠI
    public String returnFullNameAndSexIfAccountExist(String phone){
        String query = String.format("SELECT VIPCUSTOMER.FULL_NAME, VIPCUSTOMER.SEX,TOTAL_POINT FROM VIPCUSTOMER INNER JOIN CUSTOMER ON VIPCUSTOMER.PHONE_NUMBER = CUSTOMER.PHONE_NUMBER AND VIPCUSTOMER.PHONE_NUMBER='%s'",phone);
        ResultSet resultSet;
        String result = "";
        try {
            resultSet = connection.createStatement().executeQuery(query);
            resultSet.next();
            result = resultSet.getString("FULL_NAME")+"@"+resultSet.getString("SEX")+"@"+resultSet.getString("TOTAL_POINT");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("RESULT : "+result);
        return result;
    }

    //CẬP NHẬT ĐIỂM
    public void updatePoints(String phone,String newPoint){
        String query = String.format("""
                UPDATE CUSTOMER\s
                SET TOTAL_POINT = '%s'
                WHERE PHONE_NUMBER='%s'""",newPoint,phone);
        try {
            connection.createStatement().executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //KHỞI TẠO COMBO BOX SEX
        comboBoxSex.getItems().addAll("Male","Female","Others");
        comboBoxSex.getSelectionModel().selectFirst();
        controller = new ConnectionToDatabaseController();
        connection = controller.getConnection();
    }

}
