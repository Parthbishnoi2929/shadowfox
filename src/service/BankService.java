package service;
import model.BankAccount;
import java.util.HashMap;
import java.util.Map;
public class BankService {
    private final Map<String, BankAccount> accounts = new HashMap<>();
    public void createAccount(String accNo, String holder, double openingBalance) {
        if (accounts.containsKey(accNo)) {
            throw new IllegalArgumentException("Account already exists.");
        }
        accounts.put(accNo, new BankAccount(accNo, holder, openingBalance));
    }
    public BankAccount getAccount(String accNo) {
        BankAccount account = accounts.get(accNo);
        if (account == null) {
            throw new IllegalArgumentException("Account not found.");}
        return account;
    }
    public void deposit(String accNo, double amount) {
        getAccount(accNo).deposit(amount);
    }
    public void withdraw(String accNo, double amount) {
        getAccount(accNo).withdraw(amount);
    }
    public double checkBalance(String accNo) {
        return getAccount(accNo).getBalance();
    }
    public void printTransactions(String accNo) {
        BankAccount acc = getAccount(accNo);
        System.out.println("Transaction history for " + acc.getAccountHolder() + ":");
        acc.getTransactionHistory().forEach(System.out::println);
    }
}
