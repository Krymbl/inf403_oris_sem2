package ru.itis.dis403.lab01.di.config;

import ru.itis.dis403.lab01.di.annotation.Component;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class PathScan {

    public static List<Class<?>> find(String scannedPackage) {
        String scannedPath = scannedPackage.replace(".", "/");
        //Получает URL (ссылку) на папку в classpath, где лежат скомпилированные .class файлы.
        URL scannedUrl = Thread.currentThread().getContextClassLoader().getResource(scannedPath);
        if (scannedUrl == null) {
            throw new IllegalArgumentException("Bad package " + scannedPackage);
        }
        //Превращает URL в объект File, представляющий эту папку на диске.
        File scannedDir = new File(scannedUrl.getFile());
        List<Class<?>> classes = new ArrayList<>();
        //Проходит по всем файлам и папкам внутри целевой папки и вызывает метод find для каждого.
        for (File file : scannedDir.listFiles()) {
            classes.addAll(find(file, scannedPackage));
        }
        return classes;
    }

    private static List<Class<?>> find(File file, String scannedPackage) {
        List<Class<?>> classes = new ArrayList<>();
        // Формирует полное имя класса (ru.itis...di.Application.class).
        String resource = scannedPackage + "." + file.getName();
        //Если это папка, заходим в нее рекурсивно
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                classes.addAll(find(child, resource));
            }
        } else if (resource.endsWith(".class")) {
            //Отрезаем .class
            String className = resource.substring(0, resource.length() - 6);
            try {
                Class clazz = Class.forName(className);
                //Проверяет, есть ли на загруженном классе метка
                if (clazz.isAnnotationPresent(Component.class)) {
                    classes.add(clazz);
                }
            } catch (ClassNotFoundException ignore) {
            }
        }
        return classes;
    }
}