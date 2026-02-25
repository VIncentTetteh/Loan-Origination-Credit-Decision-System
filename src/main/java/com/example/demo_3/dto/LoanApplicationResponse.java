package com.example.demo_3.dto;

import com.example.demo_3.domain.entity.LoanApplication;
import com.example.demo_3.domain.enums.ApplicationStatus;
import com.example.demo_3.domain.enums.CreditAssessmentResult;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class LoanApplicationResponse {

    private UUID id;
    private String applicantName;
    private BigDecimal monthlyIncome;
    private BigDecimal requestedLoanAmount;
    private Integer tenorMonths;
    private ApplicationStatus status;
    private CreditAssessmentResult creditAssessmentResult;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static LoanApplicationResponse from(LoanApplication entity) {
        return new LoanApplicationResponse(
                entity.getId(),
                entity.getApplicantName(),
                entity.getMonthlyIncome(),
                entity.getRequestedLoanAmount(),
                entity.getTenorMonths(),
                entity.getStatus(),
                entity.getCreditAssessmentResult(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
