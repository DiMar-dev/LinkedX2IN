package com.linked_x2_in.demo.DTOs;

import com.linked_x2_in.demo.Repository.ConfStrings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class CV {

    LinkedUser person;
    List<Education> educationHistory;
    List<Experience> experienceHistory;
    List<Course> coursesHistory;
    List<String> skills;

}
