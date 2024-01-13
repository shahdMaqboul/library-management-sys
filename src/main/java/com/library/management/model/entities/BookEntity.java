package com.library.management.model.entities;

import jakarta.persistence.*;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
//import jakarta.validation.constraints.Size;

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
//    @NotBlank(message = "Title is mandatory")
    private String title;
//    @NotBlank(message = "Author name is mandatory")
    private String author;
//    @NotBlank(message = "Publication year is mandatory")
    private Integer publicationYear;

    @Column(unique = true)
//    @Size(min = 10, max = 13, message = "isbn should be valid")
    private String isbn;

}
