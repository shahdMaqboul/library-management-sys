package com.library.management;

import com.library.management.model.dto.BookDto;
import com.library.management.model.dto.PatronDto;
import com.library.management.model.entities.BookEntity;
import com.library.management.model.entities.PatronEntity;

public final class TestDataUtil {
    private TestDataUtil(){
    }

    public static BookEntity createTestBookEntityA() {
        return BookEntity.builder()
                .id(1L)
                .title("The Shadow in the Attic")
                .author("Sila Sea")
                .publicationYear(2005)
                .isbn("978-1-2345-6789-0")
                .build();
    }

    public static BookDto createTestBookDtoA() {
        return BookDto.builder()
                .id(1L)
                .title("The Shadow in the Attic")
                .author("Sila Sea")
                .publicationYear(2005)
                .isbn("978-1-2345-6789-0")
                .build();
    }

    public static BookEntity createTestBookEntityB() {
        return BookEntity.builder()
                .id(2L)
                .title("Beyond the Horizon")
                .author("John Writer")
                .publicationYear(2010)
                .isbn("978-2-3456-7890-1")
                .build();
    }

    public static BookDto createTestBookDtoB() {
        return BookDto.builder()
                .id(2L)
                .title("Beyond the Horizon")
                .author("John Writer")
                .publicationYear(2010)
                .isbn("978-2-3456-7890-1")
                .build();
    }

    public static BookEntity createTestBookEntityC() {
        return BookEntity.builder()
                .id(3L)
                .title("Lost in the Library")
                .author("Emma Reader")
                .publicationYear(2018)
                .isbn("978-3-4567-8901-2")
                .build();
    }

    public static BookDto createTestBookDtoC() {
        return BookDto.builder()
                .id(3L)
                .title("Lost in the Library")
                .author("Emma Reader")
                .publicationYear(2018)
                .isbn("978-3-4567-8901-2")
                .build();
    }

    public static PatronEntity createTestPatronEntityA() {
        return PatronEntity.builder()
                .id(1L)
                .name("John Doe")
                .contactInformation("john.doe@example.com")
                .build();
    }

    public static PatronDto createTestPatronDtoA() {
        return PatronDto.builder()
                .id(1L)
                .name("John Doe")
                .contactInformation("john.doe@example.com")
                .build();
    }

    public static PatronEntity createTestPatronEntityB() {
        return PatronEntity.builder()
                .id(2L)
                .name("Jane Smith")
                .contactInformation("jane.smith@example.com")
                .build();
    }

    public static PatronDto createTestPatronDtoB() {
        return PatronDto.builder()
                .id(2L)
                .name("Jane Smith")
                .contactInformation("jane.smith@example.com")
                .build();
    }

    public static PatronEntity createTestPatronEntityC() {
        return PatronEntity.builder()
                .id(3L)
                .name("Bob Johnson")
                .contactInformation("bob.johnson@example.com")
                .build();
    }

    public static PatronDto createTestPatronDtoC() {
        return PatronDto.builder()
                .id(3L)
                .name("Bob Johnson")
                .contactInformation("bob.johnson@example.com")
                .build();
    }
}
