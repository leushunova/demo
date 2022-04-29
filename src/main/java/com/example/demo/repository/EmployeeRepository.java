package com.example.demo.repository;

import com.example.demo.dto.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  EmployeeRepository extends MongoRepository<Employee, Long> {
}
