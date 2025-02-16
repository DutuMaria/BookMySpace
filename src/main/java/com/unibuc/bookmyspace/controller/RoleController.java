package com.unibuc.bookmyspace.controller;

import com.unibuc.bookmyspace.entity.Role;
import com.unibuc.bookmyspace.exception.RoleNotFoundException;
import com.unibuc.bookmyspace.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/role")
@Tag(name = "Role",  description = "Controller for managing roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/createRole/{name}")
    @Operation(summary = "Create a new role", description = "Create a new role based on the information received in the request's body")
    @ApiResponse(responseCode = "201", description = "Role created successfully")
    public ResponseEntity<Role> create(@PathVariable("name") @Parameter(description = "Name of the new role: should be user or admin") String name) {
        return ResponseEntity.ok(roleService.create(name));
    }

    @Operation(summary = "Get all roles", description = "Returns all roles in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A list containing all the roles in the database")
    })
    @GetMapping("/getAll")
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @GetMapping("/getRole/{id}")
    @Operation(summary = "Get information about a role", description = "Get information about a certain role by providing their id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role was found in the database"),
            @ApiResponse(responseCode = "404", description = "Role was NOT found in the database")
    })
    public ResponseEntity<Role> getRoleById(@PathVariable("id") @Parameter(description = "The id of the user you want to get information about") Long id) {
        Role role = roleService.getRoleById(id);
        if (role == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(role);
    }

    @Operation(summary = "Delete a given role", description = "Delete a certain role by providing its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role was found in the database"),
            @ApiResponse(responseCode = "404", description = "Role was NOT found in the database")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Role>  delete(@PathVariable("id") @Parameter(description = "The id of the user you want to get information about") Long id) {
        try {
            roleService.delete(id);
            return ResponseEntity.ok().build();
        } catch (RoleNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
