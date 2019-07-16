package io.kauri.java.test;

import org.awaitility.Awaitility;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.web3j.abi.*;
import org.web3j.abi.datatypes.*;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Async;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TestWeb3jPantheon {

    private static final String DOCUMENT_HASH = "QmXoypizjW3WknFiJnKLwHCnL72vedxjQkDDP1mXWo6uco";

    private static final Credentials CREDS = Credentials.create("0x8f2a55949038a9610f50fb23b5883af3b4ecb3c3bb792cbcefbd1542c692be63");

    private Web3j web3j;

    private List<String> emittedDocumentHashes;

    //This will start a Pantheon 1.1.3 node before all tests, and stop it after all tests have executed
    @ClassRule
    public static final GenericContainer pantheonContainer =
            new GenericContainer("pegasyseng/pantheon:1.1.3")
                    .withExposedPorts(8545, 8546)
                    .withCommand(
                            "--miner-enabled",
                            "--miner-coinbase=0xfe3b557e8fb62b89f4916b721be55ceb828dbd73",
                            "--rpc-http-enabled",
                            "--rpc-ws-enabled",
                            "--network=dev")
                    .waitingFor(Wait.forHttp("/liveness").forStatusCode(200).forPort(8545));

    @Before
    public void init() {
        final Integer port = pantheonContainer.getMappedPort(8545);

        web3j = Web3j.build(new HttpService(
                "http://localhost:" + port), 500, Async.defaultExecutorService());

        emittedDocumentHashes = new ArrayList<>();
    }

    @After
    public void shutdownWeb3j() {
        web3j.shutdown();
    }

    @Test
    public void listenForEventsUsingContractWrapper() throws Exception {

        //Deploy DocumentRegistry.sol
        final DocumentRegistry documentRegistry = DocumentRegistry.deploy(web3j, CREDS, new DefaultGasProvider()).send();

        //Construct filter
        final EthFilter ethFilter = new EthFilter(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST,
                documentRegistry.getContractAddress());

        //First set the encoded event specification topic
        ethFilter.addSingleTopic(EventEncoder.encode(documentRegistry.NOTARIZED_EVENT));

        //Add an additional topic to only receive events where _signer event argument is a specific address
        ethFilter.addOptionalTopics("0x" + TypeEncoder.encode(new Address("0xfe3b557e8fb62b89f4916b721be55ceb828dbd73")));

        //Subscribe to events matching ethFilter
        documentRegistry
                .notarizedEventFlowable(ethFilter)
                .subscribe(event -> {
                    final String notary = event._signer;
                    emittedDocumentHashes.add(event._documentHash);
                });

        //Call the notarizeDocument function of the deployed DocumentRegistry smart contract
        final TransactionReceipt txReceipt = documentRegistry.notarizeDocument(DOCUMENT_HASH).send();

        System.out.println("notarizeDocument transaction hash: " + txReceipt.getTransactionHash());

        waitForEventWithDocumentHash(DOCUMENT_HASH);
    }

    private void waitForEventWithDocumentHash(String documentHash) {
        Awaitility
                .await()
                .atMost(20, TimeUnit.SECONDS)
                .until(() -> {
                    return emittedDocumentHashes.size() == 1
                            && DOCUMENT_HASH.equals(emittedDocumentHashes.get(0));
                });
    }

}
