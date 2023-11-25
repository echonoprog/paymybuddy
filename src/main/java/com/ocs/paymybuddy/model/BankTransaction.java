package com.ocs.paymybuddy.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "bank_transaction")
public class BankTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "bank_account_id")
    private BankAccount bankAccount;

    @Column(name = "date")
    private Date date;

    @Column(name = "amountbank")
    private float amountbank;

    @Column(name = "credit")
    private boolean credit;

    // Getters and Setters

    @Override
    public String toString() {
        return "BankTransaction{" +
                "id=" + id +
                ", bankAccount=" + bankAccount +
                ", date=" + date +
                ", amountBank=" + amountbank +
                ", credit=" + credit +
                '}';
    }
}
