package ru.itis.dis403.lab01.homework;


import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import ru.itis.dis403.lab01.homework.component.Application;
import ru.itis.dis403.lab01.homework.config.Context;
import ru.itis.dis403.lab01.homework.config.DispatcherServlet;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        Context context = new Context();
        Application app = (Application) context.getComponent(Application.class);
        app.run();

        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir("temp");
        Connector conn = new Connector();
        conn.setPort(8080);
        tomcat.setConnector(conn);

        String contextPath = "";

        String docBase = new File(".").getAbsolutePath();
        org.apache.catalina.Context tomcatContext = tomcat.addContext(contextPath, docBase);

        DispatcherServlet dispatcherServlet = new DispatcherServlet(context);
        String servletName = "dispatcherServlet";
        tomcat.addServlet(contextPath, servletName, dispatcherServlet);

        tomcatContext.addServletMappingDecoded("/*", servletName);

        try {
            tomcat.start();
            tomcat.getServer().await();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }



    }
}
