package com.library.management.services;

import com.library.management.model.dto.BorrowingRecordDto;

public interface BorrowingRecordService {
    BorrowingRecordDto borrowBook(Long bookId, Long patronId);

    BorrowingRecordDto returnBook(Long bookId, Long patronId);

    void deleteAll();
}
