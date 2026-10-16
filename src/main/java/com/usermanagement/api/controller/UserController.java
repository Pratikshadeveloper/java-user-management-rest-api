package com.usermanagement.api.controller;

import com.usermanagement.api.dto.UserRequestDTO;
import com.usermanagement.api.dto.UserResponseDTO;
import com.usermanagement.api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller exposing User Management CRUD endpoints.
 * Base route: /api/users
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") // Allows cross-origin requests from frontend apps or API test tools
public class UserController {

    private final UserService userService;

    /**
     * Dependency injection of UserService via constructor.
     *
     * @param userService user service implementation
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint 1: Create a new user
     * HTTP Method: POST
     * Route: /api/users
     *
     * @param requestDTO validated user payload
     * @return 201 CREATED with created user response DTO
     */
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO requestDTO) {
        UserResponseDTO createdUser = userService.createUser(requestDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    /**
     * Endpoint 2: Retrieve all users
     * HTTP Method: GET
     * Route: /api/users
     *
     * @return 200 OK with list of all user response DTOs
     */
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Endpoint 3: Retrieve single user by ID
     * HTTP Method: GET
     * Route: /api/users/{id}
     *
     * @param id user unique identifier
     * @return 200 OK with found user or 404 NOT FOUND via GlobalExceptionHandler
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        UserResponseDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * Endpoint 4: Update user by ID
     * HTTP Method: PUT
     * Route: /api/users/{id}
     *
     * @param id user unique identifier
     * @param requestDTO validated update user payload
     * @return 200 OK with updated user response DTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDTO requestDTO) {
        UserResponseDTO updatedUser = userService.updateUser(id, requestDTO);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Endpoint 5: Delete user by ID
     * HTTP Method: DELETE
     * Route: /api/users/{id}
     *
     * @param id user unique identifier
     * @return 200 OK with confirmation message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("message", "User deleted successfully");
        response.put("deletedUserId", id);
        return ResponseEntity.ok(response);
    }
}
