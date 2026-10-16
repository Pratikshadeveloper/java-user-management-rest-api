package com.usermanagement.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * User Entity representing the 'users' table in the database.
 * Maps relational database columns to Java fields using JPA/Hibernate annotations.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    /**
     * Primary key with automatic generation strategy (IDENTITY).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * User's full name. Cannot be blank.
     */
    @NotBlank(message = "Name is required and cannot be blank")
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * User's email address. Must be unique and follow standard email formatting.
     */
    @NotBlank(message = "Email is required and cannot be blank")
    @Email(message = "Please provide a valid email address")
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    /**
     * User's age. Must be at least 1.
     */
    @NotNull(message = "Age is required")
    @Min(value = 1, message = "Age must be at least 1")
    @Column(nullable = false)
    private Integer age;

    /**
     * Timestamp recording when the user record was created.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Lifecycle callback hook executed before the entity is persisted into the database.
     * Automatically initializes createdAt if not already set.
     */
    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    // Explicit Getters and Setters to guarantee compatibility across all compiler setups
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
