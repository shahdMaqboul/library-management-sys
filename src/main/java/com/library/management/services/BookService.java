package com.library.management.services;

import com.library.management.model.dto.BookDto;
import java.util.List;
import java.util.Optional;

public interface BookService {
    BookDto save(BookDto bookDto);

    List<BookDto> findAll();

    Optional<BookDto> findOne(Long id);

    boolean isExists(Long id);

    boolean isExists(String isbn);

    BookDto updateBook(Long id, BookDto bookDto);

    void delete(Long id);

    void deleteAll();

    boolean isBorrowed(Long bookId);
}
