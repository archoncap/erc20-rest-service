package io.blk.erc20.Request;

public class ConsumerRequest {
    String consumerAddr;
    String infoFeedAddr;

    public String getConsumerAddr() {
        return consumerAddr;
    }

    public void setConsumerAddr(String consumerAddr) {
        this.consumerAddr = consumerAddr;
    }

    public String getInfoFeedAddr() {
        return infoFeedAddr;
    }

    public void setInfoFeedAddr(String infoFeedAddr) {
        this.infoFeedAddr = infoFeedAddr;
    }
}
