package com.ocs.paymybuddy.service;

import com.ocs.paymybuddy.model.BankTransaction;
import com.ocs.paymybuddy.model.BankAccount;
import com.ocs.paymybuddy.model.User;
import com.ocs.paymybuddy.repository.BankTransactionRepository;
import com.ocs.paymybuddy.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Transactional
@Service
public class BankTransactionService {

    @Autowired
    private BankTransactionRepository bankTransactionRepository;

    @Autowired
    private UserRepository userRepository;


    private static final Logger log = LoggerFactory.getLogger(BankTransactionService.class);


    public BankTransaction save(BankTransaction transaction) {

        if (transaction == null) {
            log.error("La transaction est nulle.");
            return null;
        }

        BankAccount bankAccount = transaction.getBankAccount();


        if (bankAccount == null) {
            log.error("Le compte bancaire est nul dans la transaction.");
            return null;
        }

        User user = bankAccount.getUser();


        if (user == null) {
            log.error("L'utilisateur est nul dans le compte bancaire.");
            return null;
        }

        float montantTransaction = transaction.getAmountbank();
        if (transaction.isCredit()) {
            float nouveauSolde = user.getBalance() + montantTransaction;
            user.setBalance(nouveauSolde);
        } else {
            float nouveauSolde = user.getBalance() - montantTransaction;
            // Vérifier si le solde devient négatif après la transaction
            if (nouveauSolde < 0) {
                log.warn("Solde insuffisant pour effectuer la transaction.");
                return null;
            }
            user.setBalance(nouveauSolde);
        }

        if (transaction.getDate() == null) {
            transaction.setDate(new Date());
        }

        transaction.setUser(user);
        bankTransactionRepository.save(transaction);
        userRepository.save(user);


        log.info("Transaction enregistrée avec succès.");

        return transaction;
    }
}
