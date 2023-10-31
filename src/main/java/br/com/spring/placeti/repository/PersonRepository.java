package br.com.spring.placeti.repository;

import br.com.spring.placeti.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {
    List<Person> findByProfession(String profession);
    List<Person> findByProfessionContainingIgnoreCase(String profession);

    @Query("SELECT p from Person p where p.profession like %:profession% AND p.age = :age order by p.age")
    List<Person> findByProfessionAndAge(@Param("profession") String profession, @Param("age") int age);

    @Query(value = "SELECT AVG(p.age) FROM Person p WHERE p.profession LIKE %:profession%",
            nativeQuery = true)
    Double findAverageAgeProfession(@Param("profession") String profession);


}
