package com.ocs.paymybuddy.controller;

import com.ocs.paymybuddy.model.BankAccount;
import com.ocs.paymybuddy.model.BankTransaction;
import com.ocs.paymybuddy.model.User;
import com.ocs.paymybuddy.service.BankAccountService;
import com.ocs.paymybuddy.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class BankAccountController {

    private static final Logger log = LoggerFactory.getLogger(BankAccountController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private BankAccountService bankAccountService;

    @GetMapping("/profile")
    public String showUserProfile(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        List<BankAccount> userBankAccounts = bankAccountService.getUserBankAccounts(user);

        log.info("Nombre de comptes bancaires de l'utilisateur : {}", userBankAccounts.size());

        model.addAttribute("user", user);
        model.addAttribute("userBankAccounts", userBankAccounts);
        model.addAttribute("bankTransaction", new BankTransaction());
        return "profile";
    }

    @GetMapping("/addbankaccount")
    public String showAddBankAccountForm(Model model) {
        model.addAttribute("bankAccount", new BankAccount());
        return "addbankaccount";
    }

    @PostMapping("/addbankaccount")
    public String addBankAccount(@AuthenticationPrincipal UserDetails userDetails,
                                 @ModelAttribute("bankAccount") BankAccount bankAccount,
                                 RedirectAttributes redirectAttributes) {
        try {
            bankAccountService.addBankAccount(userDetails, bankAccount);
            redirectAttributes.addFlashAttribute("successAddBankAccount", "Bank account added successfully!");
        } catch (RuntimeException e) {
            log.error("Failed to add bank account", e);
            redirectAttributes.addFlashAttribute("errorAddBankAccount", "Failed to add bank account: " + e.getMessage());
        }
        return "redirect:/profile";
    }


    @GetMapping("/updateprofile")
    public String showUpdateProfileForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        model.addAttribute("user", user);
        return "updateprofile";
    }

    @PostMapping("/updateprofile")
    public String updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                @ModelAttribute("user") User updatedUser,
                                RedirectAttributes redirectAttributes) {
        try {
            userService.updateUser(userDetails.getUsername(), updatedUser);
            redirectAttributes.addFlashAttribute("successUpdateProfile", "Profile updated successfully!");
        } catch (RuntimeException e) {
            log.error("Failed to update profile", e);
            redirectAttributes.addFlashAttribute("errorUpdateProfile", "Failed to update profile: " + e.getMessage());
        }
        return "redirect:/profile";
    }



}