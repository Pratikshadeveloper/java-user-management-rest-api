package com.usermanagement.api.exception;

/**
 * Custom runtime exception thrown when attempting to create or update a user with a duplicate email.
 */
public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
