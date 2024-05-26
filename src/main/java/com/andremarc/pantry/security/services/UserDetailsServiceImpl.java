package com.andremarc.pantry.security.services;

import com.andremarc.pantry.entity.User;
import com.andremarc.pantry.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = null;

        if (userRepository.findUserByUsername(username).isPresent()) {
            user =
                    userRepository
                            .findUserByUsername(username)
                            .orElseThrow(
                                    () ->
                                            new UsernameNotFoundException(
                                                    "Usuário {} não encontrado! " + username));
        }

        return UserDetailsImpl.build(user);
    }}
