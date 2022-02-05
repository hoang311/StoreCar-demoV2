package com.collectiongarage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.collectiongarage.model.Employee;

public interface IEmployeeRepository extends JpaRepository<Employee, Integer> {

}
