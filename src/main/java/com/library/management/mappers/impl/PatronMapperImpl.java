package com.library.management.mappers.impl;

import com.library.management.model.dto.PatronDto;
import com.library.management.model.entities.PatronEntity;
import com.library.management.mappers.Mapper;
import org.springframework.stereotype.Component;
import org.modelmapper.ModelMapper;

@Component
public class PatronMapperImpl implements Mapper<PatronEntity, PatronDto> {

    private ModelMapper modelMapper;

    public PatronMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public PatronDto mapEntityToDto(PatronEntity patronEntity) {
        return modelMapper.map(patronEntity, PatronDto.class);
    }

    @Override
    public PatronEntity mapEntityFromDto(PatronDto patronDto) {
        return modelMapper.map(patronDto, PatronEntity.class);
    }
}

