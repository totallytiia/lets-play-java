package com.example.letsplay.controller;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.letsplay.converter.TempConverter;
import com.example.letsplay.exceptions.DataNotValidatedException;
import com.example.letsplay.model.User;
import com.example.letsplay.response.UserResponse;
import com.example.letsplay.services.UserService;
import com.example.letsplay.utils.AuthenticationRequest;
import com.example.letsplay.utils.JwtUtil;


@RestController
@RestControllerAdvice
@RequestMapping(value = "/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private TempConverter tempConverter;


    @PostMapping(value = "/register")
    public ResponseEntity<String> registerUser(@Validated @RequestBody UserResponse user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMessages = bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            if (errorMessages.contains("must not be empty")) {
                errorMessages = "Not valid data. Needs to have name, email, password and role.";
            }
            return ResponseEntity.badRequest().body(errorMessages);
        }

        if (!Objects.equals(user.getRole().toString(), "ADMIN") && !Objects.equals(user.getRole().toString(), "USER")) {
            return ResponseEntity.badRequest().body("Not valid data. Role needs to be ADMIN or USER.");
        }

        UserResponse newUser = userService.storeUser(user);

        return ResponseEntity.ok().body("Registration completed! Your ID is: " + newUser.getId());
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (AuthenticationException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Username or Password incorrect");
        }

        final User userDetails = userService.getUserByEmail(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        UserResponse userResponse = tempConverter.userToUserResponse(userDetails);
        userResponse.setAuthToken(jwt);

        return ResponseEntity.ok().body(userResponse);
    }
    
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @GetMapping(value = "/userDetails/{userId}")
    public ResponseEntity<UserResponse> getUserDetails(@PathVariable ("userId") String userId){
        UserResponse userDetails = userService.getUserById(userId);
        return  ResponseEntity.ok().body(userDetails);
    }
    
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @GetMapping(value = "/allUsers")
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        List<UserResponse> allUsers = userService.listAll();
        return ResponseEntity.ok().body(allUsers);
    }

    @GetMapping(value = "/currentUser")
    public ResponseEntity<UserResponse> getCurrentUser() {
        UserResponse userResponse = userService.getCurrentUser();
        return ResponseEntity.ok().body(userResponse);
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @PutMapping(value = "/updateUser/{userId}")
    public ResponseEntity<String> updateUser(@Validated @RequestBody UserResponse userResponse,
                                             Errors errors, @PathVariable("userId") String userId) {
        if(errors.hasErrors()) {
            String validationErrors = errors.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining("; "));
            throw new DataNotValidatedException(new Error(validationErrors));
        }

        UserResponse updatedUser = userService.updateUser(userResponse, userId);
        return ResponseEntity.ok().body("The user with id " + updatedUser.getId()
                + " has been updated without any issues");
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @DeleteMapping(value = "/deleteUser/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") String userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok().body("The user with id " + userId + " has been deleted without any issues");
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id " + userId + " not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred while deleting the user.");
        }
    }

}
