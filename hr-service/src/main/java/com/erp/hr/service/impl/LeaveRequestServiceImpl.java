package com.erp.hr.service.impl;

import com.erp.common.dto.LeaveRequestDto;
import com.erp.common.entity.Employee;
import com.erp.common.entity.LeaveRequest;
import com.erp.common.enums.LeaveStatus;
import com.erp.common.enums.LeaveType;
import com.erp.hr.exception.ResourceNotFoundException;
import com.erp.hr.mapper.LeaveRequestMapper;
import com.erp.hr.repository.EmployeeRepository;
import com.erp.hr.repository.LeaveRequestRepository;
import com.erp.hr.service.LeaveRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class LeaveRequestServiceImpl implements LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveRequestMapper leaveRequestMapper;

    @Override
    @Transactional
    public LeaveRequestDto create(LeaveRequestDto dto) {
        Employee employee = findEmployee(dto.getEmployeeId());
        LeaveRequest entity = leaveRequestMapper.toEntity(dto);
        entity.setEmployee(employee);
        LeaveRequest saved = leaveRequestRepository.save(entity);
        log.info("Created leave request [{}] for employee [{}]", saved.getId(), employee.getId());
        return leaveRequestMapper.toDto(saved);
    }

    @Override
    @Transactional
    public LeaveRequestDto update(Long id, LeaveRequestDto dto) {
        LeaveRequest entity = findEntityById(id);
        if (entity.getStatus() != LeaveStatus.PENDING) {
            throw new IllegalArgumentException("Only PENDING leave requests can be updated");
        }
        leaveRequestMapper.updateEntityFromDto(dto, entity);
        return leaveRequestMapper.toDto(leaveRequestRepository.save(entity));
    }

    @Override
    public LeaveRequestDto findById(Long id) {
        return leaveRequestMapper.toDto(findEntityById(id));
    }

    @Override
    public Page<LeaveRequestDto> search(Long employeeId, LeaveStatus status, LeaveType type,
                                         String department, Pageable pageable) {
        return leaveRequestRepository.searchLeaveRequests(employeeId, status, type, department, pageable)
                .map(leaveRequestMapper::toDto);
    }

    @Override
    public List<LeaveRequestDto> findByEmployee(Long employeeId) {
        findEmployee(employeeId);
        return leaveRequestRepository.findByEmployeeId(employeeId).stream()
                .map(leaveRequestMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public LeaveRequestDto approve(Long id, String approvedBy) {
        LeaveRequest entity = findEntityById(id);
        if (entity.getStatus() != LeaveStatus.PENDING) {
            throw new IllegalArgumentException("Only PENDING leave requests can be approved");
        }
        entity.setStatus(LeaveStatus.APPROVED);
        entity.setApprovedBy(approvedBy);
        log.info("Approved leave request [{}] by {}", id, approvedBy);
        return leaveRequestMapper.toDto(leaveRequestRepository.save(entity));
    }

    @Override
    @Transactional
    public LeaveRequestDto reject(Long id, String approvedBy) {
        LeaveRequest entity = findEntityById(id);
        if (entity.getStatus() != LeaveStatus.PENDING) {
            throw new IllegalArgumentException("Only PENDING leave requests can be rejected");
        }
        entity.setStatus(LeaveStatus.REJECTED);
        entity.setApprovedBy(approvedBy);
        log.info("Rejected leave request [{}] by {}", id, approvedBy);
        return leaveRequestMapper.toDto(leaveRequestRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        LeaveRequest entity = findEntityById(id);
        leaveRequestRepository.delete(entity);
    }

    private LeaveRequest findEntityById(Long id) {
        return leaveRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LeaveRequest", "id", id));
    }

    private Employee findEmployee(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));
    }
}

