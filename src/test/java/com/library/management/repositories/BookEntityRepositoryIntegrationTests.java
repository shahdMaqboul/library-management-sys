package com.library.management.repositories;

import com.library.management.TestDataUtil;
import com.library.management.model.entities.BookEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class BookEntityRepositoryIntegrationTests {

    private BookRepository underTest;

    private BorrowingRecordRepository borrowingRecordRepository;

    @Autowired
    public BookEntityRepositoryIntegrationTests(
            BookRepository underTest,
            BorrowingRecordRepository borrowingRecordRepository
            ) {
        this.underTest = underTest;
        this.borrowingRecordRepository = borrowingRecordRepository;
    }

    @BeforeEach
    public void setUp() {
        // Delete all records from borrowing_records and books
        borrowingRecordRepository.deleteAll();
        underTest.deleteAll();
    }

    @Test
    public void testThatBookCanBeCreatedAndRecalled() {

        // Given
        BookEntity bookEntity = TestDataUtil.createTestBookEntityA();

        // When
        underTest.save(bookEntity);

        // Then
        Optional<BookEntity> result = underTest.findById(bookEntity.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(bookEntity);
    }

    @Test
    public void testThatMultipleBooksCanBeCreatedAndRecalled() {
        // Given
        BookEntity bookEntityB = TestDataUtil.createTestBookEntityB();
        BookEntity bookEntityC = TestDataUtil.createTestBookEntityC();

        // When
        underTest.save(bookEntityB);
        underTest.save(bookEntityC);

        // Then
        Iterable<BookEntity> result = underTest.findAll();
        assertThat(result)
                .hasSize(2)
                .containsExactly(bookEntityB, bookEntityC);
    }

    @Test
    public void testThatBookCanBeUpdated() {
        // Given
        BookEntity bookEntityA = TestDataUtil.createTestBookEntityA();
        underTest.save(bookEntityA);

        // When
        bookEntityA.setTitle("UPDATED");
        underTest.save(bookEntityA);

        // Then
        Optional<BookEntity> result = underTest.findById(bookEntityA.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(bookEntityA);
    }

    @Test
    public void testThatBookCanBeDeleted() {
        // Given
        BookEntity bookEntityA = TestDataUtil.createTestBookEntityA();
        underTest.save(bookEntityA);

        // When
        underTest.deleteById(bookEntityA.getId());

        // Then
        Optional<BookEntity> result = underTest.findById(bookEntityA.getId());
        assertThat(result).isEmpty();
    }

    @Test
    public void testFindByIsbn() {
        // Given
        BookEntity bookEntity = TestDataUtil.createTestBookEntityC();
        underTest.save(bookEntity);

        // When
        Optional<BookEntity> result = underTest.findByIsbn(bookEntity.getIsbn());

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(bookEntity);
    }
}
