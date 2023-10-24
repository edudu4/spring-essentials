package br.com.spring.placeti.repository;

import br.com.spring.placeti.domain.PersonUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonUserRepository extends JpaRepository<PersonUser, Long> {
    PersonUser findByUsername(String username);

}
