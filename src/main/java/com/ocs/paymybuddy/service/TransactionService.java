package com.ocs.paymybuddy.service;

import com.ocs.paymybuddy.model.Transaction;
import com.ocs.paymybuddy.model.User;
import com.ocs.paymybuddy.repository.TransactionRepository;
import com.ocs.paymybuddy.repository.UserRepository;
import io.swagger.models.Swagger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Transactional
@Service
public class TransactionService {

    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);




    @Autowired
    private TransactionRepository transactionRepository;



    public void effectuerPaiement(Transaction transaction) {

        User sender = transaction.getSender();
        User receiver = transaction.getReceiver();


        if (sender != null && receiver != null) {


            float amount = transaction.getAmount();
            if (sender.getBalance() >= amount) {


                float commission = calculateCommission(transaction);
                transaction.setCommission(commission);


                float totalAmount = amount + commission;
                sender.setBalance(sender.getBalance() - totalAmount);
                receiver.setBalance(receiver.getBalance() + amount);
                transaction.setDate(new Date());
                transactionRepository.save(transaction);

                log.info("Transfert effectué avec succès. Montant: {}, De: {}, À: {}, Description: {}",
                        transaction.getAmount(), sender.getEmail(), receiver.getEmail(), transaction.getDescription());

            } else {
                throw new RuntimeException("Solde insuffisant pour effectuer le paiement.");
            }
        } else {

            String errorMessage;
            if (sender == null && receiver == null) {
                errorMessage = "L'expéditeur et le destinataire de la transaction sont nuls.";
            } else if (sender == null) {
                errorMessage = "L'expéditeur de la transaction est nul.";
            } else {
                errorMessage = "Le destinataire de la transaction est nul.";
            }
            throw new RuntimeException(errorMessage);
        }
    }



    private float calculateCommission(Transaction transaction) {
        return transaction.getAmount() * 0.05f;
    }

    public List<Transaction> findAllTransactionsSentByUser(int userId) {

        return transactionRepository.findAllBySender_Id(userId);
    }

    public List<Transaction> findAllTransactionsReceivedByUser(int userId) {
        return transactionRepository.findAllByReceiver_Id(userId);
    }

    public List<Transaction> findAllTransactions() {
        return transactionRepository.findAll();
    }


}