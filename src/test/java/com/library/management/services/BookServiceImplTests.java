package com.library.management.services;

import com.library.management.TestDataUtil;
import com.library.management.mappers.Mapper;
import com.library.management.model.dto.BookDto;
import com.library.management.model.entities.BookEntity;
import com.library.management.repositories.BookRepository;
import com.library.management.services.impl.BookServiceImpl;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class BookServiceImplTests {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private Mapper<BookEntity, BookDto> bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveBook() {
        // Given
        BookDto inputBookDto = TestDataUtil.createTestBookDtoA();
        BookEntity savedBookEntity = TestDataUtil.createTestBookEntityA();
        Mockito.when(bookMapper.mapEntityFromDto(inputBookDto)).thenReturn(savedBookEntity);
        Mockito.when(bookRepository.save(savedBookEntity)).thenReturn(savedBookEntity);
        Mockito.when(bookMapper.mapEntityToDto(savedBookEntity)).thenReturn(inputBookDto);

        // Act
        BookDto result = bookService.save(inputBookDto);

        // Assert
        Assertions.assertEquals(inputBookDto, result);
        Mockito.verify(bookRepository, Mockito.times(1)).save(savedBookEntity);
    }

    @Test
    public void testFindAllBooks() {
        // Given
        List<BookEntity> bookEntities = Arrays.asList(
                TestDataUtil.createTestBookEntityA(),
                TestDataUtil.createTestBookEntityB()
        );
        List<BookDto> expectedBookDtos = Arrays.asList(
                TestDataUtil.createTestBookDtoA(),
                TestDataUtil.createTestBookDtoB()
        );
        Mockito.when(bookRepository.findAll()).thenReturn(bookEntities);
        Mockito.when(bookMapper.mapEntityToDto(Mockito.any(BookEntity.class)))
                .thenReturn(expectedBookDtos.get(0), expectedBookDtos.get(1));

        // When
        List<BookDto> result = bookService.findAll();

        // Then
        Assertions.assertEquals(expectedBookDtos, result);
        Mockito.verify(bookRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void testFindOneBookById() {
        // Given
        Long bookId = 1L;
        BookEntity bookEntity = TestDataUtil.createTestBookEntityA();
        BookDto expectedBookDto = TestDataUtil.createTestBookDtoA();
        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookEntity));
        Mockito.when(bookMapper.mapEntityToDto(bookEntity)).thenReturn(expectedBookDto);

        // When
        Optional<BookDto> result = bookService.findOne(bookId);

        // Then
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(expectedBookDto, result.get());
        Mockito.verify(bookRepository, Mockito.times(1)).findById(bookId);
    }

    @Test
    public void testFindOneBookByIdNotFound() {
        // Given
        Long bookId = 1L;
        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // When
        Optional<BookDto> result = bookService.findOne(bookId);

        // Then
        Assertions.assertTrue(result.isEmpty());
        Mockito.verify(bookRepository, Mockito.times(1)).findById(bookId);
    }

    @Test
    public void testIsBookExists() {
        // Given
        Long bookId = 1L;
        Mockito.when(bookRepository.existsById(bookId)).thenReturn(true);

        // When
        boolean result = bookService.isExists(bookId);

        // Then
        Assertions.assertTrue(result);
        Mockito.verify(bookRepository, Mockito.times(1)).existsById(bookId);
    }

    @Test
    public void testIsBookNotExists() {
        // Given
        Long bookId = 1L;
        Mockito.when(bookRepository.existsById(bookId)).thenReturn(false);

        // When
        boolean result = bookService.isExists(bookId);

        // Then
        Assertions.assertFalse(result);
        Mockito.verify(bookRepository, Mockito.times(1)).existsById(bookId);
    }

    @Test
    public void testIsIsbnExists() {
        // Given
        String isbn = "123456789";
        Mockito.when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(TestDataUtil.createTestBookEntityA()));

        // When
        boolean result = bookService.isExists(isbn);

        // Then
        Assertions.assertTrue(result);
        Mockito.verify(bookRepository, Mockito.times(1)).findByIsbn(isbn);
    }

    @Test
    public void testIsIsbnNotExists() {
        // Given
        String isbn = "123456789";
        Mockito.when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());

        // When
        boolean result = bookService.isExists(isbn);

        // Then
        Assertions.assertFalse(result);
        Mockito.verify(bookRepository, Mockito.times(1)).findByIsbn(isbn);
    }

    @Test
    public void testUpdateBook() {
        // Given
        Long bookId = 1L;
        BookDto updatedBookDto = TestDataUtil.createTestBookDtoB();
        BookEntity existingBookEntity = TestDataUtil.createTestBookEntityA();
        BookEntity updatedBookEntity = TestDataUtil.createTestBookEntityB();

        Mockito.when(bookRepository.existsById(bookId)).thenReturn(true);
        Mockito.when(bookMapper.mapEntityFromDto(updatedBookDto)).thenReturn(updatedBookEntity);
        Mockito.when(bookRepository.save(updatedBookEntity)).thenReturn(updatedBookEntity);
        Mockito.when(bookMapper.mapEntityToDto(updatedBookEntity)).thenReturn(updatedBookDto);

        // When
        BookDto result = bookService.updateBook(bookId, updatedBookDto);

        // Then
        Assertions.assertEquals(updatedBookDto, result);
        Mockito.verify(bookRepository, Mockito.times(1)).existsById(bookId);
        Mockito.verify(bookRepository, Mockito.times(1)).save(updatedBookEntity);
    }

    @Test
    public void testUpdateBookNotFound() {
        // Given
        Long bookId = 1L;
        BookDto updatedBookDto = TestDataUtil.createTestBookDtoB();

        Mockito.when(bookRepository.existsById(bookId)).thenReturn(false);

        // When & Then
        Assertions.assertThrows(RuntimeException.class, () -> {
            bookService.updateBook(bookId, updatedBookDto);
        });

        Mockito.verify(bookRepository, Mockito.times(1)).existsById(bookId);
        Mockito.verify(bookRepository, Mockito.never()).save(Mockito.any(BookEntity.class));
    }

    @Test
    public void testDeleteBook() {
        // Given
        Long bookId = 1L;
        Mockito.when(bookRepository.existsById(bookId)).thenReturn(true);

        // When
        bookService.delete(bookId);

        // Then
        Mockito.verify(bookRepository, Mockito.times(1)).deleteById(bookId);
    }

    @Test
    public void testDeleteBookNotFound() {
        // Given
        Long bookId = 1L;
        Mockito.when(bookRepository.existsById(bookId)).thenReturn(false);

        // When & Then
        Assertions.assertThrows(RuntimeException.class, () -> {
            bookService.delete(bookId);
        });

        Mockito.verify(bookRepository, Mockito.times(1)).existsById(bookId);
        Mockito.verify(bookRepository, Mockito.never()).deleteById(Mockito.anyLong());
    }

    @Test
    public void testDeleteAllBooks() {
        // When
        bookService.deleteAll();

        // Then
        Mockito.verify(bookRepository, Mockito.times(1)).deleteAll();
    }

    @Test
    public void testIsBookBorrowedTrue() {
        // Given
        Long bookId = 1L;
        BookEntity borrowedBookEntity = TestDataUtil.createTestBookEntityA();
        borrowedBookEntity.setBorrowed(true);
        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(borrowedBookEntity));

        // When
        boolean result = bookService.isBorrowed(bookId);

        // Then
        Assertions.assertTrue(result);
        Mockito.verify(bookRepository, Mockito.times(1)).findById(bookId);
    }

    @Test
    public void testIsBookBorrowedFalse() {
        // Given
        Long bookId = 1L;
        BookEntity notBorrowedBookEntity = TestDataUtil.createTestBookEntityA();
        notBorrowedBookEntity.setBorrowed(false);
        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(notBorrowedBookEntity));

        // When
        boolean result = bookService.isBorrowed(bookId);

        // Then
        Assertions.assertFalse(result);
        Mockito.verify(bookRepository, Mockito.times(1)).findById(bookId);
    }
}
