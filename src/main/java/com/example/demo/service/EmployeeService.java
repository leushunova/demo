package com.example.demo.service;

import com.example.demo.dto.Employee;
import com.example.demo.dto.Message;
import com.example.demo.repository.EmployeeRepository;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Employee update(Long employeeId, Employee employeeDetails) throws ResourceNotFoundException {
        Optional<Employee> findEmpoyee = employeeRepository.findById(employeeId);
        if(!findEmpoyee.isPresent()) {
            Message errorMessage = new Message("Employee", "create", "post");
            messageService.create(errorMessage);
        }
        Employee employee = findEmpoyee.get();
        employee.setAge(employeeDetails.getAge());
        employee.setName(employeeDetails.getName());
        employee.setContract(employeeDetails.getContract());
        return employeeRepository.save(employee);
    }

    public Optional<Employee> findById(Long employeeId) {
        return employeeRepository.findById(employeeId);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
}
