package com.example.movietickets;

import com.example.movietickets.model.ConnectionToDatabaseController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


//CONTROLLER ĐĂNG NHẬP
public class LoginActivityController implements Initializable {


    @FXML Label buttonSignUp;
    @FXML Label forgot_pass_button;
    @FXML Label warningLogin;
    @FXML ImageView ic_show_pass;
    @FXML PasswordField passwordFieldHide;
    @FXML TextField emailField;
    @FXML TextField passwordFieldShow;
    @FXML Button loginButton;

    boolean showPass = false;
    ConnectionToDatabaseController controller;
    Connection connection;

    //SIGN UP LAYOUT

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showHidePassWord(showPass);
        controller = new ConnectionToDatabaseController();
        connection = controller.getConnection();
    }

    //NẾU TRUE THÌ SHOW PASS, KHÔNG THÌ THÔI
    //HIỂN THỊ HOẶC ẨN PASSWORD
    public void showHidePassWord(boolean state){
        //NẾU STATE BẰNG TRUE THÌ SẼ HIỂN THỊ PASSWORD
        if(state){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    passwordFieldShow.setText(passwordFieldHide.getText());
                    passwordFieldShow.setVisible(true);
                    passwordFieldHide.setVisible(false);
                    try {
                        ic_show_pass.setImage(new Image(new FileInputStream("C:\\Users\\duy khan\\IdeaProjects\\MovieTickets\\src\\main\\resources\\com\\example\\movietickets\\image\\show_pass.png")));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
        //NGƯỢC LẠI SẼ ẨN PASSWORD
        else{
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    passwordFieldShow.setVisible(false);
                    passwordFieldHide.setVisible(true);
                    try {
                        ic_show_pass.setImage(new Image(new FileInputStream("C:\\Users\\duy khan\\IdeaProjects\\MovieTickets\\src\\main\\resources\\com\\example\\movietickets\\image\\hide_pass.png")));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    //BẮT SỰ KIỆN SHOW OR HIDE PASSWORD
    @FXML
    public void onButtonShowOrHidePassword(MouseEvent mouseEvent){
        showPass = !showPass;
        showHidePassWord(showPass);
    }

    //BẮT SỰ KIỆN CLICK TRÊN BUTTON ĐĂNG NHẬP
    @FXML
    public void onButtonLogin(ActionEvent actionEvent){
        //KIỂM TRA ACCOUNT HỢP LỆ KHÔNG
        //NẾU HỢP LỆ THÌ CHUYỂN SANG MÀN HÌNH LÀM VIỆC CHÍNH
        login();
    }


    //XỬ LÝ SỰ KIỆN ĐĂNG NHẬP
    public void login(){
        boolean validAccount = checkInformationLoginValid(emailField.getText(),passwordFieldHide.getText());
        System.out.println(validAccount);
        //KIỂM TRA TÀI KHOẢN HỢP LỆ HAY KHÔNG
        //EMAIL ĐÚNG FORMAT VÀ MẬT KHẨU KHÁC RỖNG
        if(validAccount){
            //KIỂM TRA TÀI KHOẢN CÓ TỒN TẠI HAY KHÔNG
            if(checkAccountExits()){
                Stage stage = (Stage) loginButton.getScene().getWindow();
                Stage stageMain = new Stage();
                Scene scene = null;
                warningLogin.setVisible(false);
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(MainApplication.class.getResource("view/main_activity.fxml"));
                try {
                    scene = new Scene(fxmlLoader.load(), 1200.0D, 650.0D);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                MainController mainController = fxmlLoader.getController();
                mainController.setLoginActivityController(this);
                stageMain.centerOnScreen();
                stageMain.setTitle("Cinema");
                try {
                    stageMain.getIcons().add(new Image(new FileInputStream("C:\\Users\\duy khan\\IdeaProjects\\MovieTickets\\src\\main\\resources\\com\\example\\movietickets\\image\\background_login.png")));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                stageMain.setScene(scene);
                stageMain.show();
                stage.close();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Sign Up");
                alert.setHeaderText("Email or password not incorrect, please check again! !");
                alert.initOwner(loginButton.getScene().getWindow());
                alert.showAndWait();
            }
        }
        else {
            warningLogin.setVisible(true);
        }
    }


    //KIỂM TRA SĐT VA PASSWORD ĐÃ ĐÚNG CHƯA
    public boolean checkInformationLoginValid(String email, String passWord){
        //REGEX VALID PHONE NUMBER
        //SĐT ĐÚNG BẮT ĐẦU BẰNG 0 VÀ TỐI ĐA 10 SỐ
        //ĐÚNG TRẢ VỀ TRUE, SAI TRẢ VỀ FALSE
        Pattern pattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches() && !passWord.isEmpty();
    }

    //BẮT SỰ KIỆN CLICK TRÊN BUTTON SIGN UP
    //TẠO BẢNG ĐĂNG KÍ TÀI KHOẢN
    public void onClickButtonSignUp(MouseEvent mouseEvent){
        Stage signUpStage = new Stage();
        Scene scene = null;
        if (signUpStage.getScene() == null) {
            System.out.println("FIRST INIT");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/sign_up_layout.fxml"));
            try {
                scene = new Scene(loader.load(), 430, 530);
            } catch (IOException e) {
                e.printStackTrace();
            }
            signUpStage.setResizable(false);
            signUpStage.setScene(scene);
            signUpStage.initModality(Modality.APPLICATION_MODAL);
            try {
                signUpStage.getIcons().add(new Image(new FileInputStream("C:\\Users\\duy khan\\IdeaProjects\\MovieTickets\\src\\main\\resources\\com\\example\\movietickets\\image\\background_login.png")));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            signUpStage.setTitle("Sign Up");
        }
        System.out.println("SHOWs");
        signUpStage.show();
    }

    //BẮT SỰ KIỆN NÚT QUÊN MẬT KHẨU
    @FXML
    public void onButtonForgotPassword(MouseEvent mouseEvent){
        Stage stageForgotPass = new Stage();
        stageForgotPass.setTitle("Forgot Password");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("view/forgot_password_layout.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(loader.load(),500,450);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            stageForgotPass.getIcons().add(new Image(new FileInputStream("C:\\Users\\duy khan\\IdeaProjects\\MovieTickets\\src\\main\\resources\\com\\example\\movietickets\\image\\background_login.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        stageForgotPass.setScene(scene);
        stageForgotPass.show();
    }

    //BẮT SỰ KIỆN KHI NHẤN ENTER TRÊN FIELD PASSWORD
    @FXML
    public void onEnterPressed(KeyEvent keyEvent){
        if(keyEvent.getCode() == KeyCode.ENTER){
            System.out.println("ENTER PRESSED ON TEXTFIELD");
            login();
        }
    }

    //KIỂM TRA ACCOUNT TỒN TẠI HAY KHÔNG
    public boolean checkAccountExits(){
        String username = emailField.getText().trim();
        String password = passwordFieldHide.getText().trim();
        String query = String.format("SELECT * FROM ACCOUNT WHERE USERNAME='%s' AND PASSWORD = '%s'",username,password);
        ResultSet resultSet = null;
        boolean check = false;
        try {
            resultSet = connection.createStatement().executeQuery(query);
            check = resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return check;
    }


}
