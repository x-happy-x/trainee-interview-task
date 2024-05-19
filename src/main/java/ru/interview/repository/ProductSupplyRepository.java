package ru.interview.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.interview.entity.ProductSupply;

@Repository
public interface ProductSupplyRepository extends JpaRepository<ProductSupply, Long> {


}
