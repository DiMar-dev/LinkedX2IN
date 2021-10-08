package com.linked_x2_in.demo.Controller;

import com.linked_x2_in.demo.DTOs.Education;
import com.linked_x2_in.demo.DTOs.LinkedUser;
import com.linked_x2_in.demo.Service.core.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/users")
public class LinkedUserController {

    UserService userService;

    public LinkedUserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/add")
    public LinkedUser insertUser(@RequestBody LinkedUser linkedUser) throws ParseException {
        return this.userService.insertUser(linkedUser);
    }

    @GetMapping("/all")
    public List<LinkedUser> findAll(){
        return userService.findAll();
    }

    @PostMapping("/connect")
    public List<LinkedUser> connect(@RequestParam String userURI, @RequestParam String connectionURI){
        return this.userService.connect(userURI,connectionURI);
    }


    @GetMapping("/connections")
    public List<LinkedUser> allConnections(@RequestParam String userURI){
        return this.userService.findAllConnections(userURI);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Education> delete(@RequestBody LinkedUser linkedUser){
        this.userService.deleteUser(linkedUser);
        return ResponseEntity.ok().build();
    }



}
