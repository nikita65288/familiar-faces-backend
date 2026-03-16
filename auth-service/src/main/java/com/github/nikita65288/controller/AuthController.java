package com.github.nikita65288.controller;

import com.github.nikita65288.dto.AuthRequest;
import com.github.nikita65288.dto.CreateUserCredentialDto;
import com.github.nikita65288.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(
            AuthService authService,
            AuthenticationManager authenticationManager
    ) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public String addNewUser(@RequestBody CreateUserCredentialDto dto) {
        return authService.createUser(dto);
    }

    @PostMapping("/login")
    public String getToken(@RequestBody AuthRequest authRequest) {
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());

        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException e) {
            // TODO: change return param
            return "Incorrect credentials";
        }

        return authService.generateToken(authRequest.getUsername());
    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token) {
        authService.validateToken(token);
        // TODO: change return param
        return "Token is valid";
    }
}
