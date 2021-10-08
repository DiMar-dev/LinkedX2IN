package com.linked_x2_in.demo.Service.core;

import com.linked_x2_in.demo.DTOs.CV;
import com.linked_x2_in.demo.Repository.CVRepository;
import com.linked_x2_in.demo.Service.CourseService;
import com.linked_x2_in.demo.Service.EducationService;
import com.linked_x2_in.demo.Service.ExperienceService;
import com.linked_x2_in.demo.Service.SkillService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CVService {

    CVRepository cvRepository;
    UserService userService;
    EducationService educationService;
    ExperienceService experienceService;
    CourseService courseService;
    SkillService skillService;


    public Optional<CV> create(String userURI){
        if (cvRepository.checkIfExists(userURI))
            return getCV(userURI.concat("/CV"));

        cvRepository.createCV(userURI);
        return getCV(userURI.concat("/CV"));
    }

    public void delete(String userURI){
        cvRepository.delete(userURI);
    }

    public Optional<CV> getCV(String cvURI){

//        if (!cvRepository.checkIfExists(cvURI.replace("/CV","")))
//            return Optional.empty();

            return Optional.of(CV.builder()
                                .person(userService.getUser(cvURI.split("/")[cvURI.split("/").length - 2]))
                                .educationHistory(educationService.findAllByCV(cvURI))
                                .experienceHistory(experienceService.findAllByCV(cvURI))
                                .coursesHistory(courseService.findAllByCV(cvURI))
                                .skills(skillService.findAllByCV(cvURI))
                                .build());

    }


}
