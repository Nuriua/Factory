package com.example.factory.modules;

public class User {
    private String name;
    private String email, pass, phone, role, photoUrl;
    public User() {}

    public User(String name, String email, String pass, String phone, String role, String photoUrl) {
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.phone = phone;
        this.role = role;
        this.photoUrl = photoUrl;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
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
        return getPhotoUrl();
    }

    public void setPhotoUrl(String photoUrl){
        this.photoUrl = photoUrl;
    }

}
