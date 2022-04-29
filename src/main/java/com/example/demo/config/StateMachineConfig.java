package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.Arrays;
import java.util.HashSet;

@Configuration
@EnableStateMachineFactory
public class StateMachineConfig extends StateMachineConfigurerAdapter<String, String> {

    @Override
    public void configure(StateMachineStateConfigurer<String, String> states)
            throws Exception {

        states
                .withStates()
                .initial("ADDED")
                .end("ACTIVE")
                .states(
                        new HashSet<>(Arrays.asList("ADDED", "IN-CHECK", "APPROVED", "ACTIVE")));

    }

    @Override
    public void configure(
            StateMachineTransitionConfigurer<String, String> transitions)
            throws Exception {

        transitions.withExternal()
                .source("ADDED").target("IN-CHECK").event("IN-CHECK").and()
                .withExternal()
                .source("IN-CHECK").target("APPROVED").event("APPROVED").and()
                .withExternal()
                .source("APPROVED").target("ACTIVE").event("ACTIVE");
    }
}
