package com.example.letsplay.converter;

import java.util.Optional;

import com.example.letsplay.model.Product;
import com.example.letsplay.response.ProductResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.letsplay.model.Role;
import com.example.letsplay.model.User;
import com.example.letsplay.response.UserResponse;

@Component
public class TempConverter {

    @Autowired
    private ModelMapper mapper;


    // Convert User to UserResponse
    public UserResponse userToUserResponse(User user) {
        UserResponse returnValue = mapper.map(user, UserResponse.class);
        Optional<Role> roleOptional = Optional.ofNullable(user.getRole());
        if (roleOptional.isPresent()) {
            Role role = roleOptional.get();
            returnValue.setRole(role);
        }
        return returnValue;
    }

    // Convert UserResponse to User
    public User userResponseToUser(UserResponse userResponse) {
        return mapper.map(userResponse, User.class);
    }


    public ProductResponse productToResponse(Product product) {
        return mapper.map(product, ProductResponse.class);
    }


    public Product responseToProduct(ProductResponse productResponse) {
        return mapper.map(productResponse, Product.class);
    }
}
