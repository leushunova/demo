package com.example.demo.consumer;

import com.example.demo.constant.ApplicationConstant;
import com.example.demo.dto.Employee;
import com.example.demo.dto.EmployeeEvent;
import com.example.demo.dto.EmployeeState;
import com.example.demo.dto.Message;
import com.example.demo.mapper.StateMachineMapper;
import com.example.demo.service.EmployeeService;
import com.example.demo.service.MessageService;
import com.example.demo.service.StateMachineService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.data.mongodb.MongoDbRepositoryStateMachine;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class KafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @Autowired
    private StateMachineService stateMachineService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private StateMachineMapper stateMachineMapper;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @KafkaListener(groupId = ApplicationConstant.GROUP_ID_STRING, topics = ApplicationConstant.TOPIC_STATE_MACHINE_CREATION,
            containerFactory = ApplicationConstant.KAFKA_LISTENER_CONTAINER_FACTORY)
    public void receivedMessageFromTopic1(ConsumerRecord message) {
        MongoDbRepositoryStateMachine stateMachine = stateMachineService.create(message.value().toString());
        String state = String.valueOf(stateMachine.getState());
        String id = stateMachine.getId();
        logger.info("State machine was created " + state);
        kafkaTemplate.send(ApplicationConstant.TOPIC_STATE_MACHINE_STATUS, id, state);

    }

    @KafkaListener(groupId = ApplicationConstant.GROUP_ID_STRING, topics = ApplicationConstant.TOPIC_STATE_MACHINE_STATUS,
            containerFactory = ApplicationConstant.KAFKA_LISTENER_CONTAINER_FACTORY)
    public void receivedMessageFromTopic5(ConsumerRecord message) {
        logger.info("Message Received form topic 5 " + message.value().toString());
        if(!("200").equals(message.value().toString())) {
            messageService.create(new Message("State machine", "create", "post"));
        }
    }

    @KafkaListener(groupId = ApplicationConstant.GROUP_ID_STRING, topics = ApplicationConstant.TOPIC_STATE_MACHINE_SEARCH,
            containerFactory = ApplicationConstant.KAFKA_LISTENER_CONTAINER_FACTORY)
    public void receivedMessageFromTopic3(ConsumerRecord message) {
        Employee updateEmployee = (Employee) message.value();
        Optional<MongoDbRepositoryStateMachine> mongoDbRepositoryStateMachine = stateMachineService.getStateMachineById(message.key().toString());
        if(mongoDbRepositoryStateMachine.isPresent()) {
            StateMachine<String, String> stateMachine =  stateMachineMapper.stateMachineMapper(mongoDbRepositoryStateMachine.get());

            stateMachine.start();
            stateMachine.sendEvent(updateEmployee.getState());
            String newState = stateMachine.getState().toString();
            stateMachine.stop();
            stateMachineService.update(mongoDbRepositoryStateMachine.get().getId(), newState);
            updateEmployee.setState(newState);
            kafkaTemplate.send(ApplicationConstant.TOPIC_UPDATE_EMPLOYEE, message.key().toString(), updateEmployee);
        } else {
            String resource = "state machine with id " + mongoDbRepositoryStateMachine.get().getId();
            messageService.create(new Message(resource, "try to find", "get"));
        }

    }

    @KafkaListener(groupId = ApplicationConstant.GROUP_ID_STRING, topics = ApplicationConstant.TOPIC_UPDATE_EMPLOYEE,
            containerFactory = ApplicationConstant.KAFKA_LISTENER_CONTAINER_FACTORY)
    public void receivedMessageFromTopic4(ConsumerRecord message) {
        Employee employee = (Employee) message.value();
        Employee updatedEmployee = employeeService.update(employee.getId(), employee);
        logger.info("Employee was updated " + updatedEmployee.getState());

    }
}
