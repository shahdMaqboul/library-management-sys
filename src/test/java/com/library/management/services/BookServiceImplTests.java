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
        // Arrange
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
        // Arrange
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

        // Act
        List<BookDto> result = bookService.findAll();

        // Assert
        Assertions.assertEquals(expectedBookDtos, result);
        Mockito.verify(bookRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void testFindOneBookById() {
        // Arrange
        Long bookId = 1L;
        BookEntity bookEntity = TestDataUtil.createTestBookEntityA();
        BookDto expectedBookDto = TestDataUtil.createTestBookDtoA();
        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookEntity));
        Mockito.when(bookMapper.mapEntityToDto(bookEntity)).thenReturn(expectedBookDto);

        // Act
        Optional<BookDto> result = bookService.findOne(bookId);

        // Assert
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(expectedBookDto, result.get());
        Mockito.verify(bookRepository, Mockito.times(1)).findById(bookId);
    }

    @Test
    public void testFindOneBookByIdNotFound() {
        // Arrange
        Long bookId = 1L;
        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Act
        Optional<BookDto> result = bookService.findOne(bookId);

        // Assert
        Assertions.assertTrue(result.isEmpty());
        Mockito.verify(bookRepository, Mockito.times(1)).findById(bookId);
    }

    @Test
    public void testIsBookExists() {
        // Arrange
        Long bookId = 1L;
        Mockito.when(bookRepository.existsById(bookId)).thenReturn(true);

        // Act
        boolean result = bookService.isExists(bookId);

        // Assert
        Assertions.assertTrue(result);
        Mockito.verify(bookRepository, Mockito.times(1)).existsById(bookId);
    }

    @Test
    public void testIsBookNotExists() {
        // Arrange
        Long bookId = 1L;
        Mockito.when(bookRepository.existsById(bookId)).thenReturn(false);

        // Act
        boolean result = bookService.isExists(bookId);

        // Assert
        Assertions.assertFalse(result);
        Mockito.verify(bookRepository, Mockito.times(1)).existsById(bookId);
    }

    @Test
    public void testIsIsbnExists() {
        // Arrange
        String isbn = "123456789";
        Mockito.when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(TestDataUtil.createTestBookEntityA()));

        // Act
        boolean result = bookService.isExists(isbn);

        // Assert
        Assertions.assertTrue(result);
        Mockito.verify(bookRepository, Mockito.times(1)).findByIsbn(isbn);
    }

    @Test
    public void testIsIsbnNotExists() {
        // Arrange
        String isbn = "123456789";
        Mockito.when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());

        // Act
        boolean result = bookService.isExists(isbn);

        // Assert
        Assertions.assertFalse(result);
        Mockito.verify(bookRepository, Mockito.times(1)).findByIsbn(isbn);
    }

    @Test
    public void testUpdateBook() {
        // Arrange
        Long bookId = 1L;
        BookDto updatedBookDto = TestDataUtil.createTestBookDtoB();
        BookEntity existingBookEntity = TestDataUtil.createTestBookEntityA();
        BookEntity updatedBookEntity = TestDataUtil.createTestBookEntityB();

        Mockito.when(bookRepository.existsById(bookId)).thenReturn(true);
        Mockito.when(bookMapper.mapEntityFromDto(updatedBookDto)).thenReturn(updatedBookEntity);
        Mockito.when(bookRepository.save(updatedBookEntity)).thenReturn(updatedBookEntity);
        Mockito.when(bookMapper.mapEntityToDto(updatedBookEntity)).thenReturn(updatedBookDto);

        // Act
        BookDto result = bookService.updateBook(bookId, updatedBookDto);

        // Assert
        Assertions.assertEquals(updatedBookDto, result);
        Mockito.verify(bookRepository, Mockito.times(1)).existsById(bookId);
        Mockito.verify(bookRepository, Mockito.times(1)).save(updatedBookEntity);
    }

    @Test
    public void testUpdateBookNotFound() {
        // Arrange
        Long bookId = 1L;
        BookDto updatedBookDto = TestDataUtil.createTestBookDtoB();

        Mockito.when(bookRepository.existsById(bookId)).thenReturn(false);

        // Act & Assert
        Assertions.assertThrows(RuntimeException.class, () -> {
            bookService.updateBook(bookId, updatedBookDto);
        });

        Mockito.verify(bookRepository, Mockito.times(1)).existsById(bookId);
        Mockito.verify(bookRepository, Mockito.never()).save(Mockito.any(BookEntity.class));
    }

    @Test
    public void testDeleteBook() {
        // Arrange
        Long bookId = 1L;
        Mockito.when(bookRepository.existsById(bookId)).thenReturn(true);

        // Act
        bookService.delete(bookId);

        // Assert
        Mockito.verify(bookRepository, Mockito.times(1)).deleteById(bookId);
    }

    @Test
    public void testDeleteBookNotFound() {
        // Arrange
        Long bookId = 1L;
        Mockito.when(bookRepository.existsById(bookId)).thenReturn(false);

        // Act & Assert
        Assertions.assertThrows(RuntimeException.class, () -> {
            bookService.delete(bookId);
        });

        Mockito.verify(bookRepository, Mockito.times(1)).existsById(bookId);
        Mockito.verify(bookRepository, Mockito.never()).deleteById(Mockito.anyLong());
    }

    @Test
    public void testDeleteAllBooks() {
        // Act
        bookService.deleteAll();

        // Assert
        Mockito.verify(bookRepository, Mockito.times(1)).deleteAll();
    }

    @Test
    public void testIsBookBorrowedTrue() {
        // Arrange
        Long bookId = 1L;
        BookEntity borrowedBookEntity = TestDataUtil.createTestBookEntityA();
        borrowedBookEntity.setBorrowed(true);
        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(borrowedBookEntity));

        // Act
        boolean result = bookService.isBorrowed(bookId);

        // Assert
        Assertions.assertTrue(result);
        Mockito.verify(bookRepository, Mockito.times(1)).findById(bookId);
    }

    @Test
    public void testIsBookBorrowedFalse() {
        // Arrange
        Long bookId = 1L;
        BookEntity notBorrowedBookEntity = TestDataUtil.createTestBookEntityA();
        notBorrowedBookEntity.setBorrowed(false);
        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(notBorrowedBookEntity));

        // Act
        boolean result = bookService.isBorrowed(bookId);

        // Assert
        Assertions.assertFalse(result);
        Mockito.verify(bookRepository, Mockito.times(1)).findById(bookId);
    }
}
