package com.library.management.services;

import com.library.management.TestDataUtil;
import com.library.management.mappers.Mapper;
import com.library.management.model.dto.PatronDto;
import com.library.management.model.entities.PatronEntity;
import com.library.management.repositories.PatronRepository;
import com.library.management.services.impl.PatronServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class PatronServiceImplTests {

    @Mock
    private PatronRepository patronRepository;

    @Mock
    private Mapper<PatronEntity, PatronDto> patronMapper;

    @InjectMocks
    private PatronServiceImpl patronService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSavePatron() {
        // Arrange
        PatronDto inputPatronDto = TestDataUtil.createTestPatronDtoA();
        PatronEntity savedPatronEntity = TestDataUtil.createTestPatronEntityA();
        Mockito.when(patronMapper.mapEntityFromDto(inputPatronDto)).thenReturn(savedPatronEntity);
        Mockito.when(patronRepository.save(savedPatronEntity)).thenReturn(savedPatronEntity);
        Mockito.when(patronMapper.mapEntityToDto(savedPatronEntity)).thenReturn(inputPatronDto);

        // Act
        PatronDto result = patronService.save(inputPatronDto);

        // Assert
        Assertions.assertEquals(inputPatronDto, result);
        Mockito.verify(patronRepository, Mockito.times(1)).save(savedPatronEntity);
    }

    @Test
    public void testFindAllPatrons() {
        // Arrange
        List<PatronEntity> patronEntities = Arrays.asList(
                TestDataUtil.createTestPatronEntityA(),
                TestDataUtil.createTestPatronEntityB()
        );
        List<PatronDto> expectedPatronDtos = Arrays.asList(
                TestDataUtil.createTestPatronDtoA(),
                TestDataUtil.createTestPatronDtoB()
        );
        Mockito.when(patronRepository.findAll()).thenReturn(patronEntities);
        Mockito.when(patronMapper.mapEntityToDto(Mockito.any(PatronEntity.class)))
                .thenReturn(expectedPatronDtos.get(0), expectedPatronDtos.get(1));

        // Act
        List<PatronDto> result = patronService.findAll();

        // Assert
        Assertions.assertEquals(expectedPatronDtos, result);
        Mockito.verify(patronRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void testFindOnePatronById() {
        // Arrange
        Long patronId = 1L;
        PatronEntity patronEntity = TestDataUtil.createTestPatronEntityA();
        PatronDto expectedPatronDto = TestDataUtil.createTestPatronDtoA();
        Mockito.when(patronRepository.findById(patronId)).thenReturn(Optional.of(patronEntity));
        Mockito.when(patronMapper.mapEntityToDto(patronEntity)).thenReturn(expectedPatronDto);

        // Act
        Optional<PatronDto> result = patronService.findOne(patronId);

        // Assert
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(expectedPatronDto, result.get());
        Mockito.verify(patronRepository, Mockito.times(1)).findById(patronId);
    }

    @Test
    public void testFindOnePatronByIdNotFound() {
        // Arrange
        Long patronId = 1L;
        Mockito.when(patronRepository.findById(patronId)).thenReturn(Optional.empty());

        // Act
        Optional<PatronDto> result = patronService.findOne(patronId);

        // Assert
        Assertions.assertTrue(result.isEmpty());
        Mockito.verify(patronRepository, Mockito.times(1)).findById(patronId);
    }

    @Test
    public void testIsPatronExists() {
        // Arrange
        Long patronId = 1L;
        Mockito.when(patronRepository.existsById(patronId)).thenReturn(true);

        // Act
        boolean result = patronService.isExists(patronId);

        // Assert
        Assertions.assertTrue(result);
        Mockito.verify(patronRepository, Mockito.times(1)).existsById(patronId);
    }

    @Test
    public void testIsPatronNotExists() {
        // Arrange
        Long patronId = 1L;
        Mockito.when(patronRepository.existsById(patronId)).thenReturn(false);

        // Act
        boolean result = patronService.isExists(patronId);

        // Assert
        Assertions.assertFalse(result);
        Mockito.verify(patronRepository, Mockito.times(1)).existsById(patronId);
    }

    @Test
    public void testUpdatePatron() {
        // Arrange
        Long patronId = 1L;
        PatronDto updatedPatronDto = TestDataUtil.createTestPatronDtoB();
        PatronEntity existingPatronEntity = TestDataUtil.createTestPatronEntityA();
        PatronEntity updatedPatronEntity = TestDataUtil.createTestPatronEntityB();

        Mockito.when(patronRepository.existsById(patronId)).thenReturn(true);
        Mockito.when(patronMapper.mapEntityFromDto(updatedPatronDto)).thenReturn(updatedPatronEntity);
        Mockito.when(patronRepository.save(updatedPatronEntity)).thenReturn(updatedPatronEntity);
        Mockito.when(patronMapper.mapEntityToDto(updatedPatronEntity)).thenReturn(updatedPatronDto);

        // Act
        PatronDto result = patronService.updatePatron(patronId, updatedPatronDto);

        // Assert
        Assertions.assertEquals(updatedPatronDto, result);
        Mockito.verify(patronRepository, Mockito.times(1)).existsById(patronId);
        Mockito.verify(patronRepository, Mockito.times(1)).save(updatedPatronEntity);
    }

    @Test
    public void testUpdatePatronNotFound() {
        // Arrange
        Long patronId = 1L;
        PatronDto updatedPatronDto = TestDataUtil.createTestPatronDtoB();

        Mockito.when(patronRepository.existsById(patronId)).thenReturn(false);

        // Act & Assert
        Assertions.assertThrows(RuntimeException.class, () -> {
            patronService.updatePatron(patronId, updatedPatronDto);
        });

        Mockito.verify(patronRepository, Mockito.times(1)).existsById(patronId);
        Mockito.verify(patronRepository, Mockito.never()).save(Mockito.any(PatronEntity.class));
    }

    @Test
    public void testDeletePatron() {
        // Arrange
        Long patronId = 1L;
        Mockito.when(patronRepository.existsById(patronId)).thenReturn(true);

        // Act
        patronService.delete(patronId);

        // Assert
        Mockito.verify(patronRepository, Mockito.times(1)).deleteById(patronId);
    }

    @Test
    public void testDeletePatronNotFound() {
        // Arrange
        Long patronId = 1L;
        Mockito.when(patronRepository.existsById(patronId)).thenReturn(false);

        // Act & Assert
        Assertions.assertThrows(RuntimeException.class, () -> {
            patronService.delete(patronId);
        });

        Mockito.verify(patronRepository, Mockito.times(1)).existsById(patronId);
        Mockito.verify(patronRepository, Mockito.never()).deleteById(Mockito.anyLong());
    }

    @Test
    public void testDeleteAllPatrons() {
        // Act
        patronService.deleteAll();

        // Assert
        Mockito.verify(patronRepository, Mockito.times(1)).deleteAll();
    }
}
