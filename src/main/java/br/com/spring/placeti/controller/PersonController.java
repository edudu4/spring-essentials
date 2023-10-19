package br.com.spring.placeti.controller;

import br.com.spring.placeti.domain.Person;
import br.com.spring.placeti.dto.PersonPostDTO;
import br.com.spring.placeti.dto.PersonPutDTO;
import br.com.spring.placeti.service.PersonService;
import br.com.spring.placeti.util.DateUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("persons")
@RequiredArgsConstructor
@Log4j2
public class PersonController {
    private final DateUtil dateUtil;
    private final PersonService personService;


    @GetMapping(path = "/all")
    public ResponseEntity<List<Person>> listAll() {
        log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return new ResponseEntity<>(personService.listAllNoPageable(), HttpStatus.OK);
    }
    @GetMapping()
    public ResponseEntity<Page<Person>> list(Pageable pageable) {
        log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return new ResponseEntity<>(personService.listAll(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Person> findById(@PathVariable long id) {
        return new ResponseEntity<>(personService.findById(id), HttpStatus.OK);
    }

    @GetMapping(path = "/find")
    public ResponseEntity<List<Person>> findByProfession(@RequestParam String profession) {
        return new ResponseEntity<>(personService.findByProfession(profession), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Person> save(@RequestBody @Valid PersonPostDTO personPostDTO) {
        return new ResponseEntity<>(personService.save(personPostDTO), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Void> replace(@RequestBody @Valid PersonPutDTO personPutDTO) {
        personService.replace(personPutDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        personService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
