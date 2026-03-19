package ru.itis.dis403.lab03.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.itis.dis403.lab03.model.Phone;

import java.util.List;

public interface PhoneRepository extends JpaRepository<Phone, Long> {

    // Свой JPQL запрос — явно написанный
    //В JPQL запросе :num — это именованный параметр (placeholder).
    // @Param("num") связывает параметр метода String num с placeholder :num в запросе.
    @Query("select p from Phone p where p.number like :num")
    List<Phone> getPhoneLike(@Param("num") String num);


    // Метод по соглашению об именовании — Spring сам генерирует запрос
    // findBy + Number + Like → SELECT * FROM phone WHERE phone_number LIKE ?
    List<Phone> findByNumberLike(String num);
}
