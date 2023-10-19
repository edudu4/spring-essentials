package br.com.spring.placeti.util;

import br.com.spring.placeti.domain.Person;
import br.com.spring.placeti.dto.PersonPostDTO;

public class PersonPostDTOCreator {
    public static PersonPostDTO createPersonPostDTO() {
        Person personToBeSaved = PersonCreator.createPersonToBeSaved();
        return PersonPostDTO.builder()
                .name(personToBeSaved.getName())
                .profession(personToBeSaved.getProfession())
                .age(personToBeSaved.getAge())
                .build();
    }
}
