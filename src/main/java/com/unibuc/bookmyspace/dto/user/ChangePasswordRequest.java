package com.unibuc.bookmyspace.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Required details for changing user's password request")
public class ChangePasswordRequest {
    @NotBlank(message = "Email cannot be null")
    @Schema(description = "User's email", required = true)
    private String email;

    @NotBlank(message = "Password cannot be null")
    @Schema(description = "User's password", required = true)
    private String currentPassword;

    @NotBlank(message = "New Password cannot be null")
    @Schema(description = "User's new password", required = true)
    private String newPassword;
}