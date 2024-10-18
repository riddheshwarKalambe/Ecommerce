package com.ecommerce.dtos;

//import com.ecommerce.validate.ImageNameValid;
//import jakarta.persistence.Column;
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.Pattern;
//import jakarta.validation.constraints.Size;
import com.ecommerce.validate.ImageNameValid;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UserDto {

    private String userId;

    @Size(min = 3, max = 15, message = "Invalid Name !!")
    private String userName;

    @Email(message = "Invalid User Email !!")
    //@Pattern(regexp = "^[a-z0-9][-a-z0-9.-]+@([-a-z0-9]+\\.)+[a-z]{2,5}$", message = "Invalid User Email !!")
    @NotBlank(message = "Email is required !!")
    private String email;

    @NotBlank(message = "Password is required !!")
    private String password;

    @Size(min = 4, max = 6, message ="Invalid gender !!")
    private String gender;

    @NotBlank(message = "Write about your self about !!")
    private String about;

    // @Pattern valid
    //@Custom validator

    @ImageNameValid
    private String imageName;
}
