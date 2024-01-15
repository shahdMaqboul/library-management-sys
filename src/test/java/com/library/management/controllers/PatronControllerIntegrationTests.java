package com.library.management.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.management.TestDataUtil;
import com.library.management.model.dto.PatronDto;
import com.library.management.services.BorrowingRecordService;
import com.library.management.services.PatronService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@Transactional
public class PatronControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PatronService patronService;

    @Autowired
    private BorrowingRecordService borrowingRecordService;

    @BeforeEach
    public void setUp() {
        // Delete all records from borrowing_records and patrons
        patronService.deleteAll();
        borrowingRecordService.deleteAll();
    }

    @Test
    public void testThatGetAllPatronsReturnsHttpStatus200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/patrons")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatGetAllPatronsReturnsListOfPatrons() throws Exception {
        PatronDto testPatronDto = TestDataUtil.createTestPatronDtoA();
        patronService.save(testPatronDto);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/patrons")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value("John Doe")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].contactInformation").value("john.doe@example.com")
        );
    }

    @Test
    public void testThatGetPatronReturnsHttpStatus200WhenPatronExists() throws Exception {
        PatronDto testPatronDto = TestDataUtil.createTestPatronDtoA();
        PatronDto savedPatronDto = patronService.save(testPatronDto);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/patrons/{id}", savedPatronDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatGetPatronReturnsPatronWhenPatronExists() throws Exception {
        PatronDto testPatronDto = TestDataUtil.createTestPatronDtoA();
        PatronDto savedPatronDto = patronService.save(testPatronDto);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/patrons/{id}", savedPatronDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedPatronDto.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("John Doe")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.contactInformation").value("john.doe@example.com")
        );
    }

    @Test
    public void testThatGetPatronReturnsHttpStatus404WhenNoPatronExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/patrons/99")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatCreatePatronSuccessfullyReturnsHttp201Created() throws Exception {
        PatronDto testPatronDto = TestDataUtil.createTestPatronDtoA();
        testPatronDto.setId(null);
        String patronJson = objectMapper.writeValueAsString(testPatronDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/patrons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patronJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreatePatronSuccessfullyReturnsSavedPatron() throws Exception {
        PatronDto testPatronDto = TestDataUtil.createTestPatronDtoA();
        testPatronDto.setId(null);
        String patronJson = objectMapper.writeValueAsString(testPatronDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/patrons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patronJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("John Doe")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.contactInformation").value("john.doe@example.com")
        );
    }

    @Test
    public void testThatUpdatePatronReturnsHttpStatus404WhenNoPatronExists() throws Exception {
        PatronDto testPatronDto = TestDataUtil.createTestPatronDtoA();
        String patronDtoJson = objectMapper.writeValueAsString(testPatronDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/patrons/{id}", testPatronDto.getId() + 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patronDtoJson)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatUpdatePatronReturnsHttpStatus200WhenPatronExists() throws Exception {
        PatronDto testPatronDto = TestDataUtil.createTestPatronDtoA();
        PatronDto savedPatron = patronService.save(testPatronDto);

        String patronDtoJson = objectMapper.writeValueAsString(testPatronDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/patrons/" + savedPatron.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patronDtoJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatUpdatePatronUpdatesExistingPatron() throws Exception {
        PatronDto testPatronDto = TestDataUtil.createTestPatronDtoA();
        PatronDto savedPatron = patronService.save(testPatronDto);

        PatronDto patronDtoUpdate = TestDataUtil.createTestPatronDtoB();
        patronDtoUpdate.setId(savedPatron.getId());

        String patronDtoUpdateJson = objectMapper.writeValueAsString(patronDtoUpdate);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/patrons/" + savedPatron.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patronDtoUpdateJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedPatron.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(patronDtoUpdate.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.contactInformation").value(patronDtoUpdate.getContactInformation())
        );
    }

    @Test
    public void testThatDeletePatronReturnsHttpStatus404ForNonExistingPatron() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/patrons/999")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatDeletePatronReturnsHttpStatus204ForExistingPatron() throws Exception {
        // Arrange
        PatronDto testPatronDto = TestDataUtil.createTestPatronDtoA();
        PatronDto savedPatron = patronService.save(testPatronDto);

        // Act & Assert
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/patrons/" + savedPatron.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
