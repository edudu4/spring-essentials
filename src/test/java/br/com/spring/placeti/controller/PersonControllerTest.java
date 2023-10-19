package br.com.spring.placeti.controller;

import br.com.spring.placeti.domain.Person;
import br.com.spring.placeti.dto.PersonPostDTO;
import br.com.spring.placeti.dto.PersonPutDTO;
import br.com.spring.placeti.service.PersonService;
import br.com.spring.placeti.util.PersonCreator;
import br.com.spring.placeti.util.PersonPostDTOCreator;
import br.com.spring.placeti.util.PersonPutDTOCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
class PersonControllerTest {
    @InjectMocks
    private PersonController personController;

    @Mock
    private PersonService personServiceMock;

    @BeforeEach
    void setUp() {
        PageImpl<Person> personPage = new PageImpl<>(List.of(PersonCreator.createValidPerson()));
        BDDMockito.when(personServiceMock.listAll(ArgumentMatchers.any()))
                .thenReturn(personPage);

        BDDMockito.when(personServiceMock.listAllNoPageable())
                .thenReturn(List.of(PersonCreator.createValidPerson()));

        BDDMockito.when(personServiceMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(PersonCreator.createValidPerson());

        BDDMockito.when(personServiceMock.findByProfession(ArgumentMatchers.anyString()))
                .thenReturn(List.of(PersonCreator.createValidPerson()));

        BDDMockito.when(personServiceMock.save(ArgumentMatchers.any(PersonPostDTO.class)))
                .thenReturn(PersonCreator.createValidPerson());

        BDDMockito.doNothing().when(personServiceMock).replace(ArgumentMatchers.any(PersonPutDTO.class));

        BDDMockito.doNothing().when(personServiceMock).delete(ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("list returns list of person inside page object when successful")
    void list_ReturnsListOfPersonInsidePageObject_WhenSuccessful() {

        String expectedValue = PersonCreator.createValidPerson().getName();

        Page<Person> personPage = personController.list(null).getBody();

        Assertions.assertThat(personPage).isNotNull();

        Assertions.assertThat(personPage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(personPage.toList().get(0).getName()).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("listAll returns list of person when successful")
    void listAll_ReturnsListOfPerson_WhenSuccessful() {

        String expectedValue = PersonCreator.createValidPerson().getName();

        List<Person> persons = personController.listAll().getBody();

        Assertions.assertThat(persons)
                .isNotNull()
                .hasSize(1);

        Assertions.assertThat(persons.get(0).getName()).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("findById returns person when successful")
    void findById_ReturnsPerson_WhenSuccessful() {

        Person expectedValue = PersonCreator.createValidPerson();

        Person person = personController.findById(1).getBody();

        Assertions.assertThat(person)
                .isNotNull();

        Assertions.assertThat(person.getId())
                .isNotNull()
                .isEqualTo(expectedValue.getId());
    }

    @Test
    @DisplayName("findByProfession returns list of person with that profession when successful")
    void findByProfession_ReturnsListOfPerson_WhenSuccessful() {

        String expectedValue = PersonCreator.createValidPerson().getProfession();

        List<Person> persons = personController.findByProfession("Dev").getBody();

        Assertions.assertThat(persons)
                .isNotNull()
                .hasSize(1);

        Assertions.assertThat(persons.get(0).getProfession()).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("findByProfession returns an empty list of person when profession is not found")
    void findByProfession_ReturnsEmptyListOfPerson_WhenPersonIsNotFound() {

        BDDMockito.when(personServiceMock.findByProfession(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Person> persons = personController.findByProfession("Engenheiro").getBody();

        Assertions.assertThat(persons)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("save returns person when successful")
    void save_ReturnsPerson_WhenSuccessful() {

        Person person = personController.save(PersonPostDTOCreator.createPersonPostDTO()).getBody();

        Assertions.assertThat(person)
                .isNotNull()
                .isEqualTo(PersonCreator.createValidPerson());
    }

    @Test
    @DisplayName("replace updates person when successful")
    void update_UpdatesPerson_WhenSuccessful() {

        Assertions.assertThatCode(() -> personController.replace(PersonPutDTOCreator.createPersonPutDTO()))
                .doesNotThrowAnyException();

        ResponseEntity<Void> person = personController.replace(PersonPutDTOCreator.createPersonPutDTO());

        Assertions.assertThat(person).isNotNull();

        Assertions.assertThat(person.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete remove person when successful")
    void delete_DeletesPerson_WhenSuccessful() {

        Assertions.assertThatCode(() -> personController.delete(1))
                .doesNotThrowAnyException();

        ResponseEntity<Void> person = personController.delete(1);

        Assertions.assertThat(person).isNotNull();

        Assertions.assertThat(person.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}