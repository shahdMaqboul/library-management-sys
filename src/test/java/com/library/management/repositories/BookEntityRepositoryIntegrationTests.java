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
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_CLASS)
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
        // Delete all records from borrowing_records
        borrowingRecordRepository.deleteAll();
//        // Clean the database, e.g., truncate the table
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
//        System.out.println("11 " +underTest.findAll());
//        System.out.println("22 " +borrowingRecordRepository.findAll());
//        BookEntity bookEntityA = TestDataUtil.createTestBookEntityA();
//        System.out.println("1 " + underTest.save(bookEntityA));
//        System.out.println("2 " +underTest.findAll());

        BookEntity bookEntityB = TestDataUtil.createTestBookEntityB();
//        underTest.save(bookEntityB);
        System.out.println("3 " +underTest.save(bookEntityB));
        System.out.println(underTest.findAll());

        BookEntity bookEntityC = TestDataUtil.createTestBookEntityC();
//        underTest.save(bookEntityC);
        System.out.println(underTest.save(bookEntityC));
        System.out.println(underTest.findAll());

        Iterable<BookEntity> result = underTest.findAll();
        assertThat(result)
                .hasSize(2)
                .containsExactly(
//                        bookEntityA,
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
