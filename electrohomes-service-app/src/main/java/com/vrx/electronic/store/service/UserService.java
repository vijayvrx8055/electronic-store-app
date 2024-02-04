package com.vrx.electronic.store.service;

import com.vrx.electronic.store.dto.PageableResponse;
import com.vrx.electronic.store.dto.UserDto;

import java.util.List;

public interface UserService {

    //create
    UserDto createUser(UserDto user);

    //update
    UserDto updateUser(UserDto userDto, String userId);

    //delete
    void deleteUser(String userId);

    //get all users
    PageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir);

    //get single user by id
    UserDto getUserById(String userId);

    //get single user by email
    UserDto getUserByEmail(String email);

    // search user
    List<UserDto> searchUsers(String keyword);

    //other user specific features

}
