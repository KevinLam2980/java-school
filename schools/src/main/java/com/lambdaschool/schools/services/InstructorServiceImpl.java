package com.lambdaschool.schools.services;

import com.lambdaschool.schools.exceptions.ResourceNotFoundException;
import com.lambdaschool.schools.models.Advice;
import com.lambdaschool.schools.models.Instructor;
import com.lambdaschool.schools.models.Slip;
import com.lambdaschool.schools.repositories.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service(value = "instructorService")
public class InstructorServiceImpl implements InstructorService{

    @Autowired
    InstructorRepository instructorRepo;

    @Override
    public Instructor addAdvice(long instructorid) {
        Instructor instructor = instructorRepo.findById(instructorid)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor " + instructorid + " not found!"));

        /*
         * Creates the object that is needed to do a client side Rest API call.
         * We are the client getting data from a remote API.
         */
        RestTemplate restTemplate = new RestTemplate();

        // we need to tell our RestTemplate what format to expect
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        // a couple of common formats
//         converter.setSupportedMediaTypes(Collections.singletonList(MediaType.TEXT_HTML));
//         converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
        // or we can accept all formats! Easiest but least secure
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        restTemplate.getMessageConverters().add(converter);

        // create the url to access the API
        String requestURL = "https://api.adviceslip.com/advice";
        // create the responseType expected. Notice the YearFact is the data type we are expecting back from the API!
        ParameterizedTypeReference<Advice> responseType = new ParameterizedTypeReference<>(){};
        System.out.println(responseType);
        // create the response entity. do the get and get back information
        ResponseEntity<Advice> responseEntity = restTemplate.exchange(requestURL,
                HttpMethod.GET,
                null,
                responseType);

        // now that we have our data, let's print it to the console!
        Slip advice = responseEntity.getBody().getSlip();
        System.out.println(responseEntity);
        System.out.println(advice);

        instructor.setAdvice(advice.getAdvice());

        return instructor;
    }
}
