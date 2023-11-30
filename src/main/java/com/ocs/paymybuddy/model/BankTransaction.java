package com.ocs.paymybuddy.model;

import lombok.Data;
import lombok.Getter;

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
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "bank_account_id")
    private BankAccount bankAccount;

    @Column(name = "date")
    private Date date;

    @Column(name = "amountbank")
    private float amountbank;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getAmountbank() {
        return amountbank;
    }

    public void setAmountbank(float amountbank) {
        this.amountbank = amountbank;
    }

    public boolean isCredit() {
        return credit;
    }

    public void setCredit(boolean credit) {
        this.credit = credit;
    }

    @Column(name = "credit")
    private boolean credit;


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