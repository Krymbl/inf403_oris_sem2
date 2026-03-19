package ru.itis.dis403.lab04.model;

import jakarta.persistence.Entity;

@Entity
public class Admin extends Person {

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getExtra() {
        return getEmail();
    }
}