package com.example.movietickets.model;

import com.example.movietickets.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Customer extends People{

    String typeCustomer;//LOẠI KHÁCH HÀNG
    int totalPoint;//TỔNG ĐIỂM
    Bill bill;//HÓA ĐƠN

    public Customer(){
        this.typeCustomer = Utils.CUSTOMER_NORMAL;
        this.totalPoint = 0;
        this.bill = new Bill();
    }

    public Customer(String id, String name, String phone, String sex ,String typeCustomer, String totalPoint) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phone;
        this.sex = sex;
        this.typeCustomer = typeCustomer;
        this.totalPoint = Integer.parseInt(totalPoint);
    }

    public void setId(String id){
        super.setId(id);
    }

    public String getId(){
        return super.getId();
    }

    public void setName(String name){
        super.setName(name);
    }

    public String getName(){
        return super.getName();
    }

    public void setPhoneNumber(String phoneNumber){
        super.setPhoneNumber(phoneNumber);
    }

    public String getPhoneNumber(){
        return super.getPhoneNumber();
    }

    public void setSex(String sex){
        super.setSex(sex);
    }

    public String getSex(){
        return super.getSex();
    }

    public String getTypeCustomer() {
        return typeCustomer;
    }

    public void setTypeCustomer(String typeCustomer) {
        this.typeCustomer = typeCustomer;
    }

    public int getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(int totalPoint) {
        this.totalPoint = totalPoint;
    }

    public void plusPoints(int points){
        this.totalPoint+=points;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    //CỨ MỖI VÉ MUA SẼ ĐƯỢC TẶNG 100 ĐIỂM
    // MỖI 1000 ĐIỂM ĐƯỢC QUY ĐỔI SANG 10000VNĐ
    public void usingDiscount(){
        int bonus = (totalPoint*10000)/1000;
        int total = this.bill.getTotalPrice();
        int sub = 0;
        if(bonus > total){
            sub = bonus - total;
            this.bill.totalPrice = 0;
            totalPoint = (sub*1000)/10000;
        }
        else{
            this.bill.totalPrice -= bonus;
            this.totalPoint = 0;
            this.bill.setDiscount(bonus);
        }
    }

    public boolean checkCanUsingDiscount(){
        return totalPoint >= 1000;
    }

}
