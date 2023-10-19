package br.com.spring.placeti.repository;

import br.com.spring.placeti.domain.Person;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;


@DataJpaTest
@DisplayName("Tests for Person Repository")
@Log4j2
class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Test
    @DisplayName("Save Person when Sucessful")
    void save_PersistPerson_WhenSucessful() {
        Person personToBeSaved = createPerson();

        Person personSaved = this.personRepository.save(personToBeSaved);

        Assertions.assertThat(personSaved).isNotNull();

        Assertions.assertThat(personSaved.getId()).isNotNull();

        Assertions.assertThat(personSaved.getName()).isEqualTo(personToBeSaved.getName());
    }

    @Test
    @DisplayName("Update Person when Sucessful")
    void update_UpdatesPerson_WhenSucessful() {
        Person personToBeSaved = createPerson();

        Person personSaved = this.personRepository.save(personToBeSaved);

        personSaved.setName("Ana");

        Person personUpdated = this.personRepository.save(personSaved);

        Assertions.assertThat(personUpdated).isNotNull();

        Assertions.assertThat(personUpdated.getId()).isNotNull();

        Assertions.assertThat(personUpdated.getName()).isEqualTo(personToBeSaved.getName());
    }

    @Test
    @DisplayName("Delete Person when Sucessful")
    void delete_RemovesPerson_WhenSucessful() {
        Person personToBeSaved = createPerson();

        Person personSaved = this.personRepository.save(personToBeSaved);

        this.personRepository.delete(personSaved);

        Optional<Person> personOptional = this.personRepository.findById(personSaved.getId());

        Assertions.assertThat(personOptional).isEmpty();
    }

    @Test
    @DisplayName("Find By Profession returns list of Person with the Profession when Sucessful")
    void findByProfession_ReturnsListOfPerson_WhenSucessful() {
        Person personToBeSaved = createPerson();

        Person personSaved = this.personRepository.save(personToBeSaved);

        String profession = personSaved.getProfession();

        List<Person> professionList = this.personRepository.findByProfession(profession);

        Assertions.assertThat(professionList).isNotEmpty().contains(personSaved);
    }

    @Test
    @DisplayName("Find By Profession returns empty list Person with the Profession when Not Found")
    void findByProfession_ReturnsEmptyList_WhenProfessionNotFound() {
        List<Person> professionList = this.personRepository.findByProfession("Engenheiro");

        Assertions.assertThat(professionList).isEmpty();

    }

    @Test
    @DisplayName("Save Throw ConstraintViolationException when Person Attribute is empty ")
    void save_ThrowsConstraintViolationException_WhenPersonAttributeIsEmpty() {
        Person person = new Person();

        Assertions.assertThatThrownBy(() -> this.personRepository.save((person)))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Name must not be blank")
                .hasMessageContaining("Profession must not be blank")
                .hasMessageContaining("The person must have a valid age")
                .hasMessageContaining("Age should not be less than 18");

    }

    private Person createPerson() {
        return Person.builder()
                .name("Eduardo Alves")
                .age(22)
                .profession("Desenvolvedor")
                .build();
    }

}