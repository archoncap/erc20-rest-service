package io.blk.erc20.Response;

import io.blk.erc20.generated.HumanStandardToken;

public class TransferEventResponse {
    private String from;
    private String to;
    private long value;

    public TransferEventResponse() { }

    public TransferEventResponse(
            HumanStandardToken.TransferEventResponse transferEventResponse) {
        this.from = transferEventResponse._from;
        this.to = transferEventResponse._to;
        this.value = transferEventResponse._value.longValueExact();
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
