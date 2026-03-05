package ru.itis.dis403.lab02.orm.sqlConfig;

import ru.itis.dis403.lab02.orm.entityConfig.EntityScanner;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SchemaValidator {

    private Connection connection;
    private List<String> errors = new ArrayList<>();
    private EntityScanner entityScanner = new EntityScanner();


    public SchemaValidator(Connection connection) {
        this.connection = connection;
    }

    public boolean validateEntities(Set<Class<?>> setEntities) {
        boolean isValidate = true;

        String sql = "SELECT\n" +
                "table_name\n" +
                "FROM\n" +
                "information_schema.tables\n" +
                "WHERE\n" +
                "table_type = 'BASE TABLE'\n" +
                "AND\n" +
                "table_schema NOT IN ('pg_catalog', 'information_schema');";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            Set<String> entityNames = setEntities.stream()
                    .map(entity -> entity.getSimpleName().toLowerCase())
                    .collect(Collectors.toSet());
            Set<String> tables = new HashSet<>();

            while (resultSet.next()) {
                tables.add(resultSet.getString("table_name").toLowerCase());
            }

            for (String entity : entityNames) {
                if (!tables.contains(entity)) {
                    errors.add("Таблица " + entity + " не содержится в базе данных");
                    isValidate = false;
                }
            }

            for (String table : tables) {
                if (!entityNames.contains(table)) {
                    errors.add("В множестве сущностей нет сущности: " + table + " из базы данных");
                    isValidate = false;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return isValidate;
    }

    public boolean validateColumns(Set<Class<?>> setEntities) {
        boolean isValid = true;

        for (Class<?> entity : setEntities) {
            System.out.println("Начало валидации столбцов сущности: " + entity.getSimpleName());
            if (!validateColumnEntity(entity)) {
                isValid = false;
            }
        }

        return isValid;
    }

    private boolean validateColumnEntity(Class<?> entity) {
        boolean isValidate = true;

        String tableName = entity.getSimpleName().toLowerCase();

        String sql = "SELECT a.attname\n" +
                "FROM pg_catalog.pg_attribute a\n" +
                "WHERE a.attrelid = (\n" +
                "\tSELECT c.oid FROM pg_catalog.pg_class c \n" +
                "\tLEFT JOIN pg_catalog.pg_namespace n ON n.oid = c.relnamespace\n" +
                "\tWHERE pg_catalog.pg_table_is_visible(c.oid) AND c.relname = ?\n" +
                ")\n" +
                "AND a.attnum > 0 AND NOT a.attisdropped";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, tableName);
            ResultSet resultSet = statement.executeQuery();

            Set<String> columnNames = entityScanner.getEntityColumns(entity).stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toSet());

            Set<String> columnsDB = new HashSet<>();

            while (resultSet.next()) {
                String columnName = resultSet.getString("attname").toLowerCase();
                columnsDB.add(columnName);
            }

            for (String actualColumn : columnNames) {
                if (!columnsDB.contains(actualColumn)) {
                    errors.add("Таблица " + entity + " не содержит поле " + actualColumn + " в базе данных");
                    isValidate = false;
                }
            }

            for (String columnDB : columnsDB) {
                if (!columnNames.contains(columnDB)) {
                    errors.add("В множестве полей сущности: " + tableName + " из базы данных нет поля " + columnDB);
                    isValidate = false;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return isValidate;
    }


    public List<String> getErrors() {
        return errors;
    }
}
