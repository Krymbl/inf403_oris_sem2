package ru.itis.dis403.lab04;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.itis.dis403.lab04.config.Config;
import ru.itis.dis403.lab04.model.Phone;
import ru.itis.dis403.lab04.service.PhoneService;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context =
                new AnnotationConfigApplicationContext(Config.class);

        Phone phone = new Phone();
        phone.setNumber("123456");
        PhoneService phoneService = context.getBean(PhoneService.class);
        phoneService.save(phone);
        phoneService.getPhoneLike("1");

        System.out.println(phoneService.getPhoneLike("1%"));
    }
}