package com.unibuc.bookmyspace.controller;

import com.unibuc.bookmyspace.entity.UserRole;
import com.unibuc.bookmyspace.service.UserRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController()
@RequestMapping("/userRole")
@Tag(name = "User - Role", description = "Controller for managing user-roles")
public class UserRoleController {

    private final UserRoleService userRoleService;

    public UserRoleController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @Operation(summary = "Check if a user is admin", description = "Check if a user is admin by providing their id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User was found in the database"),
            @ApiResponse(responseCode = "404", description = "User was NOT found in the database")
    })
    @GetMapping("/checkAdmin/{id}")
    public ResponseEntity<Boolean> checkIfUserIsAdmin(@PathVariable("id") @Parameter(description = "The id of the user you want to check") UUID userId) {
        return ResponseEntity.ok(userRoleService.checkAdminRoleForGivenUser(userId));
    }

    @Operation(summary = "Add role for user", description = "Add role for user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User was found in the database"),
            @ApiResponse(responseCode = "404", description = "User was NOT found in the database")
    })
    @PostMapping("/addRole/{id}/{role}")
    public ResponseEntity<UserRole> addUserRole(@PathVariable("id") @Parameter(description = "Id of the user") UUID userId, @PathVariable("role") @Parameter(description = "Role name") String role) {
        return ResponseEntity.ok(userRoleService.addUserRole(userId, role));
    }
}
