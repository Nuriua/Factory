package com.example.factory.modules;

public class Operation {
    private String name, size, price, amount, sum, seamstress;
    public Operation() {}

    public Operation(String name, String size, String price, String amount, String sum, String seamstress) {
        this.name = name;
        this.size = size;
        this.price = price;
        this.amount = amount;
        this.sum = sum;
        this.seamstress = seamstress;
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

    public String getSeamstress(){
        return seamstress;
    }

    public void setSeamstress(String seamstress){
        this.seamstress = seamstress;
    }

}
