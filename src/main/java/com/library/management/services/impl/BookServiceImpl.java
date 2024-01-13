package com.library.management.services.impl;

import com.library.management.mappers.Mapper;
import com.library.management.model.dto.BookDto;
import com.library.management.model.entities.BookEntity;
import com.library.management.repositories.BookRepository;
import com.library.management.services.BookService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BookServiceImpl implements BookService {


    private BookRepository bookRepository;

    private Mapper<BookEntity, BookDto> bookMapper;

    public BookServiceImpl(BookRepository bookRepository, Mapper<BookEntity, BookDto> bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public BookDto save(BookDto bookDto) {
        BookEntity bookEntity = bookMapper.mapEntityFromDto(bookDto);

        return  bookMapper.mapEntityToDto(bookRepository.save(bookEntity));
    }

    @Override
    public List<BookDto> findAll() {
        List<BookEntity> books =
                StreamSupport.stream(
                        bookRepository.findAll().spliterator(), false
                        ).collect(Collectors.toList());

        return books.stream()
                .map(bookEntity -> bookMapper.mapEntityToDto(bookEntity))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BookDto> findOne(Long id) {
        Optional<BookEntity> foundBook = bookRepository.findById(id);

        return foundBook.map(bookEntity -> bookMapper.mapEntityToDto(bookEntity));
    }

    @Override
    public boolean isExists(Long id) {
        return bookRepository.existsById(id);
    }

    @Override
    public BookDto updateBook(Long id, BookDto bookDto) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book does not exist");
        }

        BookEntity bookEntity = bookMapper.mapEntityFromDto(bookDto);
        bookEntity.setId(id);
        return  bookMapper.mapEntityToDto(bookRepository.save(bookEntity));
    }

    @Override
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        bookRepository.deleteAll();
    }
}


