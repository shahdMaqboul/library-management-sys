package com.library.management.repositories;

import com.library.management.model.entities.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {
    // Additional custom methods if needed
    Optional<BookEntity> findByIsbn(String isbn);
}

