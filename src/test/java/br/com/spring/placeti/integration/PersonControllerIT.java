package br.com.spring.placeti.integration;

import br.com.spring.placeti.domain.Person;
import br.com.spring.placeti.dto.PersonPostDTO;
import br.com.spring.placeti.repository.PersonRepository;
import br.com.spring.placeti.util.PersonCreator;
import br.com.spring.placeti.util.PersonPostDTOCreator;
import br.com.spring.placeti.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PersonControllerIT {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private PersonRepository personRepository;

    @Test
    @DisplayName("list returns list of person inside page object when successful")
    void list_ReturnsListOfAnimesInsidePageObject_WhenSuccessful() {

        Person personSaved = personRepository.save(PersonCreator.createPersonToBeSaved());

        String expectedName = personSaved.getName();

        PageableResponse<Person> personPage = testRestTemplate.exchange("/persons",
                HttpMethod.GET,
                null, new ParameterizedTypeReference<PageableResponse<Person>>() {
                }).getBody();

        Assertions.assertThat(personPage).isNotNull();

        Assertions.assertThat(personPage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(personPage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("listAll returns list of person when successful")
    void listAll_ReturnsListOfPerson_WhenSuccessful() {

        Person personSaved = personRepository.save(PersonCreator.createPersonToBeSaved());

        String expectedValue = personSaved.getName();

        List<Person> persons = testRestTemplate.exchange("/persons/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Person>>() {
                }).getBody();

        Assertions.assertThat(persons)
                .isNotNull()
                .hasSize(1);

        Assertions.assertThat(persons.get(0).getName()).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("findById returns person when successful")
    void findById_ReturnsPerson_WhenSuccessful() {

        Person personSaved = personRepository.save(PersonCreator.createPersonToBeSaved());

        Long expectedId = personSaved.getId();

        Person person = testRestTemplate.getForObject("/persons/{id}", Person.class, expectedId);

        Assertions.assertThat(person)
                .isNotNull();

        Assertions.assertThat(person.getId())
                .isNotNull()
                .isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByProfession returns list of person with that profession when successful")
    void findByProfession_ReturnsListOfPerson_WhenSuccessful() {

        Person personSaved = personRepository.save(PersonCreator.createPersonToBeSaved());

        String expectedValue = personSaved.getProfession();

        String url = String.format("/persons/find?profession=%s", expectedValue);

        List<Person> persons = testRestTemplate.exchange(url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Person>>() {
                }).getBody();

        Assertions.assertThat(persons)
                .isNotNull()
                .hasSize(1);

        Assertions.assertThat(persons.get(0).getProfession()).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("findByProfession returns an empty list of person when profession is not found")
    void findByProfession_ReturnsEmptyListOfPerson_WhenPersonIsNotFound() {

        List<Person> persons = testRestTemplate.exchange("/persons/find?profession=engenheiro",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Person>>() {
                }).getBody();

        Assertions.assertThat(persons)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("save returns person when successful")
    void save_ReturnsPerson_WhenSuccessful() {

        PersonPostDTO personPostDTO = PersonPostDTOCreator.createPersonPostDTO();

        ResponseEntity<Person> personResponseEntity = testRestTemplate.postForEntity("/persons",
                personPostDTO,
                Person.class);

        Assertions.assertThat(personResponseEntity).isNotNull();

        Assertions.assertThat(personResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Assertions.assertThat(personResponseEntity.getBody()).isNotNull();

        Assertions.assertThat(personResponseEntity.getBody().getId()).isNotNull();
    }

    @Test
    @DisplayName("replace updates person when successful")
    void update_UpdatesPerson_WhenSuccessful() {

        Person personSaved = personRepository.save(PersonCreator.createPersonToBeSaved());

        personSaved.setName("Bia");

        ResponseEntity<Void> personResponseEntity = testRestTemplate.exchange("/persons",
                HttpMethod.PUT,
                new HttpEntity<>(personSaved),
                Void.class);

        Assertions.assertThat(personResponseEntity).isNotNull();

        Assertions.assertThat(personResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete remove person when successful")
    void delete_DeletesPerson_WhenSuccessful() {

        Person personSaved = personRepository.save(PersonCreator.createPersonToBeSaved());

        ResponseEntity<Void> personEntity = testRestTemplate.exchange("/persons/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                personSaved.getId());

        Assertions.assertThat(personEntity).isNotNull();

        Assertions.assertThat(personEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

}
