package com.accenture.entity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Entity class for products
 */
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer productId;

    private String productName;
    private String productCategory;
    private BigDecimal productPrice;
    private String productColor;

    /**
     * Product class constructor
     */
    public Product() {}

    /**
     * Product class constructor
     * @param productName name of the product
     * @param productCategory category of the product
     * @param productPrice price of the product
     * @param productColor color of the product
     */
    public Product(String productName, String productCategory, BigDecimal productPrice, String productColor) {
        this.productName = productName;
        this.productCategory = productCategory;
        this.productPrice = productPrice;
        this.productColor = productColor;
    }

    public Integer getProductId() {
        return productId;
    }
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCategory() {
        return productCategory;
    }
    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }
    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductColor() {
        return productColor;
    }
    public void setProductColor(String productColor) {
        this.productColor = productColor;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productCategory='" + productCategory + '\'' +
                ", productPrice=" + productPrice +
                ", productColor='" + productColor + '\'' +
                '}';
    }
}