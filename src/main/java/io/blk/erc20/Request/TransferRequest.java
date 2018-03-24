package io.blk.erc20.Request;

import java.math.BigInteger;

public class TransferRequest {
    String to="";
    BigInteger value=null;

    public String getTo() {
        return to;
    }

    public BigInteger getValue() {
        return value;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setValue(BigInteger value) {
        this.value = value;
    }
}
