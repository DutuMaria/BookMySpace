package com.unibuc.bookmyspace.mapper;

import com.unibuc.bookmyspace.dto.user.UserRegisterRequest;
import com.unibuc.bookmyspace.entity.AppUser;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public AppUser userRequestToUser(UserRegisterRequest appUserRequest) {
        return new AppUser(
                appUserRequest.getFirstName(),
                appUserRequest.getLastName(),
                appUserRequest.getEmail(),
                appUserRequest.getPassword()
        );
    }
}
