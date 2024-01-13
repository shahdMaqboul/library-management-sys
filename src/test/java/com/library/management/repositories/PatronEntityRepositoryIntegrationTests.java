package com.library.management.repositories;

import com.library.management.TestDataUtil;
import com.library.management.model.entities.PatronEntity;
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
public class PatronEntityRepositoryIntegrationTests {

    private PatronRepository underTest;

    private BorrowingRecordRepository borrowingRecordRepository;

    @Autowired
    public PatronEntityRepositoryIntegrationTests(
            PatronRepository underTest,
            BorrowingRecordRepository borrowingRecordRepository
    ) {
        this.underTest = underTest;
        this.borrowingRecordRepository = borrowingRecordRepository;
    }

    @BeforeEach
    public void setUp() {
        // Clean the database, e.g., truncate the table
        borrowingRecordRepository.deleteAll();
        underTest.deleteAll();

    }

    @Test
    public void testThatPatronCanBeCreatedAndRecalled() {
        PatronEntity patronEntity = TestDataUtil.createTestPatronEntityA();
        underTest.save(patronEntity);
        Optional<PatronEntity> result = underTest.findById(patronEntity.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(patronEntity);
    }

    @Test
    public void testThatMultiplePatronsCanBeCreatedAndRecalled() {

        PatronEntity patronEntityA = TestDataUtil.createTestPatronEntityA();
        underTest.save(patronEntityA);

        PatronEntity patronEntityB = TestDataUtil.createTestPatronEntityB();
        underTest.save(patronEntityB);

        PatronEntity patronEntityC = TestDataUtil.createTestPatronEntityC();
        underTest.save(patronEntityC);

        Iterable<PatronEntity> result = underTest.findAll();
        assertThat(result)
                .hasSize(3)
                .containsExactly(
                        patronEntityA,
                        patronEntityB, patronEntityC);
    }

    @Test
    public void testThatPatronCanBeUpdated() {

        PatronEntity patronEntityA = TestDataUtil.createTestPatronEntityA();
        underTest.save(patronEntityA);

        patronEntityA.setName("UPDATED");
        underTest.save(patronEntityA);

        Optional<PatronEntity> result = underTest.findById(patronEntityA.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(patronEntityA);
    }

    @Test
    public void testThatPatronCanBeDeleted() {

        PatronEntity patronEntityB = TestDataUtil.createTestPatronEntityB();
        underTest.save(patronEntityB);

        underTest.deleteById(patronEntityB.getId());

        Optional<PatronEntity> result = underTest.findById(patronEntityB.getId());
        assertThat(result).isEmpty();
    }
}
