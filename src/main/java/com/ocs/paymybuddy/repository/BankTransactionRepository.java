package com.ocs.paymybuddy.repository;

import com.ocs.paymybuddy.model.BankTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankTransactionRepository extends JpaRepository<BankTransaction, Integer> {

}