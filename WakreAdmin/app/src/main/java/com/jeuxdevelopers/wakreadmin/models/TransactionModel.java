package com.jeuxdevelopers.wakreadmin.models;

import com.jeuxdevelopers.wakreadmin.enums.TransactionState;
import com.jeuxdevelopers.wakreadmin.enums.TransactionType;

import java.io.Serializable;
import java.util.List;

public class TransactionModel implements Serializable {
    private String transactionId, notes;
    private double amount;
    private TransactionUser sender;
    private TransactionUser receiver;
    private TransactionType type;
    private TransactionState state;
    private List<String> users;
    private long date;

    public TransactionModel() {
    }


    public TransactionModel(String transactionId, double amount, TransactionUser sender,
                            TransactionUser receiver, TransactionType type, TransactionState state, long date) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.sender = sender;
        this.receiver = receiver;
        this.type = type;
        this.state = state;
        this.date = date;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public TransactionUser getSender() {
        return sender;
    }

    public void setSender(TransactionUser sender) {
        this.sender = sender;
    }

    public TransactionUser getReceiver() {
        return receiver;
    }

    public void setReceiver(TransactionUser receiver) {
        this.receiver = receiver;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public TransactionState getState() {
        return state;
    }

    public void setState(TransactionState state) {
        this.state = state;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}
