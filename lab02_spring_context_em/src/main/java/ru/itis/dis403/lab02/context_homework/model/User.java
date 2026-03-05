package ru.itis.dis403.lab02.context_homework.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class User {
    private int id;
    private String name;
    private String email;
    private String phone;
    private Language preferLanguage;
    private boolean isHideOnline;
    private PageType pageType;

    public User(int id, String name, String email, String phone, Language preferLanguage, boolean isHideOnline, PageType pageType) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.preferLanguage = preferLanguage;
        this.isHideOnline = isHideOnline;
        this.pageType = pageType;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id + '\'' +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", preferLanguage=" + preferLanguage +
                ", isOnline=" + isHideOnline +
                ", pageType=" + pageType +
                '}';
    }
}
