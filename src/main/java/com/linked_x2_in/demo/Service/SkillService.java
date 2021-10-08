package com.linked_x2_in.demo.Service;

import com.linked_x2_in.demo.DTOs.Experience;
import com.linked_x2_in.demo.Repository.SkillsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SkillService {

    SkillsRepository skillsRepository;

    public SkillService(SkillsRepository skillsRepository) {
        this.skillsRepository = skillsRepository;
    }

    public Optional<String> add(String CV_URI, String skill){
        skillsRepository.addSkill(CV_URI, skill);

        return skillsRepository.findAllByCV(CV_URI)
                .stream()
                .filter(item -> item.equals(skill))
                .findFirst();
    }

    public List<String> findAllByCV(String CV_URI){
        return skillsRepository.findAllByCV(CV_URI);
    }

    public void remove(String CV_URI, String skill){
        skillsRepository.remove(CV_URI, skill);
    }
}
