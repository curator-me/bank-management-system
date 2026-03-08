package com.bankmanagement.debug;

import com.bankmanagement.model.Customer;

public class customerDebug {
    private Customer customer;
    
    public customerDebug(Customer customer) {
        this.customer = customer;
    }

    public void printCustomerDetails() {
        System.out.println("Debug start:");
        System.out.println("Customer Details:");
        System.out.println("Name: " + customer.getName());
        System.out.println("Phone Number: " + customer.getPhoneNumber());
        System.out.println("Email: " + customer.getEmail());
        System.out.println("City: " + customer.getCity());
        System.out.println("Street: " + customer.getStreet());
        System.out.println("House No: " + customer.getHouseNo());
        System.out.println("Password: " + customer.getPassword());
        System.out.println("Debug end.");
    }
}
