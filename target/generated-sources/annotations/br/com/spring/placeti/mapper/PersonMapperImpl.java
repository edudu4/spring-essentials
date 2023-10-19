package br.com.spring.placeti.mapper;

import br.com.spring.placeti.domain.Person;
import br.com.spring.placeti.dto.PersonPostDTO;
import br.com.spring.placeti.dto.PersonPutDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-10-19T17:52:57-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.8 (Oracle Corporation)"
)
@Component
public class PersonMapperImpl extends PersonMapper {

    @Override
    public Person toPerson(PersonPostDTO personPostDTO) {
        if ( personPostDTO == null ) {
            return null;
        }

        Person.PersonBuilder person = Person.builder();

        person.name( personPostDTO.getName() );
        person.profession( personPostDTO.getProfession() );
        person.age( personPostDTO.getAge() );

        return person.build();
    }

    @Override
    public Person toPerson(PersonPutDTO personPutDTO) {
        if ( personPutDTO == null ) {
            return null;
        }

        Person.PersonBuilder person = Person.builder();

        person.id( personPutDTO.getId() );
        person.name( personPutDTO.getName() );
        person.profession( personPutDTO.getProfession() );
        person.age( personPutDTO.getAge() );

        return person.build();
    }
}
