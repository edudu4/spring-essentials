package br.com.spring.placeti.util;

import br.com.spring.placeti.domain.Person;

public class PersonCreator {
    public static Person createPersonToBeSaved() {
        return Person.builder()
                .name("Eduardo Alves")
                .age(22)
                .profession("Desenvolvedor")
                .build();
    }
    public static Person createValidPerson() {
        return Person.builder()
                .name("Eduardo Alves")
                .id(1L)
                .age(22)
                .profession("Desenvolvedor")
                .build();
    }
    public static Person createValidUpdatedPerson() {
        return Person.builder()
                .name("Eduardo Alves")
                .id(1L)
                .age(22)
                .profession("Desenvolvedor Java")
                .build();
    }
}
