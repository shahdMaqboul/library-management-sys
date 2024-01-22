package com.library.management.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "books")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_id_seq")
    private Long id;

    @NotBlank(message = "Title is mandatory")
    @NotNull(message = "Title is mandatory")
    private String title;

    @NotBlank(message = "Author name is mandatory")
    @NotNull(message = "Author name is mandatory")
    private String author;

    @Min(value = 0, message = "Publication year should be a positive number")
    @NotNull(message = "Publication year is mandatory")
    private Integer publicationYear;

    @Size(min = 10, max = 13, message = "isbn should be valid (10-13 characters)")
    @NotBlank(message = "isbn is mandatory")
    @NotNull(message = "isbn is mandatory")
    @Column(unique = true)
    private String isbn;

    @NotNull(message = "Borrowing Status is mandatory")
    private Boolean borrowed;
}
