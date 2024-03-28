package ru.grak.mediasofttask.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import ru.grak.mediasofttask.validation.ValidationMarker;

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

    @NotBlank
    private String description;

    @NotBlank
    private String category;

    @NotNull
    @Positive
    private BigDecimal price;

    @NotNull
    @PositiveOrZero
    private Integer quantity;

    @Null(groups = ValidationMarker.OnCreate.class)
    @NotNull(groups = ValidationMarker.OnUpdate.class)
    private LocalDateTime lastQuantityChangeDateTime;

    @Null(groups = ValidationMarker.OnCreate.class)
    @NotNull(groups = ValidationMarker.OnUpdate.class)
    private LocalDateTime creationDateTime;

}
