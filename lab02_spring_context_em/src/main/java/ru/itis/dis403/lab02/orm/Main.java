package ru.itis.dis403.lab02.orm;

import ru.itis.dis403.lab02.orm.entityConfig.EntityManagerFactory;
import ru.itis.dis403.lab02.orm.entityConfig.EntityManagerImpl;
import ru.itis.dis403.lab02.orm.entityConfig.EntityScanner;
import ru.itis.dis403.lab02.orm.model.City;
import ru.itis.dis403.lab02.orm.model.Country;
import ru.itis.dis403.lab02.orm.sqlConfig.SQLGenerator;
import ru.itis.dis403.lab02.orm.sqlConfig.SchemaValidator;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        String packageName = "ru.itis.dis403.lab02.orm";
        EntityScanner scanner = new EntityScanner();
        SQLGenerator sqlGenerator = new SQLGenerator();

        try {
            EntityManagerFactory emf = new EntityManagerFactory();
            EntityManagerImpl entityManager = emf.getEntityManager();
            Connection connection = entityManager.getConnection();
            SchemaValidator schemaValidator = new SchemaValidator(connection);

            Set<Class<?>> setEntities = scanner.scanEntities(packageName);
            List<String> listSQLs = sqlGenerator.generateAllCreateTableSql(setEntities);

            Statement statement = connection.createStatement();

            for (String sql : listSQLs) {
                try {
                    statement.executeUpdate(sql);
                    System.out.println("Выполнили create table запрос в классе Main:\n" + sql);
                } catch (SQLException e) {
                    throw new RuntimeException("Проблема с выполнением create table запроса в классе Main:\n" + sql);
                }

            }

            Country russia = new Country();
            russia.setName("Russia");
            entityManager.save(russia);

            City moscow = new City();
            moscow.setName("Moscow");
            moscow.setCountry(russia);
            entityManager.save(moscow);

            System.out.println(entityManager.findAll(City.class));

            City found = entityManager.find(City.class, moscow.getId());
            System.out.println(found.getName() + " - " + found.getCountry().getName());

            entityManager.remove(moscow);
            System.out.println(entityManager.findAll(City.class));

            System.out.println("Проверка соответствия таблиц: " + schemaValidator.validateEntities(setEntities));

            System.out.println("Проверка соответствия полей таблиц: " + schemaValidator.validateColumns(setEntities));
            System.out.println(schemaValidator.getErrors());


            System.out.println(setEntities);
            entityManager.close();
            emf.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}
