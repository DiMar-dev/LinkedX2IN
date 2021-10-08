package com.linked_x2_in.demo.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Experience {

    String companyName;
    String companyLink;
    String jobTitle;
    String startDate;
    String endDate;
}
