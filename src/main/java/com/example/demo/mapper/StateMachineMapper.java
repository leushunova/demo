package com.example.demo.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.data.mongodb.MongoDbRepositoryStateMachine;
import org.springframework.stereotype.Component;

@Component
public class StateMachineMapper {

    @Autowired
    private StateMachineFactory<String, String> stateMachineFactory;

    public StateMachine<String, String> stateMachineMapper(MongoDbRepositoryStateMachine mongoDbRepositoryStateMachine) {
        return stateMachineFactory.getStateMachine(mongoDbRepositoryStateMachine.getId());
    }
}
