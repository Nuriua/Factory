package com.example.factory.modules;

public class Operation {
    private String name;
    private String size, price, amount, sum;
    public Operation() {}

    public Operation(String name, String size, String price, String amount, String sum) {
        this.name = name;
        this.size = size;
        this.price = price;
        this.amount = amount;
        this.sum = sum;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
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

    public String getSum(){
        return sum;
    }

    public void setSum(String sum){
        this.sum = sum;
    }

}
