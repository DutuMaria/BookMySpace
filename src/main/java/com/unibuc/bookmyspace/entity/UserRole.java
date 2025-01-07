package com.unibuc.bookmyspace.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.unibuc.bookmyspace.embeddedId.UserRoleEmbeddedId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
public class UserRole {
    @EmbeddedId
    @NotNull
    private UserRoleEmbeddedId userRoleId;

    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    @MapsId("roleId")
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}