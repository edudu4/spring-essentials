package br.com.spring.placeti.repository;

import br.com.spring.placeti.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {
    List<Person> findByProfession(String profession);

}
