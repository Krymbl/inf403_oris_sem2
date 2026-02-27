package ru.itis.dis403.lab01.homework.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.dis403.lab01.homework.annotation.Controller;
import ru.itis.dis403.lab01.homework.annotation.GetMapping;
import ru.itis.dis403.lab01.homework.component.Application;
import ru.itis.dis403.lab01.homework.model.User;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

@Controller
public class UserController {
    private Application application;

    public UserController(Application application) {
        this.application = application;
    }

    @GetMapping("/user/profile")
    public void profile(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=utf-8");
        Writer writer = resp.getWriter();

        String reqId = req.getParameter("id");

        try {
            int id = Integer.parseInt(reqId);
            User user = application.getUserById(id);

            writer.write(
                    "<html>" +
                            "<head>" +
                            "<title>User Profile</title>" +
                            "<meta charset='UTF-8'>" +
                            "</head>" +
                            "<body>" +
                            "<div>" +
                            "<h1>Профиль пользователя c айди " + id + "</h1>" +
                            "Имя: " + user.getName() + "</br>" +
                            "Почта: " + user.getEmail() + "</br>" +
                            "Телефон: " + user.getPhone() + "</br>" +
                            "</div>" +
                            "</body>" +
                            "</html>");

        } catch (Exception e) {
            resp.setStatus(400);
            writer.write(
                    "<html>" +
                            "<head>" +
                            "<title>400 ошибка</title>" +
                            "<meta charset='utf-8'/>" +
                            "</head>" +
                            "<body>" +
                            "<h1>Введите числовое айди от 1 до 5</h1>" +
                            "</body>" +
                            "</html>");
        }



    }

    @GetMapping("/user/settings")
    public void settings(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=utf-8");
        Writer writer = resp.getWriter();

        String reqId = req.getParameter("id");





        try {
            int id = Integer.parseInt(reqId);
            Map<String, String> userSettings = application.getUserSettingsById(id);
            writer.write(
                    "<html>" +
                            "<head>" +
                            "<title>User Settings</title>" +
                            "<meta charset='UTF-8'>" +
                            "</head>" +
                            "<body>" +
                            "<div>" +
                            "<h1>Настройки пользователя с айди " + id + "</h1>" +
                            "Предпочтительный язык: " + userSettings.get("preferLanguage") + "</br>" +
                            "Скрыт онлайн: " + userSettings.get("isHideOnline") + "</br>" +
                            "Тип профиля: " + userSettings.get("pageType") + "</br>" +
                            "</div>" +
                            "</body>" +
                            "</html>");
        } catch (Exception e) {
            resp.setStatus(400);
            writer.write(
                    "<html>" +
                            "<head>" +
                            "<title>400 ошибка</title>" +
                            "<meta charset='utf-8'/>" +
                            "</head>" +
                            "<body>"+
                            "<h1>Введите числовое айди от 1 до 5</h1>"+
                            "</body>" +
                            "</html>");
        }

    }
}
