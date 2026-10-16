package com.usermanagement.api.service;

import com.usermanagement.api.dto.UserRequestDTO;
import com.usermanagement.api.dto.UserResponseDTO;

import java.util.List;

/**
 * Service interface defining user management business operations.
 */
public interface UserService {

    /**
     * Create and persist a new user record.
     *
     * @param requestDTO user data
     * @return created user response DTO
     */
    UserResponseDTO createUser(UserRequestDTO requestDTO);

    /**
     * Retrieve all users in the system.
     *
     * @return list of user response DTOs
     */
    List<UserResponseDTO> getAllUsers();

    /**
     * Retrieve a specific user by their database identifier.
     *
     * @param id user ID
     * @return user response DTO
     */
    UserResponseDTO getUserById(Long id);

    /**
     * Update an existing user's details.
     *
     * @param id user ID
     * @param requestDTO updated user data
     * @return updated user response DTO
     */
    UserResponseDTO updateUser(Long id, UserRequestDTO requestDTO);

    /**
     * Delete a user by their database identifier.
     *
     * @param id user ID
     */
    void deleteUser(Long id);
}
