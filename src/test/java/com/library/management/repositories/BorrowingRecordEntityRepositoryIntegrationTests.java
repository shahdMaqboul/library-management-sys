package com.library.management.repositories;

import com.library.management.TestDataUtil;
import com.library.management.model.entities.BookEntity;
import com.library.management.model.entities.BorrowingRecordEntity;
import com.library.management.model.entities.PatronEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BorrowingRecordEntityRepositoryIntegrationTests {

    private BorrowingRecordRepository underTest;
    private BookRepository bookRepository;
    private PatronRepository patronRepository;

    @Autowired
    public BorrowingRecordEntityRepositoryIntegrationTests(
            BorrowingRecordRepository underTest,
            BookRepository bookRepository,
            PatronRepository patronRepository) {
        this.underTest = underTest;
        this.bookRepository = bookRepository;
        this.patronRepository = patronRepository;
    }

    @BeforeEach
    public void setUp() {
        // Clean the database, e.g., truncate the table
        underTest.deleteAll();
        bookRepository.deleteAll();
        patronRepository.deleteAll();
    }

    @Test
    public void testThatBorrowingRecordCanBeCreatedAndRecalled() {
        BookEntity bookEntity = TestDataUtil.createTestBookEntityB();
        PatronEntity patronEntity = TestDataUtil.createTestPatronEntityA();

        bookRepository.save(bookEntity);
        patronRepository.save(patronEntity);

        BorrowingRecordEntity borrowingRecordEntity = BorrowingRecordEntity.builder()
                .book(bookEntity)
                .patron(patronEntity)
                .borrowingDate(LocalDate.now())
                .build();

        underTest.save(borrowingRecordEntity);

        Optional<BorrowingRecordEntity> result = underTest.findById(borrowingRecordEntity.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(borrowingRecordEntity);
    }

    @Test
    public void testThatBorrowingRecordCanBeUpdated() {
        BookEntity bookEntity = TestDataUtil.createTestBookEntityA();
        PatronEntity patronEntity = TestDataUtil.createTestPatronEntityA();

        bookRepository.save(bookEntity);
        patronRepository.save(patronEntity);

        BorrowingRecordEntity borrowingRecordEntity = BorrowingRecordEntity.builder()
                .book(bookEntity)
                .patron(patronEntity)
                .borrowingDate(LocalDate.now())
                .build();

        underTest.save(borrowingRecordEntity);

        borrowingRecordEntity.setReturnDate(LocalDate.now());
        underTest.save(borrowingRecordEntity);

        Optional<BorrowingRecordEntity> result = underTest.findById(borrowingRecordEntity.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(borrowingRecordEntity);
    }

    @Test
    public void testThatBorrowingRecordCanBeDeleted() {
        BookEntity bookEntity = TestDataUtil.createTestBookEntityA();
        PatronEntity patronEntity = TestDataUtil.createTestPatronEntityA();

        bookRepository.save(bookEntity);
        patronRepository.save(patronEntity);

        BorrowingRecordEntity borrowingRecordEntity = BorrowingRecordEntity.builder()
                .book(bookEntity)
                .patron(patronEntity)
                .borrowingDate(LocalDate.now())
                .build();

        underTest.save(borrowingRecordEntity);

        underTest.deleteById(borrowingRecordEntity.getId());

        Optional<BorrowingRecordEntity> result = underTest.findById(borrowingRecordEntity.getId());
        assertThat(result).isEmpty();
    }
}

