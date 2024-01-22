package com.library.management.services.impl;

import com.library.management.mappers.Mapper;
import com.library.management.model.dto.BookDto;
import com.library.management.model.entities.BookEntity;
import com.library.management.repositories.BookRepository;
import com.library.management.services.BookService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    @CacheEvict(value = "bookList", allEntries = true)
    public BookDto save(BookDto bookDto) {
        BookEntity bookEntity = bookMapper.mapEntityFromDto(bookDto);
        bookEntity.setBorrowed(false);

        return  bookMapper.mapEntityToDto(bookRepository.save(bookEntity));
    }

    @Override
    @Cacheable(value = "bookList", key = "#root.methodName")
    public List<BookDto> findAll() {
        List<BookEntity> books =
                StreamSupport.stream(
                        bookRepository.findAll().spliterator(), false
                        ).toList();

        return books.stream()
                .map(bookEntity -> bookMapper.mapEntityToDto(bookEntity))
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "findBookById", key = "#id")
    public Optional<BookDto> findOne(Long id) {
        Optional<BookEntity> foundBook = bookRepository.findById(id);

        return foundBook.map(bookEntity -> bookMapper.mapEntityToDto(bookEntity));
    }

    @Override
    public boolean isExists(Long id) {
        return bookRepository.existsById(id);
    }

    @Override
    public boolean isExists(String isbn) {
        return bookRepository.findByIsbn(isbn).isPresent();
    }

    @Override
    @CacheEvict(value = {"bookList", "findBookById"}, allEntries = true)
    public BookDto updateBook(Long id, BookDto bookDto) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book does not exist");
        }

        BookEntity bookEntity = bookMapper.mapEntityFromDto(bookDto);
        bookEntity.setId(id);
        return  bookMapper.mapEntityToDto(bookRepository.save(bookEntity));
    }

    @Override
    @CacheEvict(value = {"bookList", "findBookById"}, allEntries = true)
    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book does not exist");
        }

        bookRepository.deleteById(id);
    }

    @Override
    @CacheEvict(value = {"bookList", "findBookById"}, allEntries = true)
    public void deleteAll() {
        bookRepository.deleteAll();
    }

    @Override
    public boolean isBorrowed(Long bookId){
        Optional<BookEntity> bookEntity = bookRepository.findById(bookId);
        return bookEntity.isPresent() && bookEntity.get().getBorrowed() == true;

    }
}


