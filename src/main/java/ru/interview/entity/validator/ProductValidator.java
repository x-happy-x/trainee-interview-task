package ru.interview.entity.validator;

import ru.interview.entity.Product;
import ru.interview.exception.ProductValidationException;

public class ProductValidator {
    public static void validate(Product product) throws ProductValidationException {
        validateName(product);
        validateDescription(product);
        validatePrice(product);
    }

    private static void validateName(Product product) throws ProductValidationException {
        if (product.getName() == null || product.getName().length() > 255) {
            throw new ProductValidationException("Название товара обязательно и должно быть не более 255 символов");
        }
    }

    private static void validateDescription(Product product) throws ProductValidationException {
        if (product.getDescription() != null && product.getDescription().length() > 4096) {
            throw new ProductValidationException("Описание товара должно быть не более 4096 символов");
        }
    }

    private static void validatePrice(Product product) throws ProductValidationException {
        if (product.getPrice() < 0) {
            throw new ProductValidationException("Цена товара не может быть меньше 0");
        }
    }
}
