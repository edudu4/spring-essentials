package br.com.spring.placeti.client;

import br.com.spring.placeti.domain.Person;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Log4j2
public class SpringClient {
    public static void main(String[] args) {
        ResponseEntity<Person> personResponseEntity = new RestTemplate().getForEntity("http://localhost:8080/persons/2", Person.class);
        log.info(personResponseEntity);

        Person object = new RestTemplate().getForObject("http://localhost:8080/persons/{id}", Person.class, 2);
        log.info(object);

        Person[] objectList = new RestTemplate().getForObject("http://localhost:8080/persons/all", Person[].class);
        log.info(Arrays.toString(objectList));

        ResponseEntity<List<Person>> exchange = new RestTemplate().exchange("http://localhost:8080/persons/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});
        log.info(exchange.getBody());

        Person person = Person.builder()
                .name("Duda")
                .age(26)
                .profession("Gerente de Projetos")
                .build();

//        Person objectPost = new RestTemplate().postForObject("http://localhost:8080/persons", person, Person.class);
//        log.info("saved person {}",objectPost);
//
//        Person person2 = Person.builder()
//                .name("Maria")
//                .age(28)
//                .profession("Gerente de TI")
//                .build();
//
//        ResponseEntity<Person> exchangePost = new RestTemplate().exchange("http://localhost:8080/persons",
//                HttpMethod.POST,
//                new HttpEntity<>(person2,createJsonHeader()),
//                Person.class
//        );
//        log.info("saved person {}",exchangePost);
        person.setId(19L);
        person.setName("Eduarda");
        ResponseEntity<Void> exchangePut = new RestTemplate().exchange("http://localhost:8080/persons",
                HttpMethod.PUT,
                new HttpEntity<>(person),
                Void.class
        );

        log.info(exchangePut);

        ResponseEntity<Void> exchangeDelete = new RestTemplate().exchange("http://localhost:8080/persons/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                14);

        log.info(exchangeDelete);

    }

    private static HttpHeaders createJsonHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }

}
