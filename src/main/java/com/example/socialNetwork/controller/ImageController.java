package com.example.socialNetwork.controller;

import com.example.socialNetwork.dto.UserDTO;
import com.example.socialNetwork.entity.User;
import com.example.socialNetwork.facade.UserFacade;
import com.example.socialNetwork.payload.request.LoginRequest;
import com.example.socialNetwork.payload.response.MessageResponse;
import com.example.socialNetwork.service.ImageService;
import com.example.socialNetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api/image")
@CrossOrigin
public class ImageController {

    @Autowired
    ImageService imageService;

    @Autowired
    UserService userService;

    @Autowired
    UserFacade userFacade;

    // метод загружает фотографию в профиль пользователя
    @PostMapping("/upload")
    public ResponseEntity<MessageResponse> uploadImageToProfile(@RequestParam("file") MultipartFile file,
                                                                Principal principal) throws IOException {
        imageService.uploadImageToProfile(file, principal);
        return ResponseEntity.ok(new MessageResponse("Image uploaded successfully"));
    }

    @GetMapping("/profileImage")
    public ResponseEntity<UserDTO> getCurrentUser(@Valid @RequestBody LoginRequest loginRequest) {
        User user = userService.getCurrentUser(loginRequest::getUserName);
        UserDTO userDTO = userFacade.userToUserDTO(user);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    // метод получения фотографии профиля пользователя (getUserProfileImage) ("/profileImage")
}
