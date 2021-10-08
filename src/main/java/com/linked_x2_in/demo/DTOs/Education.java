package com.linked_x2_in.demo.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class Education {

    String schoolName;
    //dbo Resource
    String schoolLink;
    String start;
    String graduate;
}
