package com.example.letsplay.response;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;


@Getter
@Setter
public class ProductResponse {

    @Id
    private String id;

    @NotEmpty(message = "Name can't be null")
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 30, message = "Name needs to be between 2-30 characters")
    private String name;

    @NotEmpty(message = "Description can't be null")
    @Size(min = 6, max = 50, message = "Description needs to be between 6-50 characters.")
    private String description;

    @NotNull(message = "Price can't be null")
    @DecimalMin(value = "0.01", message = "Price needs to be at least 0.10")
    @DecimalMax(value = "10000.00", message = "Price can't be higher than 10000.00")
    private Double price;

    private String userId;

}
