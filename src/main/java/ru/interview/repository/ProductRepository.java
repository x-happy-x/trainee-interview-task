package ru.interview.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.interview.entity.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE " +
            "(LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) OR :name IS NULL) AND " +
            "(p.price >= :minPrice OR :minPrice IS NULL) AND " +
            "(p.price <= :maxPrice OR :maxPrice IS NULL) AND " +
            "(p.inStock = :inStock OR :inStock IS NULL)")
    Page<Product> findByFiltersAndSort(@Param("name") String name,
                                @Param("minPrice") Double minPrice,
                                @Param("maxPrice") Double maxPrice,
                                @Param("inStock") Boolean inStock,
                                Pageable pageable);


    @Query("SELECT p FROM Product p WHERE " +
            "(LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) OR :name IS NULL) AND " +
            "(p.price >= :minPrice OR :minPrice IS NULL) AND " +
            "(p.price <= :maxPrice OR :maxPrice IS NULL) AND " +
            "(p.inStock = :inStock OR :inStock IS NULL) ")
    List<Product> findByFilters(@Param("name") String name,
                                @Param("minPrice") Double minPrice,
                                @Param("maxPrice") Double maxPrice,
                                @Param("inStock") Boolean inStock);

}
