package com.glow.payment.domain.shared;

/**
 * Base exception for domain layer violations.
 * Used for business logic errors that should be handled at the API layer.
 */
public class DomainException extends RuntimeException {
    
    public DomainException(String message) {
        super(message);
    }
    
    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
