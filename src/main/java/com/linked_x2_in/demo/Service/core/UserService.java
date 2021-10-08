package com.linked_x2_in.demo.Service.core;

import com.apicatalog.jsonld.http.link.Link;
import com.linked_x2_in.demo.DTOs.LinkedUser;
import com.linked_x2_in.demo.Repository.LinkedUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    LinkedUserRepository linkedUserRepository;

    public UserService(LinkedUserRepository linkedUserRepository) {
        this.linkedUserRepository = linkedUserRepository;
    }

    public List<LinkedUser> findAll(){
        return linkedUserRepository.findAll();
    }

    public LinkedUser getUser(String nameSurname){
        return linkedUserRepository.getUserByNameSurname(nameSurname);
    }

    public LinkedUser insertUser(LinkedUser linkedUser){
        linkedUserRepository.addUser(linkedUser);
        return linkedUser;
    }

    public List<LinkedUser> connect(String userURI, String connectionURI){
        linkedUserRepository.connect(userURI, connectionURI);
        linkedUserRepository.connect(connectionURI, userURI);

        return this.findAllConnections(userURI);

    }

    public List<LinkedUser> findAllConnections(String userURI){
        return linkedUserRepository.findAllConnections(userURI);
    }

    public void deleteUser(LinkedUser linkedUser){
        linkedUserRepository.deleteUSer(linkedUser);
    }

}
