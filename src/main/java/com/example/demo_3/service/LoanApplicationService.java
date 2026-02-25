package com.example.demo_3.service;

import com.example.demo_3.domain.entity.LoanApplication;
import com.example.demo_3.domain.enums.CreditAssessmentResult;
import com.example.demo_3.dto.CreateLoanApplicationRequest;
import com.example.demo_3.exception.LoanApplicationNotFoundException;
import com.example.demo_3.repository.LoanApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanApplicationService {

    private final LoanApplicationRepository repository;
    private final CreditAssessmentService creditAssessmentService;

    @Transactional
    public LoanApplication create(CreateLoanApplicationRequest request) {
        LoanApplication application = new LoanApplication(
                request.getApplicantName(),
                request.getMonthlyIncome(),
                request.getRequestedLoanAmount(),
                request.getTenorMonths()
        );
        return repository.save(application);
    }

    @Transactional(readOnly = true)
    public LoanApplication getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new LoanApplicationNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<LoanApplication> getAll() {
        return repository.findAll();
    }

    /**
     * Runs credit assessment on the application and persists the result.
     * If assessment fails, the application is automatically rejected.
     */
    @Transactional
    public LoanApplication assessCredit(UUID id) {
        LoanApplication application = getById(id);
        CreditAssessmentResult result = creditAssessmentService.evaluate(application);
        application.applyCreditAssessment(result);
        return repository.save(application);
    }

    @Transactional
    public LoanApplication approve(UUID id) {
        LoanApplication application = getById(id);
        application.approve();
        return repository.save(application);
    }

    @Transactional
    public LoanApplication reject(UUID id) {
        LoanApplication application = getById(id);
        application.reject();
        return repository.save(application);
    }
}
