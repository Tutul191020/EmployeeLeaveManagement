package com.tutul.leavemanagement.controller;

import com.tutul.leavemanagement.entities.Employee;
import com.tutul.leavemanagement.entities.LeaveRequest;
import com.tutul.leavemanagement.entities.LeaveType;
import com.tutul.leavemanagement.repositories.EmployeeRepository;
import com.tutul.leavemanagement.services.EmployeeService;
import com.tutul.leavemanagement.services.LeaveRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/hrm")
public class LeaveRequestController {
    private final LeaveRequestService leaveRequestService;
    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;

    public LeaveRequestController(LeaveRequestService leaveRequestService, EmployeeService employeeService, EmployeeRepository employeeRepository) {
        this.leaveRequestService = leaveRequestService;
        this.employeeService = employeeService;
        this.employeeRepository = employeeRepository;
    }

    @PostMapping("/add-employee")  // Add Employees
    public ResponseEntity<List<Employee>> addEmployee(@RequestBody List<Employee> employee) {
        List<Employee> savedEmployees = employeeRepository.saveAll(employee);
        return new ResponseEntity<>(savedEmployees, HttpStatus.CREATED);

    }

    @GetMapping("/fetch")   // view the lists of all employees
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employee = employeeService.getAllEmployees();
        if (employee.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @PostMapping("/submit")
    public ResponseEntity<LeaveRequest> applyLeaveRequest(@RequestParam Long employeeId, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate, @RequestParam LeaveType leaveType) {

        try {

            LeaveRequest leaveRequest = leaveRequestService.applyLeaveRequest(employeeId, startDate, endDate, leaveType);
            return new ResponseEntity<>(leaveRequest, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST); // Server response
        }
    }

    @GetMapping("/view-leave-status")
    public ResponseEntity<List<LeaveRequest>> getLeaveRequestsByEmployeeId(@RequestParam Long employeeId) {
        List<LeaveRequest> leaveRequests = leaveRequestService.getLeaveRequestsByEmployeeId(employeeId);
        if (leaveRequests.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(leaveRequests, HttpStatus.OK);
    }

    @PutMapping("/leave-request/{id}/Approve")
    public ResponseEntity<String> approveLeaveRequest(@PathVariable long id) {

        try {
            leaveRequestService.updateLeaveRequest(id);
            return new ResponseEntity<>("Leave Request Approve successfully ", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        }
    }

    @GetMapping("employees-applied")
    public ResponseEntity<List<Employee>> getEmployeesWhoAppliedLeave() {
        List<Employee>employees = leaveRequestService.getAllEmployeesWhoAppliedForLeave();
        if(employees.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(employees, HttpStatus.OK);

    }
    @GetMapping("HR/viewAllWithAppliedLeaves")
    public ResponseEntity<List<LeaveRequest>> getAllLeaveRequests() {
        List<LeaveRequest> leaveRequests = leaveRequestService.getAllLeaveRequests();
        if (leaveRequests.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // If no leave requests found
        }
        return new ResponseEntity<>(leaveRequests, HttpStatus.OK); // Return all leave requests
    }


}


