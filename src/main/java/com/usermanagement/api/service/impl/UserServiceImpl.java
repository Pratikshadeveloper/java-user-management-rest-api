package com.usermanagement.api.service.impl;

import com.usermanagement.api.dto.UserRequestDTO;
import com.usermanagement.api.dto.UserResponseDTO;
import com.usermanagement.api.entity.User;
import com.usermanagement.api.exception.UserAlreadyExistsException;
import com.usermanagement.api.exception.UserNotFoundException;
import com.usermanagement.api.repository.UserRepository;
import com.usermanagement.api.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation handling business logic for User CRUD operations.
 * Implements transaction boundaries and domain validation rules.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * Constructor-based dependency injection (Spring Best Practice).
     *
     * @param userRepository repository instance injected by Spring IoC container
     */
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Create a new user after verifying that the email address is unique.
     */
    @Override
    public UserResponseDTO createUser(UserRequestDTO requestDTO) {
        // Business Rule: Email uniqueness check
        if (userRepository.existsByEmail(requestDTO.getEmail())) {
            throw new UserAlreadyExistsException("User with email '" + requestDTO.getEmail() + "' already exists");
        }

        // Map DTO to Entity
        User user = new User();
        user.setName(requestDTO.getName());
        user.setEmail(requestDTO.getEmail());
        user.setAge(requestDTO.getAge());
        user.setCreatedAt(LocalDateTime.now());

        // Persist entity to database via JPA Repository
        User savedUser = userRepository.save(user);

        // Map saved entity back to response DTO
        return mapToResponseDTO(savedUser);
    }

    /**
     * Retrieve all users mapped to response DTOs.
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve single user by ID. Throws UserNotFoundException if not found.
     */
    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return mapToResponseDTO(user);
    }

    /**
     * Update an existing user's name, email, and age.
     * Validates that the email is not already taken by another user.
     */
    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO requestDTO) {
        // Locate existing user or throw 404
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        // Check if another user already owns the target email
        if (userRepository.existsByEmailAndIdNot(requestDTO.getEmail(), id)) {
            throw new UserAlreadyExistsException("Email '" + requestDTO.getEmail() + "' is already in use by another user");
        }

        // Apply updates
        existingUser.setName(requestDTO.getName());
        existingUser.setEmail(requestDTO.getEmail());
        existingUser.setAge(requestDTO.getAge());

        // Save updated entity
        User updatedUser = userRepository.save(existingUser);
        return mapToResponseDTO(updatedUser);
    }

    /**
     * Delete user by ID or throw UserNotFoundException if non-existent.
     */
    @Override
    public void deleteUser(Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        userRepository.delete(existingUser);
    }

    /**
     * Helper method to map User entity to UserResponseDTO.
     * Decouples persistent model state from client view representation.
     */
    private UserResponseDTO mapToResponseDTO(User user) {
        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setId(user.getId());
        responseDTO.setName(user.getName());
        responseDTO.setEmail(user.getEmail());
        responseDTO.setAge(user.getAge());
        responseDTO.setCreatedAt(user.getCreatedAt());
        return responseDTO;
    }
}
