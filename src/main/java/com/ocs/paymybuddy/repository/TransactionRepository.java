package com.ocs.paymybuddy.repository;

import com.ocs.paymybuddy.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findAllBySender_Id(int senderId);
    List<Transaction> findAllByReceiver_Id(int receiverId);
    Page<Transaction> findAllBySender_IdOrReceiver_Id(int senderId, int receiverId, Pageable pageable);

}
