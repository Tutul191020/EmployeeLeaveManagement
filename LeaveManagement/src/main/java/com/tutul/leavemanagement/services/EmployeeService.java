package com.tutul.leavemanagement.services;


import com.tutul.leavemanagement.entities.Employee;
import com.tutul.leavemanagement.repositories.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }


    public Employee getEmployeeById(Long employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee not found" + employeeId));

    }

    public List<Employee> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();

        // Remove Duplicates based on id
        Set<Long> seenIds = new HashSet<>();  // Prevent duplicate By using set
        return employees.stream()
                .filter(employee -> seenIds.add(employee.getId()))  // Add id to seenIds,if it true ,{predicate}
                .collect(Collectors.toList());

    }

    public void validateLeaveBalance(Long employeeId, LocalDate startDate, LocalDate endDate) {

        if (startDate.isAfter(endDate) || startDate.isBefore(LocalDate.now())) {
            throw new RuntimeException("Something is Inappropriate happened please check again ");
        }
        Employee employee = getEmployeeById(employeeId); // Fetch Employee BY id

        // Calculate the number of days between startDate and endDate
        long requestedDays = ChronoUnit.DAYS.between(startDate, endDate) + 1; // Inclusive to count the both days

        if (employee.getTotalLeavesAvailable() < requestedDays) {
            throw new RuntimeException("Insufficient leave balance for employee with ID: " + employeeId);
        }
    }

    public void deductLeave(Long employeeId, LocalDate startDate, LocalDate endDate) {
        Employee employee = getEmployeeById(employeeId); // fetch

        // Calculate days for deduct
        long daysToDeduct = ChronoUnit.DAYS.between(startDate, endDate) + 1;  // Inclusive to count the both days

        // Deduct the days if employee get the leave
        int updatedBalance = (int) (employee.getTotalLeavesAvailable() - daysToDeduct);
        employee.setTotalLeavesAvailable(updatedBalance);

        // Save updated Data
        employeeRepository.save(employee);
    }
}

