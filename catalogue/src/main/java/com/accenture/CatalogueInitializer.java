package com.accenture;

import com.accenture.entity.Product;
import com.accenture.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CatalogueInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(CatalogueInitializer.class);

    private final ProductRepository productRepository;

    @Autowired
    public CatalogueInitializer (ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... strings) throws Exception {

        //---------------------------- Repository initial content ----------------------------------------

        productRepository.save(new Product("Nike Trainers", "trainers",
                new BigDecimal(1.0), "red"));
        productRepository.save(new Product("Adidas Trainers", "trainers",
                new BigDecimal(2.0), "blue"));
        productRepository.save(new Product("NB Trainers", "trainers",
                new BigDecimal(3.0), "green"));
        productRepository.save(new Product("Reebook Trainers", "trainers",
                new BigDecimal(4.0), "yellow"));
        productRepository.save(new Product("Puma shirt", "shirts",
                new BigDecimal(5.0), "orange"));
    }
}