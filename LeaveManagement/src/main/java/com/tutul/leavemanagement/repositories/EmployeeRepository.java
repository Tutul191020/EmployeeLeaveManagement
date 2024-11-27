package com.tutul.leavemanagement.repositories;

import com.tutul.leavemanagement.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface EmployeeRepository extends JpaRepository<Employee, Long> {


}
