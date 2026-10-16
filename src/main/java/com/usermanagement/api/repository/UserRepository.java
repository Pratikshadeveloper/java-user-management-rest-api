package com.usermanagement.api.repository;

import com.usermanagement.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for User persistence operations.
 * Extends JpaRepository to inherit standard CRUD, pagination, and sorting methods.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by unique email address.
     *
     * @param email user's email
     * @return Optional containing User if found, empty Optional otherwise
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if a user with the specified email already exists.
     *
     * @param email user's email
     * @return true if exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Check if another user with the specified email exists (excluding a specific user ID).
     * Used during update operations to ensure email uniqueness.
     *
     * @param email user's email
     * @param id user's current ID to exclude
     * @return true if another user has this email, false otherwise
     */
    boolean existsByEmailAndIdNot(String email, Long id);
}
