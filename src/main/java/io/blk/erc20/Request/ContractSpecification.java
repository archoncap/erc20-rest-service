package io.blk.erc20.Request;

import java.math.BigInteger;

public class ContractSpecification {
    BigInteger initialAmount=null;
    String tokenName="";
    BigInteger decimalUnits=null;
    String tokenSymbol="";

    public BigInteger getInitialAmount() {
        return initialAmount;
    }

    public String getTokenName() {
        return tokenName;
    }

    public BigInteger getDecimalUnits() {
        return decimalUnits;
    }

    public String getTokenSymbol() {
        return tokenSymbol;
    }

    public void setInitialAmount(BigInteger initialAmount) {
        this.initialAmount = initialAmount;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public void setDecimalUnits(BigInteger decimalUnits) {
        this.decimalUnits = decimalUnits;
    }

    public void setTokenSymbol(String tokenSymbol) {
        this.tokenSymbol = tokenSymbol;
    }
}
