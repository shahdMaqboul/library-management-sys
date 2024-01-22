package com.library.management.repositories;

import com.library.management.model.entities.BookEntity;
import com.library.management.model.entities.BorrowingRecordEntity;
import com.library.management.model.entities.PatronEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecordEntity, Long> {
    Optional<BorrowingRecordEntity> findByBookAndPatronAndReturnDateIsNull(BookEntity book, PatronEntity patron);
}

