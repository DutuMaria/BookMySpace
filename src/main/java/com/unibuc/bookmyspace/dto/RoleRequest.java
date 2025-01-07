package com.unibuc.bookmyspace.dto;

import com.unibuc.bookmyspace.entity.RoleName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Required details for creating a new role")
public class RoleRequest {

    @NotBlank(message = "Role Name cannot be null")
    @Schema(description = "Role name for the new role, should ADMIN or USER", required = true)
    private RoleName roleName;
}