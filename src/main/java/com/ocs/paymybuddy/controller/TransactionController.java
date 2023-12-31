package com.ocs.paymybuddy.controller;

import com.ocs.paymybuddy.model.Transaction;
import com.ocs.paymybuddy.model.User;
import com.ocs.paymybuddy.service.TransactionService;
import com.ocs.paymybuddy.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class TransactionController {

    private static final Logger log = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/transfer")
    public String transferForm(@AuthenticationPrincipal UserDetails userDetails,
                               Model model,
                               @RequestParam(name = "page", defaultValue = "0") int page) {
        if (userDetails != null) {
            String userEmail = userDetails.getUsername();
            User user = userService.findByEmail(userEmail);

            List<User> userContacts = userService.getUserConnections(userEmail);
            log.info("Contacts de l'expéditeur: {}", userContacts);

            Transaction transaction = new Transaction();
            transaction.setSender(user);

            Page<Transaction> transactionPage = transactionService.findPaginatedTransactionsByUser(user.getId(), page, 5);

            model.addAttribute("user", user);
            model.addAttribute("contacts", userContacts);
            model.addAttribute("transaction", transaction);
            model.addAttribute("transactionPage", transactionPage);

            return "transfer";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/transfer")
    public String transfer(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute Transaction transaction,
                           RedirectAttributes redirectAttributes) {
        if (userDetails != null) {
            try {
                String userEmail = userDetails.getUsername();
                User sender = userService.findByEmail(userEmail);

                User receiver = userService.findById(transaction.getReceiver().getId());

                transaction.setReceiver(receiver);
                transaction.setSender(sender);

                transactionService.effectuerPaiement(transaction);

                log.info("Transfert effectué avec succès. Montant: {}, De: {}, À: {}, Description: {}",
                        transaction.getAmount(), transaction.getSender().getEmail(),
                        transaction.getReceiver().getEmail(), transaction.getDescription());


                redirectAttributes.addFlashAttribute("success", "Transaction completed successfully!");
            } catch (Exception e) {
                log.error("Erreur lors du transfert d'argent.", e);
                redirectAttributes.addFlashAttribute("error", "Failed to process transaction: " + e.getMessage());
            }
            return "redirect:/transfer";
        } else {
            log.warn("Tentative de transfert par un utilisateur non authentifié.");
            return "redirect:/login";
        }
    }
}
