package com.lambdaschool.schools;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import com.lambdaschool.schools.models.slip;

import java.util.Collections;

/**
 * Main class to start the application.
 */
@EnableJpaAuditing
@SpringBootApplication
public class SchoolsApplication
{

    /**
     * Main method to start the application.
     *
     * @param args Not used in this application.
     */
    public static void main(String[] args)
    {

        SpringApplication.run(SchoolsApplication.class,
            args);
    }

}
