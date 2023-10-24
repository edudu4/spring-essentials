package br.com.spring.placeti.controller;

import br.com.spring.placeti.domain.Person;
import br.com.spring.placeti.dto.PersonPostDTO;
import br.com.spring.placeti.dto.PersonPutDTO;
import br.com.spring.placeti.service.PersonService;
import br.com.spring.placeti.util.DateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("persons")
@RequiredArgsConstructor
@Log4j2
public class PersonController {
    private final PersonService personService;


    @GetMapping(path = "/all")
    @Operation(summary = "List all persons with no pagination", tags = {"Person"})
    public ResponseEntity<List<Person>> listAll() {
        return new ResponseEntity<>(personService.listAllNoPageable(), HttpStatus.OK);
    }

    @GetMapping()
    @Operation(summary = "List all persons paginated", description = "The default size is 20, use the parameter size to change the default value",
            tags = {"Person"})
    public ResponseEntity<Page<Person>> list(@ParameterObject Pageable pageable) {
        return new ResponseEntity<>(personService.listAll(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "Return person by your id", tags = {"Person"})
    public ResponseEntity<Person> findById(@PathVariable long id) {
        return new ResponseEntity<>(personService.findById(id), HttpStatus.OK);
    }

    @GetMapping(path = "by-id/{id}")
    @Operation(summary = "Return person by your id and log of user that send the request", tags = {"Person"})
    public ResponseEntity<Person> findByIdAuthenticationPrincipal(@PathVariable long id,
                                                                  @AuthenticationPrincipal UserDetails userDetails) {
        log.info(userDetails);
        return new ResponseEntity<>(personService.findById(id), HttpStatus.OK);
    }

    @GetMapping(path = "/find")
    @Operation(summary = "List all persons with a same profession", tags = {"Person"})
    public ResponseEntity<List<Person>> findByProfession(@RequestParam String profession) {
        return new ResponseEntity<>(personService.findByProfession(profession), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Saves new person", tags = {"Person"})
    public ResponseEntity<Person> save(@RequestBody @Valid PersonPostDTO personPostDTO) {
        return new ResponseEntity<>(personService.save(personPostDTO), HttpStatus.CREATED);
    }

    @PutMapping
    @Operation(summary = "Replace person that already exists", tags = {"Person"})
    public ResponseEntity<Void> replace(@RequestBody @Valid PersonPutDTO personPutDTO) {
        personService.replace(personPutDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = "/admin/{id}")
    @Operation(summary = "Delete person by your id", tags = {"Person"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sucessfull operation"),
            @ApiResponse(responseCode = "400", description = "When Person Does Not Exist in The Database")
    })
    public ResponseEntity<Void> delete(@PathVariable long id) {
        personService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
