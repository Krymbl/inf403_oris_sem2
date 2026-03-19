package ru.itis.dis403.lab03;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import ru.itis.dis403.lab03.model.Admin;
import ru.itis.dis403.lab03.model.Client;
import ru.itis.dis403.lab03.model.Phone;

public class MainCreate {
    public static void main(String[] args) {
        //"lab03" — это имя persistence unit. Persistence unit — это именованный набор настроек для подключения к БД.
        // Hibernate ищет файл persistence.xml и внутри него блок с name="lab03".
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("lab03");
        Admin admin = new Admin();
        admin.setId(10L);
        admin.setName("admin1");
        admin.setEmail("asd@awd");

        Client client = new Client();
        client.setId(11L);
        client.setName("client2");
        client.setAddress("addr");

        Phone phone1 = new Phone();
        //phone1.setId(1L);
        phone1.setNumber("1111111");

        Phone phone2 = new Phone();
        //phone2.setId(2L);
        phone2.setNumber("222222");

        client.getPhones().add(phone1);
        client.getPhones().add(phone2);

        admin.getPhones().add(phone2);

        EntityManager entityManager = emf.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        //persist — это метод EntityManager из стандарта JPA.
        // Говорит JPA: "возьми этот объект под управление и сохрани в БД".
        entityManager.persist(phone1);
        // Теперь JPA следит за объектом
        // Если изменишь phone.setNumber("9999999") — JPA это заметит
        // При commit() автоматически выполнит UPDATE

        // JPA сам сгенерирует UPDATE phone SET phone_number='9999999' WHERE id=1
        // Ты не вызывал никакого save/update!

        //Порядок важен без CascadeType.PERSIST. Если бы сначала сохранили client — JPA попытался бы
        //записать person_phones с phones_id который ещё не существует в таблице phone → ошибка внешнего ключа.
        //С CascadeType.PERSIST порядок не важен — JPA сам разберётся и сначала сохранит зависимые объекты.
        entityManager.persist(phone2);

        entityManager.persist(admin);
        entityManager.persist(client);

        transaction.commit();
        entityManager.close();

        emf.close();
    }
}
