package com.unibuc.bookmyspace.controller;

import com.unibuc.bookmyspace.entity.RoleName;
import com.unibuc.bookmyspace.entity.UserRole;
import com.unibuc.bookmyspace.entity.AppUser;
import com.unibuc.bookmyspace.entity.Role;
import com.unibuc.bookmyspace.exception.UserNotFoundException;
import com.unibuc.bookmyspace.service.UserRoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserRoleControllerTest {

    @Mock
    private UserRoleService userRoleService;

    @InjectMocks
    private UserRoleController userRoleController;

    private Long userId;
    private String role;
    private UserRole userRole;
    private AppUser appUser;
    private Role roleEntity;

    @BeforeEach
    void setUp() {
        userId = 1L;

        appUser = new AppUser();
        appUser.setUserId(userId);

        roleEntity = new Role();
        roleEntity.setRoleName(RoleName.USER);

        userRole = new UserRole();
        userRole.setUser(appUser);
        userRole.setRole(roleEntity);
    }

    @Test
    void checkIfUserIsAdmin_ShouldReturnTrue_WhenUserIsAdmin() {
        when(userRoleService.checkAdminRoleForGivenUser(userId)).thenReturn(true);

        ResponseEntity<Boolean> response = userRoleController.checkIfUserIsAdmin(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
        verify(userRoleService, times(1)).checkAdminRoleForGivenUser(userId);
    }

    @Test
    void checkIfUserIsAdmin_ShouldReturnFalse_WhenUserIsNotAdmin() {
        when(userRoleService.checkAdminRoleForGivenUser(userId)).thenReturn(false);

        ResponseEntity<Boolean> response = userRoleController.checkIfUserIsAdmin(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody());
        verify(userRoleService, times(1)).checkAdminRoleForGivenUser(userId);
    }

    @Test
    void checkIfUserIsAdmin_ShouldReturnNotFound_WhenUserDoesNotExist() {
        when(userRoleService.checkAdminRoleForGivenUser(userId)).thenThrow(new UserNotFoundException("User not found"));

        ResponseEntity<Boolean> response = userRoleController.checkIfUserIsAdmin(userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userRoleService, times(1)).checkAdminRoleForGivenUser(userId);
    }


    @Test
    void addUserRole_ShouldReturnAddedRole_WhenUserExists() {
        when(userRoleService.addUserRole(userId, role)).thenReturn(userRole);

        ResponseEntity<UserRole> response = userRoleController.addUserRole(userId, role);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userRole, response.getBody());
        verify(userRoleService, times(1)).addUserRole(userId, role);
    }

    @Test
    void addUserRole_ShouldReturnNotFound_WhenUserDoesNotExist() {
        doThrow(new RuntimeException("User not found")).when(userRoleService).addUserRole(userId, role);

        ResponseEntity<UserRole> response = userRoleController.addUserRole(userId, role);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userRoleService, times(1)).addUserRole(userId, role);
    }

}
