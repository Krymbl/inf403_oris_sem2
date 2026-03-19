package ru.itis.dis403.lab04.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.InheritanceType.*;

//@Getter@Setter
@Entity
@Inheritance(strategy = JOINED)
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected String name;

    @ManyToOne
    protected Phone phone;

    protected String role;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    protected Set<Phone> phones = new HashSet<>();

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public Set<Phone> getPhones() {
        return phones;
    }

    public void setPhones(Set<Phone> phones) {
        this.phones = phones;
    }

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getExtra() {
        return "-";
    }

    @Override
    public String toString() {
        return
                this.getClass().getSimpleName() + "{" +
                        "id=" + id +
                        ", name='" + name + '\'' +
                        ", phone=" + phone +
                        '}';
    }
}