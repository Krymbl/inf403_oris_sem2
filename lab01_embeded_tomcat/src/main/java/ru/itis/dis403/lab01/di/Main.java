package ru.itis.dis403.lab01.di;

import ru.itis.dis403.lab01.di.component.Application;
import ru.itis.dis403.lab01.di.config.Context;

public class Main {
    public static void main(String[] args) {
        Context context = new Context();

        Application application = (Application) context.getComponent(Application.class);
        application.run();
    }
}