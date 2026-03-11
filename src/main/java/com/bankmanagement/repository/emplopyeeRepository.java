package com.bankmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bankmanagement.model.Employee;

public interface emplopyeeRepository extends JpaRepository<Employee, Integer> {
    
}
