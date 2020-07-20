package blockchain;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class Block implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int id;
    private final long timeStamp;
    private final long timeGeneration;
    private final String previousHash;
    private final String hash;
    private final long magicNumber;
    private final int zeros;
    private final String miner;
    private final String messages;

    public Block(int id, String previousHash, int zeros, String miner, String messages) {
        this.id = id;
        this.timeStamp = new Date().getTime();
        this.previousHash = previousHash;
        this.zeros = zeros;
        this.miner = miner;
        this.messages = messages;

        Object[] hashAmdMagic = findHashAndMagicNumber();
        this.hash = (String) hashAmdMagic[1];
        this.magicNumber = (long) hashAmdMagic[0];

        long endTime = new Date().getTime();
        this.timeGeneration = getSeconds(timeStamp, endTime);
    }

    public String getString(long magic) {
        return id + " " + timeStamp + " " + previousHash + " " + zeros +
                " " + messages + " " + magic;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getHash() {
        return hash;
    }

    public long getMagicNumber() {
        return magicNumber;
    }

    @Override
    public String toString() {
        String increasing = "";
        if (timeGeneration < 1) {
            increasing = "N was increaser to " + (zeros + 1) + "\n";
        } else if (timeGeneration > 60) {
            increasing = "N was decreased to " + (zeros - 1) + "\n";
        } else {
            increasing = "N stays the same\n";
        }
        return "Block:\n" +
                "Created by miner # " + miner + "\n" +
                "Id: " + id + "\n" +
                "Timestamp: " + timeStamp + "\n" +
                "Magic number: " + magicNumber + "\n" +
                "Hash of the previous block:\n" + previousHash + "\n" +
                "Hash of the block: \n" + hash + "\n" +
                "Block data: " + getMessage() + "\n" +
                "Block was generating for " + timeGeneration + " seconds\n" +
                increasing;
    }

    private Object[] findHashAndMagicNumber() {
        long magic = ThreadLocalRandom.current().nextLong();
        String hash = StringUtil.applySha256(getString(magic));
        while (!firstZeros(hash, zeros)) {
            magic = ThreadLocalRandom.current().nextLong();
            hash = StringUtil.applySha256(getString(magic));
            if(Thread.currentThread().isInterrupted()){
                return null;
            }
        }

        Object[] hashAndMagic = new Object[2];
        hashAndMagic[0] = magic;
        hashAndMagic[1] = hash;
        return hashAndMagic;

    }

    static boolean firstZeros(String hash, int zeros) {
        for (int i = 0; i < zeros; i++) {
            if (hash.charAt(i) != '0') {
                return false;
            }
        }
        return true;
    }

    public static long getSeconds(long startTime, long endTime) {
        long dif = endTime - startTime;
        return dif / 1000;
    }

    public int getZeros() {
        return zeros;
    }

    public long getTimeGeneration() {
        return timeGeneration;
    }

    private String getMessage() {
        if (messages.equals("")) {
            return "no messages";
        } else {
            return messages;
        }
    }

}
