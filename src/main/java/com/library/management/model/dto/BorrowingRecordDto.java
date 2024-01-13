package com.library.management.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BorrowingRecordDto {

    private Long id;

    private BookDto book;

    private PatronDto patron;

    private LocalDate borrowingDate;

    private LocalDate returnDate;

}
