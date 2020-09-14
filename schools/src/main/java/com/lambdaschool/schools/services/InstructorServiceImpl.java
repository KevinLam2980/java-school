package com.lambdaschool.schools.services;

import com.lambdaschool.schools.exceptions.ResourceNotFoundException;
import com.lambdaschool.schools.models.Instructor;
import com.lambdaschool.schools.repositories.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "instructorService")
public class InstructorServiceImpl implements InstructorService{

    @Autowired
    InstructorRepository instructorRepo;

    @Override
    public Instructor addAdvice(long instructorid) {
        Instructor instructor = instructorRepo.findById(instructorid)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor " + instructorid + " not found!"));

        return instructor;
    }
}
