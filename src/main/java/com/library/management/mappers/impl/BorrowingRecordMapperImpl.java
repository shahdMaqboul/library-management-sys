package com.library.management.mappers.impl;
import com.library.management.model.dto.BorrowingRecordDto;
import com.library.management.model.entities.BorrowingRecordEntity;
import com.library.management.mappers.Mapper;
import org.springframework.stereotype.Component;

import org.modelmapper.ModelMapper;

@Component
public class BorrowingRecordMapperImpl implements Mapper<BorrowingRecordEntity, BorrowingRecordDto> {

    private ModelMapper modelMapper;

    public BorrowingRecordMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public BorrowingRecordDto mapEntityToDto(BorrowingRecordEntity borrowingRecordEntity) {
        return modelMapper.map(borrowingRecordEntity, BorrowingRecordDto.class);
    }

    @Override
    public BorrowingRecordEntity mapEntityFromDto(BorrowingRecordDto borrowingRecordDto) {
        return modelMapper.map(borrowingRecordDto, BorrowingRecordEntity.class);
    }
}

