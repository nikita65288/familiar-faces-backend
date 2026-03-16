package com.github.nikita65288.security;

import com.github.nikita65288.entity.UserCredential;
import com.github.nikita65288.repository.UserCredentialRepository;
import com.github.nikita65288.security.SocialNetworkUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SocialNetworkUserDetailsService implements UserDetailsService {

    private final UserCredentialRepository userCredentialRepository;

    @Autowired
    public SocialNetworkUserDetailsService(UserCredentialRepository userCredentialRepository) {
        this.userCredentialRepository = userCredentialRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<UserCredential> userCredential = userCredentialRepository.findByUsername(username);

        if (userCredential.isEmpty()) {
            throw new UsernameNotFoundException("User not found!");
        }

        return new SocialNetworkUserDetails(userCredential.get());
    }
}
