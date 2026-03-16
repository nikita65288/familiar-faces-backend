package com.github.nikita65288.mapper;

import com.github.nikita65288.dto.CreateUserCredentialDto;
import com.github.nikita65288.entity.UserCredential;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public abstract class UserCredentialMapper {

    protected PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Mapping(source = "password", target = "password", qualifiedByName = "encodePassword")
    public abstract UserCredential createUserCredentialDtoToUserCredential(CreateUserCredentialDto dto);

    @Named("encodePassword")
    protected String encrypt(String rawPassword) {
        return (rawPassword != null) ? passwordEncoder.encode(rawPassword) : null;
    }
}
