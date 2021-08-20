package com.pack.model;

public class Email {

    private String name;
    private String address;

    public Email(String name, String address){
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}
