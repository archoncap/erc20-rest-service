package io.blk.erc20;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

import io.blk.erc20.Response.ApprovalEventResponse;
import io.blk.erc20.Response.TransactionResponse;
import io.blk.erc20.Response.TransferEventResponse;
import io.blk.erc20.generated.HumanStandardToken;
import lombok.Getter;
import lombok.Setter;
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

import static org.web3j.tx.Contract.GAS_LIMIT;
import static org.web3j.tx.ManagedTransaction.GAS_PRICE;

/**
 * Our smart contract service.
 */
@Service
public class ContractService {

    private final Web3j web3j;

    private Credentials credentials;


    private static final Logger log = LoggerFactory.getLogger(ContractService.class);

    @Autowired
    public ContractService() throws IOException, CipherException {
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


    public String deploy(BigInteger initialAmount, String tokenName, BigInteger decimalUnits,
            String tokenSymbol) throws Exception {
        try {

            HumanStandardToken humanStandardToken = HumanStandardToken.deploy(
                    web3j, credentials,
                    ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT,
                    initialAmount,tokenName, decimalUnits,
                    tokenSymbol).send();
            return humanStandardToken.getContractAddress();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public String name(String contractAddress) throws Exception {
        HumanStandardToken humanStandardToken = load(contractAddress);
        try {
            return humanStandardToken.name().send();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public TransactionResponse<ApprovalEventResponse> approve( String contractAddress, String spender, BigInteger value) throws Exception {
        HumanStandardToken humanStandardToken = load(contractAddress);
        try {
            TransactionReceipt transactionReceipt = humanStandardToken
                    .approve(spender, value).send();
            return processApprovalEventResponse(humanStandardToken, transactionReceipt);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public long totalSupply(String contractAddress) throws Exception {
        HumanStandardToken humanStandardToken = load(contractAddress);
        try {
            return extractLongValue(humanStandardToken.totalSupply().send());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public TransactionResponse<TransferEventResponse> transferFrom(
            String contractAddress, String from, String to, BigInteger value) throws Exception {
        HumanStandardToken humanStandardToken = load(contractAddress);
        try {
            TransactionReceipt transactionReceipt = humanStandardToken
                    .transferFrom(from, to, value).send();
            return processTransferEventsResponse(humanStandardToken, transactionReceipt);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public long decimals(String contractAddress) throws Exception {
        HumanStandardToken humanStandardToken = load(contractAddress);
        try {
            return extractLongValue(humanStandardToken.decimals().send());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public String version(String contractAddress) throws Exception {
        HumanStandardToken humanStandardToken = load(contractAddress);
        try {
            return humanStandardToken.version().send();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public long balanceOf(String contractAddress, String ownerAddress) throws Exception {
        HumanStandardToken humanStandardToken = load(contractAddress);
        try {
            return extractLongValue(humanStandardToken.balanceOf(ownerAddress).send());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public String symbol(String contractAddress) throws Exception {
        HumanStandardToken humanStandardToken = load(contractAddress);
        try {
            return humanStandardToken.symbol().send();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public TransactionResponse<TransferEventResponse> transfer(String contractAddress, String to, BigInteger value) throws Exception {
        HumanStandardToken humanStandardToken = load(contractAddress);
        try {
            TransactionReceipt transactionReceipt = humanStandardToken
                    .transfer(to, value).send();
            return processTransferEventsResponse(humanStandardToken, transactionReceipt);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public TransactionResponse<ApprovalEventResponse> approveAndCall(
            String contractAddress, String spender, BigInteger value,
            String extraData) throws Exception {
        HumanStandardToken humanStandardToken = load(contractAddress);
        try {
            TransactionReceipt transactionReceipt = humanStandardToken
                    .approveAndCall(
                            spender, value,
                            extraData.getBytes())
                    .send();
            return processApprovalEventResponse(humanStandardToken, transactionReceipt);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public long allowance(String contractAddress, String ownerAddress, String spenderAddress) throws Exception {
        HumanStandardToken humanStandardToken = load(contractAddress);
        try {
            return extractLongValue(humanStandardToken.allowance(
                    ownerAddress, spenderAddress)
                    .send());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }


    private HumanStandardToken load(String contractAddress) {
        return HumanStandardToken.load(contractAddress,web3j,credentials,GAS_PRICE, GAS_LIMIT);
    }

    private long extractLongValue(BigInteger value) {
        return value.longValueExact();
    }

    private TransactionResponse<ApprovalEventResponse>
            processApprovalEventResponse(
            HumanStandardToken humanStandardToken,
            TransactionReceipt transactionReceipt) {

        return processEventResponse(
                humanStandardToken.getApprovalEvents(transactionReceipt),
                transactionReceipt,
                ApprovalEventResponse::new);
    }

    private TransactionResponse<TransferEventResponse>
            processTransferEventsResponse(
            HumanStandardToken humanStandardToken,
            TransactionReceipt transactionReceipt) {

        return processEventResponse(
                humanStandardToken.getTransferEvents(transactionReceipt),
                transactionReceipt,
                TransferEventResponse::new);
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
