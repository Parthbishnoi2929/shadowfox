package Test;

import model.BankAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class BankAccountTest {
        private BankAccount account;

        @BeforeEach
        void setUp() {
            account = new BankAccount("1001", "Test User", 1000);
        }

        @Test
        void testDeposit() {
            account.deposit(500);
            assertEquals(1500, account.getBalance());
        }

        @Test
        void testWithdraw() {
            account.withdraw(400);
            assertEquals(600, account.getBalance());
        }

        @Test
        void testWithdrawInsufficientFunds() {
            Exception ex = assertThrows(IllegalArgumentException.class, () -> account.withdraw(1200));
            assertEquals("Insufficient balance.", ex.getMessage());
        }

        @Test
        void testDepositNegativeAmount() {
            Exception ex = assertThrows(IllegalArgumentException.class, () -> account.deposit(-100));
            assertEquals("Deposit amount must be positive.", ex.getMessage());
        }

        @Test
        void testTransactionHistory() {
            account.deposit(300);
            account.withdraw(200);
            assertEquals(3, account.getTransactionHistory().size());
        }
}


