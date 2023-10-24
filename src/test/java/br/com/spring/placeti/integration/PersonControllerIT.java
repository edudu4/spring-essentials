package br.com.spring.placeti.integration;

import br.com.spring.placeti.domain.Person;
import br.com.spring.placeti.domain.PersonUser;
import br.com.spring.placeti.dto.PersonPostDTO;
import br.com.spring.placeti.repository.PersonRepository;
import br.com.spring.placeti.repository.PersonUserRepository;
import br.com.spring.placeti.util.PersonCreator;
import br.com.spring.placeti.util.PersonPostDTOCreator;
import br.com.spring.placeti.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
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
    @Qualifier(value = "testRestTemplateRoleUser")
    private TestRestTemplate testRestTemplateRoleUser;

    @Autowired
    @Qualifier(value = "testRestTemplateRoleAdmin")
    private TestRestTemplate testRestTemplateRoleAdmin;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonUserRepository personUserRepository;

    private static final PersonUser ADMIN = PersonUser.builder()
            .name("Eduardo Alves")
            .password("{bcrypt}$2a$10$hOtGS/.wlg1h0K.lGe5ZDOatHlFStZ6ALJC/OAeCQ8XQyI/pVK5z.")
            .username("eduardo")
            .authorities("ROLE_ADMIN")
            .build();

    private static final PersonUser USER = PersonUser.builder()
            .name("Eduardo Alves")
            .password("{bcrypt}$2a$10$hOtGS/.wlg1h0K.lGe5ZDOatHlFStZ6ALJC/OAeCQ8XQyI/pVK5z.")
            .username("eduardoTest")
            .authorities("ROLE_USER")
            .build();

    @TestConfiguration
    @Lazy
    static class Config {
        @Bean(name = "testRestTemplateRoleUser")
        public TestRestTemplate testRestTemplateRoleUserCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:" + port)
                    .basicAuthentication("eduardoTest", "1234");
            return new TestRestTemplate(restTemplateBuilder);
        }

        @Bean(name = "testRestTemplateRoleAdmin")
        public TestRestTemplate testRestTemplateRoleAdminCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:" + port)
                    .basicAuthentication("eduardo", "1234");
            return new TestRestTemplate(restTemplateBuilder);
        }
    }


    @Test
    @DisplayName("list returns list of person inside page object when successful")
    void list_ReturnsListOfAnimesInsidePageObject_WhenSuccessful() {

        Person personSaved = personRepository.save(PersonCreator.createPersonToBeSaved());

        personUserRepository.save(USER);

        String expectedName = personSaved.getName();

        PageableResponse<Person> personPage = testRestTemplateRoleUser.exchange("/persons",
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

        personUserRepository.save(USER);

        String expectedValue = personSaved.getName();

        List<Person> persons = testRestTemplateRoleUser.exchange("/persons/all",
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

        personUserRepository.save(USER);

        Long expectedId = personSaved.getId();

        Person person = testRestTemplateRoleUser.getForObject("/persons/{id}", Person.class, expectedId);

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

        personUserRepository.save(USER);

        String expectedValue = personSaved.getProfession();

        String url = String.format("/persons/find?profession=%s", expectedValue);

        List<Person> persons = testRestTemplateRoleUser.exchange(url,
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

        personUserRepository.save(USER);

        List<Person> persons = testRestTemplateRoleUser.exchange("/persons/find?profession=engenheiro",
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

        personUserRepository.save(USER);

        PersonPostDTO personPostDTO = PersonPostDTOCreator.createPersonPostDTO();

        ResponseEntity<Person> personResponseEntity = testRestTemplateRoleUser.postForEntity("/persons",
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

        personUserRepository.save(USER);

        personSaved.setName("Bia");

        ResponseEntity<Void> personResponseEntity = testRestTemplateRoleUser.exchange("/persons",
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

        personUserRepository.save(ADMIN);

        ResponseEntity<Void> personEntity = testRestTemplateRoleAdmin.exchange("/persons/admin/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                personSaved.getId());

        Assertions.assertThat(personEntity).isNotNull();

        Assertions.assertThat(personEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete returns 403 when user is not admin")
    void delete_Retuns403_WhenUserIsNotAdmin() {

        Person personSaved = personRepository.save(PersonCreator.createPersonToBeSaved());

        personUserRepository.save(USER);


        ResponseEntity<Void> personEntity = testRestTemplateRoleUser.exchange("/persons/admin/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                personSaved.getId());

        Assertions.assertThat(personEntity).isNotNull();

        Assertions.assertThat(personEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

}
