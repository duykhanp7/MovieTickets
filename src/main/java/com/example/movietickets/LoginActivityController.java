package com.example.movietickets;

import com.example.movietickets.model.SendSMS;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivityController implements Initializable {


    @FXML Label buttonSignUp;
    @FXML Label forgot_pass_button;
    @FXML Label warningLogin;
    @FXML ImageView ic_show_pass;
    @FXML PasswordField passwordFieldHide;
    @FXML TextField phoneNumberField;
    @FXML TextField passwordFieldShow;
    @FXML Button loginButton;

    boolean showPass = false;

    //SIGN UP LAYOUT
    Stage signUpStage = new Stage();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showHidePassWord(showPass);
    }

    //NẾU TRUE THÌ SHOW PASS, KHÔNG THÌ THÔI
    public void showHidePassWord(boolean state){
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
        boolean validAccount = checkInformationLoginValid(phoneNumberField.getText(),passwordFieldHide.getText());
        if(validAccount){
            //LOAD STAGE CỦA LOGIN ACTIVITY VÀ ĐÓNG NÓ
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
            warningLogin.setVisible(true);
        }
    }


    //KIỂM TRA SĐT VA PASSWORD ĐÃ ĐÚNG CHƯA
    public boolean checkInformationLoginValid(String phoneNumber, String passWord){
        //REGEX VALID PHONE NUMBER
        //SĐT ĐÚNG BẮT ĐẦU BẰNG 0 VÀ TỐI ĐA 10 SỐ
        //ĐÚNG TRẢ VỀ TRUE, SAI TRẢ VỀ FALSE
        Pattern pattern = Pattern.compile("0+[0-9]{9}");
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    //BẮT SỰ KIỆN CLICK TRÊN BUTTON SIGN UP
    //TẠO BẢNG ĐĂNG KÍ TÀI KHOẢN
    public void onClickButtonSignUp(MouseEvent mouseEvent){
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
            signUpStage.setTitle("Sign Up");
        }
        System.out.println("SHOWs");
        signUpStage.show();
    }

}
