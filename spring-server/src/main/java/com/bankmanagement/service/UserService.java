package com.bankmanagement.service;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bankmanagement.model.User;
// import com.bankmanagement.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
@Service
public class UserService {

    // @Autowired
    // private UserRepository userRepository;

    public List<User> getAllUsers() {
        // return userRepository.findAll();
        List<User> users = new ArrayList<>();
        return users;
    } 
}