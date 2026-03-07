package com.bankmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bankmanagement.model.Customer;
import com.bankmanagement.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository userRepository;

    public List<Customer> getAllCustomers() {
        System.out.println(userRepository.findAll());
        return userRepository.findAll();
    }

    public Customer getCustomerById(Integer id) {
        Optional<Customer> res = userRepository.findById(id); // store result in a variable

        if (res.isEmpty()) { // check if user exists
            throw new RuntimeException("Customer not found with id: " + id);
        }

        System.out.println(res + "\n" + res.get());

        Customer user = res.get(); // extract Customer from Optional

        // you can do more logic here if needed
        // e.g., check if user is active, log something, etc.

        return user;
    }
}