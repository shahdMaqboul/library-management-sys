package com.library.management.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDto {

    @Valid

    private Long id;
    @NotBlank(message = "Title is mandatory")
    @NotNull(message = "Title is mandatory")
    private String title;
    @NotBlank(message = "Author name is mandatory")
    @NotNull(message = "Author name is mandatory")
    private String author;
    private Integer publicationYear;
    @Size(min = 10, max = 13, message = "isbn should be valid")
    @NotBlank(message = "isbn is mandatory")
    @NotNull(message = "isbn is mandatory")
    private String isbn;
}