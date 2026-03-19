package ru.itis.dis403.lab04.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.dis403.lab04.model.Person;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {

    List<Person> findAllByOrderByIdAsc();
}
