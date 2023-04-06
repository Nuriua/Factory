package com.example.factory.modules;

import java.util.ArrayList;

public class Item {
    private String title;
    private ArrayList<Operation> operations;

    public Item() {}
    public Item(String title) {
        this.title = title;
    }
    public Item(String title, ArrayList<Operation> operations) {
        this.operations = operations;
        this.title = title;
    }

    public String getTitle () {
        return title;
    }
    public void setTitle (String title) {
        this .title = title;
    }

    public ArrayList<Operation> getOperations () {
        return operations;
    }
    public void setOperations (ArrayList<Operation> operations) {
        this .operations = operations;
    }
}
