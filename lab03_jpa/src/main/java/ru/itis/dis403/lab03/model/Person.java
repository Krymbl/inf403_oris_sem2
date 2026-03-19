package ru.itis.dis403.lab03.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.InheritanceType.JOINED;

@Entity
@Inheritance(strategy = JOINED) //Это стратегия наследования в БД
//Стратегия JOINED означает — для каждого класса своя таблица, связанные через id:
//При запросе Admin Hibernate делает JOIN между person и admin
public class Person {

    @Id
    protected Long id;

    protected String name;

    @ManyToOne
    protected Phone phone;

    //Много Person — много Phone. Создаётся промежуточная таблица. Так со всеми manyToMany - persons_phones

    //cascade — что делать с связанными объектами:
    //PERSIST — если сохраняешь Person, сохранятся и его Phone автоматически
    //MERGE — если обновляешь Person, обновятся и Phone

    //fetch — когда загружать связанные данные:
    //EAGER — сразу вместе с Person (один большой JOIN запрос)
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