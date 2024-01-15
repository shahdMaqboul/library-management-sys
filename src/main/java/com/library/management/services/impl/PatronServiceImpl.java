package com.library.management.services.impl;

import com.library.management.mappers.Mapper;
import com.library.management.model.dto.PatronDto;
import com.library.management.model.entities.PatronEntity;
import com.library.management.repositories.PatronRepository;
import com.library.management.services.PatronService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PatronServiceImpl implements PatronService {

    private PatronRepository patronRepository;
    private Mapper<PatronEntity, PatronDto> patronMapper;

    public PatronServiceImpl(PatronRepository patronRepository, Mapper<PatronEntity, PatronDto> patronMapper) {
        this.patronRepository = patronRepository;
        this.patronMapper = patronMapper;
    }

    @Override
    public PatronDto save(PatronDto patronDto) {
        PatronEntity patronEntity = patronMapper.mapEntityFromDto(patronDto);
        return patronMapper.mapEntityToDto(patronRepository.save(patronEntity));
    }

    @Override
    public List<PatronDto> findAll() {
        List<PatronEntity> patrons =  StreamSupport.stream(patronRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        return patrons.stream()
                .map(patronMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PatronDto> findOne(Long id) {
        Optional<PatronEntity> foundBook = patronRepository.findById(id);

        return foundBook.map(bookEntity -> patronMapper.mapEntityToDto(bookEntity));
    }

    @Override
    public boolean isExists(Long id) {
        return patronRepository.existsById(id);
    }

    @Override
    public PatronDto updatePatron(Long id, PatronDto patronDto) {
        if (!patronRepository.existsById(id)) {
            throw new RuntimeException("Patron does not exist");
        }

        PatronEntity patronEntity = patronMapper.mapEntityFromDto(patronDto);
        patronEntity.setId(id);
        return patronMapper.mapEntityToDto(patronRepository.save(patronEntity));
    }

    @Override
    public void delete(Long id) {
        patronRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        patronRepository.deleteAll();
    }
}
