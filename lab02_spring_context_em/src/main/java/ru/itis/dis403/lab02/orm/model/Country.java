package ru.itis.dis403.lab02.orm.model;

import jakarta.persistence.*;

@Entity
public class Country {
    @Id
    Long id;
    @Column
    String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}