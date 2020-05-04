package com.mylibrary.rental;


import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.hateoas.config.EnableHypermediaSupport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Main starting point of the application.
 * SpringBoot and SpringCloud features are enabled through annotations on {@link MyLibraryRentalApplication} class.
 *
 * @author Bharadwaj Adepu
 * @see SpringBootApplication
 * @see EnableSwagger2
 */
@SpringBootApplication
@EnableSwagger2
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class MyLibraryRentalApplication {

	  @Autowired
	  private ObjectMapper objectMapper;
	  
    /**
     * Main starting point of the microservice.
     *
     * @param args an array of arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(MyLibraryRentalApplication.class, args);
    }
    
    @PostConstruct
    public void setUp() {
      objectMapper.registerModule(new JavaTimeModule());
    }
}
