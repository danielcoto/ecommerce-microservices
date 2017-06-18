package com.accenture;

import com.accenture.entity.CartItem;
import com.accenture.repository.CartItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CartInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(CartInitializer.class);

    private CartItemRepository cartItemRepository;

    @Autowired
    public CartInitializer(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public void run(String... strings) throws Exception {

        //--------------------- Repository initial content ----------------------------------------

        cartItemRepository.save(new CartItem(1,1, "Nike Trainers",
                new BigDecimal(1.0), 3));
        cartItemRepository.save(new CartItem(2, 2, "Adidas Trainers",
                new BigDecimal(2.0), 1));
        cartItemRepository.save(new CartItem(5, 3, "Puma shirt",
                new BigDecimal(5.0), 4));
    }
}