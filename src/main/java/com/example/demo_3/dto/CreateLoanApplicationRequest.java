package com.example.demo_3.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateLoanApplicationRequest {

    @NotBlank(message = "Applicant name is required")
    private String applicantName;

    @NotNull(message = "Monthly income is required")
    @DecimalMin(value = "0.01", message = "Monthly income must be positive")
    private BigDecimal monthlyIncome;

    @NotNull(message = "Requested loan amount is required")
    @DecimalMin(value = "0.01", message = "Requested loan amount must be positive")
    private BigDecimal requestedLoanAmount;

    @NotNull(message = "Tenor (months) is required")
    @Min(value = 1, message = "Tenor must be at least 1 month")
    private Integer tenorMonths;
}
