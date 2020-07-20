package blockchain;

import java.io.Serializable;
import java.util.ArrayList;

public class BlockChain implements Serializable {

    private static final long serialVersionUID = 3L;




    private int size;
    private ArrayList<Block> chain;
    private int zeros;
    private String message = "";


    public BlockChain(int zeros) {
        this.size = 0;
        this.chain = new ArrayList<>();
        this.zeros = zeros;
    }

    public ArrayList<Block> getChain() {
        return chain;
    }

    public Block generate(String m) {
        String previousHash;
        if (chain.size() == 0) {
            previousHash = "0";
        } else {
            previousHash = chain.get(size - 1).getHash();
        }

        String name = Thread.currentThread().getName();
        String miner = Character.toString(name.charAt(name.length()-1));

        Block block = new Block(size+1, previousHash, zeros, miner, m);
        return block;

    }

    public void addToChain(Block block) {
        chain.add(block);
        size += 1;

        if (block.getTimeGeneration() < 1) {
            zeros += 1;
        } else if (block.getTimeGeneration() > 60) {
            zeros -= 1;
        }
    }

    public synchronized String getMessage() {
        return message;
    }

    public synchronized void addMessage(String m) {
        message += m;
    }

    public synchronized void resetMessage() {
        message = "";
    }

    public boolean validate() {
        String hash1 = chain.get(size-1).getPreviousHash();
        if (hash1.equals("0")) {
            return Block.firstZeros(chain.get(0).getHash(), chain.get(0).getZeros());
        }

        if (!validateBlock(chain.get(size-1))) {
            return false;
        }

        String hash2 = chain.get(size-2).getHash();

        for (int i = size-2; i > 0; i--) {
            if (!hash1.equals(hash2)) {
                return false;
            }
            if (!validateBlock(chain.get(i))) {
                return false;
            }
            if (!validateBlock(chain.get(i-1))) {
                return false;
            }

            hash1 = chain.get(i).getPreviousHash();
            hash2 = chain.get(i-1).getHash();

        }

        if (!chain.get(0).getPreviousHash().equals("0")) {
            return false;
        }
        return true;
    }

    private boolean validateBlock(Block block) {
        String findHash = StringUtil.applySha256(block.getString(block.getMagicNumber()));
        String hash = block.getHash();
        if (!Block.firstZeros(hash, block.getZeros())) {
            return false;
        }
        return findHash.equals(hash);
    }

    public int getZeros() {
        return zeros;
    }
}
