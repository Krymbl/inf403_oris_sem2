package ru.itis.dis403.lab02.orm.entityConfig;

import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EntityManagerImpl implements EntityManager {
    private Connection connection;

    public EntityManagerImpl(Connection connection) {
        this.connection = connection;
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T save(T entity) {
        // Определяем имя таблицы по имени класса.toLower()
        // Ищем в классе поля (Id, Column, ManyToOne)
        // Строим SQL запрос: Insert (id = null), Update (id !=null)
        // выполняем через JDBC запрос

        String table = entity.getClass().getSimpleName().toLowerCase();
        Field[] fields = entity.getClass().getDeclaredFields();

        List<ColumnInfo> columns = new ArrayList<>();
        ColumnInfo idColumn = null;

        for (Field field : fields) {
            field.setAccessible(true);
            ColumnInfo columnInfo = new ColumnInfo();

            try {
                columnInfo.name = field.getName();
                columnInfo.value = field.get(entity);

                if (field.isAnnotationPresent(Id.class)) {
                    idColumn = columnInfo;
                } else if (field.isAnnotationPresent(ManyToOne.class)) {
                    columnInfo.name += "_id";

                    Object fkObject = field.get(entity);

                    if (fkObject != null) {
                        for (Field f : fkObject.getClass().getDeclaredFields()) {
                            if (f.isAnnotationPresent(Id.class)) {
                                f.setAccessible(true);
                                columnInfo.value = f.get(fkObject);
                                break;
                            }
                        }
                    }
                    columns.add(columnInfo);

                } else {
                    columns.add(columnInfo);
                }

            } catch (IllegalAccessException e) {
                throw new RuntimeException("Ошибка чтения полей или записи данных в ColumnInfo. Метод save. Сущность: " + table + "\nОшибка: " + e.getMessage());
            }
        }

        String sql;

        if (idColumn == null || idColumn.value == null) {

            String columnsName = columns.stream()
                    .map(col -> col.name)
                    .collect(Collectors.joining(", "));

            String placeholder = columns.stream()
                    .map(col -> "?")
                    .collect(Collectors.joining(", "));

            sql = "insert into " + table + " (" + columnsName + ") values (" + placeholder + ")";

            try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < columns.size(); i++) {
                    statement.setObject(i + 1, columns.get(i).value);
                }
                statement.executeUpdate();

                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next() && idColumn != null) {
                    idColumn.value = generatedKeys.getLong(1);
                    for (Field field : fields) {
                        if (field.isAnnotationPresent(Id.class)) {
                            field.setAccessible(true);
                            field.set(entity, idColumn.value);
                            break;
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Ошибка добавления в таблицу. Сущность: " + table + "\nОшибка: " + e.getMessage());
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Не смогли установить значение полю id сущности при update.\nОшибка:" + e.getMessage());
            }
        } else {

            String placeHolder = columns.stream()
                    .map(col -> col.name + " = ?")
                    .collect(Collectors.joining(", "));

            sql = "update " + table + " set " + placeHolder + " where " + idColumn.name + " = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                for (int i = 0; i < columns.size(); i++) {
                    statement.setObject(i + 1, columns.get(i).value);
                }
                statement.setObject(columns.size() + 1, idColumn.value);
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Ошибка обновления данных в таблице. Сущность: " + table + "\nОшибка: " + e.getMessage());
            }
        }

        return entity;
    }

    @Override
    public void remove(Object entity) {
        String table = entity.getClass().getSimpleName().toLowerCase();
        Field[] fields = entity.getClass().getDeclaredFields();
        ColumnInfo idColumn = new ColumnInfo();

        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Id.class)) {
                try {
                    idColumn.name = field.getName();
                    idColumn.value = field.get(entity);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Ошибка при получении поля с аннотацией id при remove, поле нашли");
                }
            }
        }

        if (idColumn.name == null || idColumn.value == null) {
            throw new RuntimeException("У сущности отсутствует id или его значение null, не смогли удалить");
        }

        String sql = "delete from " + table + " where " + idColumn.name + " = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, idColumn.value);
            statement.executeUpdate();
            System.out.println("УСПЕХ! Удалили объект:" + table + " с айди: " + idColumn.value);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка удаления данных из базы");
        }

    }

    @Override
    public <T> T find(Class<T> entityType, Object key) {
        // по имени класса получаем имя таблицы, фиксируем id
        // select * from tableName where id = key
        // если результат не пустой - создаем объект
        // Ищем в классе поля (Id, Column, ManyToOne)
        // Для каждого поля пытаемся получить значение из ResultSet по имени
        // задаем значения
        // возвращаем созданный объект

        String table = entityType.getSimpleName().toLowerCase();
        Field[] fields = entityType.getDeclaredFields();

        String idName = null;

        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Id.class)) {
                idName = field.getName();
                break;
            }
        }

        if (idName == null) {
            throw new RuntimeException("Класс " + entityType.getName() + " не имеет поля с аннотацией @Id");
        }

        String sql = "select * from " + table + " where " + idName + " = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, key);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                T entity = entityType.getDeclaredConstructor().newInstance();

                for (Field field : fields) {
                    field.setAccessible(true);
                    String fieldName = field.getName();


                    if (field.isAnnotationPresent(Id.class)) {
                        Object value = resultSet.getLong(fieldName);
                        field.set(entity, value);
                    } else if (field.isAnnotationPresent(ManyToOne.class)) {
                        String fkColumnName = field.getName() + "_id";
                        Object fkValue = resultSet.getLong(fkColumnName);

                        Class<?> fkClazz = field.getType();

                        Object fkObject = find(fkClazz, fkValue);
                        field.set(entity, fkObject);

                    } else {
                        Object value = resultSet.getObject(fieldName);
                        field.set(entity, value);
                    }
                }
                System.out.println("Успех! Нашли сущность: " + table);
                return entity;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при выполнения запроса select.\nОшибка: " + e.getMessage());
        } catch (InstantiationException e) {
            throw new RuntimeException("Не смогли создать экземпляр класса.\nОшибка:" + e.getMessage());
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Не смогли установить значение полю.\nОшибка:" + e.getMessage());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Не смогли получить метод.\nОшибка:" + e.getMessage());
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Не смогли выполнить метод.\nОшибка:" + e.getMessage());
        }
        System.out.println("Не нашли сущность " + table);
        return null;
    }

    @Override
    public <T> List<T> findAll(Class<T> entityType) {
        String table = entityType.getSimpleName().toLowerCase();
        List<T> result = new ArrayList<>();

        String idFieldName = null;
        for (Field field : entityType.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                idFieldName = field.getName();
                break;
            }
        }

        if (idFieldName == null) {
            throw new RuntimeException("Класс " + entityType.getName() + " не имеет поля с аннотацией @Id");
        }

        String sql = "select * from " + table;
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                T entity = find(entityType, resultSet.getObject(idFieldName));
                if (entity != null) {
                    result.add(entity);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при выполнении запроса \"select * from " + table + "\" \n Ошибка: " + e.getMessage());
        }
        System.out.println("УСПЕХ! Выполнили метод findAll");
        return result;
    }

    public Connection getConnection() {
        return connection;
    }

    private static class ColumnInfo {
        public String name;
        public Object value;
    }
}
