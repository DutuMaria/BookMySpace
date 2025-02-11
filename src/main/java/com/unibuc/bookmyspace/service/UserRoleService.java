package com.unibuc.bookmyspace.service;

import com.unibuc.bookmyspace.embeddedId.UserRoleEmbeddedId;
import com.unibuc.bookmyspace.entity.AppUser;
import com.unibuc.bookmyspace.entity.Role;
import com.unibuc.bookmyspace.entity.RoleName;
import com.unibuc.bookmyspace.entity.UserRole;
import com.unibuc.bookmyspace.repository.UserRepository;
import com.unibuc.bookmyspace.repository.UserRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final RoleService roleService;

    public UserRoleService(UserRoleRepository userRoleRepository, UserRepository userRepository, RoleService roleService) {
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    public void addRoleForUser(AppUser user) {
        Role role = roleService.getRoleByName(RoleName.USER);
        UserRoleEmbeddedId userRoleEmbeddedId = new UserRoleEmbeddedId(role.getRoleId(), user.getUserId());

        UserRole userRole = new UserRole(userRoleEmbeddedId, user, role);
        userRoleRepository.save(userRole);
    }

    public List<UserRole> getAllRolesForGivenUser(Long userId){
        return userRoleRepository.queryBy(userId);
    }

    public Boolean checkAdminRoleForGivenUser(Long userId) {
        var userRoles = getAllRolesForGivenUser(userId);

        for (var userRole : userRoles) {
            if (userRole.getRole().getRoleName().equals(RoleName.ADMIN)) {
                return true;
            }
        }
        return false;
    }

    public UserRole addUserRole(Long userId, String roleName) {
        RoleName roleNameEnum = switch (roleName.toLowerCase()) {
            case "admin" -> RoleName.ADMIN;
            case "user" -> RoleName.USER;
            default -> throw new IllegalArgumentException("Invalid role name");
        };

        Role role = roleService.getRoleByName(roleNameEnum);
        AppUser user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        UserRoleEmbeddedId userRoleEmbeddedId = new UserRoleEmbeddedId(role.getRoleId(), userId);
        UserRole userRole = new UserRole(userRoleEmbeddedId, user, role);

        return userRoleRepository.save(userRole);
    }

}

