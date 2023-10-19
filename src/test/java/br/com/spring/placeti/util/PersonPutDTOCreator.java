package br.com.spring.placeti.util;

import br.com.spring.placeti.domain.Person;
import br.com.spring.placeti.dto.PersonPostDTO;
import br.com.spring.placeti.dto.PersonPutDTO;

public class PersonPutDTOCreator {
    public static PersonPutDTO createPersonPutDTO() {
        Person personUpdated = PersonCreator.createValidUpdatedPerson();
        return PersonPutDTO.builder()
                .id(personUpdated.getId())
                .name(personUpdated.getName())
                .profession(personUpdated.getProfession())
                .age(personUpdated.getAge())
                .build();
    }
}
