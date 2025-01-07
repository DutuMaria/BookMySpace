package com.unibuc.bookmyspace.repository;

import com.unibuc.bookmyspace.entity.Role;
import com.unibuc.bookmyspace.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByRoleName(RoleName name);
}
