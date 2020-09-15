package com.lambdaschool.schools.controllers;


import com.lambdaschool.schools.models.Instructor;
import com.lambdaschool.schools.services.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/instructors")
public class InstructorController {

    @Autowired
    InstructorService instructorService;

    // http://localhost:2019/instructors/instructor/3/advice
    // /instructors/instructor/{instructorid}/advice
    @GetMapping(value = "/instructor/{instructorid}/advice")
    public ResponseEntity<?> getInstructorAdvice(@PathVariable long instructorid)
    {
        Instructor instructor = instructorService.addAdvice(instructorid);
        return new ResponseEntity<>(instructor, HttpStatus.OK);
    }
}
