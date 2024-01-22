package com.library.management.model.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "patrons")
public class PatronEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "patron_id_seq")
    private Long id;

    @NotBlank(message = "Patron name is mandatory")
    @NotNull(message = "Patron name is mandatory")
    private String name;

    @NotBlank(message = "Contact Information is mandatory")
    @NotNull(message = "Contact Information is mandatory")
    private String contactInformation;

}
