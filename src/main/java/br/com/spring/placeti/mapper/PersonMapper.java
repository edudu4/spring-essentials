package br.com.spring.placeti.mapper;

import br.com.spring.placeti.domain.Person;
import br.com.spring.placeti.dto.PersonPostDTO;
import br.com.spring.placeti.dto.PersonPutDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class PersonMapper {
    public static final PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    public abstract Person toPerson(PersonPostDTO personPostDTO);

    public abstract Person toPerson(PersonPutDTO personPutDTO);
}
