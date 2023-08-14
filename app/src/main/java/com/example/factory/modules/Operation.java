package com.example.factory.modules;

public class Operation {
    private String name, model, size, price, amount, amountOfDone, sum, seamstress, uid, pack;
    public Operation() {}

    public Operation(String name, String model, String size, String price, String amount, String amountOfDone, String sum, String seamstress, String uid, String pack) {
        this.name = name;
        this.model = model;
        this.size = size;
        this.price = price;
        this.amount = amount;
        this.amountOfDone = amountOfDone;
        this.sum = sum;
        this.seamstress = seamstress;
        this.uid = uid;
        this.pack = pack;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getModel(){
        return model;
    }

    public void setModel(String model){
        this.model = model;
    }

    public String getSize(){
        return size;
    }

    public void setSize(String size){
        this.size = size;
    }

    public String getPrice(){
        return price;
    }

    public void setPrice(String price){
        this.price = price;
    }

    public String getAmount(){
        return amount;
    }

    public void setAmount(String amount){
        this.amount = amount;
    }

    public String getAmountOfDone(){
        return amountOfDone;
    }

    public void setAmountOfDone(String amountOfDone){
        this.amountOfDone = amountOfDone;
    }

    public String getSum(){
        return sum;
    }

    public void setSum(String sum){
        this.sum = sum;
    }

    public String getSeamstress(){
        return seamstress;
    }

    public void setSeamstress(String seamstress){
        this.seamstress = seamstress;
    }

    public String getUid(){
        return uid;
    }

    public void setUid(String uid){
        this.uid = uid;
    }

    public String getPack(){
        return pack;
    }

    public void setPack(String pack){
        this.pack = pack;
    }

}
