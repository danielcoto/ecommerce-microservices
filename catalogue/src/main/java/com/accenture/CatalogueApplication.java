package com.accenture;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Default Spring Boot application for passing arguments to the application,
 * It captures all arguments and components and creates Spring beans to put them
 * in the application context.
 *
 * It also enables the DiscoveryClient to allow Consul service discovery.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class CatalogueApplication {

	/**
	 * Run the application using Spring Boot’s SpringApplication.run().
	 * Let Spring Boot decide the exit code and use its value for System.exit().
	 * @param args: the command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(CatalogueApplication.class, args);
	}
}
