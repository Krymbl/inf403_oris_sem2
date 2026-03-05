package ru.itis.dis403.lab02.context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.itis.dis403.lab02.context.component.Application;
import ru.itis.dis403.lab02.context.config.Config;

public class Main {
    public static void main(String[] args) {

        System.out.println(123);
        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);

        context.getBean(Application.class).run();
    }
}
