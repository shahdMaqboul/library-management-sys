package com.library.management.controllers;

import com.library.management.model.dto.BorrowingRecordDto;
import com.library.management.model.dto.PatronDto;
import com.library.management.model.dto.BookDto;
import com.library.management.TestDataUtil;
import com.library.management.services.BookService;
import com.library.management.services.BorrowingRecordService;
import com.library.management.services.PatronService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class BorrowingRecordControllerIntegrationTests {

    private MockMvc mockMvc;
    private BorrowingRecordService borrowingRecordService;
    private BookService bookService;
    private PatronService patronService;

    @Autowired
    public BorrowingRecordControllerIntegrationTests(
            MockMvc mockMvc,
            BorrowingRecordService borrowingRecordService,
            BookService bookService,
            PatronService patronService
    ) {
        this.mockMvc = mockMvc;
        this.borrowingRecordService=borrowingRecordService;
        this.bookService = bookService;
        this.patronService = patronService;
    }

    @BeforeEach
    public void setUp() {
        // Delete all records from borrowing_records, books and patrons
        borrowingRecordService.deleteAll();
        bookService.deleteAll();
        patronService.deleteAll();
    }

    @Test
    public void testThatBorrowBookReturnsHttpStatus201() throws Exception {
        // Arrange
        BookDto testBookDto = TestDataUtil.createTestBookDtoA();
        PatronDto testPatronDto = TestDataUtil.createTestPatronDtoA();
        BookDto savedBookDto = bookService.save(testBookDto);
        PatronDto savedPatronDto =patronService.save(testPatronDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/borrowing/borrow/{bookId}/patron/{patronId}", savedBookDto.getId(), savedPatronDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testThatBorrowBookReturnsBorrowingRecord() throws Exception {
        // Arrange
        BookDto testBookDto = TestDataUtil.createTestBookDtoA();
        PatronDto testPatronDto = TestDataUtil.createTestPatronDtoA();
        BookDto savedBookDto = bookService.save(testBookDto);
        PatronDto savedPatronDto =patronService.save(testPatronDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/borrowing/borrow/{bookId}/patron/{patronId}", savedBookDto.getId(), savedPatronDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.book.id").value(savedBookDto.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.book.borrowed").value(true)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.patron.id").value(savedPatronDto.getId())
        );
    }

    @Test
    public void testThatBorrowBookReturnsHttpStatus404WhenNoBookAndNoPatronExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/borrowing/borrow/999/patron/999")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatBorrowBookReturnsHttpStatusConflictWhenBookIsAlreadyBorrowed() throws Exception {
        // Arrange
        BookDto testBookDto = TestDataUtil.createTestBookDtoA();
        PatronDto testPatronDto1 = TestDataUtil.createTestPatronDtoA();
        PatronDto testPatronDto2 = TestDataUtil.createTestPatronDtoB();

        BookDto savedBookDto = bookService.save(testBookDto);
        PatronDto savedPatronDto1 = patronService.save(testPatronDto1);
        PatronDto savedPatronDto2 = patronService.save(testPatronDto2);

        // Patron 1 borrows the book
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/borrowing/borrow/{bookId}/patron/{patronId}", savedBookDto.getId(), savedPatronDto1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        // Attempting to borrow the book again with Patron 2 should result in conflict
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/borrowing/borrow/{bookId}/patron/{patronId}", savedBookDto.getId(), savedPatronDto2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isConflict());
    }


    @Test
    public void testThatReturnBookReturnsHttpStatus200() throws Exception {
        // Arrange
        BookDto testBookDto = TestDataUtil.createTestBookDtoA();
        PatronDto testPatronDto = TestDataUtil.createTestPatronDtoA();
        BookDto savedBookDto = bookService.save(testBookDto);
        PatronDto savedPatronDto =patronService.save(testPatronDto);
        BorrowingRecordDto borrowingRecordDto = borrowingRecordService.borrowBook(savedBookDto.getId(), savedPatronDto.getId());

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/borrowing/return/{bookId}/patron/{patronId}", savedBookDto.getId(), savedPatronDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatReturnBookReturnsBorrowingRecord() throws Exception {
        // Arrange
        BookDto testBookDto = TestDataUtil.createTestBookDtoA();
        PatronDto testPatronDto = TestDataUtil.createTestPatronDtoA();
        BookDto savedBookDto = bookService.save(testBookDto);
        PatronDto savedPatronDto =patronService.save(testPatronDto);
        BorrowingRecordDto borrowingRecordDto = borrowingRecordService.borrowBook(savedBookDto.getId(), savedPatronDto.getId());

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/borrowing/return/{bookId}/patron/{patronId}", savedBookDto.getId(), savedPatronDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(borrowingRecordDto.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.book.id").value(savedBookDto.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.book.borrowed").value(false)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.patron.id").value(savedPatronDto.getId())
        );
    }

    @Test
    public void testThatReturnBookReturnsHttpStatus400WhenNoBookAndNoPatronExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/borrowing/return/999/patron/999")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatReturnBookReturnsHttpStatusConflictWhenBookIsNotBorrowed() throws Exception {
        // Arrange
        BookDto testBookDto = TestDataUtil.createTestBookDtoA();
        PatronDto testPatronDto = TestDataUtil.createTestPatronDtoA();

        BookDto savedBookDto = bookService.save(testBookDto);
        PatronDto savedPatronDto = patronService.save(testPatronDto);

        // Attempting to return a book that is not borrowed should result in conflict
        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/borrowing/return/{bookId}/patron/{patronId}", savedBookDto.getId(), savedPatronDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isConflict());
    }


}
