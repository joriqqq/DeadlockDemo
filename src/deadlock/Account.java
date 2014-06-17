package deadlock;

public class Account {

    private final int number;

    private int balance;

    public Account(int number, int openingBalance) {
        this.number = number;
        this.balance = openingBalance;
    }

    public void withdraw(int amount) throws Exception {

        if (amount > balance) {
            throw new Exception("no cache");
        }

        balance -= amount;
    }

    public void deposit(int amount) {

        balance += amount;
    }

    public int getNumber() {
        return number;
    }

    public int getBalance() {
        return balance;
    }
}