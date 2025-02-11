package com.unibuc.bookmyspace.service;

import com.unibuc.bookmyspace.dto.UserRequest;
import com.unibuc.bookmyspace.entity.AppUser;
import com.unibuc.bookmyspace.entity.Desk;
import com.unibuc.bookmyspace.exception.UserAlreadyExistsException;
import com.unibuc.bookmyspace.exception.UserNotFoundException;
import com.unibuc.bookmyspace.mapper.UserMapper;
import com.unibuc.bookmyspace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserRoleService userRoleService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserRoleService userRoleService, BCryptPasswordEncoder bCryptPasswordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userRoleService = userRoleService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userMapper = userMapper;
    }

    public AppUser register(UserRequest userRequest) {
        userRepository.findByEmail(userRequest.getEmail()).ifPresent(ex -> {
            throw new UserAlreadyExistsException();
        });

        AppUser appUser = userMapper.userRequestToUser(userRequest);
        appUser.setPassword(bCryptPasswordEncoder.encode(userRequest.getPassword()));
        userRoleService.addRoleForUser(appUser);

        return userRepository.save(appUser);
    }

    public AppUser login(AppUser user) {
        Optional<AppUser> userFromDB = userRepository.findByEmail(user.getEmail());
        if (userFromDB.isPresent()) {
            if (bCryptPasswordEncoder.matches(user.getPassword(), userFromDB.get().getPassword())){
                return userFromDB.get();
            }
        }
        return null;
    }

    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    public AppUser getUserById(Long id) {
        Optional<AppUser> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UserNotFoundException();
        }
    }

    public void changePassword(Long id, String password) {
        Optional<AppUser> user = userRepository.findById(id);
        if (user.isPresent()) {
            user.get().setPassword(bCryptPasswordEncoder.encode(password));
            userRepository.save(user.get());
        } else {
            throw new UserNotFoundException();
        }
    }

    public AppUser addFavouriteDesk(AppUser user, Desk desk) {
        user.setFavouriteDesk(desk);
        return userRepository.save(user);
    }

    public AppUser updateFavouriteDesk(AppUser user, Desk desk) {
        user.setFavouriteDesk(desk);
        return userRepository.save(user);
    }

    public AppUser removeFavouriteDesk(AppUser user) {
        user.setFavouriteDesk(null);
        return userRepository.save(user);
    }

    public void delete(Long id) {
        Optional<AppUser> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
        } else {
            throw new UserNotFoundException();
        }
    }
}
