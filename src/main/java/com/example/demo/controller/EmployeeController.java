package com.example.demo.controller;

import com.example.demo.constant.ApplicationConstant;
import com.example.demo.dto.Employee;
import com.example.demo.dto.Message;
import com.example.demo.service.EmployeeService;
import com.example.demo.service.KafkaService;
import com.example.demo.service.MessageService;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private KafkaService kafkaService;

    @PostMapping("/newEmployee")
    public void createEmployee(@RequestBody Employee employee) {
        employeeService.create(employee);
        kafkaService.createNewEmployeeMessage(employee);
    }

    @PutMapping("/updateEmployee/{id}")
    public void updateEmployee(@PathVariable(value = "id") Long employeeId, @RequestBody Employee employeeDetails)
            throws ResourceNotFoundException {
        Optional<Employee> findEmployee = employeeService.findById(employeeId);
        kafkaService.searchStateMachineMessage(findEmployee, employeeDetails);
    }

    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }
}
