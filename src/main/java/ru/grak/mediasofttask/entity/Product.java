package ru.grak.mediasofttask.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    private String name;

    @NotBlank
    @Column(unique = true)
    private String article;

    private String description;

    @NotBlank
    private String category;

    @NotNull
    private BigDecimal price;

    @NotNull
    private Integer quantity;

    private LocalDateTime lastQuantityChangeDateTime;

    @NotNull
    private LocalDateTime creationDateTime;

}
