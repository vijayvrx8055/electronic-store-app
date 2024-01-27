package com.vrx.electronic.store.service.impl;

import com.vrx.electronic.store.dto.UserDto;
import com.vrx.electronic.store.entity.User;
import com.vrx.electronic.store.repository.UserRepository;
import com.vrx.electronic.store.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        //generate unique id in string format
        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);

        User user = dtoToEntity(userDto);
        User savedUser = userRepository.save(user);
        return entityToDto(savedUser);
    }


    @Override
    public UserDto updateUser(UserDto userDto, String userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with given id."));
        user.setName(userDto.getName());
        user.setAbout(userDto.getAbout());
        user.setGender(userDto.getGender());
        user.setPassword(userDto.getPassword());
        user.setImageName(userDto.getImageName());
        User updatedUser = userRepository.save(user);
        return entityToDto(updatedUser);
    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found for given userID."));
        userRepository.delete(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found for given userID."));
        return entityToDto(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found for given EMAIL."));
        return entityToDto(user);
    }

    @Override
    public List<UserDto> searchUsers(String keyword) {
        List<User> users = userRepository.findByNameContaining(keyword).orElseThrow(() -> new RuntimeException("Users not found with given keywords"));
        return users.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    //-----------------------------------------
    private UserDto entityToDto(User savedUser) {
//        return UserDto.builder().userId(savedUser.getUserId()).name(savedUser.getName()).email(savedUser.getEmail()).password(savedUser.getPassword()).gender(savedUser.getGender()).about(savedUser.getAbout()).image_name(savedUser.getImage_name()).build();
        return modelMapper.map(savedUser, UserDto.class);
    }

    private User dtoToEntity(UserDto userDto) {
//        return User.builder().userId(userDto.getUserId()).name(userDto.getName()).email(userDto.getEmail()).password(userDto.getPassword()).about(userDto.getAbout()).gender(userDto.getGender()).image_name(userDto.getImage_name()).build();
        return modelMapper.map(userDto, User.class);
    }
}
