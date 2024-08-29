package com.example.socialNetwork.controller;

import com.example.socialNetwork.dto.UserDTO;
import com.example.socialNetwork.entity.User;
import com.example.socialNetwork.facade.UserFacade;
import com.example.socialNetwork.payload.request.LoginRequest;
import com.example.socialNetwork.payload.request.SignupRequest;
import com.example.socialNetwork.payload.response.MessageResponse;
import com.example.socialNetwork.security.jwt.JWTProvider;
import com.example.socialNetwork.service.UserService;
import com.example.socialNetwork.validators.ResponseErrorValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
@PreAuthorize("permitAll()")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ResponseErrorValidator responseErrorValidator;

    @Autowired
    private JWTProvider jwtProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private UserFacade userFacade;

    // api/auth/signup
    // метод, который будет принимать данные пользователей, чтобы они могли зарегистрироваться

    @PostMapping("/api/auth/signup")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SignupRequest signupRequest, BindingResult bindingResult) {

        ResponseEntity<Object> listErrors = responseErrorValidator.mappedValidatorService(bindingResult);
        if (!ObjectUtils.isEmpty(listErrors)) return listErrors;

        // пробуем создать пользователя

        try {
            userService.createUser(signupRequest);
            return ResponseEntity.ok(new MessageResponse("Registration successfully completed"));
        } catch (Exception e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @PostMapping(value = "/signin")
    public ResponseEntity<UserDTO> loginUser(@Valid @RequestBody LoginRequest loginRequest) {

        User currentUser = userService.getCurrentUser(loginRequest::getUserName);

        return new ResponseEntity<>(userFacade.userToUserDTO(currentUser), HttpStatus.OK);
    }


    // api/auth/signin
    // метод, который будет данные пользователей, чтобы они могли авторизоваться
}
