package com.unibuc.bookmyspace.embeddedId;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleEmbeddedId implements Serializable {
    @Column(name = "role_id", nullable = false)
    private UUID roleId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;
}