package com.geoxus.modules.ethereum.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.geoxus.core.common.oauth.GXTokenManager;
import com.geoxus.core.common.util.GXAuthCodeUtils;
import com.geoxus.core.common.util.GXCommonUtils;
import com.geoxus.core.common.util.GXRedisUtils;
import com.geoxus.modules.ethereum.config.EthConfig;
import com.geoxus.modules.ethereum.constant.SummaryUSDTConstants;
import com.geoxus.modules.ethereum.constant.UWalletConstants;
import com.geoxus.modules.ethereum.contract.USDTContract;
import com.geoxus.modules.ethereum.entity.UWalletEntity;
import com.geoxus.modules.ethereum.service.SummaryUSDTService;
import com.geoxus.modules.ethereum.service.UWalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@ConditionalOnExpression(value = "${eth-enable:true}")
public class SummaryUSDTServiceImpl implements SummaryUSDTService {
    @Autowired
    private UWalletService uWalletService;

    @Autowired
    private EthConfig ethConfig;

    @Autowired
    private GXRedisUtils redisUtils;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    @Scheduled(cron = "0 */2 * * * ?")
    public void gather() {
        if (StrUtil.isNotBlank(redisUtils.get(SummaryUSDTConstants.REDIS_LOCK_KEY))) {
            log.info("上一次的收币操作还未执行完毕......");
            return;
        }
        redisUtils.set(SummaryUSDTConstants.REDIS_LOCK_KEY, "gather.usdt", 3600);
        log.info("平台开始执行收币...");
        List<UWalletEntity> list = Optional.ofNullable(uWalletService.list(new QueryWrapper<UWalletEntity>()
                .eq("is_platform", 0)
                .ne(GXTokenManager.USER_ID, UWalletConstants.PLATFORM_USER_ID)))
                .orElse(new ArrayList<>());
        for (UWalletEntity walletEntity : list) {
            if (uWalletService.getWalletPlatForm().getUserId() == walletEntity.getUserId()) {
                continue;
            }
            //获取用户在区块链上的余额
            final Dict blockChainBalance = uWalletService.getBlockChainBalance(Dict.create()
                    .set(GXTokenManager.USER_ID, walletEntity.getUserId()));
            final Double ethAmount = blockChainBalance.getDouble("eth");
            final Double tokenAmount = blockChainBalance.getDouble("token");
            log.info("账户 {} 在以太坊的以太币 : {} , USDT : {} ", walletEntity.getUserId(), ethAmount, tokenAmount);
            if (tokenAmount > 0) {
                //假如用户当前的ETH不足，则先从平台账户转ETH到用户的钱包,在将用户的USDT转入平台(目标)账户
                if (ethAmount < UWalletConstants.SERVICE_CHARGE) {
                    uWalletService.transferETHToAccount(walletEntity.getAddress(), UWalletConstants.SERVICE_CHARGE);
                }
                //将用户的USDT余额转入平台指定的账户
                transfer(walletEntity.getUserId(), tokenAmount.toString());
            }
        }
        if (redisUtils.delete(SummaryUSDTConstants.REDIS_LOCK_KEY)) {
            log.info("平台收账执行完毕......");
        }
    }

    /**
     * 发起交易
     *
     * @param userId 用户ID
     * @param amount 转账金额
     * @return
     */
    private String transfer(long userId, String amount) {
        try {
            UWalletEntity uWalletEntity = uWalletService.getWalletByUserId(userId);
            final Web3j web3jHttp = Web3j.build(new HttpService(ethConfig.getHttpClientUrl()));
            final Credentials credentials = uWalletService.getCredentials(uWalletEntity.getUserId());
            assert credentials != null;
            final TransactionManager transactionManager = new RawTransactionManager(web3jHttp, credentials);
            final USDTContract contract = USDTContract.load(ethConfig.getContractAddress(), web3jHttp, transactionManager, new UWalletServiceImpl.TetherGasProvider());
            final TransactionReceipt receipt;
            String targetWalletAddress = ethConfig.getExternalWalletAddress();
            receipt = contract.transfer(targetWalletAddress, org.web3j.utils.Convert.toWei(amount, org.web3j.utils.Convert.Unit.MWEI).toBigInteger()).send();
            if (null != receipt) {
                log.info("收账处理  >>>  用户充值成功.....  {}", receipt);
                final Dict notifyData = Dict.create().set("user_id", userId).set("amount", amount).set("tx_hash", receipt.getTransactionHash());
                final String s = GXAuthCodeUtils.authCodeEncode(JSONUtil.toJsonStr(notifyData), SummaryUSDTConstants.QUEUE_SECRET_KEY, 600);
                final String routingKey = GXCommonUtils.getEnvironmentValue("rabbit.notify-app-queue-name", String.class);
                rabbitTemplate.convertAndSend(routingKey, s);
                return receipt.getStatus();
            }
        } catch (TransactionException e) {
            // org.web3j.protocol.exceptions.TransactionException:
            // Transaction receipt was not generated after 600 seconds for transaction: 0xd20774e46306da0b49f30f4af903838321ba9257ef60df3d4ed2496923c8d236
            if (e.getTransactionHash().isPresent()) {
            }
            log.error("平台收账执行失败 >>> 平台收币 >>> 以太坊交易发生异常 >>> TransactionException", e);
        } catch (Exception e) {
            log.error("平台收账执行失败 >>> 平台收币 >>> 以太坊交易发生异常 >>> Exception", e);
        } finally {
            if (redisUtils.delete(SummaryUSDTConstants.REDIS_LOCK_KEY)) {
                log.info("平台收账执行完毕遇到异常......");
            }
        }
        return "fail";
    }
}