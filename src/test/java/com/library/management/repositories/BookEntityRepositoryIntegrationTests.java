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
        BookEntity bookEntity = TestDataUtil.createTestBookEntityA();
        underTest.save(bookEntity);
        Optional<BookEntity> result = underTest.findById(bookEntity.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(bookEntity);
    }

    @Test
    public void testThatMultipleBooksCanBeCreatedAndRecalled() {
        BookEntity bookEntityB = TestDataUtil.createTestBookEntityB();
        underTest.save(bookEntityB);
        underTest.findAll();

        BookEntity bookEntityC = TestDataUtil.createTestBookEntityC();
        underTest.save(bookEntityC);
        underTest.findAll();

        Iterable<BookEntity> result = underTest.findAll();
        assertThat(result)
                .hasSize(2)
                .containsExactly(
                        bookEntityB, bookEntityC);
    }

    @Test
    public void testThatBookCanBeUpdated() {

        BookEntity bookEntityA = TestDataUtil.createTestBookEntityA();
        underTest.save(bookEntityA);

        bookEntityA.setTitle("UPDATED");
        underTest.save(bookEntityA);

        Optional<BookEntity> result = underTest.findById(bookEntityA.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(bookEntityA);
    }

    @Test
    public void testThatBookCanBeDeleted() {

        BookEntity bookEntityA = TestDataUtil.createTestBookEntityA();
        underTest.save(bookEntityA);

        underTest.deleteById(bookEntityA.getId());

        Optional<BookEntity> result = underTest.findById(bookEntityA.getId());
        assertThat(result).isEmpty();
    }
}
