package com.erp.hr;

import com.erp.common.entity.Employee;
import com.erp.common.entity.LeaveRequest;
import com.erp.common.enums.*;
import com.erp.hr.repository.EmployeeRepository;
import com.erp.hr.repository.LeaveRequestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class HrServiceApplicationTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Test
    void contextLoads() {
        assertThat(employeeRepository).isNotNull();
    }

    @Test
    void shouldCreateAndRetrieveEmployee() {
        Employee employee = Employee.builder()
                .employeeCode("E-TEST-001")
                .firstName("John")
                .lastName("Doe")
                .department("IT")
                .position("Developer")
                .email("john.doe.test@erp.com")
                .phone("+1-555-9999")
                .startDate(LocalDate.of(2024, 1, 1))
                .salary(new BigDecimal("85000.00"))
                .status(EmployeeStatus.ACTIVE)
                .dateOfBirth(LocalDate.of(1990, 5, 15))
                .gender(Gender.MALE)
                .employmentType(EmploymentType.FULL_TIME)
                .city("New York")
                .country("United States")
                .build();

        Employee saved = employeeRepository.save(employee);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getEmployeeCode()).isEqualTo("E-TEST-001");
        assertThat(saved.getEmail()).isEqualTo("john.doe.test@erp.com");

        assertThat(employeeRepository.findByEmail("john.doe.test@erp.com")).isPresent();
        assertThat(employeeRepository.findByEmployeeCode("E-TEST-001")).isPresent();
    }

    @Test
    void shouldCreateLeaveRequest() {
        Employee employee = employeeRepository.save(Employee.builder()
                .employeeCode("E-TEST-002")
                .firstName("Jane")
                .lastName("Smith")
                .department("HR")
                .position("HR Manager")
                .email("jane.smith.test@erp.com")
                .startDate(LocalDate.of(2023, 3, 1))
                .salary(new BigDecimal("78000.00"))
                .status(EmployeeStatus.ACTIVE)
                .employmentType(EmploymentType.FULL_TIME)
                .build());

        LeaveRequest leave = LeaveRequest.builder()
                .leaveCode("LR-TEST-001")
                .employee(employee)
                .type(LeaveType.ANNUAL)
                .startDate(LocalDate.of(2026, 4, 1))
                .endDate(LocalDate.of(2026, 4, 5))
                .days(5)
                .reason("Annual vacation")
                .status(LeaveStatus.PENDING)
                .appliedOn(LocalDate.now())
                .build();

        LeaveRequest savedLeave = leaveRequestRepository.save(leave);

        assertThat(savedLeave.getId()).isNotNull();
        assertThat(savedLeave.getStatus()).isEqualTo(LeaveStatus.PENDING);
        assertThat(leaveRequestRepository.findByEmployeeId(employee.getId())).hasSize(1);
    }

    @Test
    void shouldSearchEmployeesByDepartment() {
        employeeRepository.save(Employee.builder()
                .employeeCode("E-TEST-003")
                .firstName("Alice")
                .lastName("Brown")
                .department("Finance")
                .position("Accountant")
                .email("alice.brown.test@erp.com")
                .startDate(LocalDate.now())
                .salary(new BigDecimal("65000.00"))
                .status(EmployeeStatus.ACTIVE)
                .employmentType(EmploymentType.FULL_TIME)
                .build());

        assertThat(employeeRepository.findByDepartment("Finance")).isNotEmpty();
        assertThat(employeeRepository.findByStatus(EmployeeStatus.ACTIVE)).isNotEmpty();
    }
}

