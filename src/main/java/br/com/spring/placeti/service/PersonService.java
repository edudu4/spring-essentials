package br.com.spring.placeti.service;

import br.com.spring.placeti.domain.Person;
import br.com.spring.placeti.dto.PersonPostDTO;
import br.com.spring.placeti.dto.PersonPutDTO;
import br.com.spring.placeti.exception.BadRequestException;
import br.com.spring.placeti.mapper.PersonMapper;
import br.com.spring.placeti.repository.PersonRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;

    public Page<Person> listAll(Pageable pageable) {
        return personRepository.findAll(pageable);
    }
    public List<Person> listAllNoPageable() {
        return personRepository.findAll();
    }

    public List<Person> findByProfession(String profession){
        return personRepository.findByProfession(profession);
    }

    public Person findById(long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Person not found."));
    }

    @Transactional
    public Person save(PersonPostDTO personPostDTO) {
        return personRepository.save(PersonMapper.INSTANCE.toPerson(personPostDTO));
    }

    @Transactional
    public void replace(PersonPutDTO personPutDTO) {
        Person personOld = findById(personPutDTO.getId());
        Person person = PersonMapper.INSTANCE.toPerson(personPutDTO);
        person.setId(personOld.getId());
        personRepository.save(person);
    }

    public void delete(long id) {
        personRepository.delete(findById(id));
    }


}
