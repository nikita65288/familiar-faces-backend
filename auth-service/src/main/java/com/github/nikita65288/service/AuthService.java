package com.github.nikita65288.service;


import com.github.nikita65288.client.UserClient;
import com.github.nikita65288.dto.CreateUserCredentialDto;
import com.github.nikita65288.dto.user.UserDto;
import com.github.nikita65288.entity.UserCredential;
import com.github.nikita65288.exception.FFNotFoundException;
import com.github.nikita65288.jwt.JwtProvider;
import com.github.nikita65288.mapper.UserCredentialMapper;
import com.github.nikita65288.repository.UserCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserCredentialRepository userCredentialRepository;
    private final UserCredentialMapper userCredentialMapper;
    private final JwtProvider jwtProvider;
    private final UserClient userClient;

    @Autowired
    public AuthService(
            UserCredentialRepository userCredentialRepository,
            UserCredentialMapper userCredentialMapper,
            JwtProvider jwtProvider,
            UserClient userClient
    ) {
        this.userCredentialRepository = userCredentialRepository;
        this.userCredentialMapper = userCredentialMapper;
        this.jwtProvider = jwtProvider;
        this.userClient = userClient;
    }

    public String createUser(CreateUserCredentialDto createUserCredentialDto) {
        UserCredential credential = userCredentialMapper.createUserCredentialDtoToUserCredential(createUserCredentialDto);
        userCredentialRepository.save(credential);

        // Send request to User Service
        UserDto userDto = new UserDto();
        userDto.setAuthId(credential.getId());
        userDto.setUsername(credential.getUsername());
        userDto.setEmail(credential.getEmail());

        try {
            userClient.createUser(userDto);
        } catch (Exception e) {
            // TODO:
            // Если User Service недоступен, нужно откатить транзакцию!
            // Но для MVP пока просто логируем ошибку
            System.err.println("Error creating user profile: " + e.getMessage());
        }

        // TODO: change return param
        return "User added to the system";
    }

    public String generateToken(String username) {
        UserCredential userCredential = userCredentialRepository.findByUsername(username)
                .orElseThrow(() -> new FFNotFoundException("User credential record was not found in DB."));

        return jwtProvider.generateToken(userCredential.getId().toString());
    }

    public void validateToken(String token) {
        jwtProvider.validateToken(token);
    }
}
