package com.ocs.paymybuddy.service;

import com.ocs.paymybuddy.model.BankAccount;
import com.ocs.paymybuddy.model.User;
import com.ocs.paymybuddy.repository.BankAccountRepository;
import com.ocs.paymybuddy.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Transactional
@Service
public class BankAccountService {

    private static final Logger log = LoggerFactory.getLogger(BankAccountService.class);

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private UserRepository userRepository;

    public void addBankAccount(@AuthenticationPrincipal UserDetails userDetails, BankAccount bankAccount) {
        String userEmail = userDetails.getUsername();
        User user = userRepository.findByEmail(userEmail);

        if (user != null) {

            if (bankAccount.getIban() == null || bankAccount.getBankName() == null) {
                throw new IllegalArgumentException("L'IBAN et le nom de la banque sont requis.");
            }

            bankAccount.setUser(user);
            bankAccountRepository.save(bankAccount);
            log.info("Compte bancaire ajouté avec succès pour l'utilisateur avec l'ID : {}", user.getId());
        } else {
            log.error("Utilisateur avec l'e-mail {} non trouvé. Impossible d'ajouter le compte bancaire.", userEmail);
        }
    }


    public List<BankAccount> getUserBankAccounts(User user) {
        List<BankAccount> userBankAccounts = bankAccountRepository.findByUser(user);
        log.info("Nombre de comptes bancaires pour l'utilisateur {} : {}", user.getEmail(), userBankAccounts.size());
        return userBankAccounts;
    }

}
