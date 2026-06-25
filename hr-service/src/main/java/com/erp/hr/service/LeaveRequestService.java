package com.erp.hr.service;

import com.erp.common.dto.LeaveRequestDto;
import com.erp.common.enums.LeaveStatus;
import com.erp.common.enums.LeaveType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LeaveRequestService {

    LeaveRequestDto create(LeaveRequestDto dto);

    LeaveRequestDto update(Long id, LeaveRequestDto dto);

    LeaveRequestDto findById(Long id);

    Page<LeaveRequestDto> search(Long employeeId, LeaveStatus status, LeaveType type, String department, Pageable pageable);

    List<LeaveRequestDto> findByEmployee(Long employeeId);

    LeaveRequestDto approve(Long id, String approvedBy);

    LeaveRequestDto reject(Long id, String approvedBy);

    void delete(Long id);
}

