package ru.itis.dis403.lab03.config;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement // включает поддержку @Transactional
@EnableJpaRepositories("ru.itis.dis403.lab03.repository") // сканирует репозитории
@ComponentScan("ru.itis.dis403.lab03")
public class Config {

    @Bean
    public HikariConfig hikariConfig() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/oris_lab03");
        config.setUsername("postgres");
        config.setPassword("qwerty007");
        config.setDriverClassName("org.postgresql.Driver");
        return config;
    }

    @Bean
    public DataSource dataSource() {
        return new HikariDataSource(hikariConfig());
    }


    // Фабрика EntityManager — главный объект JPA
    // Знает о всех @Entity классах, умеет создавать EntityManager
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

        //Hibernate — это одна из самых популярных библиотек для Java, которая решает задачу объектно-реляционного отображения
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.POSTGRESQL); // говорим что БД = PostgreSQL
        vendorAdapter.setGenerateDdl(true); // разрешаем генерацию DDL (CREATE TABLE)

        // Фабрика EntityManager — главный объект JPA
        // "Local" — соединение управляется локально (не сервером приложений)
        // "Container" — управляется Spring контейнером
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();

        factory.setJpaVendorAdapter(vendorAdapter); // какую реализацию JPA использовать
        factory.setPackagesToScan("ru.itis.dis403.lab03.model"); // где искать @Entity
        factory.setDataSource(dataSource());  // откуда брать соединения с БД
        factory.setJpaProperties(additionalProperties()); // дополнительные настройки
        return factory;
    }

    private Properties additionalProperties() {
        Properties properties = new Properties();

        // Что делать со схемой БД при старте:
        // "update"  — создать если нет, обновить если изменилось
        // "create"  — удалить всё и создать заново (данные теряются!)
        // "none"    — ничего не делать
        // "validate"— только проверить соответствие, при несоответствии — ошибка
        properties.setProperty("hibernate.hbm2ddl.auto", "update"); //update, none, create

        // Какой диалект SQL использовать
        // У каждой БД свои особенности синтаксиса
        // PostgreSQL диалект знает про SERIAL, SEQUENCE и т.д.
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        // Печатать ли SQL запросы в консоль — удобно для отладки
        properties.setProperty("hibernate.show_sql", "true");
        return properties;
    }


    // Менеджер транзакций — управляет BEGIN/COMMIT/ROLLBACK
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory);
        return txManager;
    }

}
