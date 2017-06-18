package com.accenture.repository;

import com.accenture.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository class for orders
 */
public interface OrderRepository extends JpaRepository<Order, Integer> {

    /**
     * Finds all orders associated with an account
     * @param accountId accound id associated to the orders
     * @return a list of the orders made by the provided account
     */
    List<Order> findAllByAccountId (@Param("accountId") Integer accountId);

    /**
     * Deletes all orders made for an account
     * @param accountId account id associated to the orders
     */
    void deleteAllByAccountId(@Param("accountId") Integer accountId);
}
