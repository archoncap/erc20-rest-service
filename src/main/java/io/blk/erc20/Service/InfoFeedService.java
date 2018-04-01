package io.blk.erc20.Service;

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

import javax.sound.sampled.Line;
import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

import static org.web3j.tx.Contract.GAS_LIMIT;
import static org.web3j.tx.ManagedTransaction.GAS_PRICE;

@Service
public class InfoFeedService {
    private final Web3j web3j;

    private final Credentials credentials;


    private static final Logger log = LoggerFactory.getLogger(InfoFeedService.class);

    @Autowired
    public InfoFeedService() throws IOException, CipherException {
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
            InfoFeed infoFeed = InfoFeed.deploy(web3j, credentials,
                    ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT).send();
            return infoFeed.getContractAddress();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }



    public long testWrite(String contractAddress) throws Exception {
        InfoFeed infoFeed = load(contractAddress);
        try {
            return extractLongValue(infoFeed.testWrite().send());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }


    public String callContract(String infoFeedAddr) throws Exception{
        InfoFeed infoFeed = load(infoFeedAddr);
        try {
            TransactionReceipt transactionReceipt = infoFeed.contractCall().send();
            return transactionReceipt.toString();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }


    private InfoFeed load(String contractAddress) {
        return InfoFeed.load(contractAddress,web3j,credentials,GAS_PRICE, GAS_LIMIT);
    }

    private long extractLongValue(BigInteger value) {
        return value.longValueExact();
    }
}
