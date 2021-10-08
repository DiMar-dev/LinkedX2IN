package com.linked_x2_in.demo.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class LinkedUser {

    String URI;
    String firstName;
    String lastName;
    String gender;
    String description;
    String mail;
    String image;
    String dateOfBirth;


}
