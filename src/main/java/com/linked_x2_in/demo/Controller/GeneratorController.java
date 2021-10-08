package com.linked_x2_in.demo.Controller;


import com.linked_x2_in.demo.Service.EducationService;
import com.linked_x2_in.demo.Service.ExperienceService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
@AllArgsConstructor
public class GeneratorController {

    ExperienceService experienceService;
    EducationService educationService;


    @GetMapping("/companies")
    public List<String> getAllCompanies(){
        return experienceService.getAllCompanies();
    }

    @GetMapping("/educationals")
    public List<String> getAllEducationalInstitutions(){
        return educationService.getAllEducationalInstitutions();
    }
}
