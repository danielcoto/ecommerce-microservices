package com.accenture.repository;

import com.accenture.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository class for products
 */
public interface ProductRepository extends JpaRepository<Product, Integer> {

    /**
     * Finds all products of one category
     * @param productCategory category of the products
     * @return a list of products with the provided category
     */
    List<Product> findAllByProductCategory(@Param("productCategory") String productCategory);

    /**
     * Finds all product of one color
     * @param productColor color of the products
     * @return a list of products with the provided color
     */
    List<Product> findAllByProductColor(@Param("productColor") String productColor);
}