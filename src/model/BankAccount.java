package model;
import java.util.ArrayList;
import java.util.List;
public class BankAccount {
    private final String accountNumber;
    private final String accountHolder;
    private double balance;
    private final List<String> transactions = new ArrayList<>();
    public BankAccount(String accountNumber, String accountHolder, double openingBalance) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = openingBalance;
        transactions.add("Account opened with balance: " + openingBalance);}
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            transactions.add("Deposited: " + amount);
        } else {
            throw new IllegalArgumentException("Deposit amount must be positive."); }    }
    public void withdraw(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Withdrawal amount must be positive.");
        if (amount > balance) throw new IllegalArgumentException("Insufficient balance.");
        balance -= amount;
        transactions.add("Withdrawn: " + amount);
    }
    public double getBalance() {
        return balance;
    }
    public List<String> getTransactionHistory() {
        return transactions;
    }
    public String getAccountNumber() {
        return accountNumber;
    }
    public String getAccountHolder() {
        return accountHolder;
    }
}

