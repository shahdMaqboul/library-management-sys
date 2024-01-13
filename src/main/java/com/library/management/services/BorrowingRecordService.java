package com.library.management.services;

import com.library.management.model.dto.BorrowingRecordDto;
import com.library.management.model.entities.BorrowingRecordEntity;

public interface BorrowingRecordService {
    BorrowingRecordDto borrowBook(Long bookId, Long patronId);

    BorrowingRecordDto returnBook(Long bookId, Long patronId);

    void deleteAll();
}
