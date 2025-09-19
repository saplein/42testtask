package com.example.test42task.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatchResponse {
    @Schema(description = "Human readable message", example = "User updated successfully")
    private String message;
    @Schema(description = "Timestamp of response", example = "2023-10-10T12:00:00")
    private LocalDateTime timestamp;
    @Schema(description = "HTTP status", example = "200 OK")
    private String status;
    @Schema(description = "ID of updated user", example = "1")
    private Long userId;
    @Schema(
            description = "Map of fields that were successfully updated with their new values",
            example = """
            {
                "lastName": "Ивлев"
            }
            """,
            additionalProperties = Schema.AdditionalPropertiesValue.TRUE
    )
    private UserPatchRequest updatedFields;

    public PatchResponse(String message, Long userId, UserPatchRequest updatedFields) {
        this.message = message;
        this.userId = userId;
        this.updatedFields = updatedFields;
        this.timestamp = LocalDateTime.now();
        this.status = "200 OK";
    }
}
