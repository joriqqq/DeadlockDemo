package deadlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DeadlockDemo {

    private static final int NUM_ACCOUNTS = 2;
    private static final int NUM_THREADS = 20;
    private static final int NUM_ITERATIONS = 10000;

    static final Random rnd = new Random();

    List<Account> accounts = new ArrayList<Account>();

    public static void main(String args[]) {

        DeadlockDemo demo = new DeadlockDemo();
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
                    boolean result = transfer(account2, account1, amount);
                    System.out.print(result ? "+" : "-");
                }
            }
            System.out.println("Thread Complete: " + threadNum);
        }

        private boolean transfer(Account fromAccount, Account toAccount, int transferAmount) {
            try {
                Thread.sleep(3);
            } catch (InterruptedException e) {
                System.out.println(e);
            }

            synchronized (fromAccount) {
                synchronized (toAccount) {
                    if (fromAccount.withdraw(transferAmount)) {
                        toAccount.deposit(transferAmount);
                        return true;
                    }
                    return false;
                }
            }
        }
    }
}

