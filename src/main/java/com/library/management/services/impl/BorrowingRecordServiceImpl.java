package com.library.management.services.impl;

import com.library.management.mappers.Mapper;
import com.library.management.model.dto.BorrowingRecordDto;
import com.library.management.model.entities.BookEntity;
import com.library.management.model.entities.BorrowingRecordEntity;
import com.library.management.model.entities.PatronEntity;
import com.library.management.repositories.BookRepository;
import com.library.management.repositories.BorrowingRecordRepository;
import com.library.management.repositories.PatronRepository;
import com.library.management.services.BorrowingRecordService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class BorrowingRecordServiceImpl implements BorrowingRecordService {

    private BorrowingRecordRepository borrowingRecordRepository;
    private Mapper<BorrowingRecordEntity, BorrowingRecordDto> borrowingRecordMapper;
    private BookRepository bookRepository;
    private PatronRepository patronRepository;

    public BorrowingRecordServiceImpl(
            BorrowingRecordRepository borrowingRecordRepository,
            Mapper<BorrowingRecordEntity, BorrowingRecordDto> borrowingRecordMapper,
            BookRepository bookRepository,
            PatronRepository patronRepository
    ) {
        this.borrowingRecordRepository = borrowingRecordRepository;
        this.borrowingRecordMapper = borrowingRecordMapper;
        this.bookRepository = bookRepository;
        this.patronRepository = patronRepository;
    }

    @Override
    @Transactional
    public BorrowingRecordDto borrowBook(Long bookId, Long patronId) {
        BookEntity bookEntity = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        PatronEntity patronEntity = patronRepository.findById(patronId)
                .orElseThrow(() -> new RuntimeException("Patron not found"));

        BorrowingRecordEntity borrowingRecordEntity = new BorrowingRecordEntity();
        borrowingRecordEntity.setBook(bookEntity);
        borrowingRecordEntity.setPatron(patronEntity);
        borrowingRecordEntity.setBorrowingDate(LocalDate.now());

        bookEntity.setBorrowed(true);
        bookRepository.save(bookEntity);

        return borrowingRecordMapper.mapEntityToDto(borrowingRecordRepository.save(borrowingRecordEntity));
    }

    @Override
    @Transactional
    public BorrowingRecordDto returnBook(Long bookId, Long patronId) {
        BookEntity bookEntity = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        PatronEntity patronEntity = patronRepository.findById(patronId)
                .orElseThrow(() -> new RuntimeException("Patron not found"));

        Optional<BorrowingRecordEntity> borrowingRecordOptional = borrowingRecordRepository
                .findByBookAndPatronAndReturnDateIsNull(bookEntity, patronEntity);

        return borrowingRecordOptional.map(borrowingRecordEntity -> {
            borrowingRecordEntity.setReturnDate(LocalDate.now());

            bookEntity.setBorrowed(false);
            bookRepository.save(bookEntity);


            return borrowingRecordMapper.mapEntityToDto(borrowingRecordRepository.save(borrowingRecordEntity));
        }).orElseThrow(() -> new RuntimeException("Borrowing record not found"));
    }

    @Override
    public void deleteAll() {
        borrowingRecordRepository.deleteAll();
    }
}
