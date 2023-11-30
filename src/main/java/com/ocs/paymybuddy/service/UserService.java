package com.ocs.paymybuddy.service;

import com.ocs.paymybuddy.model.User;
import com.ocs.paymybuddy.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.password.PasswordEncoder;


import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Transactional
@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private  UserRepository userRepository;


    public User save(User user) {
        log.info("Enregistrement de l'utilisateur : {}", user);

        try {

            if (userRepository.findByEmail(user.getEmail()) != null) {
                log.error("Échec de l'enregistrement - Cet e-mail est déjà utilisé : {}", user.getEmail());
                throw new RuntimeException("Cet e-mail est déjà utilisé.");
            }

            // Encoder MDP avant enregistrement dans la BDD
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);

            log.info("Utilisateur enregistré avec succès : {}", savedUser);

            return savedUser;
        } catch (Exception e) {
            log.error("Erreur lors de l'enregistrement de l'utilisateur", e);
            throw e;
        }
    }



    public void addContact(@AuthenticationPrincipal UserDetails userDetails, String contactEmail) {

        String userEmail = userDetails.getUsername();
        User user = userRepository.findByEmail(userEmail);
        if (user != null) {

            User contact = userRepository.findByEmail(contactEmail);

            // Vérifier si le contact n'est pas déjà dans la liste des contacts de l'utilisateur
            if (contact != null && !contact.equals(user)) {
                if (!user.getContacts().contains(contact)) {
                    // Ajout du contact à la liste des contacts de l'utilisateur
                    user.getContacts().add(contact);
                    userRepository.save(user);
                } else {
                    throw new RuntimeException("Le contact est déjà dans la liste des contacts de l'utilisateur.");
                }
            } else {
                throw new RuntimeException("Contact introuvable avec l'email fourni.");
            }
        } else {
            throw new RuntimeException("Utilisateur introuvable pour l'email connecté.");
        }
    }

    public List<User> getUserConnections(String email) {
        User user = userRepository.findByEmail(email);

        if (user != null) {
            return user.getContacts();
        } else {
            throw new RuntimeException("Utilisateur introuvable pour l'e-mail fourni.");
        }
    }

    public void deleteContact(int userId, String email) {

        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            User contact = userRepository.findByEmail(email);
            if (contact != null && !contact.equals(user)) {

                if (user.getContacts().contains(contact)) {
                    user.getContacts().remove(contact);
                    userRepository.save(user);
                } else {
                    throw new RuntimeException("Le contact n'est pas présent dans la liste des contacts de l'utilisateur.");
                }
            } else {
                throw new RuntimeException("Contact introuvable.");
            }
        } else {
            throw new RuntimeException("Utilisateur introuvable pour l'ID fourni.");
        }
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public User findById(int Id) {
        return userRepository.findById(Id).orElse(null);
    }

    public void deleteUserById(int Id) {
        Optional<User> userId = userRepository.findById(Id);

        if (userId.isPresent()) {
            User user = userId.get();

            // Supprimer l'utilisateur de la base de données
            userRepository.delete(user);
        } else {
            throw new RuntimeException("Utilisateur introuvable pour l'ID fourni.");
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public List<User> getAllContactsForUser(int Id) {
        Optional<User> userContacts = userRepository.findById(Id);

        if (userContacts.isPresent()) {
            User user = userContacts.get();
            return user.getContacts();
        } else {
            throw new RuntimeException("Utilisateur introuvable pour l'ID fourni.");
        }
    }

    public User updateUser(int Id, User user) {

        User existingUser = userRepository.findById(Id).orElse(null);

        if (existingUser != null) {
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setEmail(user.getEmail());
            existingUser.setBalance(user.getBalance());

            return userRepository.save(existingUser);
        } else {
            throw new RuntimeException("Utilisateur introuvable pour l'ID fourni.");
        }
    }
}










