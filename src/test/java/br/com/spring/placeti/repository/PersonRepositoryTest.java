package br.com.spring.placeti.repository;

import br.com.spring.placeti.domain.Person;
import br.com.spring.placeti.util.PersonCreator;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;
import java.util.Optional;


@DataJpaTest
@DisplayName("Tests for Person Repository")
@ComponentScan
class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonCustomRepositoryImpl personCustomRepository;

    @Test
    @DisplayName("Save Person when Sucessful")
    void save_PersistPerson_WhenSucessful() {
        Person personToBeSaved = PersonCreator.createPersonToBeSaved();

        Person personSaved = this.personRepository.save(personToBeSaved);

        Assertions.assertThat(personSaved).isNotNull();

        Assertions.assertThat(personSaved.getId()).isNotNull();

        Assertions.assertThat(personSaved.getName()).isEqualTo(personToBeSaved.getName());
    }

    @Test
    @DisplayName("Update Person when Sucessful")
    void update_UpdatesPerson_WhenSucessful() {
        Person personToBeSaved = PersonCreator.createPersonToBeSaved();

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
        Person personToBeSaved = PersonCreator.createPersonToBeSaved();

        Person personSaved = this.personRepository.save(personToBeSaved);

        this.personRepository.delete(personSaved);

        Optional<Person> personOptional = this.personRepository.findById(personSaved.getId());

        Assertions.assertThat(personOptional).isEmpty();
    }

    @Test
    @DisplayName("Find By Profession returns list of Person with the Profession when Sucessful")
    void findByProfession_ReturnsListOfPerson_WhenSucessful() {
        Person personToBeSaved = PersonCreator.createPersonToBeSaved();

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
    @DisplayName("findPersonsInRangeWithSameProfession returns list of object that represents persons in a specific range with the same profession when successful")
    void findPersonsInRangeWithSameProfession_ReturnsListOfObject_WhenSuccessful() {

        this.personRepository.save(PersonCreator.createPersonToBeSaved());
        this.personRepository.save(PersonCreator.createPersonToBeSaved());


        List<Object> objectList = this.personCustomRepository.findPersonsInRangeWithSameProfession(0,22);

        Assertions.assertThat(objectList).isNotEmpty();
    }

}