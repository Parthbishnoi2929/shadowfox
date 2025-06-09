
import ui.StudentGUI;
import service.BankService;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        new StudentGUI();

        BankService bank = new BankService();

        // Sample usage (just like a test run)
        bank.createAccount("1001", "Parth Bishnoi", 1000);
        bank.deposit("1001", 500);
        bank.withdraw("1001", 200);
        System.out.println("Balance: " + bank.checkBalance("1001"));
        bank.printTransactions("1001");
    }
}