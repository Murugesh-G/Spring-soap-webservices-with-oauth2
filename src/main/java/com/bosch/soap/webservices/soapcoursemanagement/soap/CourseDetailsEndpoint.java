package com.bosch.soap.webservices.soapcoursemanagement.soap;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.bosch.soap.webservices.soapcoursemanagement.CourseDetails;
import com.bosch.soap.webservices.soapcoursemanagement.DeleteCourseDetailsRequest;
import com.bosch.soap.webservices.soapcoursemanagement.DeleteCourseDetailsResponse;
import com.bosch.soap.webservices.soapcoursemanagement.GetAllCourseDetailsRequest;
import com.bosch.soap.webservices.soapcoursemanagement.GetAllCourseDetailsResponse;
import com.bosch.soap.webservices.soapcoursemanagement.GetCourseDetailsRequest;
import com.bosch.soap.webservices.soapcoursemanagement.GetCourseDetailsResponse;
import com.bosch.soap.webservices.soapcoursemanagement.Status;
import com.bosch.soap.webservices.soapcoursemanagement.soap.bean.Course;
import com.bosch.soap.webservices.soapcoursemanagement.soap.exception.CourseNotFoundException;
import com.bosch.soap.webservices.soapcoursemanagement.soap.service.CourseDetailsService;

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
