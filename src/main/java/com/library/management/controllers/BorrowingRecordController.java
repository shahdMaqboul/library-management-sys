package com.library.management.controllers;

import com.library.management.model.dto.BorrowingRecordDto;
import com.library.management.services.BookService;
import com.library.management.services.BorrowingRecordService;
import com.library.management.services.PatronService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/borrowing")
public class BorrowingRecordController {

    private BorrowingRecordService borrowingRecordService;

    private BookService bookService;

    private PatronService patronService;

    public BorrowingRecordController(
            BorrowingRecordService borrowingRecordService,
            BookService bookService,
            PatronService patronService
            ) {
        this.borrowingRecordService = borrowingRecordService;
        this.bookService = bookService;
        this.patronService = patronService;
    }


    @PostMapping("/borrow/{bookId}/patron/{patronId}")
    public ResponseEntity<BorrowingRecordDto> borrowBook(
            @PathVariable Long bookId,
            @PathVariable Long patronId) {
        if (!bookService.isExists(bookId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!patronService.isExists(patronId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(bookService.isBorrowed(bookId) == true){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        BorrowingRecordDto borrowingRecordDto = borrowingRecordService.borrowBook(bookId, patronId);
        return new ResponseEntity<>(borrowingRecordDto, HttpStatus.CREATED);
    }

    @PutMapping("/return/{bookId}/patron/{patronId}")
    public ResponseEntity<BorrowingRecordDto> returnBook(
            @PathVariable Long bookId,
            @PathVariable Long patronId) {
        if (!bookService.isExists(bookId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!patronService.isExists(patronId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(bookService.isBorrowed(bookId) == false){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        BorrowingRecordDto borrowingRecordDto = borrowingRecordService.returnBook(bookId, patronId);
        return new ResponseEntity<>(borrowingRecordDto, HttpStatus.OK);
    }
}

