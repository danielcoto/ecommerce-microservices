package com.accenture;

import com.accenture.entity.Product;
import com.accenture.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the different methods to process the requested actions
 */
@Component
public class CatalogueService {

    private final ProductRepository productRepository;

    /**
     * Default constructor to allow injecting the repository class as dependency.
     * @param productRepository the repository for the products.
     */
    @Autowired
    public CatalogueService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Retrieves all product categories
     * @return a list of the different product categories
     */
    public List<String> retrieveAllCategoriesNames() {

        List<String> categoriesNames = new ArrayList<>();

        for (Product product : productRepository.findAll()) {
            if (!categoriesNames.contains(product.getProductCategory()) && product.getProductCategory() != null)
                categoriesNames.add(product.getProductCategory());
        }
        return categoriesNames;
    }

    /**
     * Retrieves the products which have the same category
     * @param categoryName the category to be listed
     * @return a list with all products belonging to the requested category
     */
    public List<Product> retrieveProductsByCategory(String categoryName) {
        return productRepository.findAllByProductCategory(categoryName);
    }

    /**
     * Retrieves the products which have the same color
     * @param productColor the color to be listed
     * @return a list with all the products with the requested color
     */
    public List<Product> retrieveProductsByColor(String productColor) {
        return productRepository.findAllByProductColor(productColor);
    }

    /**
     * Retrieves all products
     * @return a list with all products stored
     */
    public List<Product> retrieveAllProducts() {
        return productRepository.findAll();
    }


    /**
     * Deletes all products from the repository
     */
    public void deleteAllProducts() {
        productRepository.deleteAll();
    }

    /**
     * Retrieve a product
     * @param productId the id of the product to be returned
     * @return the product with the provided id
     */
    public Product retrieveProductById (Integer productId) {
        return productRepository.findOne(productId);
    }

    /**
     * Stores a new product in the repository
     * @param newProduct the new product to be stored
     * @return the product stored
     */
    public Product saveProduct(Product newProduct) {
        return productRepository.save(newProduct);
    }

    /**
     * Updates a product
     * @param productId the id of the product to be updated
     * @param productUpdate the content to be changed in the product
     */
    public void updateProduct(Integer productId, Product productUpdate) {

        Product product = productRepository.findOne(productId);

        product.setProductId(product.getProductId());
        product.setProductName(productUpdate.getProductName());
        product.setProductCategory(productUpdate.getProductCategory());
        product.setProductPrice(productUpdate.getProductPrice());
        product.setProductColor(productUpdate.getProductColor());

        productRepository.save(product);
    }

    /**
     * Deletes a product
     * @param productId the id of the product to be deleted
     */
    public void deleteProduct(Integer productId) {
        productRepository.delete(productId);
    }

    /**
     * Verifies whether a product exists
     * @param productId the id of the product to be checked
     * @return true if product is stored; false if not
     */
    public boolean checkProductExist(Integer productId) {

        if (productRepository.findOne(productId) == null)
            return false;
        return true;
    }


}