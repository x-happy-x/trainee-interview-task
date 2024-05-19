package ru.interview.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Data
public class ProductSupply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Document name is mandatory")
    @Size(max = 255, message = "Document name must be less than 255 characters")
    private String documentName;

    @NotNull(message = "Product is mandatory")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @Min(value = 1, message = "Quantity must be greater than 0")
    private Integer quantity;

}
