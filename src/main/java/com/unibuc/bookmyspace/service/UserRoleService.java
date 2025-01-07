package com.unibuc.bookmyspace.service;

import com.unibuc.bookmyspace.embeddedId.UserRoleEmbeddedId;
import com.unibuc.bookmyspace.entity.Role;
import com.unibuc.bookmyspace.entity.AppUser;
import com.unibuc.bookmyspace.entity.UserRole;
import com.unibuc.bookmyspace.entity.RoleName;
import com.unibuc.bookmyspace.repository.UserRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final RoleService roleService;

    public UserRoleService(UserRoleRepository userRoleRepository, RoleService roleService) {
        this.userRoleRepository = userRoleRepository;
        this.roleService = roleService;
    }

    public void addRoleForUser(AppUser user) {
        Role role = roleService.getRoleByName(RoleName.USER);
        UserRoleEmbeddedId userRoleEmbeddedId = new UserRoleEmbeddedId(role.getRoleId(), user.getUserId());

        UserRole userRole = new UserRole(userRoleEmbeddedId, user, role);
        userRoleRepository.save(userRole);
    }

    public List<UserRole> getAllRolesForGivenUser(UUID userId){
        return userRoleRepository.queryBy(userId);
    }

    public Boolean checkAdminRoleForGivenUser(UUID userId) {
        var userRoles = getAllRolesForGivenUser(userId);

        for (var userRole : userRoles) {
            if (userRole.getRole().getRoleName().equals(RoleName.ADMIN)) {
                return true;
            }
        }
        return false;
    }

    public UserRole addUserRole(UUID userId, String roleName) {
        RoleName roleNameEnum = switch (roleName.toLowerCase()) {
            case "admin" -> RoleName.ADMIN;
            case "user" -> RoleName.USER;
            default -> null;
        };

        Role role = roleService.getRoleByName(roleNameEnum);
        AppUser user = new AppUser(userId, null, null, null, null);
        UserRoleEmbeddedId userRoleEmbeddedId = new UserRoleEmbeddedId(role.getRoleId(), userId);

        UserRole userRole = new UserRole(userRoleEmbeddedId, user, role);
        return userRoleRepository.save(userRole);
    }
}

