package com.ocs.paymybuddy.service;

import com.ocs.paymybuddy.model.User;
import com.ocs.paymybuddy.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service
public class CustomUserDetailsService implements UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        LOGGER.info("Tentative de chargement de l'utilisateur avec l'adresse e-mail : {}", email);

        User user = userRepository.findByEmail(email);

        if (user == null) {
            LOGGER.error("Utilisateur non trouvé avec l'adresse e-mail : {}", email);
            throw new UsernameNotFoundException("Utilisateur non trouvé avec l'adresse e-mail : " + email);
        }

        LOGGER.info("Utilisateur trouvé : {}", user.getEmail());

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles("USER")
                .build();

    }
}




