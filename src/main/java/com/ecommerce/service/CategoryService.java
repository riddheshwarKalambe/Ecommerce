package com.ecommerce.service;

import com.ecommerce.dtos.CategoryDto;
import com.ecommerce.dtos.PageableResponse;

public interface CategoryService {

    //Create

    CategoryDto create(CategoryDto categoryDto);


    //Update

    CategoryDto update(CategoryDto categoryDto, String categoryId);


    //Delete

    void  delete (String categoryId);


    //Get All

    PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir);


    // Get Single Category Detail

    CategoryDto get(String categoryId);

    //Search
}
