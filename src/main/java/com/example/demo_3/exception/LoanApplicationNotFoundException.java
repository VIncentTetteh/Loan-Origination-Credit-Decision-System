package com.example.demo_3.exception;

import java.util.UUID;

public class LoanApplicationNotFoundException extends RuntimeException {

    public LoanApplicationNotFoundException(UUID id) {
        super("Loan application not found with id: " + id);
    }
}
