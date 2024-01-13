package com.library.management.services;

import com.library.management.model.dto.PatronDto;

import java.util.List;
import java.util.Optional;

public interface PatronService {
    PatronDto save(PatronDto patronDto);

    List<PatronDto> findAll();

    Optional<PatronDto> findOne(Long id);

    boolean isExists(Long id);

    PatronDto updatePatron(Long id, PatronDto patronDto);

    void delete(Long id);

    void deleteAll();
}

