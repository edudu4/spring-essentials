package br.com.spring.placeti.service;

import br.com.spring.placeti.domain.Person;
import br.com.spring.placeti.exception.BadRequestException;
import br.com.spring.placeti.repository.PersonRepository;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class PersonServiceTest {
    @InjectMocks
    private PersonService personService;

    @Mock
    private PersonRepository personRepositoryMock;

    @BeforeEach
    void setUp() {
        PageImpl<Person> personPage = new PageImpl<>(List.of(PersonCreator.createValidPerson()));
        BDDMockito.when(personRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(personPage);

        BDDMockito.when(personRepositoryMock.findAll())
                .thenReturn(List.of(PersonCreator.createValidPerson()));

        BDDMockito.when(personRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(PersonCreator.createValidPerson()));

        BDDMockito.when(personRepositoryMock.findByProfession(ArgumentMatchers.anyString()))
                .thenReturn(List.of(PersonCreator.createValidPerson()));

        BDDMockito.when(personRepositoryMock.save(ArgumentMatchers.any(Person.class)))
                .thenReturn(PersonCreator.createValidPerson());

        BDDMockito.doNothing().when(personRepositoryMock).delete(ArgumentMatchers.any(Person.class));
    }

    @Test
    @DisplayName("listAll returns list of person inside page object when successful")
    void listAll_ReturnsListOfPersonInsidePageObject_WhenSuccessful() {

        String expectedValue = PersonCreator.createValidPerson().getName();

        Page<Person> personPage = personService.listAll(PageRequest.of(1, 1));

        Assertions.assertThat(personPage).isNotNull();

        Assertions.assertThat(personPage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(personPage.toList().get(0).getName()).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("listAllNoPageable returns list of person when successful")
    void listAllNoPageable_ReturnsListOfPerson_WhenSuccessful() {

        String expectedValue = PersonCreator.createValidPerson().getName();

        List<Person> persons = personService.listAllNoPageable();

        Assertions.assertThat(persons)
                .isNotNull()
                .hasSize(1);

        Assertions.assertThat(persons.get(0).getName()).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("findById returns person when successful")
    void findById_ReturnsPerson_WhenSuccessful() {

        Person expectedValue = PersonCreator.createValidPerson();

        Person person = personService.findById(1);

        Assertions.assertThat(person)
                .isNotNull();

        Assertions.assertThat(person.getId())
                .isNotNull()
                .isEqualTo(expectedValue.getId());
    }

    @Test
    @DisplayName("findById throws BadRequestException when person is not found")
    void findById_ThrowsBadRequestException_WhenPersonIsNotFound(){
        BDDMockito.when(personRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> personService.findById(1));
    }

    @Test
    @DisplayName("findByProfession returns list of person with that profession when successful")
    void findByProfession_ReturnsListOfPerson_WhenSuccessful() {

        String expectedValue = PersonCreator.createValidPerson().getProfession();

        List<Person> persons = personService.findByProfession("Dev");

        Assertions.assertThat(persons)
                .isNotNull()
                .hasSize(1);

        Assertions.assertThat(persons.get(0).getProfession()).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("findByProfession returns an empty list of person when person is not found")
    void findByProfession_ReturnsEmptyListOfPerson_WhenProfessionIsNotFound() {

        BDDMockito.when(personService.findByProfession(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Person> persons = personService.findByProfession("Engenheiro");

        Assertions.assertThat(persons)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("save returns person when successful")
    void save_ReturnsPerson_WhenSuccessful() {

        Person person = personService.save(PersonPostDTOCreator.createPersonPostDTO());

        Assertions.assertThat(person)
                .isNotNull()
                .isEqualTo(PersonCreator.createValidPerson());
    }

    @Test
    @DisplayName("replace updates person when successful")
    void update_UpdatesPerson_WhenSuccessful() {

        Assertions.assertThatCode(() -> personService.replace(PersonPutDTOCreator.createPersonPutDTO()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("delete remove person when successful")
    void delete_DeletesPerson_WhenSuccessful() {

        Assertions.assertThatCode(() -> personService.delete(1))
                .doesNotThrowAnyException();
    }
}
