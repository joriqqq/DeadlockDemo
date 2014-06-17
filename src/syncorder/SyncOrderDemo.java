package syncorder;

import deadlock.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SyncOrderDemo {

    private static final int NUM_ACCOUNTS = 100;
    private static final int NUM_THREADS = 20;
    private static final int NUM_ITERATIONS = 100000;

    static final Random rnd = new Random();

    List<Account> accounts = new ArrayList<Account>();

    public static void main(String args[]) {

        SyncOrderDemo demo = new SyncOrderDemo();
        demo.setUp();
        demo.run();
    }

    void setUp() {
        for (int i = 0; i < NUM_ACCOUNTS; i++) {
            Account account = new Account(i, rnd.nextInt(1000));
            accounts.add(account);
        }
    }

    void run() {

        for (int i = 0; i < NUM_THREADS; i++) {
            new BadTransferOperation(i).start();
        }
    }

    class BadTransferOperation extends Thread {

        int threadNum;

        BadTransferOperation(int threadNum) {
            this.threadNum = threadNum;
        }

        @Override
        public void run() {
            for (int i = 0; i < NUM_ITERATIONS; i++) {
                Account account1 = accounts.get(rnd.nextInt(NUM_ACCOUNTS));
                Account account2 = accounts.get(rnd.nextInt(NUM_ACCOUNTS));
                int amount = rnd.nextInt(1000);
                if (!account1.equals(account2)) {
                    boolean result1 = transfer(account2, account1, amount);
                    boolean result2 = transfer(account1, account2, amount);
                    System.out.print((result1 ? "+" : "-") + (result2 ? "+" : "-") + " ");
                }
            }
            System.out.println("Thread Complete: " + threadNum);
        }

        private boolean transfer(Account fromAccount, Account toAccount, int transferAmount) {
            synchronized (fromAccount) {
                synchronized (toAccount) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }
                    if (fromAccount.withdraw(transferAmount)) {
                        toAccount.deposit(transferAmount);
                        return true;
                    }
                }
            }
            return false;
        }
    }
}

