package com.accenture.repository;

import com.accenture.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository class for cart items
 */
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    /**
     * Finds all cart items associated to a particular account
     * @param cartItemAccountId account id owner of the cart items
     * @return a list of cart items linked to the provided account id
     */
    List<CartItem> findAllByCartItemAccountId(@Param("cartItemAccountId") Integer cartItemAccountId);

    /**
     * Finds one cart item with a specific product id
     * @param cartItemProductId product id of the cart item
     * @return the cart item with the provided product id
     */
    CartItem findCartItemByCartItemProductId (@Param("cartItemProductId") Integer cartItemProductId);

    /**
     * Deletes the cart items associated with an account
     * @param cartItemAccountId account id owner of the cart items
     */
    void deleteAllByCartItemAccountId(@Param("cartItemAccountId") Integer cartItemAccountId);
}