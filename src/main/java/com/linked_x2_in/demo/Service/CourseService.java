package com.linked_x2_in.demo.Service;

import com.linked_x2_in.demo.DTOs.Course;
import com.linked_x2_in.demo.Repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Optional<Course> add(String CV_URI, Course course){
        courseRepository.addCourse(CV_URI, course);

        return courseRepository.findAllByCV(CV_URI)
                        .stream()
                        .filter(item -> item.getCourseLink()
                                    .equals(course.getCourseLink()))
                        .findFirst();
    }

    public List<Course> findAllByCV(String CV_URI){
        return courseRepository.findAllByCV(CV_URI);
    }

    public void delete(String CV_URI, String courseURI, Course course){
        courseRepository.deleteCourse(CV_URI, courseURI, course);
    }
}
