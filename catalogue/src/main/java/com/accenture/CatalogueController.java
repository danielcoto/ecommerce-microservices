package com.accenture;

import com.accenture.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Handles the different web requests to routes starting with /catalogue.
 */
@RestController
@RequestMapping("/catalogue")
public class CatalogueController {

    private final CatalogueService catalogueService;

    /**
     * Default constructor to allow injecting the service class as dependency.
     * @param catalogueService dependency class.
     */
    @Autowired
    public CatalogueController(CatalogueService catalogueService) {
        this.catalogueService = catalogueService;
    }

    /**
     * Maps the resource's route (/catalogue/categories) to the getAllCategories() method.
     * Gets all categories by calling the appropiate function in service class.
     * @return ResponseEntity with all categories.
     */
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        List<String> allCategories = catalogueService.retrieveAllCategoriesNames();
        if (allCategories.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(allCategories, HttpStatus.OK);
    }

    /**
     * Maps the resource's route (/catalogue/category/name={categoryName}) to the getProductsByCategory() method.
     * Gets the products with same category by calling the appropiate function in service class.
     * @param categoryName the category of the products to be listed.
     * @return ResponseEntity with products belonging to the requested category.
     */
    @GetMapping("/categories/category={categoryName}")
    public ResponseEntity<List<Product>> getProductsByrCategory(@PathVariable String categoryName) {
        List<Product> productsOfCategory = catalogueService.retrieveProductsByCategory(categoryName);
        if (productsOfCategory.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(productsOfCategory, HttpStatus.OK);
    }

    /**
     * Maps the resource's route (/catalogue/products/color={productColor}) to the getProductsByColor() method.
     * Gets the products with same color by calling the appropiate function in service class.
     * @param productColor the color of the products to be listed.
     * @return ResponseEntity with products which have the requested color.
     */
    @GetMapping("/products/color={productColor}")
    public ResponseEntity<List<Product>> getProductsByColor(@PathVariable String productColor) {
        List<Product> productsOfColor = catalogueService.retrieveProductsByColor(productColor);
        if (productsOfColor.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(productsOfColor, HttpStatus.OK);
    }

    /**
     * Maps the resource's route (/catalogue/products) to the getAllProducts() method.
     * Gets all products by calling the appropiate function in service class.
     * @return ResponseEntity with all products.
     */
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {

        List<Product> allProducts = catalogueService.retrieveAllProducts();

        if (allProducts.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(allProducts, HttpStatus.OK);
    }

    /**
     * Maps the resource's route (/catalogue/products) to the removeAllProducts() method.
     * Deletes all products by calling the appropiate function in service class.
     * @return ResponseEntity with feedback message.
     */
    @DeleteMapping("/products")
    public ResponseEntity<String> removeAllProducts() {
        if (catalogueService.retrieveAllProducts().isEmpty())
            return new ResponseEntity<>("Catalogue is empty.", HttpStatus.OK);

        catalogueService.deleteAllProducts();
        return new ResponseEntity<>("Catalogue has been removed.", HttpStatus.OK);
    }

    /**
     * Maps the resource's route (/catalogue/products/id={productId}) to the getProductById() method.
     * Gets one product by its id by calling the appropiate function in service class.
     * @param productId the id of the requested product.
     * @return ResponseEntity with the requested product.
     */
    @GetMapping("/products/id={productId}")
    public ResponseEntity<?> getProductById(@PathVariable Integer productId) {
        Product product = catalogueService.retrieveProductById(productId);
        if (product == null)
            return new ResponseEntity<>("Product with ID: " + productId + " does not exist.", HttpStatus.OK);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    /**
     * Maps the resource's route (/catalogue/products) to the addProduct() method.
     * Inserts new product by calling the appropiate function in service class.
     * @param newProduct the new product to be stored.
     * @return ResponseEntity with the new order and its location in the right header.
     */
    @PostMapping("/products")
    public ResponseEntity<Void> addProduct(@RequestBody Product newProduct) {
        Product product = catalogueService.saveProduct(newProduct);
        if (product == null)
            return ResponseEntity.noContent().build();
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/id={productId}")
                .buildAndExpand(product.getProductId()).toUri();
        return ResponseEntity.created(location).build();
    }

    /**
     * Maps the resource's route (/catalogue/products/id={productId}) to the editProduct() method.
     * Updates one product by calling the appropiate function in service class.
     * @param productId the id of the product to be updated.
     * @param productUpdate the content to be changed in the product.
     * @return ResponseEntity with feedback message.
     */
    @PutMapping("/products/id={productId}")
    public ResponseEntity<?> editProduct(@PathVariable("productId") Integer productId, @RequestBody Product productUpdate) {

        if (!catalogueService.checkProductExist(productId))
            return new ResponseEntity<>("Product requested does not exist", HttpStatus.NOT_FOUND);

        catalogueService.updateProduct(productId, productUpdate);
        return new ResponseEntity<>("Product has been updated successfully.", HttpStatus.OK);
    }

    /**
     * Maps the resource's route (/catalogue/products/id={productId}) to the removeProduct() method.
     * Deletes one product by calling the appropiate function in service class.
     * @param productId the id of the product to be removed.
     * @return ResponseEntity with feedback message.
     */
    @DeleteMapping("/products/id={productId}")
    public ResponseEntity<String> removeProduct(@PathVariable Integer productId) {
        catalogueService.deleteProduct(productId);
        return new ResponseEntity<>("The product with ID: " + productId +  " has been removed.", HttpStatus.OK);
    }
}