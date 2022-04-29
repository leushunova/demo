package com.example.demo.service;

import com.example.demo.dto.Employee;
import com.example.demo.dto.Message;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.MessageRepository;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private MessageService messageService;

    public Employee create(Employee employee) {
        return employeeRepository.save(employee);
    }

    public ResponseEntity < Employee > update(Long employeeId, Employee employeeDetails) throws ResourceNotFoundException {
        Optional<Employee> findEmpoyee = employeeRepository.findById(employeeId);
        if(!findEmpoyee.isPresent()) {
            Message errorMessage = new Message("Employee", "create", "post");
            messageService.create(errorMessage);
            return (ResponseEntity<Employee>) ResponseEntity.badRequest();
        }
        Employee employee = findEmpoyee.get();
        employee.setAge(employeeDetails.getAge());
        employee.setName(employeeDetails.getName());
        employee.setContract(employeeDetails.getContract());
        final Employee updateEmployee = employeeRepository.save(employee);
        return ResponseEntity.ok(updateEmployee);
    }

    public Optional<Employee> findById(Long employeeId) {
        return employeeRepository.findById(employeeId);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
}
