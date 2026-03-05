package ru.itis.dis403.lab02.context_homework.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.io.Writer;

@Controller
public class MainController {

    @GetMapping("/index")
    public void index(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=utf-8");
        Writer writer = resp.getWriter();
        writer.write(
                "<html>" +
                        "<head>" +
                            "<title>Index</title>" +
                            "<meta charset='UTF-8'>" +
                        "</head>" +
                        "<body>" +
                            "<div>" +
                                "<h1>Приветствую на главной странице index</h1>" +
                            "</div>" +
                        "</body>" +
                        "</html>");

    }

    @GetMapping("/home")
    public void home(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=utf-8");
        Writer writer = resp.getWriter();
        writer.write(
                "<html>" +
                        "<head>" +
                            "<title>Home</title>" +
                            "<meta charset='UTF-8'>" +
                        "</head>" +
                        "<body>" +
                            "<div>" +
                                "<h1>Приветствую на домашней странице (home)</h1>" +
                            "</div> " +
                        "</body>" +
                    "</html>");

    }

}
