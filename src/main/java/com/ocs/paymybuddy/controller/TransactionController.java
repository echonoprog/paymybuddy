package com.ocs.paymybuddy.controller;

import com.ocs.paymybuddy.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TransactionController {

    @GetMapping("/transfer")
    public String transfer(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails != null) {
            String userEmail = userDetails.getUsername();
            User user = new User();
            user.setEmail(userEmail);
            model.addAttribute("user", user);
            return "transfer";
        } else {
            return "redirect:/login";
        }
    }
}
