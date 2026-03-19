package ru.itis.dis403.lab04.model;

import jakarta.persistence.Entity;

@Entity
public class Client extends Person {

    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getExtra() {
        return getAddress();
    }
}