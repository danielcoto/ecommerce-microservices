package com.accenture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles the different web requests.
 */
@RestController
public class WelcomeController {

    private WelcomeService welcomeService;

    /**
     * Default constructor to allow injecting the service class as dependency.
     * @param welcomeService dependency class.
     */
    @Autowired
    public WelcomeController(WelcomeService welcomeService) {
        this.welcomeService = welcomeService;
    }

    /**
     * Maps the resource's routes (/, /welcome) to the welcome() method.
     * Calls the function from service class to retrieve the welcome message.
     * @return ResponseEntity with the welcome message.
     */
    @GetMapping({"/", "/welcome"})
    public ResponseEntity<String> getWelcomeMessage() {
        String message = welcomeService.retrieveWelcomeMessage();
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}

