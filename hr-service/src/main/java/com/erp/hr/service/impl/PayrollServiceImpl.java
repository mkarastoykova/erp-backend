package com.erp.hr.service.impl;

import com.erp.common.dto.PayrollRecordDto;
import com.erp.common.entity.Employee;
import com.erp.common.entity.PayrollRecord;
import com.erp.common.enums.PayrollStatus;
import com.erp.hr.exception.DuplicateResourceException;
import com.erp.hr.exception.ResourceNotFoundException;
import com.erp.hr.mapper.PayrollRecordMapper;
import com.erp.hr.repository.EmployeeRepository;
import com.erp.hr.repository.PayrollRecordRepository;
import com.erp.hr.service.PayrollService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PayrollServiceImpl implements PayrollService {

    private final PayrollRecordRepository payrollRecordRepository;
    private final EmployeeRepository employeeRepository;
    private final PayrollRecordMapper payrollRecordMapper;

    @Override
    @Transactional
    public PayrollRecordDto create(PayrollRecordDto dto) {
        if (payrollRecordRepository.findByEmployeeIdAndMonth(dto.getEmployeeId(), dto.getMonth()).isPresent()) {
            throw new DuplicateResourceException("PayrollRecord",
                    "employeeId/month", dto.getEmployeeId() + "/" + dto.getMonth());
        }
        Employee employee = findEmployee(dto.getEmployeeId());
        PayrollRecord entity = payrollRecordMapper.toEntity(dto);
        entity.setEmployee(employee);
        PayrollRecord saved = payrollRecordRepository.save(entity);
        log.info("Created payroll record [{}] for employee [{}] month {}", saved.getId(), employee.getId(), dto.getMonth());
        return payrollRecordMapper.toDto(saved);
    }

    @Override
    @Transactional
    public PayrollRecordDto update(Long id, PayrollRecordDto dto) {
        PayrollRecord entity = findEntityById(id);
        if (entity.getStatus() == PayrollStatus.PROCESSED && entity.getDisbursed()) {
            throw new IllegalArgumentException("Cannot update a disbursed payroll record");
        }
        payrollRecordMapper.updateEntityFromDto(dto, entity);
        return payrollRecordMapper.toDto(payrollRecordRepository.save(entity));
    }

    @Override
    public PayrollRecordDto findById(Long id) {
        return payrollRecordMapper.toDto(findEntityById(id));
    }

    @Override
    public Page<PayrollRecordDto> search(String month, PayrollStatus status, String department, Pageable pageable) {
        return payrollRecordRepository.searchPayrollRecords(month, status, department, pageable)
                .map(payrollRecordMapper::toDto);
    }

    @Override
    public List<PayrollRecordDto> findByEmployee(Long employeeId) {
        findEmployee(employeeId);
        return payrollRecordRepository.findByEmployeeId(employeeId).stream()
                .map(payrollRecordMapper::toDto)
                .toList();
    }

    @Override
    public List<PayrollRecordDto> findByMonth(String month) {
        return payrollRecordRepository.findByMonth(month).stream()
                .map(payrollRecordMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public PayrollRecordDto process(Long id) {
        PayrollRecord entity = findEntityById(id);
        if (entity.getStatus() == PayrollStatus.PROCESSED) {
            throw new IllegalArgumentException("Payroll record is already processed");
        }
        entity.setStatus(PayrollStatus.PROCESSED);
        log.info("Processed payroll record [{}]", id);
        return payrollRecordMapper.toDto(payrollRecordRepository.save(entity));
    }

    @Override
    @Transactional
    public PayrollRecordDto disburse(Long id) {
        PayrollRecord entity = findEntityById(id);
        if (entity.getStatus() != PayrollStatus.PROCESSED) {
            throw new IllegalArgumentException("Payroll must be PROCESSED before disbursement");
        }
        if (Boolean.TRUE.equals(entity.getDisbursed())) {
            throw new IllegalArgumentException("Payroll record is already disbursed");
        }
        entity.setDisbursed(true);
        log.info("Disbursed payroll record [{}]", id);
        return payrollRecordMapper.toDto(payrollRecordRepository.save(entity));
    }

    @Override
    public BigDecimal totalNetPayByMonth(String month) {
        return payrollRecordRepository.sumNetPayByMonth(month).orElse(BigDecimal.ZERO);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        PayrollRecord entity = findEntityById(id);
        if (Boolean.TRUE.equals(entity.getDisbursed())) {
            throw new IllegalArgumentException("Cannot delete a disbursed payroll record");
        }
        payrollRecordRepository.delete(entity);
    }

    private PayrollRecord findEntityById(Long id) {
        return payrollRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PayrollRecord", "id", id));
    }

    private Employee findEmployee(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));
    }
}

