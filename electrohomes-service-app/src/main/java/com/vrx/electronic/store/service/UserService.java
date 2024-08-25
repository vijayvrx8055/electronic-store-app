package com.vrx.electronic.store.service;

import com.vrx.electronic.store.dto.response.PageableResponse;
import com.vrx.electronic.store.dto.UserDto;
import com.vrx.electronic.store.entity.User;

import java.util.List;
import java.util.Optional;

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

    Optional<User> getUserByEmailOptional(String email);

    // search user
    List<UserDto> searchUsers(String keyword);

    void createNewUserByDefault(String email, String name, String photoUrl, String googleClientPassword);

    //other user specific features

}
