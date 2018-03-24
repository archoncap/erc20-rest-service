package io.blk.erc20.Response;

import io.blk.erc20.generated.HumanStandardToken;

public class ApprovalEventResponse {
    private String owner;
    private String spender;
    private long value;

    public ApprovalEventResponse() { }

    public ApprovalEventResponse(
            HumanStandardToken.ApprovalEventResponse approvalEventResponse) {
        this.owner = approvalEventResponse._owner;
        this.spender = approvalEventResponse._spender;
        this.value = approvalEventResponse._value.longValueExact();
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSpender() {
        return spender;
    }

    public void setSpender(String spender) {
        this.spender = spender;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
