package com.example.letsplay.response;

import com.example.letsplay.model.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;


@Getter
@Setter
public class UserResponse {

    @Id
    private String id;

    @NotEmpty
    @Size(min = 2, max = 20, message = "Name needs to be between 2-20 characters")
    private String name;

    @NotEmpty
    @Size(max = 25, message = "Email needs to be less than 25 characters")
    @Email(regexp = ".+[@].+[\\.].+", message = "Not valid email")
    private String email;

    @NotEmpty
    @Size(min = 6, message = "Password needs to be at least 6 characters")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull
    private Role role;

    private String authToken;
}

