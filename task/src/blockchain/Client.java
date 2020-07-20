package blockchain;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Client extends Thread  {

    private final String name;
    private BlockChain blockChain;
    private int messagesCount = 0;

    public Client(String name, BlockChain blockChain) {
        this.name = name;
        this.blockChain = blockChain;
    }

    public String getClientName() {
        return name;
    }

    @Override
    public void run() {

        while (messagesCount < 10) {
            try {
                TimeUnit.MILLISECONDS.sleep(10 + ThreadLocalRandom.current().nextLong(100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            messagesCount += 1;

            String text = "Message #" + messagesCount;

            blockChain.addMessage("\n" +
                    getClientName() + ": " + text);

        }
    }

}
