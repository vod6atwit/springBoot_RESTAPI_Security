package com.learning.SpringBoot.restAPIDemo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.learning.SpringBoot.restAPIDemo.entity.Employee;

// @RepositoryRestResource(path = "members")
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
  // No need to write any code
}
