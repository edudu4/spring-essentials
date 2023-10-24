package br.com.spring.placeti.service;

import br.com.spring.placeti.repository.PersonUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final PersonUserRepository personUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return Optional.ofNullable(personUserRepository.findByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
