package com.unibuc.bookmyspace.controller;

import com.unibuc.bookmyspace.dto.user.ChangePasswordRequest;
import com.unibuc.bookmyspace.dto.user.UserLoginRequest;
import com.unibuc.bookmyspace.dto.user.UserRegisterRequest;
import com.unibuc.bookmyspace.entity.AppUser;
import com.unibuc.bookmyspace.entity.Desk;
import com.unibuc.bookmyspace.service.DeskService;
import com.unibuc.bookmyspace.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@Tag(name = "User", description = "Controller for managing users")
public class UserController {

    private final UserService userService;
    private final DeskService deskService;

    public UserController(UserService userService, DeskService deskService) {
        this.userService = userService;
        this.deskService = deskService;
    }

    @PostMapping("/register")
    @Operation(summary = "Create an user", description = "Creates a new user based on the information received in the request's body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The user has been successfully created!"),
            @ApiResponse(responseCode = "409", description = "There is already an user with the specified email!")
    })
    public ResponseEntity<AppUser> create(@RequestBody @Valid @Parameter(description = "User data provided by the register form") UserRegisterRequest user) {
        return new ResponseEntity<>(userService.register(user), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "Login an user", description = "Checks if the password and email provided in the request's body are correct")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The user has been successfully logged in!"),
            @ApiResponse(responseCode = "400", description = "Email or password incorrect!")
    })
    public ResponseEntity<Object> login(@RequestBody @Parameter(description = "User data provided by the login form") UserLoginRequest user) {
        AppUser loggedInUser = userService.login(user);
        if (loggedInUser != null) {
            return ResponseEntity.ok(loggedInUser);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Email or password is incorrect");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @Operation(summary = "Get information about an user", description = "Get information about a certain user by providing their id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User was found in the database"),
            @ApiResponse(responseCode = "404", description = "User was NOT found in the database")
    })
    @GetMapping("/getById/{id}")
    public ResponseEntity<AppUser> getUserById(@PathVariable("id") @Parameter(description = "The id of the user you want to get information about") Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Get all users", description = "Returns all users in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A list containing all the users in the database")
    })
    @GetMapping("/getAll")
    public ResponseEntity<List<AppUser>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Change password for given user", description = "Change the password of a certain user by providing their id and new password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User was found in the database"),
            @ApiResponse(responseCode = "404", description = "User was NOT found in the database")
    })
    @PostMapping("/changePassword/")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest){
        userService.changePassword(changePasswordRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("addFavouriteDesk/{userId}/{deskId}")
    @Operation(summary = "Add a favourite desk to user", description = "Assign favourite desk to user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Desk added to favourite"),
            @ApiResponse(responseCode = "404", description = "User or Desk not found")
    })
    public ResponseEntity<AppUser> addFavouriteDesk(@PathVariable Long userId, @PathVariable Long deskId) {
        Optional<AppUser> user = Optional.ofNullable(userService.getUserById(userId));
        Optional<Desk> desk = Optional.ofNullable(deskService.getDeskById(deskId));

        if (user.isEmpty() || desk.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        AppUser updatedUser = userService.addFavouriteDesk(user.get(), desk.get());
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PutMapping("/updateFavouriteDesk/{userId}/{deskId}")
    @Operation(summary = "Update the user's favourite desk", description = "Change the user's favourite desk to a new desk")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Favourite desk updated"),
            @ApiResponse(responseCode = "404", description = "User or Desk not found")
    })
    public ResponseEntity<AppUser> updateFavouriteDesk(@PathVariable Long userId, @PathVariable Long deskId) {
        Optional<AppUser> user = Optional.ofNullable(userService.getUserById(userId));
        Optional<Desk> desk = Optional.ofNullable(deskService.getDeskById(deskId));

        if (user.isEmpty() || desk.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        AppUser updatedUser = userService.updateFavouriteDesk(user.get(), desk.get());
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/deleteFavouriteDesk/{userId}")
    @Operation(summary = "Remove the user's favourite desk", description = "Remove the desk from the user's favourites")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Favourite desk removed"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<AppUser> deleteFavouriteDesk(@PathVariable Long userId) {
        Optional<AppUser> user = Optional.ofNullable(userService.getUserById(userId));

        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        AppUser updatedUser = userService.removeFavouriteDesk(user.get());
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @Operation(summary = "Delete a given user", description = "Delete a certain user by providing their id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User was found in the database"),
            @ApiResponse(responseCode = "404", description = "User was NOT found in the database")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") @Parameter(description = "The id of the user") Long id){
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
