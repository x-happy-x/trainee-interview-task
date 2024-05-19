package ru.interview.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.interview.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
