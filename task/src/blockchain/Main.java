
package blockchain;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

public class Main {

    private final static int NUMBER_OF_TASKS = 5;

    public static void main(String[] args) throws ExecutionException, InterruptedException  {

        BlockChain blockChain = new BlockChain(0);

        int poolSize = Runtime.getRuntime().availableProcessors() - 3;
        ExecutorService executor;
        Set<Callable<Block>> callables;

        Client client1 = new Client("Tirion", blockChain);
        Client client2 = new Client("Cypher", blockChain);
        client1.start();
        client2.start();

        for (int i = 0; i < NUMBER_OF_TASKS; i++) {
            executor = Executors.newFixedThreadPool(poolSize);
            callables = new HashSet<>();
            String m = blockChain.getMessage();
            blockChain.resetMessage();
            for (int j = 0; j < poolSize; j++) {
                callables.add(() -> blockChain.generate(m));
            }
            Block block = executor.invokeAny(callables);
            blockChain.addToChain(block);

            executor.shutdownNow();

        }

        for (Block blockk: blockChain.getChain()) {
            System.out.println(blockk.toString());
        }


    }


}
