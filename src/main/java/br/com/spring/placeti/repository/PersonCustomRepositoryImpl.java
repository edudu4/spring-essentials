package br.com.spring.placeti.repository;

import br.com.spring.placeti.domain.Person;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class PersonCustomRepositoryImpl implements PersonCustomRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Object> findPersonsInRangeWithSameProfession(int firstAge, int lastAge) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
        Root<Person> personRoot = query.from(Person.class);

        Subquery<Long> subquery = query.subquery(Long.class);
        Root<Person> subQueryPerson = subquery.from(Person.class);

        subquery.select(cb.count(subQueryPerson))
                .where(cb.equal(subQueryPerson.get("profession"), personRoot.get("profession")));


        query.select(cb.array(personRoot.get("name"), personRoot.get("profession"), personRoot.get("age")))
                .where(cb.and(cb.between(personRoot.get("age"), firstAge, lastAge)), cb.greaterThan(subquery, 1L));

        List<Object[]> result = entityManager.createQuery(query).getResultList();

        return result.stream()
                .map(person -> {
                    Map<String, Object> personMap = new HashMap<>();
                    personMap.put("name", person[0]);
                    personMap.put("profession", person[1]);
                    personMap.put("age", person[2]);
                    return personMap;
                }).collect(Collectors.toList());
    }
}
