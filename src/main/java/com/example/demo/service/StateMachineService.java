package com.example.demo.service;

import com.example.demo.dto.EmployeeState;
import com.example.demo.dto.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.data.StateMachineRepository;
import org.springframework.statemachine.data.mongodb.MongoDbRepositoryStateMachine;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StateMachineService {

    @Autowired
    private StateMachineRepository<MongoDbRepositoryStateMachine> stateMachineRepository;

    @Autowired
    private MessageService messageService;


    public MongoDbRepositoryStateMachine create(String id) {
        MongoDbRepositoryStateMachine mongoDbRepositoryStateMachine = new MongoDbRepositoryStateMachine();
        mongoDbRepositoryStateMachine.setId(id);
        mongoDbRepositoryStateMachine.setState(EmployeeState.ADDED.toString());
        return mongoDbRepositoryStateMachine;
    }

    public Optional<MongoDbRepositoryStateMachine> getStateMachineById(String id) {
        return  stateMachineRepository.findById(id);
    }

    public void update(String id, String state) {
        Optional<MongoDbRepositoryStateMachine> mongoDbRepositoryStateMachine = getStateMachineById(id);
        if(!mongoDbRepositoryStateMachine.isPresent()) {
            messageService.create(new Message("state machine", "update", "put"));
        }
        mongoDbRepositoryStateMachine.get().setState(state);
    }
}
