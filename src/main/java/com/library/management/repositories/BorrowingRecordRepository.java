package com.library.management.repositories;

import com.library.management.model.entities.BookEntity;
import com.library.management.model.entities.BorrowingRecordEntity;
import com.library.management.model.entities.PatronEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecordEntity, Long> {
    Optional<BorrowingRecordEntity> findByBookAndPatronAndReturnDateIsNull(BookEntity book, PatronEntity patron);

    @Modifying
    @Query("UPDATE BorrowingRecordEntity b SET b.returnDate = :returnDate WHERE b.id = :id")
    int setReturnDateById(@Param("id") Long id, @Param("returnDate") LocalDate returnDate);
}

