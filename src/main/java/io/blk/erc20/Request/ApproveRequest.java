package io.blk.erc20.Request;

import java.math.BigInteger;

public class ApproveRequest {
    String spender="";
    BigInteger value=null;

    public String getSpender() {
        return spender;
    }

    public BigInteger getValue() {
        return value;
    }

    public void setSpender(String spender) {
        this.spender = spender;
    }

    public void setValue(BigInteger value) {
        this.value = value;
    }
}
