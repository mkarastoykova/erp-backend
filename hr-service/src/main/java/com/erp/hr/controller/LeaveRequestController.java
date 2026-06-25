package com.erp.hr.controller;

import com.erp.common.dto.ApiResponse;
import com.erp.common.dto.LeaveRequestDto;
import com.erp.common.enums.LeaveStatus;
import com.erp.common.enums.LeaveType;
import com.erp.hr.service.LeaveRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leave-requests")
@RequiredArgsConstructor
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    @PostMapping
    public ResponseEntity<ApiResponse<LeaveRequestDto>> create(@Valid @RequestBody LeaveRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Leave request created", leaveRequestService.create(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LeaveRequestDto>> update(@PathVariable Long id,
                                                                @Valid @RequestBody LeaveRequestDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("Leave request updated", leaveRequestService.update(id, dto)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LeaveRequestDto>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(leaveRequestService.findById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<LeaveRequestDto>>> search(
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) LeaveStatus status,
            @RequestParam(required = false) LeaveType type,
            @RequestParam(required = false) String department,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(
                leaveRequestService.search(employeeId, status, type, department, pageable)));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ApiResponse<List<LeaveRequestDto>>> findByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(ApiResponse.ok(leaveRequestService.findByEmployee(employeeId)));
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<LeaveRequestDto>> approve(@PathVariable Long id,
                                                                 @RequestParam String approvedBy) {
        return ResponseEntity.ok(ApiResponse.ok("Leave request approved", leaveRequestService.approve(id, approvedBy)));
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<LeaveRequestDto>> reject(@PathVariable Long id,
                                                                @RequestParam String approvedBy) {
        return ResponseEntity.ok(ApiResponse.ok("Leave request rejected", leaveRequestService.reject(id, approvedBy)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        leaveRequestService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Leave request deleted", null));
    }
}

