package com.geoxus.modules.ethereum.config;

import com.geoxus.modules.ethereum.actuate.Web3jHealthIndicator;
import com.geoxus.modules.ethereum.contract.USDTContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.websocket.WebSocketClient;
import org.web3j.protocol.websocket.WebSocketService;
import org.web3j.tx.ReadonlyTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;

import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;

@Configurable
@Component
@ConditionalOnExpression("'${eth-enable}'.equals('true')")
public class Web3jConfig {
    @Autowired
    private EthConfig ethConfig;

    @Bean
    public Web3j web3jWebsocket() throws URISyntaxException, ConnectException {
        final WebSocketClient webSocketClient = new WebSocketClient(new URI(ethConfig.getWebsocketClientUrl()));
        final boolean includeRawResponses = true;
        final WebSocketService webSocketService = new WebSocketService(webSocketClient, includeRawResponses);
        webSocketService.connect(
                s -> System.out.println("web3jWebsocket onMessage callback : " + s),
                t -> System.out.println("web3jWebsocket onError callback : " + t),
                () -> System.out.println("web3jWebsocket onClose callback "));
        return Web3j.build(webSocketService);
    }

    @Bean
    public EthFilter ethFilter() {
        return new EthFilter(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST, ethConfig.getContractAddress());
    }

    //@Bean
    public USDTContract usdtContractHttp() {
        final Web3j web3jHttp = Web3j.build(new HttpService(ethConfig.getHttpClientUrl()));
        final TransactionManager transactionManager = new ReadonlyTransactionManager(web3jHttp, ethConfig.getContractAddress());
        return USDTContract.load(ethConfig.getContractAddress(), web3jHttp, transactionManager, new DefaultGasProvider());
    }

    @Bean
    @Qualifier("usdtContractWebSocket")
    public USDTContract usdtContractWebSocket(@Autowired Web3j web3jWebsocket) {
        final TransactionManager transactionManager = new ReadonlyTransactionManager(web3jWebsocket, ethConfig.getContractAddress());
        return USDTContract.load(ethConfig.getContractAddress(), web3jWebsocket, transactionManager, new DefaultGasProvider());
    }

    @Bean
    Web3jHealthIndicator web3jHealthIndicator(Web3j web3jWebsocket) {
        return new Web3jHealthIndicator(web3jWebsocket);
    }
}
