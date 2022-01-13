package net.subsquid.indexerapi.dto;

public class SubsquidIndexerStatusDto {

    private int chainHeight;
    private int head;
    private String hydraVersion;
    private boolean inSync;
    private int lastComplete;
    private int maxComplete;

    public int getChainHeight() {
        return chainHeight;
    }

    public void setChainHeight(int chainHeight) {
        this.chainHeight = chainHeight;
    }

    public int getHead() {
        return head;
    }

    public void setHead(int head) {
        this.head = head;
    }

    public String getHydraVersion() {
        return hydraVersion;
    }

    public void setHydraVersion(String hydraVersion) {
        this.hydraVersion = hydraVersion;
    }

    public boolean isInSync() {
        return inSync;
    }

    public void setInSync(boolean inSync) {
        this.inSync = inSync;
    }

    public int getLastComplete() {
        return lastComplete;
    }

    public void setLastComplete(int lastComplete) {
        this.lastComplete = lastComplete;
    }

    public int getMaxComplete() {
        return maxComplete;
    }

    public void setMaxComplete(int maxComplete) {
        this.maxComplete = maxComplete;
    }
}
