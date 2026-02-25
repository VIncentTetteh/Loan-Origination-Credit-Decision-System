package com.example.demo_3.domain.entity;

import com.example.demo_3.domain.enums.ApplicationStatus;
import com.example.demo_3.domain.enums.CreditAssessmentResult;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "loan_applications")
@Getter
@NoArgsConstructor
public class LoanApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String applicantName;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal monthlyIncome;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal requestedLoanAmount;

    @Column(nullable = false)
    private Integer tenorMonths;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status;

    @Enumerated(EnumType.STRING)
    private CreditAssessmentResult creditAssessmentResult;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public LoanApplication(String applicantName, BigDecimal monthlyIncome,
                           BigDecimal requestedLoanAmount, Integer tenorMonths) {
        this.applicantName = applicantName;
        this.monthlyIncome = monthlyIncome;
        this.requestedLoanAmount = requestedLoanAmount;
        this.tenorMonths = tenorMonths;
        this.status = ApplicationStatus.DRAFT;
    }

    // --- Domain state transition methods ---

    /**
     * Records the credit assessment result and transitions the status accordingly.
     * FAIL -> automatically REJECTED. PASS -> ASSESSED (eligible for approval).
     */
    public void applyCreditAssessment(CreditAssessmentResult result) {
        if (this.status != ApplicationStatus.DRAFT) {
            throw new IllegalStateException(
                    "Credit assessment can only be performed on applications in DRAFT state. Current state: " + this.status);
        }
        this.creditAssessmentResult = result;
        if (result == CreditAssessmentResult.FAIL) {
            this.status = ApplicationStatus.REJECTED;
        } else {
            this.status = ApplicationStatus.ASSESSED;
        }
    }

    /**
     * Approves the loan application. Only allowed after passing credit assessment.
     */
    public void approve() {
        if (this.status != ApplicationStatus.ASSESSED) {
            throw new IllegalStateException(
                    "Only applications that have passed credit assessment can be approved. Current state: " + this.status);
        }
        if (this.creditAssessmentResult != CreditAssessmentResult.PASS) {
            throw new IllegalStateException(
                    "Only applications with a PASS credit assessment can be approved.");
        }
        this.status = ApplicationStatus.APPROVED;
    }

    /**
     * Rejects the loan application. Allowed from DRAFT or ASSESSED states.
     * Rejection is final and cannot be reversed.
     */
    public void reject() {
        if (this.status == ApplicationStatus.REJECTED) {
            throw new IllegalStateException("Application is already rejected.");
        }
        if (this.status == ApplicationStatus.APPROVED) {
            throw new IllegalStateException("Cannot reject an already approved application.");
        }
        this.status = ApplicationStatus.REJECTED;
    }

    public boolean isInFinalState() {
        return this.status == ApplicationStatus.APPROVED || this.status == ApplicationStatus.REJECTED;
    }
}
