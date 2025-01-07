package com.unibuc.bookmyspace.service;

import com.unibuc.bookmyspace.entity.AppUser;
import com.unibuc.bookmyspace.exception.UserAlreadyExistsException;
import com.unibuc.bookmyspace.exception.UserNotFoundException;
import com.unibuc.bookmyspace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserRoleService userRoleService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, UserRoleService userRoleService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.userRoleService = userRoleService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public AppUser register(AppUser user) {
        userRepository.findByEmail(user.getEmail()).ifPresent(ex -> {
            throw new UserAlreadyExistsException();
        });
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRoleService.addRoleForUser(user);
        return userRepository.save(user);
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

    public AppUser getUserById(UUID uuid) {
        Optional<AppUser> user = userRepository.findById(uuid);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UserNotFoundException();
        }
    }

    public void changePassword(UUID uuid, String password) {
        Optional<AppUser> user = userRepository.findById(uuid);
        if (user.isPresent()) {
            user.get().setPassword(bCryptPasswordEncoder.encode(password));
            userRepository.save(user.get());
        } else {
            throw new UserNotFoundException();
        }
    }

    public void delete(UUID uuid) {
        Optional<AppUser> user = userRepository.findById(uuid);
        if (user.isPresent()) {
            userRepository.deleteById(uuid);
        } else {
            throw new UserNotFoundException();
        }
    }
}
