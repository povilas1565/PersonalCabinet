package com.example.socialNetwork.controller;

import com.example.socialNetwork.dto.UserDTO;
import com.example.socialNetwork.entity.User;
import com.example.socialNetwork.facade.UserFacade;
import com.example.socialNetwork.payload.request.LoginRequest;
import com.example.socialNetwork.repository.UserRepository;
import com.example.socialNetwork.service.UserService;
import com.example.socialNetwork.validators.ResponseErrorValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.security.Security;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@CrossOrigin //про аннотацию: https://russianblogs.com/article/9354767831/
public class UserController {

    @Autowired
    private UserFacade userFacade;
    @Autowired
    private UserService userService;
    @Autowired
    private ResponseErrorValidator responseErrorValidator;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public ResponseEntity<UserDTO> getCurrentUser(Principal principal) {
        User user = userService.getCurrentUser(principal);
        UserDTO userDTO = userFacade.userToUserDTO(user);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserProfile(@PathVariable("userId") String userId) {
        User user = userService.getUserById(Long.parseLong(userId));
        UserDTO userDTO = userFacade.userToUserDTO(user);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PostMapping("/update")
    @Operation(summary = "updateUser", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult, Principal principal) {
        ResponseEntity<Object> listErrors = responseErrorValidator.mappedValidatorService(bindingResult);
        if (!ObjectUtils.isEmpty(listErrors)) return listErrors;

        User user = userService.updateUser(userDTO, principal);
        UserDTO userUpdated = userFacade.userToUserDTO(user);

        return new ResponseEntity<>(userUpdated, HttpStatus.OK);
    }


    @GetMapping("/")
    public ResponseEntity<UserDTO> getCurrentUser(@Valid @RequestBody LoginRequest loginRequest) {
        User user = userService.getCurrentUser(loginRequest::getUserName);
        UserDTO userDTO = userFacade.userToUserDTO(user);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/allUsers")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> allUsers = userRepository.findAll().stream().map(x -> userFacade.userToUserDTO(x)).collect(Collectors.toList());

        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

}
