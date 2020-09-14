package com.lambdaschool.schools.services;

import com.lambdaschool.schools.exceptions.ResourceFoundException;
import com.lambdaschool.schools.exceptions.ResourceNotFoundException;
import com.lambdaschool.schools.models.*;
import com.lambdaschool.schools.repositories.CourseRepository;
import com.lambdaschool.schools.repositories.InstructorRepository;
import com.lambdaschool.schools.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implements the CoursesService
 */
@Service(value = "coursesService")
public class CoursesServiceImpl
    implements CoursesService
{
    /**
     * Connects this service to the Course table.
     */
    @Autowired
    private CourseRepository courserepos;

    /**
     * Connects this service to the Student table.
     */
    @Autowired
    private StudentRepository studentrepos;

    /**
     * Connects this service to the Instructor table.
     */
    @Autowired
    private InstructorRepository instructorrepos;

    @Override
    public List<Course> findAll()
    {
        List<Course> list = new ArrayList<>();
        /*
         * findAll returns an iterator set.
         * iterate over the iterator set and add each element to an array list.
         */


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
        String requestURL = "http://api.adviceslip.com/advice";
        // create the responseType expected. Notice the YearFact is the data type we are expecting back from the API!
        ParameterizedTypeReference<Object> responseType = new ParameterizedTypeReference<>()
        {
        };
        // create the response entity. do the get and get back information
        ResponseEntity<Object> responseEntity = restTemplate.exchange(requestURL,
                HttpMethod.GET,
                null,
                responseType);

        // now that we have our data, let's print it to the console!
//        slip advice = responseEntity.getBody();
        System.out.println(responseEntity.getBody().toString());
//        System.out.println(advice);

        courserepos.findAll()
            .iterator()
            .forEachRemaining(list::add);
        return list;
    }

    @Override
    public Course findCourseById(long id)
    {
        return courserepos.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Course id " + id + " not found!"));
    }

    @Transactional
    @Override
    public void delete(long id)
    {
        courserepos.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Course id " + id + " not found!"));
        courserepos.deleteById(id);
    }

    @Transactional
    @Override
    public Course save(Course course)
    {
        Course newCourse = new Course();

        if (course.getCourseid() != 0)
        {
            Course oldCourse = courserepos.findById(course.getCourseid())
                .orElseThrow(() -> new ResourceNotFoundException("Course id " + course.getCourseid() + " not found!"));

            // delete the students for the old course we are replacing
            for (StudCourses ur : oldCourse.getStudents())
            {
                deleteStudentCourse(ur.getStudent()
                        .getStudentid(),
                    ur.getCourse()
                        .getCourseid());
            }
            newCourse.setCourseid(course.getCourseid());
        }

        newCourse.setCoursename(course.getCoursename());
        Instructor newInstructor = instructorrepos.findById(course.getInstructor()
            .getInstructorid())
            .orElseThrow(() -> new ResourceNotFoundException("Instructor id " + course.getInstructor()
                .getInstructorid() + " not found!"));
        newCourse.setInstructor(newInstructor);

        newCourse.getStudents()
            .clear();
        if (course.getCourseid() == 0)
        {
            for (StudCourses sc : course.getStudents())
            {
                Student newStudent = studentrepos.findById(sc.getStudent()
                    .getStudentid())
                    .orElseThrow(() -> new ResourceNotFoundException("Instructor id " + sc.getStudent()
                        .getStudentid() + " not found!"));

                newCourse.addStudent(newStudent);
            }
        } else
        {
            // add the new students for the course we are replacing
            for (StudCourses sc : course.getStudents())
            {
                addStudCourses(sc.getStudent()
                    .getStudentid(), newCourse.getCourseid());
            }
        }

        return courserepos.save(newCourse);
    }

    @Transactional
    @Override
    public void deleteStudentCourse(
        long studentid,
        long courseid)
    {
        studentrepos.findById(studentid)
            .orElseThrow(() -> new ResourceNotFoundException("Student id " + studentid + " not found!"));
        courserepos.findById(courseid)
            .orElseThrow(() -> new ResourceNotFoundException("Course id " + courseid + " not found!"));

        if (courserepos.checkStudentCourseCombo(studentid,
            courseid)
            .getCount() > 0)
        {
            courserepos.deleteStudentCourse(studentid,
                courseid);
        } else
        {
            throw new ResourceNotFoundException("Student and Course Combination Does Not Exists");
        }
    }

    @Transactional
    @Override
    public void addStudCourses(
        long studentid,
        long courseid)
    {
        studentrepos.findById(studentid)
            .orElseThrow(() -> new ResourceNotFoundException("Student id " + studentid + " not found!"));
        courserepos.findById(courseid)
            .orElseThrow(() -> new ResourceNotFoundException("Course id " + courseid + " not found!"));

        if (courserepos.checkStudentCourseCombo(studentid,
            courseid)
            .getCount() <= 0)
        {
            courserepos.insertStudCourses("SYSTEM",
                studentid,
                courseid);
        } else
        {
            throw new ResourceFoundException("Student and Course Combination Already Exists");
        }
    }

}
