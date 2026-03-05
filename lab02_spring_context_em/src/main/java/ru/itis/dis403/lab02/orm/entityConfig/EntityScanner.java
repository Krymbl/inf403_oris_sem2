package ru.itis.dis403.lab02.orm.entityConfig;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class EntityScanner {

    private Set<Class<?>> entityClasses;

    public Set<Class<?>> scanEntities(String packageName) {
        entityClasses = new HashSet<>();

        String packagePath = packageName.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(packagePath);

        if (resource == null) {
            throw new RuntimeException("Пакет не найден: " + packagePath);
        }

        File directory = new File(resource.getFile());
        if (directory.exists() && directory.isDirectory()) {
            scanDirectory(directory, packageName);
        } else {
            throw new RuntimeException("Это не директория" + directory.getAbsolutePath());
        }

        System.out.println("Найдено сущностей: " + entityClasses.size());
        for (Class<?> entity : entityClasses) {
            System.out.println(entity.getSimpleName());
        }
        return entityClasses;
    }

    private void scanDirectory(File dir, String packageName) {
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                scanDirectory(file, packageName + "." + file.getName());
            } else if (file.getName().endsWith(".class")) {
                String className = file.getName().substring(0, file.getName().length() - 6);
                String fullClassName = packageName + '.' + className;

                try {
                    Class<?> clazz = Class.forName(fullClassName);
                    if (clazz.isAnnotationPresent(Entity.class)) {
                        entityClasses.add(clazz);
                        System.out.println("Найден класс: " + className);
                    }
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException("Не удалось загрузить класс: " + className);
                }
            }
        }
    }

    public Set<String> getEntityColumns(Class<?> entity) {
        Set<String> columns = new HashSet<>();
        Field[] fields = entity.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(ManyToOne.class)) {
                columns.add(field.getName() + "_id");
            } else {
                if (!field.getName().isEmpty()) {
                    columns.add(field.getName());
                }
            }
        }
        return columns;
    }
}
