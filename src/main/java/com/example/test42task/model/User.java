package com.example.test42task.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID of user", example = "1")
    private Long id;

    @Column(name = "first_name", nullable = false)
    @NotBlank(message = "Email не может быть пустым")
    @Schema(description = "First name of user", example = "Роман")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotBlank(message = "Email не может быть пустым")
    @Schema(description = "Last name of user", example = "Ивлев")
    private String lastName;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    @Schema(description = "Time of create response", example = "2023-10-10T12:00:00")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    @Schema(description = "Time of update response", example = "2023-10-10T12:00:00")
    private LocalDateTime updatedAt;

    @Column(name = "is_send", nullable = false)
    @Schema(description = "Flag that indicates whether message was sent", example = "false")
    private boolean isSend = false;


    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
