package ru.itis.dis403.lab01.di.config;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Context {

    private String scanPath = "ru.itis.dis403.lab01.di";

    private Map<Class<?>, Object> components = new HashMap<>();

    public Context() {
        scanComponent();
    }

    public Object getComponent(Class clazz) {
        return components.get(clazz);
    }

    private void scanComponent() {
        List<Class<?>> classes = PathScan.find(scanPath);

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
                    System.out.println(c + " добавлен");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

}