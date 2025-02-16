package com.unibuc.bookmyspace.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Required details for login request")
public class UserLoginRequest {
    @NotBlank(message = "Email cannot be null")
    @Schema(description = "Email address used to create an account on this app", required = true)
    private String email;

    @NotBlank(message = "Password cannot be null")
    @Schema(description = "Password used to create an account on this app", required = true)
    private String password;
}
