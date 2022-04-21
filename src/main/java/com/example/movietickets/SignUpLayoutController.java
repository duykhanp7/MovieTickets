package com.example.movietickets;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpLayoutController {

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

    boolean showPass = false;
    boolean showPassConfirm = false;

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
        boolean emailValid = regexEmail(email.getText());
        boolean fullNameValid = regexFullName(fullName.getText());
        boolean phoneNumberValid = regexPhoneNumber(phoneNumber.getText());
        boolean passwordIsTheSameBoolean = passwordIsTheSame();

        textViewWrong.setVisible(false);
        fullName.setStyle("-fx-text-fill:#000000");
        email.setStyle("-fx-text-fill:#000000");
        phoneNumber.setStyle("-fx-text-fill:#000000");
        if(fullName.getText().trim().isEmpty() && email.getText().trim().isEmpty() && phoneNumber.getText().trim().isEmpty() && passWordHide.getText().trim().isEmpty() && confirmPasswordHide.getText().trim().isEmpty()){
            textViewWrong.setVisible(true);
            textViewWrong.setText("(*) Please completely fill in the blanks.");
        }
        else{
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

    }

}
