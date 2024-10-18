package com.ecommerce.service;

import com.ecommerce.dtos.PageableResponse;
import com.ecommerce.dtos.UserDto;

import java.util.List;

public interface UserService {

    // create
      UserDto createUser(UserDto userDto);

    //update
    UserDto updateUser(UserDto userDto, String userId);

    // delete
    void deleteUser(String userId);

    //get all users
   // List<UserDto> getAllUsers(int pageNumber, int pageSIze, String sortBy, String sortdir);
    PageableResponse<UserDto> getAllUsers(int pageNumber, int pageSIze, String sortBy, String sortdir);


    //get singal user by id
    UserDto getUserById(String userId);

    // get singal user by email
    UserDto getUserByEmail(String email);

    // search user
    List<UserDto> searchUser (String keyword);

    // other user specific feature


}
