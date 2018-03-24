package io.blk.erc20.Request;

public class AllowanceRequest {
    String ownerAddress="";
    String spenderAddress="";

    public String getOwnerAddress() {
        return ownerAddress;
    }

    public String getSpenderAddress() {
        return spenderAddress;
    }

    public void setOwnerAddress(String ownerAddress) {
        this.ownerAddress = ownerAddress;
    }

    public void setSpenderAddress(String spenderAddress) {
        this.spenderAddress = spenderAddress;
    }
}
