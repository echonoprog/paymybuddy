package com.ocs.paymybuddy.service;

import com.ocs.paymybuddy.model.BankAccount;
import com.ocs.paymybuddy.model.User;
import com.ocs.paymybuddy.repository.BankAccountRepository;
import com.ocs.paymybuddy.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class BankAccountService {

    private static final Logger log = LoggerFactory.getLogger(BankAccountService.class);

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private UserRepository userRepository;

    public void addBankAccount(int userId, BankAccount bankAccount) {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            bankAccount.setUser(user);
            bankAccountRepository.save(bankAccount);
            log.info("Compte bancaire ajouté avec succès pour l'utilisateur avec l'ID : {}", userId);
        } else {
            log.error("Utilisateur avec l'ID {} non trouvé. Impossible d'ajouter le compte bancaire.", userId);

        }
    }

    public List<BankAccount> getBankAccountsByUserId(int userId) {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            return bankAccountRepository.findByUser(user);
        } else {
            log.error("Utilisateur avec l'ID {} non trouvé. Impossible de récupérer la liste des comptes bancaires.", userId);
            return null;
        }
    }


}
