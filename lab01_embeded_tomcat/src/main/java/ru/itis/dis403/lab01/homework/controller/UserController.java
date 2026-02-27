package ru.itis.dis403.lab01.homework.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.dis403.lab01.homework.annotation.Controller;
import ru.itis.dis403.lab01.homework.annotation.GetMapping;

import java.io.IOException;
import java.io.Writer;

@Controller
public class UserController {

    @GetMapping("/user/profile")
    public void profile(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Writer writer = resp.getWriter();
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        writer.write("<html><head><title>User Profile</title><meta charset='UTF-8'></head><body><div><h1>Профиль пользователя</h1></div></body></html>");

    }

    @GetMapping("/user/settings")
    public void settings(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Writer writer = resp.getWriter();
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        writer.write("<html><head><title>User Settings</title><meta charset='UTF-8'></head><body><div><h1>Настройки пользователя</h1></div></body></html>");

    }
}
