package io.blk.erc20.Request;

import java.math.BigInteger;

public class ApproveAndCallRequest {
    String spender="";
    BigInteger value=null;
    String extraData="";

    public String getSpender() {
        return spender;
    }

    public BigInteger getValue() {
        return value;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setSpender(String spender) {
        this.spender = spender;
    }

    public void setValue(BigInteger value) {
        this.value = value;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }
}
