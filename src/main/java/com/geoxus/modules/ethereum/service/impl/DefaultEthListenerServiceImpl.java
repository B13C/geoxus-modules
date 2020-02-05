package com.geoxus.modules.ethereum.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSONUtil;
import com.geoxus.core.common.util.GXRedisUtils;
import com.geoxus.modules.ethereum.contract.USDTContract;
import com.geoxus.modules.ethereum.service.EthListenerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.request.EthFilter;

import javax.annotation.PostConstruct;

@Service
@Slf4j
@ConditionalOnExpression(value = "${eth-enable:true}")
public class DefaultEthListenerServiceImpl implements EthListenerService {
    @Autowired
    private Web3j web3jWebsocket;

    @Autowired
    private EthFilter ethFilter;

    @Autowired
    private GXRedisUtils redisUtils;

    @Autowired
    private USDTContract contract;

    @Override
    @PostConstruct
    public void listen() {
        contract.transferEventFlowable(ethFilter).subscribe(tx -> {
            log.info("tx " + tx + " , tetherToken tx from : " + tx.from + " , tx to : " + tx.to + " , tx value : " + tx.value + " , tetherToken tx log : " + tx.log);
            if (null == tx.from || null == tx.to || null == tx.log) {
                return;
            }
            final Dict dict = Convert.convert(Dict.class, JSONUtil.parse(tx.log)).set("from", tx.from).set("to", tx.to).set("value", tx.value);
            final String transactionHash = tx.log.getTransactionHash();
            final String s = redisUtils.get(transactionHash);
        }, (Throwable error) -> {
            try {
                log.info("以太坊监听器发生了异常哦....{}", error.toString());
            } catch (Exception e) {
                System.out.println("AAAAAAAAAAAAA");
            }
        });
    }
}
