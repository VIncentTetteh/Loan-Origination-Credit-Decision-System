package com.example.demo_3.service;

import com.example.demo_3.domain.entity.LoanApplication;
import com.example.demo_3.domain.enums.CreditAssessmentResult;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Domain service responsible for evaluating credit eligibility.
 * Rule: monthly income must be at least 3× the monthly installment.
 * Monthly installment = requestedLoanAmount / tenorMonths.
 */
@Service
public class CreditAssessmentService {

    private static final BigDecimal INCOME_MULTIPLIER = BigDecimal.valueOf(3);

    public CreditAssessmentResult evaluate(LoanApplication application) {
        BigDecimal monthlyInstallment = application.getRequestedLoanAmount()
                .divide(BigDecimal.valueOf(application.getTenorMonths()), 2, RoundingMode.HALF_UP);

        BigDecimal requiredIncome = monthlyInstallment.multiply(INCOME_MULTIPLIER);

        if (application.getMonthlyIncome().compareTo(requiredIncome) >= 0) {
            return CreditAssessmentResult.PASS;
        }
        return CreditAssessmentResult.FAIL;
    }
}
