package io.blk.erc20.Request;

import java.math.BigInteger;

public class TransferFromRequest {
    String from="";
    String to="";
    BigInteger value=null;

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public BigInteger getValue() {
        return value;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setValue(BigInteger value) {
        this.value = value;
    }
}
