package io.blk.erc20.generated;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.3.1.
 */
public class InfoFeed extends Contract {
    private static final String BINARY = "60606040526103e8600055341561001557600080fd5b60c0806100236000396000f30060606040526004361060485763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631e105ec18114604d578063fb2f26d614606f575b600080fd5b3415605757600080fd5b605d607f565b60405190815260200160405180910390f35b3415607957600080fd5b605d608e565b60008054606490810190915590565b600054815600a165627a7a723058202562fd322dfd48648bd05e100be66e8abbdc2aa79c6d54c851815ac2dc8c5a5f0029";

    protected InfoFeed(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected InfoFeed(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<TransactionReceipt> contractCall() {
        final Function function = new Function(
                "contractCall", 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> testWrite() {
        final Function function = new Function("testWrite", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public static RemoteCall<InfoFeed> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(InfoFeed.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<InfoFeed> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(InfoFeed.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static InfoFeed load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new InfoFeed(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static InfoFeed load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new InfoFeed(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }
}
