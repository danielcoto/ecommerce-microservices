package com.accenture;

import com.accenture.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class OrderInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(OrderInitializer.class);

    private OrderRepository orderRepository;

    @Autowired
    public OrderInitializer(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void run(String... strings) throws Exception {

        //--------------------- Account repository initial content ----------------------------------------

        //orderRepository.save(new Order(1,"Luis Ballestin", "Calle 1", new BigDecimal(2.0)));
    }
}