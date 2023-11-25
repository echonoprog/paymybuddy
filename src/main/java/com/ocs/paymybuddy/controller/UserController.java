package com.ocs.paymybuddy.controller;

import com.ocs.paymybuddy.model.User;
import com.ocs.paymybuddy.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@Controller
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new User());
        return "login"; // login.html
    }



    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register"; // register.html
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user) {
        try {
            LOGGER.info("Trying to register user: {}", user);
            userService.save(user);

            LOGGER.info("User registered successfully: {}", user);
            return "redirect:/login?registrationSuccess";
        } catch (RuntimeException e) {
            LOGGER.error("Erreur lors de l'enregistrement de l'utilisateur", e);
            return "redirect:/register?error=" + e.getMessage();
        }
    }
}
