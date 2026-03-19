package ru.itis.dis403.lab03.model;

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
}