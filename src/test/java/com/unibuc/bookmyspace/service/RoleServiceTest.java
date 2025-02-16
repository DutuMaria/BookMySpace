package com.unibuc.bookmyspace.service;

import com.unibuc.bookmyspace.entity.Role;
import com.unibuc.bookmyspace.entity.RoleName;
import com.unibuc.bookmyspace.exception.RoleNotFoundException;
import com.unibuc.bookmyspace.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    private Role userRole;
    private Role adminRole;

    @BeforeEach
    void setUp() {
        userRole = new Role();
        userRole.setRoleId(1L);
        userRole.setRoleName(RoleName.USER);

        adminRole = new Role();
        adminRole.setRoleId(2L);
        adminRole.setRoleName(RoleName.ADMIN);
    }

    @Test
    void getAllRoles_ShouldReturnListOfRoles() {
        List<Role> roles = List.of(userRole, adminRole);
        when(roleRepository.findAll()).thenReturn(roles);

        List<Role> result = roleService.getAllRoles();

        assertEquals(2, result.size());
        assertEquals(userRole, result.get(0));
        assertEquals(adminRole, result.get(1));
    }

    @Test
    void getRoleById_ShouldReturnRole_WhenRoleExists() {
        when(roleRepository.findById(userRole.getRoleId())).thenReturn(Optional.of(userRole));

        Role result = roleService.getRoleById(userRole.getRoleId());

        assertNotNull(result);
        assertEquals(RoleName.USER, result.getRoleName());
    }

    @Test
    void getRoleById_ShouldThrowException_WhenRoleNotFound() {
        when(roleRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> roleService.getRoleById(99L));
    }

    @Test
    void getRoleByName_ShouldReturnRole_WhenRoleExists() {
        when(roleRepository.findByRoleName(RoleName.ADMIN)).thenReturn(Optional.of(adminRole));

        Role result = roleService.getRoleByName(RoleName.ADMIN);

        assertNotNull(result);
        assertEquals(RoleName.ADMIN, result.getRoleName());
    }

    @Test
    void getRoleByName_ShouldThrowException_WhenRoleNotFound() {
        when(roleRepository.findByRoleName(RoleName.USER)).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> roleService.getRoleByName(RoleName.USER));
    }

    @Test
    void create_ShouldSaveRole_WhenValidRoleNameIsGiven() {
        when(roleRepository.save(any(Role.class))).thenReturn(userRole);

        Role result = roleService.create("user");

        assertNotNull(result);
        assertEquals(RoleName.USER, result.getRoleName());
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    void delete_ShouldRemoveRole_WhenRoleExists() {
        when(roleRepository.findById(userRole.getRoleId())).thenReturn(Optional.of(userRole));
        doNothing().when(roleRepository).deleteById(userRole.getRoleId());

        roleService.delete(userRole.getRoleId());

        verify(roleRepository, times(1)).deleteById(userRole.getRoleId());
    }

    @Test
    void delete_ShouldThrowException_WhenRoleNotFound() {
        when(roleRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> roleService.delete(99L));
    }
}
