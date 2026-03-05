package ru.itis.dis403.lab02.context_homework;


import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import ru.itis.dis403.lab02.context_homework.component.Application;
import ru.itis.dis403.lab02.context_homework.config.Config;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigWebApplicationContext context =
            new AnnotationConfigWebApplicationContext();
        context.register(Config.class);

        context.refresh(); //Создаст все Beans
        context.getBean(Application.class).run();

        DispatcherServlet dispatcher = new DispatcherServlet(context);

        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir("temp");
        Connector conn = new Connector();
        conn.setPort(8090);
        tomcat.setConnector(conn);
        String contextPath = "";

        String docBase = new File(".").getAbsolutePath();
        Context ctx = tomcat.addContext(contextPath, docBase);

        String servletName = "dispatcherServlet";
        tomcat.addServlet(contextPath, servletName, dispatcher);

        ctx.addServletMappingDecoded("/*", servletName);

        try {
            tomcat.start();

            tomcat.getServer().await();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }



    }
}
