package com.example.movietickets;

import com.example.movietickets.model.ConnectionToDatabaseController;
import com.example.movietickets.model.JavaMail;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


//CONTROLLER ĐĂNG NHẬP
public class SignUpLayoutController implements Initializable {

    @FXML TextField fullName;
    @FXML TextField email;
    @FXML TextField phoneNumber;
    @FXML TextField passWordShow;
    @FXML TextField confirmPasswordShow;
    @FXML PasswordField passWordHide;
    @FXML PasswordField confirmPasswordHide;
    @FXML Button buttonSignUp;
    @FXML ImageView imageViewShowHidePass;
    @FXML ImageView imageViewShowHidePassConfirm;
    @FXML Label textViewWrong;
    @FXML Label textViewCodeMatch;
    @FXML TextField textFieldConfirmCode;
    @FXML Button buttonSendCode;
    @FXML ComboBox<String> comboBoxSex;
    boolean showPass = false;
    boolean showPassConfirm = false;
    boolean matchCode = false;
    int code = 0;
    ConnectionToDatabaseController connectionToDatabaseController;
    Connection connection;
    public static final String STAFF_ID = "NV";
    public static int STAFF_POS_ID = 0;
    int timeCountDown = 60;
    Timer timer = null;

    //KIỂM TRA TÊN ĐÚNG CHƯA (TOÀN BỘ LÀ CHỮ)
    public boolean regexFullName(String fullName){
        Pattern pattern = Pattern.compile("^[a-zA-Z\\s]*$");
        System.out.println("FULL NAME : "+pattern.matcher(fullName).matches());
        return pattern.matcher(fullName).matches();
    }

    //KIỂM TRA EMAIL ĐÚNG CHƯA (FORMAT : name@gmail.com)
    public boolean regexEmail(String email){
        Pattern pattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        System.out.println("EMAIL : "+pattern.matcher(email).matches());
        return pattern.matcher(email).matches();
    }

    //KIỂM TRA XEM SĐT ĐÃ ĐÚNG FORMAT CHƯA(BẮT ĐẦU BẰNG 0 VÀ TỐI ĐA 10 CHỮ SỐ)
    public boolean regexPhoneNumber(String phone){
        Pattern pattern = Pattern.compile("0+[0-9]{9}");
        Matcher matcher = pattern.matcher(phone);
        System.out.println("PHONE NUMBER : "+matcher.matches());
        return matcher.matches();
    }

    public boolean checkMatchCode(){
        System.out.println("CODE EQUALS : "+String.valueOf(code).equals(textFieldConfirmCode.getText().trim()));
        matchCode = String.valueOf(code).equals(textFieldConfirmCode.getText().trim());
        return matchCode;
    }


    //KIỂM TRA 2 PASSWORD CÓ TRÙNG NHAU KHÔNG
    public boolean passwordIsTheSame(){
        return passWordHide.getText().trim().equals(confirmPasswordHide.getText().trim());
    }


    //SHOW OR HIDE PASSWORD
    @FXML
    public void showPass(){
        showPass = !showPass;
        passWordHide.setVisible(!showPass);
        passWordShow.setVisible(showPass);
        passWordShow.setText(passWordHide.getText());
        try {
            imageViewShowHidePass.setImage(new Image(new FileInputStream(showPass ? "C:\\Users\\duy khan\\IdeaProjects\\MovieTickets\\src\\main\\resources\\com\\example\\movietickets\\image\\show_pass.png":"C:\\Users\\duy khan\\IdeaProjects\\MovieTickets\\src\\main\\resources\\com\\example\\movietickets\\image\\hide_pass.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //SHOW OR HIDE PASSWORD CONFIRM
    @FXML
    public void showPassConfirm(){
        showPassConfirm = !showPassConfirm;
        confirmPasswordHide.setVisible(!showPassConfirm);
        confirmPasswordShow.setVisible(showPassConfirm);
        confirmPasswordShow.setText(confirmPasswordHide.getText());
        try {
            imageViewShowHidePassConfirm.setImage(new Image(new FileInputStream(showPassConfirm ? "C:\\Users\\duy khan\\IdeaProjects\\MovieTickets\\src\\main\\resources\\com\\example\\movietickets\\image\\show_pass.png":"C:\\Users\\duy khan\\IdeaProjects\\MovieTickets\\src\\main\\resources\\com\\example\\movietickets\\image\\hide_pass.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //BẮT SỰ KIỆN CLICK BUTTON ĐĂNG KÍ
    @FXML
    public void onButtonSignUpClick(MouseEvent mouseEvent){
        String fullNameTxt = fullName.getText().trim();
        String emailTxt = email.getText().trim();
        String phoneNumberTxt = phoneNumber.getText().trim();
        String passwordTxt = passWordHide.getText().trim();
        String sexTxt = comboBoxSex.getValue();
        ///
        boolean emailValid = regexEmail(emailTxt);
        boolean fullNameValid = regexFullName(fullNameTxt);
        boolean phoneNumberValid = regexPhoneNumber(phoneNumberTxt);
        boolean passwordIsTheSameBoolean = passwordIsTheSame();

        textViewWrong.setVisible(false);
        fullName.setStyle("-fx-text-fill:#000000");
        email.setStyle("-fx-text-fill:#000000");
        phoneNumber.setStyle("-fx-text-fill:#000000");
        if(fullNameTxt.trim().isEmpty() && emailTxt.trim().isEmpty() && phoneNumberTxt.trim().isEmpty() && passWordHide.getText().trim().isEmpty() && confirmPasswordHide.getText().trim().isEmpty() && textFieldConfirmCode.getText().isEmpty()){
            textViewWrong.setVisible(true);
            textViewWrong.setText("(*) Please completely fill in the blanks.");
        }
        else {
            if(!passwordIsTheSameBoolean){
                textViewWrong.setVisible(true);
                textViewWrong.setText("(*) Passwords are not the same.");
            }
            else if(!emailValid || !fullNameValid || !phoneNumberValid){
                textViewWrong.setVisible(true);
                fullName.setStyle(!fullNameValid ? "-fx-text-fill:#f07e00":"-fx-text-fill:#000000");
                email.setStyle(!emailValid ? "-fx-text-fill:#f07e00":"-fx-text-fill:#000000");
                phoneNumber.setStyle(!phoneNumberValid ? "-fx-text-fill:#f07e00":"-fx-text-fill:#000000");
                textViewWrong.setText("(*) Please correct the wrong field.");
            }

        }

        textViewCodeMatch.setVisible(!checkMatchCode());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sign Up");
        alert.initOwner(buttonSignUp.getScene().getWindow());
        if(emailValid && phoneNumberValid && fullNameValid && passwordIsTheSameBoolean && matchCode){
            alert.setHeaderText("Successfully created an account.");
            Platform.runLater(() -> {
                if(timer != null ){
                    timer.cancel();
                }
                buttonSendCode.setDisable(false);
                buttonSendCode.setText("Send Code");
                timer = null;
            });
            saveAccountToDatabase(fullNameTxt,emailTxt,phoneNumberTxt,sexTxt,passwordTxt);
        }
        else{
            alert.setHeaderText("Please check wrong or missing fields");
        }
        alert.showAndWait();

    }


    //BẮT SỰ KIỆN TRÊN BUTTON GỬI MÃ XÁC NHẬN
    @FXML
    public void onButtonSendCode(MouseEvent mouseEvent){
        String emailText = email.getText();
        String phoneNumberText = phoneNumber.getText().trim();
        if(!emailText.isEmpty() && regexEmail(emailText)){
            new Thread(() -> {
                //KIỂM TRA XEM EMAIL NÀY ĐÃ TỒN TẠI Ở DATABASE CHƯA
                //NẾU CHƯA THÌ ĐƯỢC TẠO ACCOUNT
                //KHÔNG THÌ HIỂN THỊ THÔNG BÁO EMAIL ĐÃ ĐƯỢC SỬ DỤNG
                if(checkEmailValid() && checkPhoneNumberValid(phoneNumberText)){
                    new Thread(() -> {
                        SecureRandom random = new SecureRandom();
                        int min = 100000;
                        int max = 999999;
                        String CODE_TO_CONFIRM = String.valueOf(random.nextInt(max-min+1)+min);
                        code = Integer.parseInt(CODE_TO_CONFIRM);
                        String details = "Please enter this code in the confirmation box to complete account registration";
                        JavaMail javaMail = new JavaMail(emailText,"Phone Number Verification Code",details+" : "+CODE_TO_CONFIRM);
                        javaMail.sendEmail();
                    }).start();
                    Platform.runLater(() -> {
                        timeCountDown = 60;
                        buttonSendCode.setDisable(true);
                        timer = new Timer();
                        timer.scheduleAtFixedRate(new TimerTask() {
                            @Override
                            public void run() {
                                --timeCountDown;
                                Platform.runLater(() -> buttonSendCode.setText(timeCountDown < 10 ? "0"+timeCountDown: String.valueOf(timeCountDown)));
                                if(timeCountDown == 0){
                                    Platform.runLater(() -> {
                                        buttonSendCode.setText("Send Code");
                                        buttonSendCode.setDisable(false);
                                        timer = null;
                                    });
                                    timer.cancel();
                                    timer = null;
                                }
                            }
                        },0,1000);
                    });
                }
                else{
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Sign Up");
                        alert.setHeaderText("Email or phone number is already in use, please choose another email or phone number!");
                        alert.initOwner(buttonSendCode.getScene().getWindow());
                        alert.showAndWait();
                    });
                }
            }).start();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Sign Up");
            alert.setHeaderText("Invalid email, please check again !");
            alert.initOwner(buttonSendCode.getScene().getWindow());
            alert.showAndWait();
        }
    }

    //KHỞI TẠO COMBO BOX CHỌN GIỚI TÍNH
    public void initComboBoxSex(){
        comboBoxSex.getItems().clear();
        comboBoxSex.getItems().addAll("Male","Female","Others");
        Platform.runLater(() -> comboBoxSex.getSelectionModel().selectFirst());
    }


    //KIỂM TRA EMAIL ĐĂNG KÍ TỒN TẠI CHƯA
    public boolean checkEmailValid(){
        String emailTxt = email.getText().trim();
        System.out.println("EMAIL TEXT : "+emailTxt);
        boolean check = false;
        ResultSet resultSet = null;
        try {
             resultSet = connection.createStatement().executeQuery(String.format("SELECT USERNAME FROM ACCOUNT WHERE USERNAME = '%s'",emailTxt));
             check =  resultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("USERNAME : "+check);
        return !check;
    }


    //KIỂM TRA SĐT ĐĂNG KÍ ĐÃ ĐƯỢC SỬ DỤNG CHƯA
    public boolean checkPhoneNumberValid(String phoneNumber){
        String query = String.format("SELECT PHONE_NUMBER FROM STAFF WHERE PHONE_NUMBER='%s'",phoneNumber);
        ResultSet resultSet = null;
        boolean check = false;
        try {
            resultSet = connection.createStatement().executeQuery(query);
            check = resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("CHECK PHONE : "+check);
        return !check;
    }

    //LƯU THÔNG TIN TÀI KHOẢN MỚI VÀO DATABASE
    public void saveAccountToDatabase(String fullNameTxt,String emailTxt,String phoneNumberTxt,String sexTxt,String passwordTxt){
        try {
            String query = String.format("INSERT INTO STAFF(ID,NAME,PHONE_NUMBER,EMAIL,SEX) " +
                    "VALUES ('%s','%s','%s','%s','%s')",STAFF_ID+(STAFF_POS_ID < 0 ? "0"+STAFF_POS_ID++:STAFF_POS_ID++),
                                                            fullNameTxt,phoneNumberTxt,emailTxt,sexTxt);
            String accountQry = String.format("INSERT INTO ACCOUNT(USERNAME,PASSWORD)" +
                                                "VALUES ('%s','%s')",emailTxt,passwordTxt);
            connection.createStatement().execute(query);
            connection.createStatement().execute(accountQry);
        } catch (SQLException e) {
            System.out.println(e.getClass().getSimpleName());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initComboBoxSex();
        connectionToDatabaseController = new ConnectionToDatabaseController();
        connection = connectionToDatabaseController.getConnection();
        STAFF_POS_ID = countStaff();
    }

    //ĐẾM SỐ LƯỢNG TÀI KHOẢN
    public int countStaff(){
        String query = "SELECT COUNT(*) AS COUNT FROM STAFF";
        ResultSet resultSet;
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
