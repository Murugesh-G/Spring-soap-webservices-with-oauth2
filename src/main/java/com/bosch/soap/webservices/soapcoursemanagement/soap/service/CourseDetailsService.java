package com.bosch.soap.webservices.soapcoursemanagement.soap.service;

import static com.bosch.soap.webservices.soapcoursemanagement.soap.service.CourseDetailsService.Status.SUCCESS;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.bosch.soap.webservices.soapcoursemanagement.soap.bean.Course;

@Component
public class CourseDetailsService {

    public enum Status {
        SUCCESS, FILAURE;
    }

    private static List<Course> courses = new ArrayList<>();

    static {
        courses.add(new Course(1, "Course1 name", "Course1 description"));
        courses.add(new Course(2, "Course2 name", "Course2 description"));
        courses.add(new Course(3, "Course3 name", "Course3 description"));
        courses.add(new Course(4, "Course4 name", "Course4 description"));
        courses.add(new Course(5, "Course5 name", "Course5 description"));
    }

    public Course findById(int id) {
        for (Course course : courses) {
            if (course.getId() == id) {
                return course;
            }
        }
        return null;
    }

    public List findAll() {
        return courses;
    }

    public Status deleteById(int id) {
        Iterator<Course> iterator = courses.iterator();
        while (iterator.hasNext()) {
            Course course = iterator.next();
            if (course.getId() == id) {
                iterator.remove();
                return Status.SUCCESS;
            }
        }
        return Status.FILAURE;
    }


}
