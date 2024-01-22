package com.library.management.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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

    @Valid
    private BookDto book;

    @Valid
    private PatronDto patron;
    @NotNull(message = "Borrowing date is mandatory")
    private LocalDate borrowingDate;

    private LocalDate returnDate;

}
