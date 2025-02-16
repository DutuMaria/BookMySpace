package com.unibuc.bookmyspace.controller;

import com.unibuc.bookmyspace.entity.Role;
import com.unibuc.bookmyspace.entity.RoleName;
import com.unibuc.bookmyspace.exception.RoleNotFoundException;
import com.unibuc.bookmyspace.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RoleControllerTest {

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setRoleId(1L);
        role.setRoleName(RoleName.USER);
    }

    @Test
    void createRole_ShouldReturnCreatedRole() {
        when(roleService.create("user")).thenReturn(role);

        ResponseEntity<Role> response = roleController.create("user");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(role, response.getBody());
        verify(roleService, times(1)).create("user");
    }

    @Test
    void getAllRoles_ShouldReturnRoleList() {
        when(roleService.getAllRoles()).thenReturn(Collections.singletonList(role));

        ResponseEntity<List<Role>> response = roleController.getAllRoles();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.singletonList(role), response.getBody());
        verify(roleService, times(1)).getAllRoles();
    }

    @Test
    void getRoleById_ShouldReturnRole_WhenRoleExists() {
        when(roleService.getRoleById(1L)).thenReturn(role);

        ResponseEntity<Role> response = roleController.getRoleById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(role, response.getBody());
        verify(roleService, times(1)).getRoleById(1L);
    }

    @Test
    void getRoleById_ShouldReturnNotFound_WhenRoleDoesNotExist() {
        when(roleService.getRoleById(1L)).thenReturn(null);

        ResponseEntity<Role> response = roleController.getRoleById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(roleService, times(1)).getRoleById(1L);
    }

    @Test
    void deleteRole_ShouldReturnOk_WhenRoleIsDeleted()  {
        doNothing().when(roleService).delete(1L);

        ResponseEntity<Role> response = roleController.delete(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(roleService, times(1)).delete(1L);
    }

    @Test
    void deleteRole_ShouldReturnNotFound_WhenRoleDoesNotExist() {
        doThrow(new RoleNotFoundException()).when(roleService).delete(1L);

        ResponseEntity<Role> response = roleController.delete(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(roleService, times(1)).delete(1L);
    }

}
