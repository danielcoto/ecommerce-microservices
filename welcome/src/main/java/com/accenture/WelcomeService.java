package com.accenture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Contains the method for retrieving the welcome message
 */
@Service
public class WelcomeService {

    @Value("${welcome.message}")
    private String welcomeMessage;

    @Value("${welcome.name}")
    private String appName;

    /**
     * Retrieves the welcome message
     * @return the appropiate welcome message
     */
    public String retrieveWelcomeMessage() {
        return welcomeMessage;
    }
}