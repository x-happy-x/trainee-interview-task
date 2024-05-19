package ru.interview.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Data
public class ProductSale {

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

    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private Double purchasePrice;

}
