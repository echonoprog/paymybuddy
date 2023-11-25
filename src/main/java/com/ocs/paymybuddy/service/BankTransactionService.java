package com.ocs.paymybuddy.service;

import com.ocs.paymybuddy.model.BankTransaction;
import com.ocs.paymybuddy.model.User;
import com.ocs.paymybuddy.repository.BankTransactionRepository;
import com.ocs.paymybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BankTransactionService {

    @Autowired
    private BankTransactionRepository bankTransactionRepository;

    @Autowired
    private UserRepository userRepository;

    public BankTransaction save(BankTransaction transaction) {
        // Vérifier si la transaction ou l'utilisateur associé est nul
        if (transaction == null || transaction.getBankAccount() == null || transaction.getBankAccount().getUser() == null) {
            return null;
        }

        // Récupérer l'utilisateur associé à la transaction
        User user = transaction.getBankAccount().getUser();

        // Vérifier si la transaction est un crédit ou un débit
        float transactionAmount = transaction.getAmountbank();
        if (transaction.isCredit()) {
            // Calculer le nouveau solde en cas de crédit
            float nouveauSolde = user.getBalance() + transactionAmount;
            user.setBalance(nouveauSolde);
        } else {
            // Calculer le nouveau solde en cas de débit
            float nouveauSolde = user.getBalance() - transactionAmount;
            if (nouveauSolde < 0) {
                // Solde insuffisant, transaction annulée
                return null;
            }
            user.setBalance(nouveauSolde);
        }

        // Définir la date de la transaction à la date actuelle si elle n'est pas déjà définie
        if (transaction.getDate() == null) {
            transaction.setDate(new Date());
        }

        // Enregistrer la transaction dans la base de données
        bankTransactionRepository.save(transaction);

        // Mettre à jour le solde de l'utilisateur dans la base de données
        userRepository.save(user);

        return transaction;
    }
}
