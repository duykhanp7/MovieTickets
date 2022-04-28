package com.example.movietickets.model;

public class Staff extends People{

    private String password;//PASSWORD NHÂN VIÊN


    public Staff() {
        this.password = "";
    }

    public Staff(String id, String name, String phone, String email,String sex){
        this.id = id;
        this.name = name;
        this.phoneNumber = phone;
        this.email = email;
        this.sex = sex;
    }

    public void setId(String id){super.setId(id);}
    public String getId(){ return super.getId();}

    public void setName(String name){super.setName(name);}
    public String getName(){return super.getName();}

    public void setPhoneNumber(String phoneNumber){super.setPhoneNumber(phoneNumber);}
    public String getPhoneNumber(){return super.getPhoneNumber();}

    public void setEmail(String email){super.setEmail(email);}
    public String getEmail(){return super.getEmail();}

    public void setSex(String sex){super.setSex(sex);}
    public String getSex(){return super.getSex();}

    public void setPassword(String password){this.password = password;}
    public String getPassword(){return this.password;}

}
