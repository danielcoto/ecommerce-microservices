package com.accenture.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * Entity class for cart items
 */
@Entity
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer cartItemId;

    private Integer cartItemProductId;
    private Integer cartItemAccountId;
    private String cartItemName;
    private BigDecimal cartItemPrice;
    private Integer cartItemQuantity = 0;

    /**
     * Cart item class constructor
     */
    public CartItem() {}

    /**
     * Cart item class constructor
     * @param cartItemProductId product id associated to the cart item
     * @param cartItemAccountId account id associated to the cart item
     * @param cartItemName product name associated to the cart item
     * @param cartItemPrice unit price associated to the cart item
     * @param cartItemQuantity quantity associated to the cart item
     */
    public CartItem(Integer cartItemProductId, Integer cartItemAccountId, String cartItemName, BigDecimal cartItemPrice, Integer cartItemQuantity) {
        this.cartItemProductId = cartItemProductId;
        this.cartItemAccountId = cartItemAccountId;
        this.cartItemName = cartItemName;
        this.cartItemPrice = cartItemPrice;
        this.cartItemQuantity = cartItemQuantity;
    }

    public Integer getCartItemProductId() {
        return cartItemProductId;
    }
    public void setCartItemProductId(Integer cartItemProductId) {
        this.cartItemProductId = cartItemProductId;
    }

    public Integer getCartItemId() {
        return cartItemId;
    }
    public void setCartItemId(Integer cartItemId) {
        this.cartItemId = cartItemId;
    }

    public Integer getCartItemAccountId() {
        return cartItemAccountId;
    }
    public void setCartItemAccountId(Integer cartItemAccountId) {
        this.cartItemAccountId = cartItemAccountId;
    }

    public String getCartItemName() {
        return cartItemName;
    }
    public void setCartItemName(String cartItemName) {
        this.cartItemName = cartItemName;
    }

    public BigDecimal getCartItemPrice() {
        return cartItemPrice;
    }
    public void setCartItemCost(BigDecimal cartItemCost) {
        this.cartItemPrice = cartItemCost;
    }

    public Integer getCartItemQuantity() {
        return cartItemQuantity;
    }
    public void setCartItemQuantity(Integer cartItemQuantity) {
        this.cartItemQuantity = cartItemQuantity;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "cartItemId=" + cartItemId +
                ", cartItemProductId=" + cartItemProductId +
                ", cartItemAccountId=" + cartItemAccountId +
                ", cartItemName='" + cartItemName + '\'' +
                ", cartItemPrice=" + cartItemPrice +
                ", cartItemQuantity=" + cartItemQuantity +
                '}';
    }
}
