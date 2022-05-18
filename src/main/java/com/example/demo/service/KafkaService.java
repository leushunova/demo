package com.example.demo.service;

import com.example.demo.constant.ApplicationConstant;
import com.example.demo.dto.Employee;
import com.example.demo.dto.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class KafkaService {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private MessageService messageService;

    public void createNewEmployeeMessage(Employee employee) {
        kafkaTemplate.send(ApplicationConstant.TOPIC_STATE_MACHINE_CREATION , "new-employee", employee.getId());
    }

    public void searchStateMachineMessage(Optional<Employee> findEmployee, Employee employeeDetails) {
        if(!findEmployee.isPresent()) {
            messageService.create(new Message("Employee", "find employee by id", "get"));
        } else {
            kafkaTemplate.send(ApplicationConstant.TOPIC_STATE_MACHINE_SEARCH, String.valueOf(findEmployee.get().getId()), employeeDetails);
        }
    }
}
