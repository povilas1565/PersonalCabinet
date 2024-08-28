package com.example.socialNetwork.controller;

import com.example.socialNetwork.dto.UserDTO;
import com.example.socialNetwork.entity.User;
import com.example.socialNetwork.facade.UserFacade;
import com.example.socialNetwork.repository.UserRepository;
import com.example.socialNetwork.service.UserService;
import com.example.socialNetwork.validators.ResponseErrorValidator;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private UserService userService;

    @Autowired
    private ResponseErrorValidator responseErrorValidator;

    @Autowired
    private UserRepository userRepository;

    // метод получения профиля пользователя
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserProfile(@PathVariable("userId") String userId) {
        User user = userService.getUserById(Long.parseLong(userId));
        UserDTO userDTO = userFacade.userToUserDTO(user);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    // метод изменения данных пользователя
    @PostMapping("/update")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult, Principal principal) {
        ResponseEntity<Object> listErrors = responseErrorValidator.mappedValidatorService(bindingResult);
        if (!ObjectUtils.isEmpty(listErrors)) return listErrors;

        User user = userService.updateUser(userDTO, principal);
        UserDTO userUpdated = userFacade.userToUserDTO(user);

        return new ResponseEntity<>(userUpdated, HttpStatus.OK);
    }

    // "/" - getCurrentUser (получение текущего пользователя в системе)
    // "allUsers" - getUsers (получение всех пользователей)
}
