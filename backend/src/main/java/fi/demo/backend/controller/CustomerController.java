package fi.demo.backend.controller;

import fi.demo.backend.entity.Customer;
import fi.demo.backend.service.CustomerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customer")
    public List<Customer> findAll() {
        return customerService.findAll();
    }

}
