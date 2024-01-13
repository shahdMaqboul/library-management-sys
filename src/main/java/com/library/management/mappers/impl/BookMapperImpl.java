package com.library.management.mappers.impl;

import com.library.management.model.dto.BookDto;
import com.library.management.model.entities.BookEntity;
import org.springframework.stereotype.Component;

import com.library.management.mappers.Mapper;
import org.modelmapper.ModelMapper;
@Component
public class BookMapperImpl implements Mapper<BookEntity, BookDto> {

    private ModelMapper modelMapper;

    public BookMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public BookDto mapEntityToDto(BookEntity bookEntity) {
        return modelMapper.map(bookEntity, BookDto.class);
    }

    @Override
    public BookEntity mapEntityFromDto(BookDto bookDto) {
        return modelMapper.map(bookDto, BookEntity.class);
    }
}
