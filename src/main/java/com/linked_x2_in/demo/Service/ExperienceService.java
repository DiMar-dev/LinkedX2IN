package com.linked_x2_in.demo.Service;

import com.linked_x2_in.demo.DTOs.Experience;
import com.linked_x2_in.demo.Repository.ExperienceRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ExperienceService {

    ExperienceRepository experienceRepository;

    public ExperienceService(ExperienceRepository experienceRepository) {
        this.experienceRepository = experienceRepository;
    }

    public Optional<Experience> add(String CV_URI, Experience experience){
        experienceRepository.addExperience(CV_URI, experience);

        return experienceRepository.findAllByCV(CV_URI)
                .stream()
                .filter(item -> item.getCompanyLink()
                        .equals(experience.getCompanyLink()))
                .findFirst();
    }

    public List<Experience> findAllByCV(String CV_URI){
        return experienceRepository.findAllByCV(CV_URI);
    }

    public void delete(String CV_URI, String companyURI, Experience experience){
        experienceRepository.delete(CV_URI, companyURI, experience);
    }

    public List<String> getAllCompanies(){
        return experienceRepository.getAllCompanies();
    }
}
