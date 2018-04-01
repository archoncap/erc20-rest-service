package io.blk.erc20.Service;


import io.blk.erc20.Response.ApprovalEventResponse;
import io.blk.erc20.Response.ConsumerEventResponse;
import io.blk.erc20.Response.TransactionResponse;
import io.blk.erc20.Response.TransferEventResponse;
import io.blk.erc20.generated.Consumer;
import io.blk.erc20.generated.HumanStandardToken;
import io.blk.erc20.generated.InfoFeed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.ManagedTransaction;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

import static org.web3j.tx.Contract.GAS_LIMIT;
import static org.web3j.tx.ManagedTransaction.GAS_PRICE;

@Service
public class ConsumerService {
    private final Web3j web3j;

    private final Credentials credentials;


    private static final Logger log = LoggerFactory.getLogger(ConsumerService.class);

    @Autowired
    public ConsumerService() throws IOException, CipherException {
        // We start by creating a new web3j instance to connect to remote nodes on the network.
        // Note: if using web3j Android, use Web3jFactory.build(...
        web3j = Web3j.build(new HttpService(
                "https://rinkeby.infura.io/gGCREfRc6e4U7GRIB43x"));  //  Enter your Infura token here;
        credentials =
                WalletUtils.loadCredentials(
                        "123456",
                        "src/main/resources/cre.json");
        log.info("Credentials loaded");
    }

    public String deploy() throws Exception {
        try {
            Consumer consumer = Consumer.deploy(web3j, credentials,
                    ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT).send();
            return consumer.getContractAddress();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public String setFeed(String consumerAddr,String infoFeedAddr) throws Exception{
        Consumer consumer = load(consumerAddr);
        try {
            TransactionReceipt transactionReceipt = consumer
                    .setFeed(infoFeedAddr).send();
            return transactionReceipt.toString();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public TransactionResponse<ConsumerEventResponse> callContract(String consumerAddr) throws Exception {
        Consumer consumer = load(consumerAddr);
        try{
            TransactionReceipt transactionReceipt = consumer
                    .callContract().send();
            return processConsumerEventsResponse(consumer, transactionReceipt);
        }catch(InterruptedException | ExecutionException e){
            throw new RuntimeException(e);
        }
    }


    private Consumer load(String contractAddress) {
        return Consumer.load(contractAddress,web3j,credentials,GAS_PRICE, GAS_LIMIT);
    }

    private long extractLongValue(BigInteger value) {
        return value.longValueExact();
    }


    private TransactionResponse<ConsumerEventResponse>
    processConsumerEventsResponse(
            Consumer  consumer,
            TransactionReceipt transactionReceipt) {

        return processEventResponse(
                consumer.getCallResultEvents(transactionReceipt),
                transactionReceipt,
                ConsumerEventResponse::new);
    }

    private <T, R> TransactionResponse<R> processEventResponse(
            List<T> eventResponses, TransactionReceipt transactionReceipt, Function<T, R> map) {
        if (!eventResponses.isEmpty()) {
            return new TransactionResponse<>(
                    transactionReceipt.getTransactionHash(),
                    map.apply(eventResponses.get(0)));
        } else {
            return new TransactionResponse<>(
                    transactionReceipt.getTransactionHash());
        }
    }
}
