package com.linked_x2_in.demo.Controller;

import com.linked_x2_in.demo.DTOs.CV;
import com.linked_x2_in.demo.DTOs.Course;
import com.linked_x2_in.demo.DTOs.Education;
import com.linked_x2_in.demo.DTOs.Experience;
import com.linked_x2_in.demo.Service.CourseService;
import com.linked_x2_in.demo.Service.EducationService;
import com.linked_x2_in.demo.Service.ExperienceService;
import com.linked_x2_in.demo.Service.SkillService;
import com.linked_x2_in.demo.Service.core.CVService;
import com.linked_x2_in.demo.Service.core.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/cv")
@AllArgsConstructor
public class CVController {

    CVService cvService;
    UserService userService;
    EducationService educationService;
    ExperienceService experienceService;
    CourseService courseService;
    SkillService skillService;


    @GetMapping()
    public ResponseEntity<CV> findCV(@RequestParam String cvURI){
        return cvService.getCV(cvURI)
                .map(cv -> ResponseEntity.ok().body(cv))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<CV> add(@RequestParam String userURI){
        return cvService.create(userURI)
                .map(cv -> ResponseEntity.ok().body(cv))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<CV> delete(@RequestParam String userURI){
        cvService.delete(userURI);
        return ResponseEntity.ok().build();
    }



    //Education

    @PostMapping("/education/add")
    public ResponseEntity<Education> addEdeucation(@RequestParam String cvURI
                                                    ,@RequestBody Education education){
        return educationService.add(cvURI,education)
                .map(cv -> ResponseEntity.ok().body(cv))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/education/delete")
    public ResponseEntity<Education> deleteEducation(@RequestParam String cvURI
                                                     ,@RequestParam String courseURI
                                                    ,@RequestBody Education education){
        educationService.delete(cvURI, courseURI, education);
        return ResponseEntity.ok().build();
    }


    //Experience
    @PostMapping("/experience/add")
    public ResponseEntity<Experience> addWork(@RequestParam String cvURI
                                            , @RequestBody Experience experience){
        return experienceService.add(cvURI,experience)
                .map(cv -> ResponseEntity.ok().body(cv))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/experience/delete")
    public ResponseEntity<Experience> deleteWork(@RequestParam String cvURI
                                                ,@RequestParam String companyURI
                                                ,@RequestBody Experience experience){
        experienceService.delete(cvURI, companyURI, experience);
        return ResponseEntity.ok().build();
    }


    //Course
    @PostMapping("/course/add")
    public ResponseEntity<Course> addCourse(@RequestParam String cvURI
                                            , @RequestBody Course course){
        return courseService.add(cvURI,course)
                .map(cv -> ResponseEntity.ok().body(cv))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/course/delete")
    public ResponseEntity<Course> deleteCourse(@RequestParam String cvURI
                                                ,@RequestParam String courseURI
                                                ,@RequestBody Course course){
        courseService.delete(cvURI, courseURI, course);
        return ResponseEntity.ok().build();
    }

    //Skills
    @PostMapping("/skill/add")
    public ResponseEntity<String> addSkill(@RequestParam String cvURI
                                            , @RequestParam String skill){
        return skillService.add(cvURI,skill)
                .map(cv -> ResponseEntity.ok().body(cv))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/skill/delete")
    public ResponseEntity<String> deleteSkill(@RequestParam String cvURI
                                                ,@RequestParam String skill){
        skillService.remove(cvURI, skill);
        return ResponseEntity.ok().build();
    }


}
