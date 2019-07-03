package io.kauri.java.test;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.2.0.
 */
public class DocumentRegistry extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b50610335806100206000396000f3fe608060405234801561001057600080fd5b50600436106100365760003560e01c80636a33bf871461003b57806373ca4a5b146100bf575b600080fd5b6100ab6004803603602081101561005157600080fd5b81019060208101813564010000000081111561006c57600080fd5b82018360208201111561007e57600080fd5b803590602001918460018302840111640100000000831117156100a057600080fd5b50909250905061012f565b604080519115158252519081900360200190f35b6100ab600480360360208110156100d557600080fd5b8101906020810181356401000000008111156100f057600080fd5b82018360208201111561010257600080fd5b8035906020019184600183028401116401000000008311171561012457600080fd5b50909250905061020c565b6000808383604051602001808383808284376040805191909301818103601f19018252835280516020918201206000818152918290529290208054336001600160a01b031990911617815542600182015591955061019794505060020191508690508561026e565b50336001600160a01b03167f4208034e4449bb439a4b1ba16ff193bec2c83ec39cd969a629aa475c7f0b8047858560405180806020018281038252848482818152602001925080828437600083820152604051601f909101601f19169092018290039550909350505050a25060019392505050565b6000806001600160a01b03166000808585604051602001808383808284376040805191909301818103601f1901825283528051602091820120875286019690965293909301600020546001600160a01b03169490941415979650505050505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106102af5782800160ff198235161785556102dc565b828001600101855582156102dc579182015b828111156102dc5782358255916020019190600101906102c1565b506102e89291506102ec565b5090565b61030691905b808211156102e857600081556001016102f2565b9056fea165627a7a723058203d38f5b6d4364dd918d9055351900a7eee25686ba0d8cf483c7122e046a8b56c0029";

    public static final String FUNC_NOTARIZEDOCUMENT = "notarizeDocument";

    public static final String FUNC_ISNOTARIZED = "isNotarized";

    public static final Event NOTARIZED_EVENT = new Event("Notarized", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Utf8String>() {}));
    ;

    @Deprecated
    protected DocumentRegistry(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected DocumentRegistry(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected DocumentRegistry(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected DocumentRegistry(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<TransactionReceipt> notarizeDocument(String _documentHash) {
        final Function function = new Function(
                FUNC_NOTARIZEDOCUMENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_documentHash)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Boolean> isNotarized(String _documentHash) {
        final Function function = new Function(FUNC_ISNOTARIZED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_documentHash)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public List<NotarizedEventResponse> getNotarizedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(NOTARIZED_EVENT, transactionReceipt);
        ArrayList<NotarizedEventResponse> responses = new ArrayList<NotarizedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            NotarizedEventResponse typedResponse = new NotarizedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._signer = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._documentHash = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NotarizedEventResponse> notarizedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, NotarizedEventResponse>() {
            @Override
            public NotarizedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(NOTARIZED_EVENT, log);
                NotarizedEventResponse typedResponse = new NotarizedEventResponse();
                typedResponse.log = log;
                typedResponse._signer = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse._documentHash = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NotarizedEventResponse> notarizedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NOTARIZED_EVENT));
        return notarizedEventFlowable(filter);
    }

    @Deprecated
    public static DocumentRegistry load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new DocumentRegistry(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static DocumentRegistry load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new DocumentRegistry(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static DocumentRegistry load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new DocumentRegistry(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static DocumentRegistry load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new DocumentRegistry(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<DocumentRegistry> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(DocumentRegistry.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<DocumentRegistry> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(DocumentRegistry.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<DocumentRegistry> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(DocumentRegistry.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<DocumentRegistry> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(DocumentRegistry.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class NotarizedEventResponse {
        public Log log;

        public String _signer;

        public String _documentHash;
    }
}
