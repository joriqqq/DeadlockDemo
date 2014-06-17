package deadlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SyncSeparatelyDemo {

    private static final int NUM_ACCOUNTS = 2;
    private static final int NUM_THREADS = 10;
    private static final int NUM_ITERATIONS = 1000;
    private static long start;

    static final Random rnd = new Random();

    List<Account> accounts = new ArrayList<Account>();

    public static void main(String args[]) {
        SyncSeparatelyDemo demo = new SyncSeparatelyDemo();
        demo.setUp();
        start = System.currentTimeMillis();
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
                int index = rnd.nextInt(NUM_ACCOUNTS);
                Account account1 = accounts.get(index);
                Account account2 = accounts.get(NUM_ACCOUNTS - index - 1);
                int amount = rnd.nextInt(1000);
                boolean result1 = transfer(account2, account1, amount);
//                    System.out.print((result1 ? "+" : "-") + " ");
            }
            System.out.println("Thread Complete: " + threadNum + ",duration = " + (System.currentTimeMillis() - start));
        }

        private boolean transfer(Account fromAccount, Account toAccount, int transferAmount) {

            boolean withdrawOk;
            synchronized (fromAccount) {

                try {
                    Thread.sleep(3);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }

                withdrawOk = fromAccount.withdraw(transferAmount);
            }
            if (withdrawOk) {
                synchronized (toAccount) {
                    toAccount.deposit(transferAmount);
                }
            }
            return withdrawOk;
        }
    }
}

