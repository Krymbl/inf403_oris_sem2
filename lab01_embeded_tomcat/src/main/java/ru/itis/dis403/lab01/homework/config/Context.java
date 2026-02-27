package ru.itis.dis403.lab01.homework.config;

import ru.itis.dis403.lab01.homework.annotation.Component;
import ru.itis.dis403.lab01.homework.annotation.Controller;
import ru.itis.dis403.lab01.homework.config.PathScan;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Context {

    private String scanPath = "ru.itis.dis403.lab01.homework";

    private Map<Class<?>, Object> components = new HashMap<>();
    private Map<Class<?>, Object> controllers = new HashMap<>();

    public Context() {
        scanComponent();
        scanController();
    }

    public Object getComponent(Class clazz) {
        return components.get(clazz);
    }

    public Object getController(Class clazz) {
        return controllers.get(clazz);
    }

    private void scanComponent() {
        List<Class<?>> classes = PathScan.findByAnnotation(scanPath, Component.class);

        // Создание экземпляров компонент
        // перебираем список классов
        // находим конструктор с аргументами, если такого нет - создаем экземпляр
        // размещаем в components, удаляем из списка
        // если есть конструктор с аргументами (только компоненты)
        // пытаемся получить из components объекты - аргументы
        // если полный набор - создаем экземпляр, иначе пропускаем итерацию
        int countClasses = classes.size();
        while (countClasses > 0) {
            // Перебираем классы компоненты
            //Метка для перехода к следующей итерации внешнего цикла.
            objectNotFound:
            for (Class c : classes) {
                if (components.get(c) != null) continue;
                // берем первый попавшийся контруктор
                Constructor constructor = c.getConstructors()[0];
                // извлекаем типы аргументов конструктора
                Class[] types = constructor.getParameterTypes();
                // Пытаемся найти готовые компоненты по аргументу
                Object[] args = new Object[types.length];
                for (int i = 0; i < types.length; ++i) {
                    args[i] = components.get(types[i]);
                    if (args[i] == null) {
                        continue objectNotFound;
                    }
                }

                try {
                    Object o = constructor.newInstance(args);
                    components.put(c, o);
                    countClasses--;
                    System.out.println(c + " добавлен Component");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private void scanController() {
        List<Class<?>> classes = PathScan.findByAnnotation(scanPath, Controller.class);
        int countClasses = classes.size();
        while (countClasses > 0) {

            objectNotFound:
            for (Class c : classes) {
                if (controllers.get(c) != null) continue;
                Constructor constructor = c.getConstructors()[0];
                Class[] types = constructor.getParameterTypes();

                // Пытаемся найти компоненты для аргументов
                Object[] args = new Object[types.length];
                for (int i = 0; i < types.length; ++i) {
                    args[i] = components.get(types[i]);
                    if (args[i] == null) {
                        continue objectNotFound; // зависимости еще не готовы
                    }
                }

                try {
                    Object o = constructor.newInstance();
                    controllers.put(c, o);
                    countClasses--;
                    System.out.println(c + " добавлен Controller");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
        }


    }

}