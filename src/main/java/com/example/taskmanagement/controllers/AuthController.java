package com.example.taskmanagement.controllers;

import com.example.taskmanagement.enums.ResultCodeEnum;
import com.example.taskmanagement.models.User;
import com.example.taskmanagement.repository.UserRepository;
import com.example.taskmanagement.request.LoginRequest;
import com.example.taskmanagement.response.Result;
import com.example.taskmanagement.security.jwt.JwtUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository userRepository;

    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserRepository userRepository, JwtUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    //Sign in using username and password
    @PostMapping(value = "/sign-in",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result<Object>> authenticateUser(@RequestBody LoginRequest loginRequest) {

        Optional<User> byAccount = userRepository.findFirstByUsername(loginRequest.getUsername());
        Result<Object> notFound = new Result<>(ResultCodeEnum.VALIDATE_ERROR.getCode(), "Login information not found");


        if (byAccount.isEmpty()) {
            return ResponseEntity.badRequest().body(notFound);
        }


        User userInfo = byAccount.get();


        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userInfo.getUsername(), loginRequest.getPassword()));
        } catch (BadCredentialsException e) {
            //Catch bad credentials exception and return bad request status
            Result<Object> invalidUsernameOrPassword = new Result<>(ResultCodeEnum.VALIDATE_ERROR.getCode(), "Invalid username or password");

            return ResponseEntity.badRequest().body(invalidUsernameOrPassword);
        }


        //Generate token
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);



        userInfo.setToken(jwt);





        return ResponseEntity.ok(new Result<>(userInfo));
    }

}
