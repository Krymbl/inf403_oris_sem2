package ru.itis.dis403.lab02.orm.entityConfig;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.SQLException;

public class EntityManagerFactory {

    private HikariDataSource dataSource;

    public EntityManagerFactory() throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/oris_lab02.2");
        config.setUsername("postgres");
        config.setPassword("qwerty007");
        config.setConnectionTimeout(50000);
        config.setMaximumPoolSize(10);
        dataSource = new HikariDataSource(config);
    }

    public EntityManagerImpl getEntityManager() {
        try {
            return new EntityManagerImpl(dataSource.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        dataSource.close();
    }
}
