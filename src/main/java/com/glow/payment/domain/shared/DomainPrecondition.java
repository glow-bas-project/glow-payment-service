package com.glow.payment.domain.shared;

/**
 * Utility class for domain precondition validation.
 * Throws DomainException if preconditions are violated.
 */
public class DomainPrecondition {
    
    /**
     * Assert that a condition is true, otherwise throw DomainException.
     */
    public static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new DomainException(message);
        }
    }
    
    /**
     * Assert that an object is not null, otherwise throw DomainException.
     */
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new DomainException(message);
        }
    }
    
    /**
     * Assert that a string is not empty, otherwise throw DomainException.
     */
    public static void notEmpty(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new DomainException(message);
        }
    }
}
