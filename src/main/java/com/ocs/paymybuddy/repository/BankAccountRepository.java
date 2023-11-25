package com.ocs.paymybuddy.repository;

import com.ocs.paymybuddy.model.BankAccount;
import com.ocs.paymybuddy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {

    List<BankAccount> findByUser(User user);

}