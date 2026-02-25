package com.example.demo_3.controller;

import com.example.demo_3.dto.CreateLoanApplicationRequest;
import com.example.demo_3.dto.LoanApplicationResponse;
import com.example.demo_3.service.LoanApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/loan-applications")
@RequiredArgsConstructor
public class LoanApplicationController {

    private final LoanApplicationService loanApplicationService;

    @PostMapping
    public ResponseEntity<LoanApplicationResponse> create(
            @Valid @RequestBody CreateLoanApplicationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(LoanApplicationResponse.from(loanApplicationService.create(request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanApplicationResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(LoanApplicationResponse.from(loanApplicationService.getById(id)));
    }

    @GetMapping
    public ResponseEntity<List<LoanApplicationResponse>> getAll() {
        List<LoanApplicationResponse> responses = loanApplicationService.getAll().stream()
                .map(LoanApplicationResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/{id}/assess")
    public ResponseEntity<LoanApplicationResponse> assessCredit(@PathVariable UUID id) {
        return ResponseEntity.ok(LoanApplicationResponse.from(loanApplicationService.assessCredit(id)));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<LoanApplicationResponse> approve(@PathVariable UUID id) {
        return ResponseEntity.ok(LoanApplicationResponse.from(loanApplicationService.approve(id)));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<LoanApplicationResponse> reject(@PathVariable UUID id) {
        return ResponseEntity.ok(LoanApplicationResponse.from(loanApplicationService.reject(id)));
    }
}
