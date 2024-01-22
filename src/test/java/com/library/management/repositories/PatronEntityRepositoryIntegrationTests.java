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
        // Delete all records from borrowing_records and patrons
        borrowingRecordRepository.deleteAll();
        underTest.deleteAll();
    }

    @Test
    public void testThatPatronCanBeCreatedAndRecalled() {
        // Given
        PatronEntity patronEntity = TestDataUtil.createTestPatronEntityA();

        // When
        underTest.save(patronEntity);

        // Then
        Optional<PatronEntity> result = underTest.findById(patronEntity.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(patronEntity);
    }

    @Test
    public void testThatMultiplePatronsCanBeCreatedAndRecalled() {
        // Given
        PatronEntity patronEntityA = TestDataUtil.createTestPatronEntityA();
        PatronEntity patronEntityB = TestDataUtil.createTestPatronEntityB();
        PatronEntity patronEntityC = TestDataUtil.createTestPatronEntityC();

        // When
        underTest.save(patronEntityA);
        underTest.save(patronEntityB);
        underTest.save(patronEntityC);

        // Then
        Iterable<PatronEntity> result = underTest.findAll();
        assertThat(result)
                .hasSize(3)
                .containsExactly(patronEntityA, patronEntityB, patronEntityC);
    }

    @Test
    public void testThatPatronCanBeUpdated() {
        // Given
        PatronEntity patronEntityA = TestDataUtil.createTestPatronEntityA();
        underTest.save(patronEntityA);

        // When
        patronEntityA.setName("UPDATED");
        underTest.save(patronEntityA);

        // Then
        Optional<PatronEntity> result = underTest.findById(patronEntityA.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(patronEntityA);
    }

    @Test
    public void testThatPatronCanBeDeleted() {
        // Given
        PatronEntity patronEntityB = TestDataUtil.createTestPatronEntityB();
        underTest.save(patronEntityB);

        // When
        underTest.deleteById(patronEntityB.getId());

        // Then
        Optional<PatronEntity> result = underTest.findById(patronEntityB.getId());
        assertThat(result).isEmpty();
    }
}
