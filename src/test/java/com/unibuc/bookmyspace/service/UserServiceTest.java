package com.unibuc.bookmyspace.service;

import com.unibuc.bookmyspace.dto.user.ChangePasswordRequest;
import com.unibuc.bookmyspace.dto.user.UserLoginRequest;
import com.unibuc.bookmyspace.dto.user.UserRegisterRequest;
import com.unibuc.bookmyspace.entity.AppUser;
import com.unibuc.bookmyspace.entity.Desk;
import com.unibuc.bookmyspace.exception.UserAlreadyExistsException;
import com.unibuc.bookmyspace.exception.UserNotFoundException;
import com.unibuc.bookmyspace.mapper.UserMapper;
import com.unibuc.bookmyspace.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRoleService userRoleService;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private AppUser user;

    @BeforeEach
    void setUp() {
        user = new AppUser();
        user.setUserId(1L);
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
    }

    @Test
    void register_ShouldThrowException_WhenUserAlreadyExists() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setEmail(user.getEmail());
        request.setPassword("password");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyExistsException.class, () -> userService.register(request));
    }

    @Test
    void register_ShouldRegisterUser_WhenUserDoesNotExist() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setEmail(user.getEmail());
        request.setPassword("password");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userMapper.userRequestToUser(request)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(bCryptPasswordEncoder.encode("password")).thenReturn("encodedPassword");

        AppUser result = userService.register(request);

        assertNotNull(result);
        assertEquals(user.getEmail(), result.getEmail());
        verify(userRoleService, times(1)).addRoleForUser(user);
    }

    @Test
    void login_ShouldReturnUser_WhenCredentialsAreValid() {
        UserLoginRequest request = new UserLoginRequest();
        request.setEmail(user.getEmail());
        request.setPassword("password");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(bCryptPasswordEncoder.matches("password", user.getPassword())).thenReturn(true);

        AppUser result = userService.login(request);

        assertNotNull(result);
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void login_ShouldReturnNull_WhenCredentialsAreInvalid() {
        UserLoginRequest request = new UserLoginRequest();
        request.setEmail(user.getEmail());
        request.setPassword("wrongPassword");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(bCryptPasswordEncoder.matches("wrongPassword", user.getPassword())).thenReturn(false);

        AppUser result = userService.login(request);

        assertNull(result);
    }

    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() {
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));

        AppUser result = userService.getUserById(user.getUserId());

        assertNotNull(result);
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void getUserById_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(user.getUserId()));
    }

//    @Test
//    void changePassword_ShouldChangePassword_WhenCurrentPasswordIsCorrect() {
//        ChangePasswordRequest request = new ChangePasswordRequest();
//        request.setEmail(user.getEmail());
//        request.setCurrentPassword("encodedPassword");
//        request.setNewPassword("newPassword");
//
//        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
//        when(bCryptPasswordEncoder.matches("encodedPassword", user.getPassword())).thenReturn(true);
//        lenient().doReturn("newEncodedPassword").when(bCryptPasswordEncoder).encode("newPassword");
//        when(userRepository.save(user)).thenReturn(user);
//
//        userService.changePassword(request);
//
//        assertEquals("newEncodedPassword", user.getPassword());
//        verify(userRepository, times(1)).save(user);
//    }

    @Test
    void changePassword_ShouldThrowException_WhenCurrentPasswordIsIncorrect() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setEmail(user.getEmail());
        request.setCurrentPassword("wrongPassword");
        request.setNewPassword("newPassword");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(bCryptPasswordEncoder.matches("wrongPassword", user.getPassword())).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.changePassword(request));
    }

    @Test
    void delete_ShouldDeleteUser_WhenUserExists() {
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(user.getUserId());

        userService.delete(user.getUserId());

        verify(userRepository, times(1)).deleteById(user.getUserId());
    }

    @Test
    void delete_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.delete(user.getUserId()));
    }

    @Test
    void addFavouriteDesk_ShouldAddFavouriteDesk() {
        Desk desk = new Desk();
        desk.setDeskId(1L);

        when(userRepository.save(user)).thenReturn(user);

        AppUser result = userService.addFavouriteDesk(user, desk);

        assertNotNull(result);
        assertEquals(desk, result.getFavouriteDesk());
    }

    @Test
    void updateFavouriteDesk_ShouldUpdateFavouriteDesk() {
        Desk desk = new Desk();
        desk.setDeskId(1L);

        when(userRepository.save(user)).thenReturn(user);

        AppUser result = userService.updateFavouriteDesk(user, desk);

        assertNotNull(result);
        assertEquals(desk, result.getFavouriteDesk());
    }

    @Test
    void removeFavouriteDesk_ShouldRemoveFavouriteDesk() {
        when(userRepository.save(user)).thenReturn(user);

        AppUser result = userService.removeFavouriteDesk(user);

        assertNotNull(result);
        assertNull(result.getFavouriteDesk());
    }
}
