package com.accenture;

import com.accenture.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Handles the different web requests to routes starting with /orders.
 */
@RestController
@RequestMapping("/orders")
public class OrderController {

    private OrderService orderService;

    /**
     * Default constructor to allow injecting the service class as dependency.
     * @param orderService dependency class.
     */
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Maps the resource's route (/orders) to the getAllOrders() method.
     * Gets all orders by calling the appropiate function in service class.
     * @return ResponseEntity with all orders.
     */
    @GetMapping
    public ResponseEntity<?> getAllOrders() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer principalId = (Integer)authentication.getDetails();

        SecurityContextHolder.getContext().setAuthentication(null);

        List<Order> orders = orderService.retrieveAllOrders(principalId);

        if (orders.isEmpty())
            return new ResponseEntity<>("You have not made any order yet.", HttpStatus.OK);

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    /**
     * Maps the resource's route (/orders) to the removeAllOrders() method.
     * Deletes all orders by calling the appropiate function in service class.
     * @return ResponseEntity with feedback message.
     */
    @DeleteMapping
    public ResponseEntity<?> removeAllOrders() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer principalId = (Integer)authentication.getDetails();

        SecurityContextHolder.getContext().setAuthentication(null);

        List<Order> orders = orderService.retrieveAllOrders(principalId);

        if (orders.isEmpty())
            return new ResponseEntity<>(authentication.getName()
                    + ", your order history is already empty.", HttpStatus.OK);

        orderService.deleteAllOrders(principalId);

        return new ResponseEntity<>(authentication.getName()
                + ", your order history has been removed successfully.", HttpStatus.OK);
    }

    /**
     * Maps the resource's route (/orders) to the getOrderById() method.
     * Gets one order by its id by calling the appropiate function in service class
     * @param orderId the id of the requested order.
     * @return ResponseEntity with the requested order
     */
    @GetMapping("/id={orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable("orderId") Integer orderId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer principalId = (Integer)authentication.getDetails();

        SecurityContextHolder.getContext().setAuthentication(null);

        Order order = orderService.retrieveOrderById(orderId);

        if (order == null)
            return new ResponseEntity<>("There is no order registered with the provided id.", HttpStatus.NOT_FOUND);

        if (principalId.equals(order.getAccountId()))
            return new ResponseEntity<>(order, HttpStatus.OK);

        return new ResponseEntity<>("Order requested does not belong to account who requested it.", HttpStatus.FORBIDDEN);
    }

    /**
     * Maps the resource's route (/orders) to the createOrder() method.
     * Creates an order by calling the appropiate function in service class.
     * @param newOrder the new order to be created.
     * @return ResponseEntity with the new order.
     */
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Order newOrder) {
        Order order = orderService.createOrder(newOrder);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}