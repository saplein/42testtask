package com.example.test42task.controller;

import com.example.test42task.dto.DeleteResponse;
import com.example.test42task.dto.ErrorResponse;
import com.example.test42task.dto.PatchResponse;
import com.example.test42task.dto.PutResponse;
import com.example.test42task.model.User;
import com.example.test42task.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Controller", description = "APIs for managing users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET /api/users
    @GetMapping
    @Operation(summary = "Get all users",
               description = "Retrieves a list of all registered users."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved list of users",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
    )
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // GET /api/users/{id}
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public ResponseEntity<User> getUserById(
            @Parameter(description = "ID of the user to be retrieved", required = true)
            @PathVariable Long id) {
        User user = userService.findById(id);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // POST /api/users
    @PostMapping
    @Operation(summary = "Create a new user")
    @ApiResponse(responseCode = "201", description = "User created successfully")
    public ResponseEntity<User> createUser(
            @Parameter(description = "User object to be created", required = true)
            @Valid @RequestBody User user) {
        User savedUser = userService.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    // PUT /api/users/{id}
    @PutMapping("/{id}")
    @Operation(summary = "Fully update a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<PutResponse> fullUpdateUser(
            @Parameter(description = "ID of the user to be updated", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated user object (all fields required)", required = true)
            @Valid @RequestBody User user) {
        userService.fullUpdate(id, user);

        PutResponse response = new PutResponse(
                "User updated successfully",
                id
        );
        return ResponseEntity.ok(response);
    }

    // PATCH /api/users/{id}
    @PatchMapping("/{id}")
    @Operation(summary = "Partially update a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User patched successfully"),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<PatchResponse> partialUpdateUser(
            @Parameter(description = "ID of the user to be updated", required = true)
            @PathVariable Long id,
            @Parameter(description = "Fields of the user to update (as key-value pairs)")
            @RequestBody Map<String, Object> updates) {

        userService.partialUpdate(id, updates);

        PatchResponse response = new PatchResponse(
                "User updated successfully",
                id,
                updates
        );
        return ResponseEntity.ok(response);
    }

    // DELETE /api/users/{id}
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<DeleteResponse> deleteUser(
            @Parameter(description = "ID of the user to be deleted", required = true)
            @PathVariable Long id) {

        userService.deleteById(id);

        DeleteResponse response = new DeleteResponse(
                "User was successfully deleted",
                id
        );
        return ResponseEntity.ok(response);
    }
}
