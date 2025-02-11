package com.unibuc.bookmyspace.mapper;

import com.unibuc.bookmyspace.dto.UserRequest;
import com.unibuc.bookmyspace.entity.AppUser;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public AppUser userRequestToUser(UserRequest appUserRequest) {
        return new AppUser(
                appUserRequest.getFirstName(),
                appUserRequest.getLastName(),
                appUserRequest.getEmail(),
                appUserRequest.getPassword()
        );
    }
}
