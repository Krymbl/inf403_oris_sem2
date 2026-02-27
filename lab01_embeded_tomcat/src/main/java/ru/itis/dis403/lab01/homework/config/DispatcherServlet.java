package ru.itis.dis403.lab01.homework.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class DispatcherServlet extends HttpServlet {

    private Context context;

    public DispatcherServlet(Context context) {
        this.context = context;
    }
    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        HandlerMethod handlerMethod = context.getMappings().get(path);
        if (handlerMethod != null) {
            try {
                handlerMethod.invoke(req,resp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            resp.setStatus(404);
            resp.setContentType("text/html; charset=utf-8");
            PrintWriter writer = resp.getWriter();
            writer.write(
                    "<html>" +
                        "<head>" +
                            "<title>404 ошибка</title>" +
                            "<meta charset='utf-8'/>" +
                        "</head>" +
                        "<body>"+
                            "<h1>Ресурс не найден: " + path + "</h1>"+
                        "</body>" +
                    "</html>");
        }


    }
}
