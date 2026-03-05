package ru.itis.dis403.lab02.orm.sqlConfig;

import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SQLGenerator {

    public List<String> generateAllCreateTableSql(Set<Class<?>> entitiesClasses) {
        List<String> sqls = new ArrayList<>();

        for (Class<?> entity : entitiesClasses) {
            sqls.add(generateCreateTableSql(entity));
        }

        return sqls;
    }

    private String generateCreateTableSql(Class<?> entityClass) {
        String table = entityClass.getSimpleName().toLowerCase();
        String sql = "CREATE TABLE IF NOT EXISTS " + table + " (\n ";

        List<String> columns = new ArrayList<>();

        for (Field field : entityClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                columns.add(field.getName() + " SERIAL PRIMARY KEY");
            } else if (field.isAnnotationPresent(ManyToOne.class)) {
                columns.add(field.getName() + "_id BIGINT");
            } else {
                String columnType = getSQLType(field.getType());
                columns.add(field.getName() + " " + columnType);
            }
        }
        sql += String.join(",\n ", columns) + "\n);";
        System.out.println(sql);
        return sql;
    }


    private String getSQLType(Class<?> type) {
        if (type == String.class) return "VARCHAR(255)";
        if (type == Integer.class || type == int.class) return "INTEGER";
        if (type == Long.class || type == long.class) return "BIGINT";
        if (type == Double.class || type == double.class) return "DECIMAL(10,2)";
        if (type == Boolean.class || type == boolean.class) return "BOOLEAN";
        return "VARCHAR(255)";
    }
}
