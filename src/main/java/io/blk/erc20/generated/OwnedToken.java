package io.blk.erc20.generated;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
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
public class OwnedToken extends Contract {
    private static final String BINARY = "6060604052341561000f57600080fd5b6040516020806102598339810160405280805160018054600160a060020a033316600160a060020a0319918216811790925560008054909116909117905560025550506101f8806100616000396000f30060606040526004361061004b5763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631a6952308114610050578063898855ed1461007e575b600080fd5b341561005b57600080fd5b61007c73ffffffffffffffffffffffffffffffffffffffff60043516610094565b005b341561008957600080fd5b61007c6004356101a3565b6001543373ffffffffffffffffffffffffffffffffffffffff9081169116146100bc576101a0565b60005460015473ffffffffffffffffffffffffffffffffffffffff9182169163c3cee9c19116836040517c010000000000000000000000000000000000000000000000000000000063ffffffff851602815273ffffffffffffffffffffffffffffffffffffffff928316600482015291166024820152604401602060405180830381600087803b151561014e57600080fd5b5af1151561015b57600080fd5b50505060405180519050156101a0576001805473ffffffffffffffffffffffffffffffffffffffff191673ffffffffffffffffffffffffffffffffffffffff83161790555b50565b6000543373ffffffffffffffffffffffffffffffffffffffff908116911614156101a0576002555600a165627a7a72305820cdcd343a6291536796816f48aa1eed351ca9b24cc00209d78b20112abd429f8f0029";

    protected OwnedToken(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected OwnedToken(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<TransactionReceipt> transfer(String newOwner) {
        final Function function = new Function(
                "transfer", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(newOwner)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> changeName(byte[] newName) {
        final Function function = new Function(
                "changeName", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(newName)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<OwnedToken> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, byte[] _name) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_name)));
        return deployRemoteCall(OwnedToken.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<OwnedToken> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, byte[] _name) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_name)));
        return deployRemoteCall(OwnedToken.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static OwnedToken load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new OwnedToken(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static OwnedToken load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new OwnedToken(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }
}
