package com.tutul.leavemanagement.services;

import com.tutul.leavemanagement.entities.Employee;
import com.tutul.leavemanagement.entities.LeaveRequest;
import com.tutul.leavemanagement.entities.LeaveType;
import com.tutul.leavemanagement.repositories.LeaveRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LeaveRequestService {
    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeService employeeService;

    public LeaveRequestService(LeaveRequestRepository leaveRequestRepository, EmployeeService employeeService) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.employeeService = employeeService;
    }

    public LeaveRequest applyLeaveRequest(Long employeeId, LocalDate startDate, LocalDate endDate, LeaveType leaveType) {
        employeeService.validateLeaveBalance(employeeId, startDate, endDate);  // validate leave balance
        System.out.println("Validating Leave Request");

        LeaveRequest leaveRequest = new LeaveRequest(); // Create new instance of LeaveRequest
        leaveRequest.setEmployee(employeeService.getEmployeeById(employeeId));
        leaveRequest.setStartTime(startDate);
        leaveRequest.setEndTime(endDate);
        leaveRequest.setLeaveType(leaveType);
        leaveRequest.setStatus("PENDING");

        System.out.println("Saving Leave Request");
        return leaveRequestRepository.save(leaveRequest);
    }

    public void updateLeaveRequest(Long leaveRequestId) {

        // Retrieve the LeaveReq BY id
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveRequestId).
                orElseThrow(() -> new RuntimeException("Leave Request for the Employee is not found " + leaveRequestId));
        if (!"PENDING".equalsIgnoreCase(leaveRequest.getStatus())) {
            throw new RuntimeException(" Only PENDING Leave Request Status can be Approved ");
        }

        leaveRequest.setStatus("APPROVED");
        employeeService.deductLeave(
                leaveRequest.getEmployee().getId(),
                leaveRequest.getStartTime(),
                leaveRequest.getEndTime());

        leaveRequestRepository.save(leaveRequest);  // updated the new leaveReq
    }


    public List<LeaveRequest> getLeaveRequestsByEmployeeId(Long employeeId) {
        return leaveRequestRepository.findByEmployeeId(employeeId);
    }

   public List<LeaveRequest> getAllLeaveRequests() {
        // Fetch all leave requests
        return leaveRequestRepository.findAll(); // Returns all leave requests with employee details
    }

    public List<Employee> getAllEmployeesWhoAppliedForLeave() {
        List<LeaveRequest> leaveRequests = leaveRequestRepository.findAll();  // Fetch all Leave req
        return leaveRequests.stream().
                map(LeaveRequest::getEmployee).  // Method reference
                        distinct().  // Remove duplicates
                        toList();   // Viewed as a List

    }

}
