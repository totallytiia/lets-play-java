package com.example.letsplay.services;


import java.util.List;

import com.example.letsplay.model.User;
import com.example.letsplay.response.UserResponse;

public interface UserService {

    UserResponse storeUser(UserResponse user);

    UserResponse getUserById(String id);

    User getUserByEmail(String email);

    UserResponse getCurrentUser();

    List<UserResponse> listAll();

    UserResponse updateUser(UserResponse userResponse, String id);

    void deleteUser(String id);
}
