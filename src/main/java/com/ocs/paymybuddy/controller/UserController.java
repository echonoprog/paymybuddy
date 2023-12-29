package com.ocs.paymybuddy.controller;

import com.ocs.paymybuddy.model.User;
import com.ocs.paymybuddy.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new User());
        return "login"; // login.html
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("welcomeMessage", "Bienvenue sur notre application!");
        return "home";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") User user, Model model) {
        System.out.println("Email from form: " + user.getEmail());
        User existingUser = userService.findByEmail(user.getEmail());

        if (existingUser != null && passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            System.out.println("Authentication successful");
            return "redirect:/transfer";
        } else {
            System.out.println("Authentication failed");
            model.addAttribute("error", "Identifiants incorrects. Veuillez réessayer.");
            return "login";
        }
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

    @GetMapping("/addContact")
    public String showAddContactForm(Model model) {
        model.addAttribute("user", new User());
        return "addContact"; // addContact.html
    }


    @PostMapping("/addContact")
    public String addContact(@AuthenticationPrincipal UserDetails userDetails,
                             @RequestParam("email") String contactEmail,
                             RedirectAttributes redirectAttributes) {
        if (userDetails != null) {
            try {
                userService.addContact(userDetails, contactEmail);
                redirectAttributes.addFlashAttribute("success", "Contact added successfully!");
            } catch (RuntimeException e) {
                LOGGER.error("Failed to add contact", e);
                redirectAttributes.addFlashAttribute("error", "Failed to add contact: " + e.getMessage());
            }
            return "redirect:/transfer";
        } else {
            LOGGER.warn("Tentative d'ajout de contact par un utilisateur non authentifié.");
            return "redirect:/login";
        }
    }



}
