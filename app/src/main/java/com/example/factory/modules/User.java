package com.example.factory.modules;

public class User {
    private String uid, name, surname, email, pass, phone, role, photoUrl;
    public User() {}

    public User(String uid, String name, String surname, String email, String pass, String phone, String role, String photoUrl) {
        this.uid = uid;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.pass = pass;
        this.phone = phone;
        this.role = role;
        this.photoUrl = photoUrl;
    }

    public String getUid(){
        return uid;
    }

    public void setUid(String uid){
        this.uid = uid;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getSurname(){
        return surname;
    }

    public void setSurname(String surname){
        this.surname = surname;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPass(){
        return pass;
    }

    public void setPass(String pass){
        this.pass = pass;
    }

    public String getPhone(){
        return phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public String getRole(){
        return role;
    }

    public void setRole(String role){
        this.role = role;
    }

    public String getPhotoUrl(){
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl){
        this.photoUrl = photoUrl;
    }

}
