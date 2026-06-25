package com.erp.hr.service.impl;

import com.erp.common.dto.TaxSummaryDto;
import com.erp.common.entity.Employee;
import com.erp.common.entity.TaxSummary;
import com.erp.hr.exception.DuplicateResourceException;
import com.erp.hr.exception.ResourceNotFoundException;
import com.erp.hr.mapper.TaxSummaryMapper;
import com.erp.hr.repository.EmployeeRepository;
import com.erp.hr.repository.TaxSummaryRepository;
import com.erp.hr.service.TaxSummaryService;
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
public class TaxSummaryServiceImpl implements TaxSummaryService {

    private final TaxSummaryRepository taxSummaryRepository;
    private final EmployeeRepository employeeRepository;
    private final TaxSummaryMapper taxSummaryMapper;

    @Override
    @Transactional
    public TaxSummaryDto create(TaxSummaryDto dto) {
        if (taxSummaryRepository.findByEmployeeIdAndYear(dto.getEmployeeId(), dto.getYear()).isPresent()) {
            throw new DuplicateResourceException("TaxSummary",
                    "employeeId/year", dto.getEmployeeId() + "/" + dto.getYear());
        }
        Employee employee = findEmployee(dto.getEmployeeId());
        TaxSummary entity = taxSummaryMapper.toEntity(dto);
        entity.setEmployee(employee);
        TaxSummary saved = taxSummaryRepository.save(entity);
        log.info("Created tax summary [{}] for employee [{}] year {}", saved.getId(), employee.getId(), dto.getYear());
        return taxSummaryMapper.toDto(saved);
    }

    @Override
    @Transactional
    public TaxSummaryDto update(Long id, TaxSummaryDto dto) {
        TaxSummary entity = findEntityById(id);
        if (Boolean.TRUE.equals(entity.getW2Issued())) {
            throw new IllegalArgumentException("Cannot update a TaxSummary after W-2 has been issued");
        }
        taxSummaryMapper.updateEntityFromDto(dto, entity);
        return taxSummaryMapper.toDto(taxSummaryRepository.save(entity));
    }

    @Override
    public TaxSummaryDto findById(Long id) {
        return taxSummaryMapper.toDto(findEntityById(id));
    }

    @Override
    public List<TaxSummaryDto> findByEmployee(Long employeeId) {
        findEmployee(employeeId);
        return taxSummaryRepository.findByEmployeeId(employeeId).stream()
                .map(taxSummaryMapper::toDto)
                .toList();
    }

    @Override
    public Page<TaxSummaryDto> search(Integer year, String department, Boolean w2Issued, Pageable pageable) {
        return taxSummaryRepository.searchTaxSummaries(year, department, w2Issued, pageable)
                .map(taxSummaryMapper::toDto);
    }

    @Override
    @Transactional
    public TaxSummaryDto issueW2(Long id) {
        TaxSummary entity = findEntityById(id);
        if (Boolean.TRUE.equals(entity.getW2Issued())) {
            throw new IllegalArgumentException("W-2 has already been issued for this record");
        }
        entity.setW2Issued(true);
        log.info("Issued W-2 for tax summary [{}]", id);
        return taxSummaryMapper.toDto(taxSummaryRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        TaxSummary entity = findEntityById(id);
        if (Boolean.TRUE.equals(entity.getW2Issued())) {
            throw new IllegalArgumentException("Cannot delete a TaxSummary after W-2 has been issued");
        }
        taxSummaryRepository.delete(entity);
    }

    private TaxSummary findEntityById(Long id) {
        return taxSummaryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TaxSummary", "id", id));
    }

    private Employee findEmployee(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));
    }
}

