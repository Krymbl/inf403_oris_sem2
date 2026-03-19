package ru.itis.dis403.lab03;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import ru.itis.dis403.lab03.model.Person;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        //Прокси — это объект-посредник, который выглядит как оригинал,
        // но добавляет поведение до или после вызова реального метода.
        EntityManagerFactory emf
                = Persistence.createEntityManagerFactory("lab03");


        EntityManager entityManager = emf.createEntityManager();

        //EntityTransaction transaction = entityManager.getTransaction();
        //transaction.begin();
        // Извлечем всех Person
        List<Person> persons = entityManager
                .createQuery("select p from Person p where p.id = 11")
                .getResultList();

        persons.forEach(System.out::println);

        if (persons != null && !persons.isEmpty()) {
            persons.get(0).getPhones().forEach(System.out::println);
        }

        entityManager.close();
        //transaction.commit();

        emf.close();
    }
}
