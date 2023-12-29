package com.ocs.paymybuddy.controller;

import com.ocs.paymybuddy.model.BankAccount;
import com.ocs.paymybuddy.model.BankTransaction;
import com.ocs.paymybuddy.model.User;
import com.ocs.paymybuddy.service.BankAccountService;
import com.ocs.paymybuddy.service.BankTransactionService;
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
public class BankTransactionController {

    private static final Logger log = LoggerFactory.getLogger(BankTransactionController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private BankTransactionService bankTransactionService;

    @Autowired
    private BankAccountService bankAccountService;

    @GetMapping("/transactionbank")
    public String showTransactionForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        List<BankAccount> userBankAccounts = bankAccountService.getUserBankAccounts(user);

        log.info("Nombre de comptes bancaires de l'utilisateur : {}", userBankAccounts.size());

        model.addAttribute("user", user);
        model.addAttribute("userBankAccounts", userBankAccounts);
        model.addAttribute("bankTransaction", new BankTransaction());
        return "transactionbank";
    }

    @PostMapping("/transactionbank")
    public String processTransaction(@AuthenticationPrincipal UserDetails userDetails,
                                     @ModelAttribute("bankTransaction") BankTransaction bankTransaction,
                                     RedirectAttributes redirectAttributes) {
        try {
            BankTransaction savedTransaction = bankTransactionService.save(bankTransaction);
            log.info("Saved transaction details: {}", savedTransaction);
            if (savedTransaction != null) {
                redirectAttributes.addFlashAttribute("successTransaction", "Transaction completed successfully!");
            } else {
                redirectAttributes.addFlashAttribute("errorTransaction", "Invalid transaction or associated user is null");
            }
        } catch (RuntimeException e) {
            log.error("Failed to process transaction", e);
            redirectAttributes.addFlashAttribute("errorTransaction", "Failed to process transaction: " + e.getMessage());
        }
        return "redirect:/profile";
    }


}