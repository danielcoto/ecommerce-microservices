package com.accenture;

import com.accenture.entity.Order;
import com.accenture.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Contains the different methods to process the requested actions
 */
@Component
public class OrderService {

    private OrderRepository orderRepository;

    /**
     * Default constructor to allow injecting the repository class as dependency.
     * @param orderRepository the repository for the products.
     */
    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Retrieves the order history of one account
     * @param accountId the id of the orders owner to be returned
     * @return a list with the orders of the provided account
     */
    public List<Order> retrieveAllOrders(Integer accountId) {
        return orderRepository.findAllByAccountId(accountId);
    }

    /**
     * Deletes the order history of one account
     * @param principalId the id of the orders owner to be deleted
     */
    @Transactional
    public void deleteAllOrders(Integer principalId) {
        orderRepository.deleteAllByAccountId(principalId);
    }

    /**
     * Finds an order by its id
     * @param orderId the id of the requested order
     * @return the requested order
     */
    public Order retrieveOrderById(Integer orderId) {
        return orderRepository.findOne(orderId);
    }

    /**
     * Creates a new order
     * @param newOrder the new order to be created
     * @return the created order
     */
    public Order createOrder(Order newOrder) {
        Order order = new Order(newOrder.getAccountId(), newOrder.getAddressee(), newOrder.getAddress(),
                newOrder.getCost(), newOrder.getDate());
        orderRepository.save(order);
        return order;
    }
}