package com.unibuc.bookmyspace.controller;

import com.unibuc.bookmyspace.dto.user.ChangePasswordRequest;
import com.unibuc.bookmyspace.dto.user.UserLoginRequest;
import com.unibuc.bookmyspace.dto.user.UserRegisterRequest;
import com.unibuc.bookmyspace.entity.AppUser;
import com.unibuc.bookmyspace.entity.Desk;
import com.unibuc.bookmyspace.service.DeskService;
import com.unibuc.bookmyspace.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private DeskService deskService;

    @InjectMocks
    private UserController userController;

    private AppUser user;
    private Desk desk;

    @BeforeEach
    void setUp() {
        user = new AppUser();
        user.setUserId(1L);
        user.setEmail("test@example.com");

        desk = new Desk();
        desk.setDeskId(1L);
    }

    @Test
    void testCreateUser() {
        when(userService.register(any(UserRegisterRequest.class))).thenReturn(user);
        UserRegisterRequest request = new UserRegisterRequest();

        ResponseEntity<AppUser> response = userController.create(request);
        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    void testLoginSuccess() {
        when(userService.login(any(UserLoginRequest.class))).thenReturn(user);
        UserLoginRequest request = new UserLoginRequest();

        ResponseEntity<Object> response = userController.login(request);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    void testLoginFailure() {
        when(userService.login(any(UserLoginRequest.class))).thenReturn(null);
        UserLoginRequest request = new UserLoginRequest();

        ResponseEntity<Object> response = userController.login(request);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testGetUserById() {
        when(userService.getUserById(1L)).thenReturn(user);

        ResponseEntity<AppUser> response = userController.getUserById(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(user, response.getBody());
    }

    @Test
    void testGetAllUsers() {
        when(userService.getAllUsers()).thenReturn(List.of(user));

        ResponseEntity<List<AppUser>> response = userController.getAllUsers();
        assertEquals(200, response.getStatusCodeValue());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void testChangePassword() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        doNothing().when(userService).changePassword(request);

        ResponseEntity<?> response = userController.changePassword(request);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userService).delete(1L);

        ResponseEntity<?> response = userController.delete(1L);
        assertEquals(200, response.getStatusCodeValue());
    }
}
