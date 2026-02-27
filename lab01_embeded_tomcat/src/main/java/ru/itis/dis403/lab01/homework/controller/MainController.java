package ru.itis.dis403.lab01.homework.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.dis403.lab01.homework.annotation.Controller;
import ru.itis.dis403.lab01.homework.annotation.GetMapping;

import java.io.IOException;
import java.io.Writer;

@Controller
public class MainController {

    @GetMapping("/index")
    public void index(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Writer writer = resp.getWriter();
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        writer.write("<html><head><title>Index</title><meta charset='UTF-8'></head><body><div><h1>Приветствую на главной странице index</h1></div></body></html>");

    }

    @GetMapping("/home")
    public void home(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Writer writer = resp.getWriter();
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        writer.write("<html><head><title>Home</title><meta charset='UTF-8'></head><body><div><h1>Приветствую на домашней странице (home)</h1></div></body></html>");

    }

}
