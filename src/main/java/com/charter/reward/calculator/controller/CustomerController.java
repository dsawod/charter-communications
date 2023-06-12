package com.charter.reward.calculator.controller;

import com.charter.reward.calculator.entity.Customer;
import com.charter.reward.calculator.entity.Purchase;
import com.charter.reward.calculator.repository.CustomerRepository;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping
    public List<Customer> createCustomers(@RequestBody List<Customer> customers) {
        return this.customerRepository.saveAll(customers);
    }

    @PostMapping("/{customerId}/purchases")
    public ResponseEntity<Customer> createPurchases(@PathVariable Long customerId, @RequestBody List<Purchase> purchases) {
        log.info("Saving purchases for customer={} ", customerId);
        Customer customer = this.customerRepository.findById(customerId).get();
        purchases.forEach(customer::addToPurchases);

        return new ResponseEntity<>(this.customerRepository.save(customer), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        return this.customerRepository.findAll();
    }
}
