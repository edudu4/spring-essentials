package br.com.spring.placeti.repository;

import java.util.List;

public interface PersonCustomRepository {
    List<Object> findPersonsInRangeWithSameProfession(int firstAge, int lastAge);
}
