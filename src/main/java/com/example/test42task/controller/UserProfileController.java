package com.example.test42task.controller;

import com.example.test42task.dto.UserWithProfileDto;
import com.example.test42task.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Profile API", description = "Операции с пользователями и их профилями")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping("/with-profiles")
    @Operation(
            summary = "Get all users with profiles",
            description = "Returns a list of all users with information about their profiles"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of users and user-profiles",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserWithProfileDto.class)
                    )
            )
    })
    public List<UserWithProfileDto> getAllUsersWithProfiles() {
        return userProfileService.getAllUsersWithProfiles();
    }
}
