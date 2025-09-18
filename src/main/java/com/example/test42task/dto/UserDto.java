package com.example.test42task.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "DTO representing user with profile information")
public class UserDto {
    @Schema(description = "User ID", example = "1")
    private Long userId;

    @Schema(description = "User creation date", example = "2024-01-15T10:30:00")
    private LocalDateTime userCreated;

    @Schema(description = "User first name", example = "John")
    private String firstName;

    @Schema(description = "User last name", example = "Doe")
    private String lastName;

    @Schema(description = "User last update date", example = "2024-01-20T14:25:00")
    private LocalDateTime userUpdated;

    @Schema(description = "Flag that indicates whether message was sent", example = "false")
    private boolean isSend;
}
