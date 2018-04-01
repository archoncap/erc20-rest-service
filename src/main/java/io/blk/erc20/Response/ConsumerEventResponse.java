package io.blk.erc20.Response;

import io.blk.erc20.generated.Consumer;
import io.blk.erc20.generated.HumanStandardToken;

import java.math.BigInteger;

public class ConsumerEventResponse {
    private long value;

    ConsumerEventResponse(){};

    public ConsumerEventResponse(
            Consumer.CallResultEventResponse consumerEventResponse) {
        this.value = consumerEventResponse._value.longValueExact();
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
