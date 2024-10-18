package com.ecommerce.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto { // for data transfer

    private  String categoryId;

    @NotBlank(message = "Title is Required !!  ")
   @Size(min = 4, message = "Title must be of minimum 4 characters !!")
    private String title;

    @NotBlank(message = "Description Required !!")
    private String description;

    @NotBlank(message = "Cover Image Required !!")
    private String coverImage;



}
