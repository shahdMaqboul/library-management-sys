package com.library.management.controllers;

import com.library.management.model.dto.PatronDto;
import com.library.management.model.entities.PatronEntity;
import com.library.management.mappers.Mapper;
import com.library.management.services.PatronService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/patrons")
public class PatronController {

    private PatronService patronService;

    public PatronController(PatronService patronService) {
        this.patronService = patronService;
    }

    @GetMapping
    public List<PatronDto> getAllPatrons() {
        return patronService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatronDto> getPatronById(@PathVariable Long id) {
        Optional<PatronDto> foundPatron = patronService.findOne(id);
        return foundPatron.map(patronDto -> new ResponseEntity<>(patronDto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<PatronDto> createPatron(@Valid  @RequestBody PatronDto patronDto) {
        PatronDto savedPatronDto = patronService.save(patronDto);
        return new ResponseEntity<>(savedPatronDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatronDto> updatePatron(@PathVariable Long id, @Valid @RequestBody PatronDto patronDto) {
        if (!patronService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        patronDto.setId(id);

        PatronDto savedPatronDto = patronService.save(patronDto);
        return new ResponseEntity<>(savedPatronDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatron(@PathVariable Long id) {
        if (!patronService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        patronService.delete(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
