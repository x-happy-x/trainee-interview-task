package ru.interview.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.*;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name is mandatory")
    @Size(max = 255, message = "Product name must be less than 255 characters")
    private String name;

    @Size(max = 4096, message = "Description must be less than 4096 characters")
    private String description;

    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private Double price;

    private Boolean inStock;
}