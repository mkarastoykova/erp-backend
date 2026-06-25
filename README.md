# ERP Backend – HR Microservice

A Java 17 / Spring Boot 3.2 multi-module Maven project containing the **HR microservice** and a shared **common-module**.

---

## Project Structure

```
erp-backend/
├── pom.xml                          ← Parent POM (multi-module)
├── common-module/                   ← Shared library (entities, DTOs, enums)
│   └── src/main/java/com/erp/common/
│       ├── entity/                  ← JPA entities (7 tables)
│       │   ├── Employee.java
│       │   ├── LeaveRequest.java
│       │   ├── PayrollRecord.java
│       │   ├── SalaryHistory.java
│       │   ├── EmployeeBenefit.java
│       │   ├── TaxSummary.java
│       │   ├── PerformanceReview.java
│       │   └── StringListConverter.java
│       ├── dto/                     ← Request/Response DTOs
│       │   ├── ApiResponse.java
│       │   ├── EmployeeDto.java
│       │   ├── LeaveRequestDto.java
│       │   ├── PayrollRecordDto.java
│       │   ├── SalaryHistoryDto.java
│       │   ├── EmployeeBenefitDto.java
│       │   ├── TaxSummaryDto.java
│       │   └── PerformanceReviewDto.java
│       └── enums/                   ← 11 enums
│           ├── EmployeeStatus, Gender, EmploymentType
│           ├── LeaveType, LeaveStatus
│           ├── PayrollStatus, PaymentMethod
│           ├── BenefitType, BenefitStatus
│           ├── FilingStatus, ReviewStatus
└── hr-service/                      ← HR microservice (Spring Boot app)
    └── src/
        ├── main/java/com/erp/hr/
        │   ├── HrServiceApplication.java
        │   ├── controller/          ← 7 REST controllers
        │   ├── service/             ← 7 service interfaces
        │   ├── service/impl/        ← 7 service implementations
        │   ├── repository/          ← 7 Spring Data JPA repositories
        │   ├── mapper/              ← 7 MapStruct mappers
        │   └── exception/           ← Global error handling
        └── main/resources/
            ├── application.yml
            └── db/changelog/        ← Liquibase migrations
                ├── db.changelog-master.xml
                └── changes/
                    ├── 001-create-employees.xml
                    ├── 002-create-leave-requests.xml
                    ├── 003-create-payroll-records.xml
                    ├── 004-create-salary-history.xml
                    ├── 005-create-employee-benefits.xml
                    ├── 006-create-tax-summaries.xml
                    └── 007-create-performance-reviews.xml
```

---

## Tech Stack

| Technology        | Version |
|-------------------|---------|
| Java              | 17      |
| Spring Boot       | 3.2.4   |
| Spring Data JPA   | 3.2.x   |
| Liquibase         | 4.20+   |
| MapStruct         | 1.5.5   |
| Lombok            | 1.18.30 |
| PostgreSQL        | 15+     |
| H2 (tests)        | 2.x     |

---

## Database Schema

| Table                | Key Columns |
|----------------------|-------------|
| `employees`          | id, employee_code, first_name, last_name, department, position, email, salary, status, employment_type, manager_id |
| `leave_requests`     | id, leave_code, employee_id, type, start_date, end_date, days, status, approved_by |
| `payroll_records`    | id, payroll_code, employee_id, month, base_salary, bonus, deductions, taxes, net_pay, status, disbursed |
| `salary_history`     | id, history_code, employee_id, effective_date, previous_salary, new_salary, increase_percent, approved_by |
| `employee_benefits`  | id, benefit_code, employee_id, type, plan, employer_cost, employee_cost, enrolled_date, status |
| `tax_summaries`      | id, tax_code, employee_id, year, gross_earnings, federal_withheld, state_withheld, social_security, medicare, total_taxes, filing_status, w2_issued |
| `performance_reviews`| id, review_code, employee_id, period, rating, goals (JSON), reviewer, status, notes, review_date |

---

## Prerequisites

- Java 17+
- Maven 3.8+
- PostgreSQL 15+ running locally

### Create the database

```sql
CREATE DATABASE erp_hr;
CREATE USER postgres WITH PASSWORD 'postgres';
GRANT ALL PRIVILEGES ON DATABASE erp_hr TO postgres;
```

---

## Configuration

Edit `hr-service/src/main/resources/application.yml` or set environment variables:

```bash
DB_USERNAME=postgres
DB_PASSWORD=postgres
```

Default: `jdbc:postgresql://localhost:5432/erp_hr`

---

## Build & Run

```bash
# Build the entire project
cd erp-backend
mvn clean install

# Run the HR service
cd hr-service
mvn spring-boot:run
```

The service starts on **http://localhost:8081/api/hr**

---

## REST API Endpoints

### Employees  `GET/POST /api/hr/employees`

| Method | Path | Description |
|--------|------|-------------|
| `POST`   | `/employees` | Create employee |
| `PUT`    | `/employees/{id}` | Update employee |
| `GET`    | `/employees/{id}` | Get by ID |
| `GET`    | `/employees/code/{code}` | Get by employee code |
| `GET`    | `/employees?department=&status=&q=` | Search (pageable) |
| `GET`    | `/employees/department/{dept}` | List by department |
| `GET`    | `/employees/{id}/direct-reports` | List direct reports |
| `PATCH`  | `/employees/{id}/status?status=` | Update status |
| `DELETE` | `/employees/{id}` | Delete employee |

### Leave Requests  `GET/POST /api/hr/leave-requests`

| Method | Path | Description |
|--------|------|-------------|
| `POST`  | `/leave-requests` | Submit leave request |
| `PUT`   | `/leave-requests/{id}` | Update (PENDING only) |
| `GET`   | `/leave-requests/{id}` | Get by ID |
| `GET`   | `/leave-requests?employeeId=&status=&type=&department=` | Search |
| `GET`   | `/leave-requests/employee/{id}` | All leave for employee |
| `PATCH` | `/leave-requests/{id}/approve?approvedBy=` | Approve |
| `PATCH` | `/leave-requests/{id}/reject?approvedBy=` | Reject |
| `DELETE`| `/leave-requests/{id}` | Delete |

### Payroll  `GET/POST /api/hr/payroll`

| Method | Path | Description |
|--------|------|-------------|
| `POST`  | `/payroll` | Create payroll record |
| `PUT`   | `/payroll/{id}` | Update (not disbursed) |
| `GET`   | `/payroll/{id}` | Get by ID |
| `GET`   | `/payroll?month=&status=&department=` | Search |
| `GET`   | `/payroll/employee/{id}` | By employee |
| `GET`   | `/payroll/month/{month}` | By month |
| `GET`   | `/payroll/month/{month}/total` | Total net pay for month |
| `PATCH` | `/payroll/{id}/process` | Mark as processed |
| `PATCH` | `/payroll/{id}/disburse` | Mark as disbursed |
| `DELETE`| `/payroll/{id}` | Delete (not disbursed) |

### Salary History  `GET/POST /api/hr/salary-history`

| Method | Path | Description |
|--------|------|-------------|
| `POST`  | `/salary-history` | Record salary change (also updates employee salary) |
| `GET`   | `/salary-history/{id}` | Get by ID |
| `GET`   | `/salary-history/employee/{id}` | All records for employee |
| `GET`   | `/salary-history/employee/{id}/paged` | Paged records |
| `DELETE`| `/salary-history/{id}` | Delete |

### Benefits  `GET/POST /api/hr/benefits`

| Method | Path | Description |
|--------|------|-------------|
| `POST`  | `/benefits` | Enroll benefit |
| `PUT`   | `/benefits/{id}` | Update benefit |
| `GET`   | `/benefits/{id}` | Get by ID |
| `GET`   | `/benefits/employee/{id}` | By employee |
| `GET`   | `/benefits?employeeId=&type=&status=` | Search |
| `DELETE`| `/benefits/{id}` | Delete |

### Tax Summaries  `GET/POST /api/hr/tax-summaries`

| Method | Path | Description |
|--------|------|-------------|
| `POST`  | `/tax-summaries` | Create tax summary |
| `PUT`   | `/tax-summaries/{id}` | Update (before W-2) |
| `GET`   | `/tax-summaries/{id}` | Get by ID |
| `GET`   | `/tax-summaries/employee/{id}` | By employee |
| `GET`   | `/tax-summaries?year=&department=&w2Issued=` | Search |
| `PATCH` | `/tax-summaries/{id}/issue-w2` | Issue W-2 |
| `DELETE`| `/tax-summaries/{id}` | Delete (before W-2) |

### Performance Reviews  `GET/POST /api/hr/performance-reviews`

| Method | Path | Description |
|--------|------|-------------|
| `POST`  | `/performance-reviews` | Create review |
| `PUT`   | `/performance-reviews/{id}` | Update (DRAFT only) |
| `GET`   | `/performance-reviews/{id}` | Get by ID |
| `GET`   | `/performance-reviews/employee/{id}` | By employee |
| `GET`   | `/performance-reviews?employeeId=&status=&period=&department=` | Search |
| `PATCH` | `/performance-reviews/{id}/complete` | Complete review |
| `DELETE`| `/performance-reviews/{id}` | Delete |

---

## Running Tests

```bash
cd erp-backend
mvn test
```

Tests use an in-memory H2 database with Liquibase migrations applied automatically.

---

## Actuator

Health check: `GET http://localhost:8081/api/hr/actuator/health`

