package com.library.management.services;

import com.library.management.TestDataUtil;
import com.library.management.mappers.Mapper;
import com.library.management.model.dto.BorrowingRecordDto;
import com.library.management.model.entities.BookEntity;
import com.library.management.model.entities.BorrowingRecordEntity;
import com.library.management.model.entities.PatronEntity;
import com.library.management.repositories.BookRepository;
import com.library.management.repositories.BorrowingRecordRepository;
import com.library.management.repositories.PatronRepository;
import com.library.management.services.impl.BorrowingRecordServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.time.LocalDate;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class BorrowingRecordServiceImplTests {

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @Mock
    private Mapper<BorrowingRecordEntity, BorrowingRecordDto> borrowingRecordMapper;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PatronRepository patronRepository;

    @InjectMocks
    private BorrowingRecordServiceImpl borrowingRecordService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Transactional
    public void testBorrowBook() {
        // Arrange
        Long bookId = 1L;
        Long patronId = 667L;

        BookEntity bookEntity = TestDataUtil.createTestBookEntityA();
        PatronEntity patronEntity = TestDataUtil.createTestPatronEntityA();
        BorrowingRecordEntity borrowingRecordEntity = TestDataUtil.createTestBorrowingRecordEntityA();
        BorrowingRecordDto expectedBorrowingRecordDto = TestDataUtil.createTestBorrowingRecordDtoA();

        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookEntity));
        Mockito.when(patronRepository.findById(patronId)).thenReturn(Optional.of(patronEntity));

        // Stubbing the method with any BorrowingRecordEntity argument
        Mockito.when(borrowingRecordMapper.mapEntityToDto(Mockito.any()))
                .thenReturn(expectedBorrowingRecordDto);

        // Act
        BorrowingRecordDto result = borrowingRecordService.borrowBook(bookId, patronId);

        // Assert
        Assertions.assertEquals(expectedBorrowingRecordDto, result);

        // Verify
        Mockito.verify(bookRepository, Mockito.times(1)).save(bookEntity);
        Mockito.verify(borrowingRecordRepository, Mockito.times(1)).save(Mockito.any(BorrowingRecordEntity.class));
    }

    @Test
    public void testReturnBook() {
        // Arrange
        Long bookId = 1L;
        Long patronId = 667L;

        BookEntity bookEntity = TestDataUtil.createTestBookEntityA();
        PatronEntity patronEntity = TestDataUtil.createTestPatronEntityA();
        BorrowingRecordEntity borrowingRecordEntity = TestDataUtil.createTestBorrowingRecordEntityA();
        BorrowingRecordDto expectedBorrowingRecordDto = TestDataUtil.createTestBorrowingRecordDtoA();

        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookEntity));
        Mockito.when(patronRepository.findById(patronId)).thenReturn(Optional.of(patronEntity));
        Mockito.when(borrowingRecordRepository.findByBookAndPatronAndReturnDateIsNull(bookEntity, patronEntity))
                .thenReturn(Optional.of(borrowingRecordEntity));
        Mockito.when(borrowingRecordMapper.mapEntityToDto(Mockito.any()))
                .thenReturn(expectedBorrowingRecordDto);

        // Act
        BorrowingRecordDto result = borrowingRecordService.returnBook(bookId, patronId);
        System.out.println("result = "+result);

        // Assert
        Assertions.assertEquals(expectedBorrowingRecordDto, result);
        Mockito.verify(bookRepository, Mockito.times(1)).save(bookEntity);
        Mockito.verify(borrowingRecordRepository, Mockito.times(1)).save(borrowingRecordEntity);
    }

    @Test
    public void testReturnBookRecordNotFound() {
        // Arrange
        Long bookId = 1L;
        Long patronId = 1L;

        BookEntity bookEntity = TestDataUtil.createTestBookEntityA();
        PatronEntity patronEntity = TestDataUtil.createTestPatronEntityA();

        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookEntity));
        Mockito.when(patronRepository.findById(patronId)).thenReturn(Optional.of(patronEntity));
        Mockito.when(borrowingRecordRepository.findByBookAndPatronAndReturnDateIsNull(bookEntity, patronEntity))
                .thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(RuntimeException.class, () -> {
            borrowingRecordService.returnBook(bookId, patronId);
        });

        Mockito.verify(bookRepository, Mockito.never()).save(Mockito.any(BookEntity.class));
        Mockito.verify(borrowingRecordRepository, Mockito.never()).save(Mockito.any(BorrowingRecordEntity.class));
    }

    @Test
    public void testDeleteAllBorrowingRecords() {
        // Act
        borrowingRecordService.deleteAll();

        // Assert
        Mockito.verify(borrowingRecordRepository, Mockito.times(1)).deleteAll();
    }
}
