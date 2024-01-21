package com.library.management;

import com.library.management.model.dto.BookDto;
import com.library.management.model.dto.BorrowingRecordDto;
import com.library.management.model.dto.PatronDto;
import com.library.management.model.entities.BookEntity;
import com.library.management.model.entities.BorrowingRecordEntity;
import com.library.management.model.entities.PatronEntity;

import java.time.LocalDate;

public final class TestDataUtil {
    private TestDataUtil(){
    }

    public static BookEntity createTestBookEntityA() {
        return BookEntity.builder()
                .title("The Shadow in the Attic")
                .author("Sila Sea")
                .publicationYear(2005)
                .isbn("9781234567890")
                .borrowed(false)
                .build();
    }

    public static BookDto createTestBookDtoA() {
        return BookDto.builder()
                .title("The Shadow in the Attic")
                .author("Sila Sea")
                .publicationYear(2005)
                .isbn("9781234567890")
                .borrowed(false)
                .build();
    }

    public static BookEntity createTestBookEntityB() {
        return BookEntity.builder()
                .title("Beyond the Horizon")
                .author("John Writer")
                .publicationYear(2010)
                .isbn("9782345678901")
                .borrowed(false)
                .build();
    }

    public static BookDto createTestBookDtoB() {
        return BookDto.builder()
                .title("Beyond the Horizon")
                .author("John Writer")
                .publicationYear(2010)
                .isbn("9782345678901")
                .borrowed(false)
                .build();
    }

    public static BookEntity createTestBookEntityC() {
        return BookEntity.builder()
                .title("Lost in the Library")
                .author("Emma Reader")
                .publicationYear(2018)
                .isbn("9783456789012")
                .borrowed(false)
                .build();
    }

    public static BookDto createTestBookDtoC() {
        return BookDto.builder()
                .title("Lost in the Library")
                .author("Emma Reader")
                .publicationYear(2018)
                .isbn("9783456789012")
                .borrowed(false)
                .build();
    }

    public static PatronEntity createTestPatronEntityA() {
        return PatronEntity.builder()
                .name("John Doe")
                .contactInformation("john.doe@example.com")
                .build();
    }

    public static PatronDto createTestPatronDtoA() {
        return PatronDto.builder()
                .name("John Doe")
                .contactInformation("john.doe@example.com")
                .build();
    }

    public static PatronEntity createTestPatronEntityB() {
        return PatronEntity.builder()
                .name("Jane Smith")
                .contactInformation("jane.smith@example.com")
                .build();
    }

    public static PatronDto createTestPatronDtoB() {
        return PatronDto.builder()
                .name("Jane Smith")
                .contactInformation("jane.smith@example.com")
                .build();
    }

    public static PatronEntity createTestPatronEntityC() {
        return PatronEntity.builder()
                .name("Bob Johnson")
                .contactInformation("bob.johnson@example.com")
                .build();
    }

    public static PatronDto createTestPatronDtoC() {
        return PatronDto.builder()
                .name("Bob Johnson")
                .contactInformation("bob.johnson@example.com")
                .build();
    }

    public static BorrowingRecordEntity createTestBorrowingRecordEntityA() {
        return BorrowingRecordEntity.builder()
                .book(createTestBookEntityA())
                .patron(createTestPatronEntityA())
                .borrowingDate(LocalDate.of(2022, 1, 1))
                .returnDate(null)
                .build();
    }

    public static BorrowingRecordDto createTestBorrowingRecordDtoA() {
        return BorrowingRecordDto.builder()
                .book(createTestBookDtoA())
                .patron(createTestPatronDtoA())
                .borrowingDate(LocalDate.of(2022, 1, 1))
                .returnDate(null)
                .build();
    }
}
