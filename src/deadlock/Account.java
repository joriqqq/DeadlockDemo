package deadlock;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class Account {

    private final int id;
    private volatile int balance;
    private final AtomicIntegerFieldUpdater updater;

    public Account(int id, int openingBalance) {
        this.id = id;
        this.balance = openingBalance;
        updater = AtomicIntegerFieldUpdater.newUpdater(Account.class, "balance");
    }

    public boolean withdraw(int amount) {
        if (amount > balance) {
            return false;
        }
        balance -= amount;
        return true;
    }

    public void deposit(int amount) {
        balance += amount;
    }

    public int getId() {
        return id;
    }

    public AtomicIntegerFieldUpdater getUpdater() {
        return updater;
    }
}