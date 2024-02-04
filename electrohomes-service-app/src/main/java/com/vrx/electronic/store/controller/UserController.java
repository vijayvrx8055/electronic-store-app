package com.vrx.electronic.store.controller;

import com.vrx.electronic.store.dto.ApiResponseMessage;
import com.vrx.electronic.store.dto.ImageResponse;
import com.vrx.electronic.store.dto.PageableResponse;
import com.vrx.electronic.store.dto.UserDto;
import com.vrx.electronic.store.service.FileService;
import com.vrx.electronic.store.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;

    //create
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        UserDto responseDto = userService.createUser(userDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    //update
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable("userId") String userId) {
        UserDto responseDto = userService.updateUser(userDto, userId);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    //delete
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable("userId") String userId) {
        userService.deleteUser(userId);
        ApiResponseMessage message = ApiResponseMessage.builder().message("User is deleted Successfully").status(HttpStatus.OK).success(true).build();
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    //get all
    @GetMapping
    public ResponseEntity<PageableResponse<UserDto>> getAllUsers(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                                 @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                                 @RequestParam(value = "sortBy", defaultValue = "userId", required = false) String sortBy,
                                                                 @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
        return new ResponseEntity<>(userService.getAllUsers(pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    //get single
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserByUserId(@PathVariable("userId") String userId) {
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    //get by email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable("email") String email) {
        return new ResponseEntity<>(userService.getUserByEmail(email), HttpStatus.OK);
    }

    //search users
    @GetMapping("/search/{keywords}")
    public ResponseEntity<List<UserDto>> searchUsers(@PathVariable("keywords") String keywords) {
        return new ResponseEntity<>(userService.searchUsers(keywords), HttpStatus.OK);
    }

    // upload user image
    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponse> uploadUserImage(@RequestParam("userImage") MultipartFile image,
                                                         @PathVariable("userId") String userId) throws IOException {
        String uploadedFile = fileService.uploadFile(image, imageUploadPath);
        UserDto user = userService.getUserById(userId);
        user.setImageName(uploadedFile);
        UserDto userDto = userService.updateUser(user, userId);
        ImageResponse imageResponse = ImageResponse.builder().imageName(uploadedFile).message("Image uploaded successfully").success(true).status(HttpStatus.CREATED).build();
        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
    }

    // get user image
    @GetMapping("/image/{userId}")
    public void serveUserImage(@PathVariable("userId") String userId,
                               HttpServletResponse response) throws IOException {
        UserDto userDto = userService.getUserById(userId);
        InputStream resource = fileService.getResource(imageUploadPath, userDto.getImageName());
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }

}
