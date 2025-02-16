package com.unibuc.bookmyspace.service;

import com.unibuc.bookmyspace.embeddedId.UserRoleEmbeddedId;
import com.unibuc.bookmyspace.entity.AppUser;
import com.unibuc.bookmyspace.entity.Role;
import com.unibuc.bookmyspace.entity.RoleName;
import com.unibuc.bookmyspace.entity.UserRole;
import com.unibuc.bookmyspace.exception.UserNotFoundException;
import com.unibuc.bookmyspace.repository.UserRepository;
import com.unibuc.bookmyspace.repository.UserRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserRoleServiceTest {

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private UserRoleService userRoleService;

    private AppUser user;
    private Role role;

    @BeforeEach
    void setUp() {
        user = new AppUser();
        user.setUserId(1L);
        user.setEmail("test@unibuc.ro");

        role = new Role();
        role.setRoleId(1L);
        role.setRoleName(RoleName.USER);
    }

    @Test
    void addRoleForUser_ShouldAddRoleToUser() {
        when(roleService.getRoleByName(RoleName.USER)).thenReturn(role);
        when(userRoleRepository.save(any(UserRole.class))).thenReturn(new UserRole(new UserRoleEmbeddedId(role.getRoleId(), user.getUserId()), user, role));

        userRoleService.addRoleForUser(user);

        verify(userRoleRepository, times(1)).save(any(UserRole.class));
    }

    @Test
    void getAllRolesForGivenUser_ShouldReturnRoles() {
        Role adminRole = new Role();
        adminRole.setRoleName(RoleName.ADMIN);

        UserRole userRole = new UserRole(new UserRoleEmbeddedId(adminRole.getRoleId(), user.getUserId()), user, adminRole);
        when(userRoleRepository.queryBy(user.getUserId())).thenReturn(Collections.singletonList(userRole));

        var roles = userRoleService.getAllRolesForGivenUser(user.getUserId());

        assertFalse(roles.isEmpty());
        assertEquals(1, roles.size());
        assertEquals(RoleName.ADMIN, roles.get(0).getRole().getRoleName());
    }

    @Test
    void checkAdminRoleForGivenUser_ShouldReturnTrue_WhenUserHasAdminRole() {
        Role adminRole = new Role();
        adminRole.setRoleName(RoleName.ADMIN);
        UserRole userRole = new UserRole(new UserRoleEmbeddedId(adminRole.getRoleId(), user.getUserId()), user, adminRole);

        when(userRoleRepository.queryBy(user.getUserId())).thenReturn(Collections.singletonList(userRole));
        when(userRepository.existsById(user.getUserId())).thenReturn(true);

        Boolean hasAdminRole = userRoleService.checkAdminRoleForGivenUser(user.getUserId());

        assertTrue(hasAdminRole);
    }

    @Test
    void checkAdminRoleForGivenUser_ShouldReturnFalse_WhenUserDoesNotHaveAdminRole() {
        Role userRole = new Role();
        userRole.setRoleName(RoleName.USER);
        UserRole userRoleEntity = new UserRole(new UserRoleEmbeddedId(userRole.getRoleId(), user.getUserId()), user, userRole);

        when(userRoleRepository.queryBy(user.getUserId())).thenReturn(Collections.singletonList(userRoleEntity));
        when(userRepository.existsById(user.getUserId())).thenReturn(true);

        Boolean hasAdminRole = userRoleService.checkAdminRoleForGivenUser(user.getUserId());

        assertFalse(hasAdminRole);
    }

    @Test
    void checkAdminRoleForGivenUser_ShouldThrowUserNotFoundException_WhenUserNotFound() {
        when(userRepository.existsById(user.getUserId())).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userRoleService.checkAdminRoleForGivenUser(user.getUserId()));
    }

    @Test
    void addUserRole_ShouldAddRoleToUser_WhenValidRoleName() {
        Role adminRole = new Role();
        adminRole.setRoleName(RoleName.ADMIN);
        when(roleService.getRoleByName(RoleName.ADMIN)).thenReturn(adminRole);
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(userRoleRepository.save(any(UserRole.class))).thenReturn(new UserRole(new UserRoleEmbeddedId(adminRole.getRoleId(), user.getUserId()), user, adminRole));

        UserRole userRole = userRoleService.addUserRole(user.getUserId(), "admin");

        assertNotNull(userRole);
        assertEquals(RoleName.ADMIN, userRole.getRole().getRoleName());
    }

    @Test
    void addUserRole_ShouldThrowIllegalArgumentException_WhenInvalidRoleName() {
        assertThrows(IllegalArgumentException.class, () -> userRoleService.addUserRole(user.getUserId(), "invalidRole"));
    }

    @Test
    void addUserRole_ShouldThrowRuntimeException_WhenUserNotFound() {
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userRoleService.addUserRole(user.getUserId(), "admin"));
    }
}
