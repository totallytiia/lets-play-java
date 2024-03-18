package com.example.letsplay.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.letsplay.converter.TempConverter;
import com.example.letsplay.exceptions.ExistingEmailException;
import com.example.letsplay.exceptions.InstanceUndefinedException;
import com.example.letsplay.exceptions.InvalidUserException;
import com.example.letsplay.model.User;
import com.example.letsplay.repository.UserRepository;
import com.example.letsplay.response.UserResponse;
import com.example.letsplay.services.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private TempConverter tempConverter;

    @Override
    public UserResponse storeUser(UserResponse user) {
        Optional<User> userOptional = userRepository.findByEmail(user.getEmail());
        if (userOptional.isPresent()) {
            Error error = new Error("Email exists already.");
            throw new ExistingEmailException(error);
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        User storedUser = userRepository.save(tempConverter.userResponseToUser(user));
        return tempConverter.userToUserResponse(storedUser);
    }

    @Override
    public UserResponse getUserById(String id) {
        UserResponse returnValue = null;
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            returnValue = tempConverter.userToUserResponse(userOptional.get());
        } else {
            Error error = new Error("The user has not been found.");
            throw new InvalidUserException(error);
        }
        return returnValue;
    }

    @Override
    public User getUserByEmail(String email) {
        User returnValue = null;
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            returnValue = userOptional.get();
        } else {
            Error error = new Error("The user has not been found.");
            throw new InvalidUserException(error);
        }
        return returnValue;
    }

    @Override
    public UserResponse getCurrentUser() {
        UserResponse returnValue = null;
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!(authentication instanceof AnonymousAuthenticationToken)) {
                String currentUserName = authentication.getName();
                Optional<User> userOptional = userRepository.findByEmail(currentUserName);
                if (userOptional.isPresent()) {
                    returnValue = tempConverter.userToUserResponse(userOptional.get());
                }
            }
        } catch (Exception exc) {
            Error error = new Error("Invalid user!");
            throw new InvalidUserException(error);
        }
        return returnValue;
    }

    @Override
    public List<UserResponse> listAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> tempConverter.userToUserResponse(user))
                .collect(Collectors.toList());
    }


    @Override
    public UserResponse updateUser(UserResponse userResponse, String id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            Error error = new Error("User not found with ID: " + id);
            throw new InvalidUserException(error);
        }
        User user = userOptional.get();

        User userUpdates = tempConverter.userResponseToUser(userResponse);

        if (userUpdates.getName() != null) {
            user.setName(userUpdates.getName());
        }
        if (userUpdates.getEmail() != null) {
            user.setEmail(userUpdates.getEmail());
        }
        if (userUpdates.getPassword() != null && !userResponse.getPassword().isEmpty()) {
            user.setPassword(bCryptPasswordEncoder.encode(userUpdates.getPassword()));
        }
        if (userUpdates.getRole() != null) {
            user.setRole(userUpdates.getRole());
        }

        User updatedUser = userRepository.save(user);
        System.out.println("user updated: " + tempConverter.userToUserResponse(updatedUser));

        return tempConverter.userToUserResponse(updatedUser);
    }

    @Override
    public void deleteUser(String id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            Error error = new Error("User not found with ID: " + id);
            throw new InstanceUndefinedException(error);
        }
        userRepository.deleteById(id);
    }
}
