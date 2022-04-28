package com.example.movietickets;

import com.example.movietickets.model.ConnectionToDatabaseController;
import com.example.movietickets.model.JavaMail;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

//CONTROLLER CHO THAY ĐỔI MẬT KHẨU, CÓ GỬI MÃ CODE XÁC NHẬN VỀ EMAIL
public class ForgotPasswordLayoutController implements Initializable {


    @FXML TextField emailRecovery;
    @FXML TextField textFiledCode;
    @FXML TextField newPasswordShow;
    @FXML TextField confirmNewPasswordShow;
    @FXML PasswordField newPasswordHide;
    @FXML PasswordField confirmNewPasswordHide;
    @FXML Button buttonConfirmPassChanged;
    @FXML Button buttonGetCode;
    @FXML ImageView showHideNewPassword;
    @FXML ImageView showHideNewPasswordConfirm;
    @FXML Label textViewError;
    boolean showHideNewPassBoolean = false;
    boolean showHideNewPassConfirmBoolean = false;
    int codeSend = 0;
    boolean matchCode;
    ConnectionToDatabaseController controller ;
    Connection connection;
    Timer timer;
    int timeCountDown = 60;

    //KIỂM TRA EMAIL ĐÚNG CHƯA (FORMAT : name@gmail.com)
    public boolean regexEmail(String email){
        Pattern pattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        System.out.println("EMAIL : "+pattern.matcher(email).matches());
        return pattern.matcher(email).matches();
    }

    //KIỂM TRA MÃ GỬI ĐI VÀ MÃ USER NHẬP CÓ TRÙNG KHỚP NHAU KHÔNG
    public boolean checkMatchCode(){
        System.out.println("CODE EQUALS : "+String.valueOf(codeSend).equals(textFiledCode.getText().trim()));
        matchCode = String.valueOf(codeSend).equals(textFiledCode.getText().trim());
        return matchCode;
    }


    //KIỂM TRA 2 PASSWORD CÓ TRÙNG NHAU KHÔNG
    public boolean passwordIsTheSame(){
        return newPasswordHide.getText().trim().equals(confirmNewPasswordHide.getText().trim());
    }

    //BẮT SỰ KIỆN NÚT SHOW HOẶC HIDE PASSWORD TRÊN 2 FIELDS NEW PASSWORD AND CONFIRM PASSWORD
    @FXML
    public void onButtonShowHide(MouseEvent mouseEvent){
        //HIỂN THỊ HOẶC CHE MẬT KHẨU
        if(mouseEvent.getSource() == showHideNewPassword){
            showHideNewPassBoolean = !showHideNewPassBoolean;
            newPasswordHide.setVisible(!showHideNewPassBoolean);
            newPasswordShow.setText(newPasswordHide.getText());
            newPasswordShow.setVisible(showHideNewPassBoolean);
        }
        else if(mouseEvent.getSource() == showHideNewPasswordConfirm){
            showHideNewPassConfirmBoolean = !showHideNewPassConfirmBoolean;
            confirmNewPasswordHide.setVisible(!showHideNewPassConfirmBoolean);
            confirmNewPasswordShow.setText(confirmNewPasswordHide.getText());
            confirmNewPasswordShow.setVisible(showHideNewPassConfirmBoolean);
        }
    }


    //GỬI MÃ CODE ĐẾN ĐỊA CHỈ EMAIL NGƯỜI DÙNG ĐÃ NHẬP VÀ TRÙNG VỚI TÀI KHOẢN ĐÃ ĐĂNG KÍ
    @FXML
    public void onButtonSendCode(ActionEvent actionEvent){
        String emailText = emailRecovery.getText();
        //NẾU EMAIL HỢP LỆ THÌ RANDOM MỘT MÃ 6 CHỮ SỐ GỬI ĐẾN EMAIL CỦA USERS ĐÃ NHẬP
        if(!emailText.isEmpty() && regexEmail(emailText)){
            boolean check = checkEmailValid();
            System.out.println("CHECK USER : "+check);
            //KIỂM TRA EMAIL TỒN TẠI TRONG DATABASE CHƯA NẾU TỒN TẠI RỒI THÌ MỚI GỬI ĐƯỢC
            //CHƯA TỒN TẠI THÌ EMAIL ĐÓ CHƯA PHẢI LÀ TÀI KHOẢN CỦA NHÂN VIÊN
            if(check){
                new Thread(() -> {
                    //RANDOM MÃ
                    SecureRandom random = new SecureRandom();
                    int min = 100000;
                    int max = 999999;
                    String CODE_TO_CONFIRM = String.valueOf(random.nextInt(max-min+1)+min);
                    codeSend = Integer.parseInt(CODE_TO_CONFIRM);
                    String details = "Please enter this code in the confirmation box to complete reset password";
                    JavaMail javaMail = new JavaMail(emailText,"Phone Number Verification Code",details+" : "+CODE_TO_CONFIRM);
                    //GỬI MAIL
                    javaMail.sendEmail();
                }).start();
                //SAU ĐÓ BẮT ĐẦU ĐẾM NGƯỢC 60 GIÂY ĐỂ GỬI LẠI MÃ XÁC NHẬN NẾU CHƯA NHẬN ĐƯỢC EMAIL
                Platform.runLater(() -> {
                    timeCountDown = 60;
                    buttonGetCode.setDisable(true);
                    timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            --timeCountDown;
                            Platform.runLater(() -> buttonGetCode.setText(timeCountDown < 10 ? "0"+timeCountDown: String.valueOf(timeCountDown)));
                            //SAU KHOẢNG THỜI GIAN 60 GIÂY THÌ CHO PHÉP RESEND LẠI MÃ XÁC NHẬN
                            if(timeCountDown == 0){
                                Platform.runLater(() -> {
                                    buttonGetCode.setText("Send Code");
                                    buttonGetCode.setDisable(false);
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
                //NẾU EMAIL KHÔNG ĐÚNG THÔNG BÁO ĐẾN USERS
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Sign Up");
                    alert.setHeaderText("Email is not incorrect, please check again! !");
                    alert.initOwner(buttonGetCode.getScene().getWindow());
                    alert.showAndWait();
                });
            }
        }
        else{
            //THÔNG BÁO RẰNG EMAIL KHÔNG HỢP LỆ
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Sign Up");
            alert.setHeaderText("Invalid email, please check again !");
            alert.initOwner(buttonGetCode.getScene().getWindow());
            alert.showAndWait();
        }
    }


    //KIỂM TRA EMAIL CÓ TỒN TẠI KHÔNG
    public boolean checkEmailValid(){
        String emailTxt = emailRecovery.getText().trim();
        boolean check = false;
        //KẾT QUẢ TRẢ VỀ RESULT SET
        ResultSet resultSet;
        try {
            resultSet = connection.createStatement().executeQuery(String.format("SELECT USERNAME FROM ACCOUNT WHERE USERNAME = '%s'",emailTxt));
            check =  resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("USERNAME : "+check);
        return check;
    }

    //KIỂM TRA CÁC FIELD ĐÃ HỢP LỆ CHƯA
    //EMAIL, NEWPASSWORD, VÀ CONFIRMPASSWORD CÓ TRÙNG NHAU KHÔNG
    @FXML
    public void checkAllFieldsIsValid(MouseEvent mouseEvent){
        String email = emailRecovery.getText().trim();
        String newPassword = newPasswordHide.getText().trim();
        boolean emailValid = regexEmail(email);
        boolean passwordValid = passwordIsTheSame();
        checkMatchCode();
        System.out.println("PASS WORD CO : "+passwordValid+" -- "+matchCode+" -- "+emailValid);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Change Password");
        //NẾU CÁC FIELDS KHÔNG RỖNG, EMAIL HỢP LỆ, PASS HỢP LỆ, VÀ MÃ XÁC NHẬN HỢP LỆ THÌ ĐỔI PASS THÀNH CÔNG
        if(emailValid && passwordValid && matchCode){
            textViewError.setVisible(false);
            changePassword(email,newPassword);
            alert.setHeaderText("Change password successfully.");
            timeCountDown = 60;
            if(timer != null){
                timer.cancel();
            }
            timer = null;
            Platform.runLater(() -> {
                buttonGetCode.setText("Send Code");
                buttonGetCode.setDisable(false);
            });
        }
        else{
            alert.setHeaderText("Password change failed, recheck the entered fields.");
            textViewError.setVisible(true);
        }
        alert.initOwner(buttonConfirmPassChanged.getScene().getWindow());
        alert.showAndWait();
    }

    //UPDATE MẬT KHẨU MỚI Ở DATABASE
    public void changePassword(String username, String newPassword){
        String query = String.format("""
                UPDATE ACCOUNT
                SET PASSWORD='%s'
                WHERE USERNAME='%s'""",newPassword,username);
        try {
            connection.createStatement().executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller = new ConnectionToDatabaseController();
        connection = controller.getConnection();
    }
}
