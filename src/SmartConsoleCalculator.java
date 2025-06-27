import java.util.InputMismatchException;
import java.util.Scanner;

public class SmartConsoleCalculator {

    private static final double USD_RATE = 83.2; // 1 USD = 83.2 INR

    // == Arithmetic ==

    private static void performArithmetic(Scanner sc) {
        System.out.print("Enter first number: ");
        double a = sc.nextDouble();

        System.out.print("Enter second number: ");
        double b = sc.nextDouble();

        System.out.println("Choose operation (+, -, *, /): ");
        char op = sc.next().charAt(0);

        switch (op) {
            case '+' -> System.out.println("Result: " + (a + b));
            case '-' -> System.out.println("Result: " + (a - b));
            case '*' -> System.out.println("Result: " + (a * b));
            case '/' -> {
                if (b == 0) System.out.println("Error: Division by zero.");
                else System.out.println("Result: " + (a / b));
            }
            default -> System.out.println("Invalid operator.");
        }
    }

    // == Scientific ==

    private static void performScientific(Scanner sc) {
        System.out.println("Choose function:");
        System.out.println("1. Square Root");
        System.out.println("2. Exponentiation (x^y)");
        System.out.println("3. Sine (in degrees)");
        System.out.println("4. Cosine (in degrees)");
        System.out.print("Your choice: ");
        int choice = sc.nextInt();

        switch (choice) {
            case 1 -> {
                System.out.print("Enter number: ");
                double num = sc.nextDouble();
                if (num < 0) System.out.println("Square root of negative number is imaginary.");
                else System.out.println("Result: " + Math.sqrt(num));
            }
            case 2 -> {
                System.out.print("Enter base: ");
                double base = sc.nextDouble();
                System.out.print("Enter exponent: ");
                double exp = sc.nextDouble();
                System.out.println("Result: " + Math.pow(base, exp));
            }
            case 3 -> {
                System.out.print("Enter angle in degrees: ");
                double degrees = sc.nextDouble();
                double radians = Math.toRadians(degrees);
                System.out.println("Result (sin): " + Math.sin(radians));
            }
            case 4 -> {
                System.out.print("Enter angle in degrees: ");
                double degrees = sc.nextDouble();
                double radians = Math.toRadians(degrees);
                System.out.println("Result (cos): " + Math.cos(radians));
            }
            default -> System.out.println("Invalid scientific operation.");
        }
    }

    // == Unit Conversion ==

    private static void performUnitConversion(Scanner sc) {
        System.out.println("Choose conversion:");
        System.out.println("1. Celsius to Fahrenheit");
        System.out.println("2. Fahrenheit to Celsius");
        System.out.println("3. INR to USD");
        System.out.println("4. USD to INR");
        System.out.print("Your choice: ");
        int choice = sc.nextInt();

        System.out.print("Enter value to convert: ");
        double value = sc.nextDouble();

        switch (choice) {
            case 1 -> System.out.println("Result: " + ((value * 9 / 5) + 32) + " °F");
            case 2 -> System.out.println("Result: " + ((value - 32) * 5 / 9) + " °C");
            case 3 -> System.out.println("Result: $" + (value / USD_RATE));
            case 4 -> System.out.println("Result: ₹" + (value * USD_RATE));
            default -> System.out.println("Invalid conversion choice.");
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            printMainMenu();
            try {
                choice = sc.nextInt();
                switch (choice) {
                    case 0 -> System.out.println("Exiting calculator. Goodbye!");
                    case 1 -> performArithmetic(sc);
                    case 2 -> performScientific(sc);
                    case 3 -> performUnitConversion(sc);
                    default -> System.out.println("Invalid choice. Please choose from the menu.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.next(); // clear the buffer
                choice = -1;
            }
        } while (choice != 0);

        sc.close();
    }

    private static void printMainMenu() {
        System.out.println("\n=== Smart Console Calculator ===");
        System.out.println("1. Basic Arithmetic");
        System.out.println("2. Scientific Calculations");
        System.out.println("3. Unit Conversions");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }
}
