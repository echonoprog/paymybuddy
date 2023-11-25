package com.ocs.paymybuddy.service;

import com.ocs.paymybuddy.model.Transaction;
import com.ocs.paymybuddy.model.User;
import com.ocs.paymybuddy.repository.TransactionRepository;
import com.ocs.paymybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Transactional
@Service
public class TransactionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public void effectuerPaiement(Transaction transaction) {
        // Récupération des utilisateurs impliqués dans la transaction
        User sender = transaction.getSender();
        User receiver = transaction.getReceiver();

        // Vérification si le destinataire est dans la liste des contacts de l'expéditeur
        if (sender.getContacts().contains(receiver)) {
            // Vérification si le solde de l'expéditeur est suffisant
            float amount = transaction.getAmount();
            if (sender.getBalance() >= amount) {
                // Calcul et ajout de la commission en utilisant la transaction
                float commission = calculateCommission(transaction);
                transaction.setCommission(commission);

                // Mise à jour des soldes
                float totalAmount = amount + commission;
                sender.setBalance(sender.getBalance() - totalAmount);
                receiver.setBalance(receiver.getBalance() + amount);

                // Enregistrement de la transaction et mise à jour des utilisateurs
                transaction.setDate(new Date());
                transactionRepository.save(transaction);

            } else {
                throw new RuntimeException("Solde insuffisant pour effectuer le paiement.");
            }
        } else {
            throw new RuntimeException("Le destinataire n'est pas dans la liste des contacts de l'expéditeur.");
        }
    }

    private float calculateCommission(Transaction transaction) {
        // Calcul de la commission à 5% basée sur les attributs de la transaction
        return transaction.getAmount() * 0.05f;
    }

    public List<Transaction> findAllTransactionsSentByUser(int userId) {
        // Récupérer toutes les transactions où l'utilisateur est l'émetteur
        return transactionRepository.findAllBySender_Id(userId);
    }

    public List<Transaction> findAllTransactionsReceivedByUser(int userId) {
        // Récupérer toutes les transactions où l'utilisateur est le récepteur
        return transactionRepository.findAllByReceiver_Id(userId);
    }

    public List<Transaction> findAllTransactions() {
        // Récupérer toutes les transactions
        return transactionRepository.findAll();
    }


}
