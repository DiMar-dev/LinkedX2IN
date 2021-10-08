package com.linked_x2_in.demo.Service;

import com.linked_x2_in.demo.DTOs.Education;
import com.linked_x2_in.demo.Repository.EducationRepository;
import org.apache.jena.shared.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EducationService {

    EducationRepository educationRepository;

    public EducationService(EducationRepository educationRepository) {
        this.educationRepository = educationRepository;
    }

    public Optional<Education> add(String CV_URI, Education education){
        educationRepository.addEducation(CV_URI, education);

        return educationRepository.findAllByCV(CV_URI)
                .stream()
                .filter(item -> item.getSchoolLink()
                        .equals(education.getSchoolLink()))
                .findFirst();
    }

    public List<Education> findAllByCV(String CV_URI){
        return educationRepository.findAllByCV(CV_URI);
    }

    public void delete(String CV_URI, String courseURI, Education education){
        educationRepository.delete(CV_URI, courseURI, education);
    }

    public List<String> getAllEducationalInstitutions(){
        return educationRepository.getAllEducationalInstitutions();
    }
}
