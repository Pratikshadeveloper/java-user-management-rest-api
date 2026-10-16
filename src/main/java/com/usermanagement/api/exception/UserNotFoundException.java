package com.usermanagement.api.exception;

/**
 * Custom runtime exception thrown when a requested user entity is not found in the database.
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(Long id) {
        super("User not found with id: " + id);
    }
}
