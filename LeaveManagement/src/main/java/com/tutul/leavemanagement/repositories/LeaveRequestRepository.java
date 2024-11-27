package com.tutul.leavemanagement.repositories;


import com.tutul.leavemanagement.entities.Employee;
import com.tutul.leavemanagement.entities.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest,Long> {

    List<LeaveRequest> findByEmployeeId(Long employeeid);
    List<LeaveRequest> findAll();
}
