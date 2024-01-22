package com.library.management.model.entities;


import jakarta.persistence.*;
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
@Entity
@Table(name = "borrowing_records")
public class BorrowingRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    @Valid
    private BookEntity book;

    @ManyToOne
    @JoinColumn(name = "patron_id")
    @Valid
    private PatronEntity patron;

    @NotNull(message = "Borrowing date is mandatory")
    private LocalDate borrowingDate;

    private LocalDate returnDate;

}

