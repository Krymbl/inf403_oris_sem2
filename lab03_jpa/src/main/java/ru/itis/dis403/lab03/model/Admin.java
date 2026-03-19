package ru.itis.dis403.lab03.model;

import jakarta.persistence.Entity;

@Entity
public class Admin extends Person { //При запросе Admin Hibernate делает JOIN между person и admin

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}