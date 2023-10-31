package br.com.spring.placeti.response;

import br.com.spring.placeti.domain.Person;
import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindAverageAgePersonsProfessionResponseBody {
    @Setter
    List<Person> persons;
    Double average;

    public List<Object> getPersons() {
        return persons.stream()
                .map(person -> {
                    Map<String, Object> personMap = new HashMap<>();
                    personMap.put("profession", person.getProfession());
                    personMap.put("age", person.getAge());
                    return personMap;
                }).collect(Collectors.toList());
    }

}
