package com.bosch.soap.webservices.courses;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.bosch.soap.webservices.courses.CourseDetails;
import com.bosch.soap.webservices.courses.DeleteCourseDetailsRequest;
import com.bosch.soap.webservices.courses.DeleteCourseDetailsResponse;
import com.bosch.soap.webservices.courses.GetAllCourseDetailsRequest;
import com.bosch.soap.webservices.courses.GetAllCourseDetailsResponse;
import com.bosch.soap.webservices.courses.GetCourseDetailsRequest;
import com.bosch.soap.webservices.courses.GetCourseDetailsResponse;
import com.bosch.soap.webservices.courses.Status;
import com.bosch.soap.webservices.courses.bean.Course;
import com.bosch.soap.webservices.courses.exception.CourseNotFoundException;
import com.bosch.soap.webservices.courses.service.CourseDetailsService;

@Endpoint
public class CourseDetailsEndpoint {

    @Autowired
    CourseDetailsService service;

    @PayloadRoot(namespace = "http://bosch.com/courses", localPart = "GetCourseDetailsRequest")
    @ResponsePayload
    public GetCourseDetailsResponse processCourseDetailsrequest(@RequestPayload GetCourseDetailsRequest request) {
        Course course = service.findById(request.getId());

        if(course==null){
            throw new CourseNotFoundException("Invalid course id : "+request.getId());
        }

        return mapCourseDetails(course);
    }

    @PayloadRoot(namespace = "http://bosch.com/courses", localPart = "GetAllCourseDetailsRequest")
    @ResponsePayload
    public GetAllCourseDetailsResponse processAllCourseDetailsrequest(@RequestPayload GetAllCourseDetailsRequest request) {
        List<Course> courses = service.findAll();
        return mapAllCourseDetails(courses);
    }

    @PayloadRoot(namespace = "http://bosch.com/courses", localPart = "DeleteCourseDetailsRequest")
    @ResponsePayload
    public DeleteCourseDetailsResponse processCourseDetailsrequest(@RequestPayload DeleteCourseDetailsRequest request) {
        CourseDetailsService.Status status = service.deleteById(request.getId());
        DeleteCourseDetailsResponse response = new DeleteCourseDetailsResponse();
        response.setStatus(mapStatus(status));
        return response;
    }

    private Status mapStatus(CourseDetailsService.Status status) {
        if (status == CourseDetailsService.Status.FILAURE) {
            return Status.FAILURE;
        }
        return Status.SUCCESS;
    }

    private GetCourseDetailsResponse mapCourseDetails(Course course) {
        GetCourseDetailsResponse response = new GetCourseDetailsResponse();
        response.setCourseDetails(mapCourse(course));
        return response;
    }

    private GetAllCourseDetailsResponse mapAllCourseDetails(List<Course> courses) {
        GetAllCourseDetailsResponse response = new GetAllCourseDetailsResponse();
        for (Course course : courses) {
            CourseDetails courseDetails = mapCourse(course);
            response.getCourseDetails().add(courseDetails);
        }
        return response;
    }


    private CourseDetails mapCourse(Course course) {
        CourseDetails courseDetails = new CourseDetails();
        courseDetails.setId(course.getId());
        courseDetails.setName(course.getName());
        return courseDetails;
    }


}
