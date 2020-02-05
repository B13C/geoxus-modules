package com.geoxus.modules.ethereum.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;
import com.geoxus.core.common.exception.GXException;
import com.geoxus.core.common.oauth.GXTokenManager;
import com.geoxus.core.common.util.GXAuthCodeUtils;
import com.geoxus.core.common.util.GXRedisUtils;
import com.geoxus.modules.ethereum.config.EthConfig;
import com.geoxus.modules.ethereum.constant.UWalletConstants;
import com.geoxus.modules.ethereum.contract.USDTContract;
import com.geoxus.modules.ethereum.entity.UWalletEntity;
import com.geoxus.modules.ethereum.mapper.UWalletMapper;
import com.geoxus.modules.ethereum.service.UWalletService;
import com.geoxus.modules.ethereum.util.*;
import com.geoxus.modules.user.constant.UBalanceConstants;
import com.geoxus.modules.user.entity.UBalanceEntity;
import com.geoxus.modules.user.entity.UUserEntity;
import com.geoxus.modules.user.service.UBalanceService;
import com.geoxus.modules.user.service.UUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.ReadonlyTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@ConditionalOnExpression(value = "${eth-enable:true}")
public class UWalletServiceImpl extends ServiceImpl<UWalletMapper, UWalletEntity> implements UWalletService {
    @Autowired
    private EthConfig ethConfig;

    @Autowired
    private UWalletService uWalletService;

    @Autowired
    private UBalanceService uBalanceService;

    @Autowired
    private UUserService uUserService;

    @Autowired
    private GXRedisUtils redisUtil;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final static Object lock = new Object();

    @GXFieldCommentAnnotation(zh = "内部转账")
    private static final int TRANSACTION_INNER_TYPE = 1;

    @GXFieldCommentAnnotation(zh = "外部转账")
    private static final int TRANSACTION_OUTER_TYPE = 2;

    @GXFieldCommentAnnotation(zh = "HTML文件的文件后缀")
    private static final String EXT_HTML = "html";

    @GXFieldCommentAnnotation(zh = "PNG文件的文件后缀")
    private static final String EXT_PNG = "png";

    @GXFieldCommentAnnotation(zh = "以太坊钱包地址助记词长度")
    private static final int WALLET_NUMBER_OF_WORDS = 24;

    @GXFieldCommentAnnotation(zh = "加密以太坊钱包地址助记词密钥的长度")
    private static final int ENCRYPT_WALLET_NUMBER_OF_WORDS_LENGTH = 12;

    @Override
    @Transactional
    public String create(Long userId) throws Exception {
        final UWalletEntity wallet = uWalletService.getWalletByUserId(userId);
        if (null != wallet) {
            return wallet.getAddress();
        }
        final PassPhraseUtility phraseUtility = new PassPhraseUtility();
        String passPhrase = phraseUtility.getPassPhrase(WALLET_NUMBER_OF_WORDS);
        PaperWallet pw = new PaperWallet(passPhrase, ethConfig.getTargetDirectory());

        final String dynamicStr = RandomUtil.randomString(ENCRYPT_WALLET_NUMBER_OF_WORDS_LENGTH);
        passPhrase = GXAuthCodeUtils.authCodeEncode(passPhrase, UWalletConstants.META_PASSWORD + dynamicStr);

        String html = WalletPageUtility.createHtml(pw, passPhrase);
        byte[] qrCode = QrCodeUtility.contentToPngBytes(pw.getAddress(), 256);

        String path = pw.getPathToFile();
        String baseName = pw.getBaseName();
        String htmlFile = String.format("%s%s%s.%s", path, File.separator, baseName, EXT_HTML);
        String pngFile = String.format("%s%s%s.%s", path, File.separator, baseName, EXT_PNG);

        FileUtility.saveToFile(html, htmlFile);
        FileUtility.saveToFile(qrCode, pngFile);
        //钱包信息入库
        final UWalletEntity walletEntity = new UWalletEntity();
        walletEntity.setWalletId(IdUtil.createSnowflake(1, 1).nextId());
        walletEntity.setPassword(passPhrase);
        walletEntity.setDynamicCode(dynamicStr);
        walletEntity.setAddress(pw.getAddress());
        walletEntity.setUserId(userId);
        walletEntity.setWalletFile(pw.getFile().getName());
        final boolean save = uWalletService.save(walletEntity);
        return save ? pw.getAddress() : "";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean processUSDTTransaction(USDTContract.TransferEventResponse event) {
        final String from = event.from;
        final String to = event.to;
        UWalletEntity uWalletEntity = uWalletService.getWalletByAddress(to);
        if (null != uWalletEntity && uWalletEntity.getUserId() > 0) {
            return processTransferInUSDT(event, uWalletEntity);
        }
        uWalletEntity = uWalletService.getWalletByAddress(from);
        if (null != uWalletEntity && uWalletEntity.getUserId() > 0) {
            return processTransferOutUSDT(event, uWalletEntity);
        }
        return false;
    }

    /**
     * 处理转入
     *
     * @return
     */
    private boolean processTransferInUSDT(USDTContract.TransferEventResponse event, UWalletEntity wallet) {
        log.info("进入用户转入处理 >> processTransferInUSDT : amount={} , tx hash={}", Convert.fromWei(String.valueOf(event.value), Convert.Unit.MWEI), event.log.getTransactionHash());
        final double newBalance = uBalanceService.increaseBalance(wallet.getUserId(), UBalanceConstants.AVAILABLE_USDT_TYPE, Convert.fromWei(String.valueOf(event.value), Convert.Unit.MWEI).doubleValue(), Dict.create().set("user_id", wallet.getUserId()).set("source", "user_recharge_usdt").set("remark", "用户充值USDT"));
        if (!uBalanceService.enoughBalance(wallet.getUserId(), UBalanceConstants.AVAILABLE_ETH_TYPE, UWalletConstants.SERVICE_CHARGE)) {
            //4. 计算提取相应USDT余额的ETH手续费，从平台账户转入ETH做为提取用户的USDT余额到平台账户的手续费(矿工费)
            // 矿工费公式 transaction fee = gas * gasPrice
            final UBalanceEntity ethBalanceEntity = uBalanceService.getUBalanceByType(wallet.getUserId(), UBalanceConstants.AVAILABLE_ETH_TYPE);
            transferETHToAccount(wallet.getAddress(), UWalletConstants.SERVICE_CHARGE - ethBalanceEntity.getBalance());
        }
        log.info("用户充值成功 >> processTransferInUSDT : amount={} , tx hash={}", Convert.fromWei(String.valueOf(event.value), Convert.Unit.MWEI), event.log.getTransactionHash());
        return true;
    }

    /**
     * 处理转出
     *
     * @return
     */
    private boolean processTransferOutUSDT(USDTContract.TransferEventResponse event, UWalletEntity wallet) {
        log.info("进入用户转出处理 >> processTransferOutUSDT : amount={} , tx hash={}", Convert.fromWei(String.valueOf(event.value), Convert.Unit.MWEI), event.log.getTransactionHash());
        final Dict balance = getBlockChainBalance(Dict.create().set("address", wallet.getAddress()).set("user_id", wallet.getUserId()));
        uBalanceService.updateFieldByCondition(Dict.create().set("user_id", wallet.getUserId()).set("type", UBalanceConstants.AVAILABLE_ETH_TYPE), "balance", balance.getDouble("eth"));
        double newBalance = uBalanceService.reduceBalance(wallet.getUserId(), UBalanceConstants.FROZEN_USDT_TYPE, Convert.fromWei(event.value.toString(), Convert.Unit.MWEI).doubleValue(), Dict.create().set("source", "user_mention_usdt").set("remark", "用户提币USDT"));
        return true;
    }

    /**
     * 从平台账户转入一定量的以太币到用户的账户，便于在平台收取用户的USDT时作为手续费
     *
     * @param to     转入的账号
     * @param amount 转入金额
     * @return TransactionReceipt
     */
    @Transactional
    public TransactionReceipt transferETHToAccount(String to, Double amount) {
        Objects.requireNonNull(to);
        final Web3j web3jHttp = Web3j.build(new HttpService(ethConfig.getHttpClientUrl()));
        final Dict blockChainBalance = getBlockChainBalance(Dict.create().set("address", uWalletService.getWalletPlatForm().getAddress()).set("user_id", uWalletService.getWalletPlatForm().getUserId()));
        log.info("平台账户转入以太币到用户账户 >> transferETHToAccount,平台账户以太币余额 : {} , 转账金额 : {} ", blockChainBalance.getDouble("eth"), amount);
        if (blockChainBalance.getDouble("eth") < 0.01) {
            log.info("平台账户的ETH不足..请管理员立刻充值......");
            throw new GXException("平台账户的ETH不足..请管理员立刻充值......");
        }
        try {
            final UWalletEntity wallet = uWalletService.getWalletByAddress(to);
            final Credentials credentials = getCredentials(uWalletService.getWalletPlatForm().getUserId());
            Objects.requireNonNull(credentials);
            final TransactionReceipt transactionReceipt = Transfer.sendFunds(web3jHttp, credentials, wallet.getAddress(), BigDecimal.valueOf(amount), Convert.Unit.ETHER).send();
            final String ethTransactionHash = transactionReceipt.getTransactionHash();
            log.info("平台账户转入以太币到用户账户---成功 >> transferETHToAccount : " + transactionReceipt.getTransactionHash() + " , transaction Status : " + transactionReceipt.getStatus() + " , tx hash : " + ethTransactionHash);
            return transactionReceipt;
        } catch (Exception e) {
            log.error("平台账户转入以太币到用户账户 {} ,发生了异常 >> transferAccountToETH : {}", to, e);
        }
        return null;
    }

    /**
     * 用户转账
     *
     * @param from        转出的账号(用户的以太坊地址)
     * @param to          转入的账号(用户的以太坊地址)
     * @param amount      转入金额
     * @param balanceType 余额类型
     * @param appendParam
     * @return TransactionReceipt
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public TransactionReceipt transferTokenToAccount(String from, String to, String amount, int balanceType, Dict appendParam) {
        final Web3j web3jHttp = Web3j.build(new HttpService(ethConfig.getHttpClientUrl()));
        //获取平台钱包
        UWalletEntity uWalletEntity = uWalletService.getWalletByAddress(from);
        if (uWalletEntity.getUserId() != UWalletConstants.PLATFORM_USER_ID) {
            throw new GXException("from的地址不是有效的平台地址");
        }
        final Credentials credentials = getCredentials(uWalletEntity.getUserId());
        assert credentials != null;
        final TransactionManager transactionManager = new RawTransactionManager(web3jHttp, credentials);
        final USDTContract tetherToken;
        tetherToken = USDTContract.load(ethConfig.getContractAddress(), web3jHttp, transactionManager, new TetherGasProvider());
        final TransactionReceipt receipt;
        try {
            long startTime = System.currentTimeMillis();
            receipt = tetherToken.transfer(to, Convert.toWei(amount, Convert.Unit.MWEI).toBigInteger()).send();
            long endTime = System.currentTimeMillis();
            log.info("以太坊发生交易时间完成时间：{} ms", endTime - startTime);
            final double amountNumber = NumberUtil.round(cn.hutool.core.convert.Convert.convert(Double.class, amount), 6).doubleValue();
            log.info("外部钱包转账成功 转账hash : {} 转账金额为 : {}", receipt.getTransactionHash(), amountNumber);
            return receipt;
        } catch (Exception e) {
            log.error("用户提币 >>> 以太坊交易发生异常", e);
        }
        return null;
    }

    @Override
    public boolean platFormInternalTransfer(String to, long userId, String amount) {
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean mentionMoney(Dict param) {
        final long userId = param.getLong(GXTokenManager.USER_ID);
        String redisKey = StrUtil.format("mention-money:{}", userId);
        synchronized (lock) {
            if (StrUtil.isNotBlank(redisUtil.get(redisKey))) {
                throw new GXException("您有正在转账的操作 , 请稍后重试....");
            }
            redisUtil.set(redisKey, param.getStr("amount"), 1200);
        }
        log.info("发起提币请求, 请求参数为 : {}", param.toString());
        final UUserEntity userEntity = uUserService.getById(userId);
        if (!userEntity.getPayPassword().equals(SecureUtil.md5(param.getStr("capital_password") + userEntity.getPaySalt()))) {
            redisUtil.delete(redisKey);
            throw new GXException("资金密码不正确");
        }
        String to = param.getStr("address");
        String amount = param.getStr("amount");
        if (Double.parseDouble(amount) < 20) {
            redisUtil.delete(redisKey);
            throw new GXException("转账金额必须大于20个");
        }
        if (!uBalanceService.enoughBalance(userId, UBalanceConstants.AVAILABLE_USDT_TYPE, Double.parseDouble(amount))) {
            redisUtil.delete(redisKey);
            throw new GXException("提币余额不足!");
        }
        // 如果to是平台内部的地址,则直接转换为平台内部之间的转账
        if (checkAddressType(to)) {
            UWalletEntity uWalletEntity = uWalletService.getWalletByUserId(userId);
            log.info("平台内部提币 , from : {} >>> to : {}", uWalletEntity.getAddress(), to);
            final UWalletEntity toWalletEntity = uWalletService.getWalletByAddress(to);
            uBalanceService.transferAccounts(userId, toWalletEntity.getUserId(), amount, UBalanceConstants.AVAILABLE_USDT_TYPE,
                    Dict.create()
                            .set("amount", amount)
                            .set("from", userId)
                            .set("to", toWalletEntity.getUserId())
                            .set("remark", "提币转换为平台内部之间转账"));
            redisUtil.delete(redisKey);
            return true;
        }
        final Dict transferData = Dict.create()
                .set("to", to)
                .set("amount", amount)
                .set("user_id", userId)
                .set("redis_key", redisKey);
        try {
            log.info("平台外部提币 , 放入队列进行处理 , {} >>> {}", userId, to);
            rabbitTemplate.convertAndSend(UWalletConstants.TRANSFER_QUEUE_NAME, GXAuthCodeUtils.authCodeEncode(JSONUtil.toJsonStr(transferData), UWalletConstants.TRANSFER_DATA_KEY));
            uBalanceService.reduceBalance(userId, UBalanceConstants.AVAILABLE_USDT_TYPE, Double.parseDouble(amount), Dict.create().set("remark", "资金:扣除用户USDT").set("reason", "用户提币>>>扣除用户可用USDT"));
            uBalanceService.increaseBalance(userId, UBalanceConstants.AVAILABLE_USDT_TYPE, Double.parseDouble(amount), Dict.create().set("source", "system").set("remark", "用户提币>>>冻结用户可用USDT"));
        } catch (AmqpException e) {
            return false;
        }
        return true;
    }

    @Override
    public Dict getBlockChainBalance(Dict param) {
        try {
            String address = param.getStr("address");
            if (null == address && null == param.getLong(GXTokenManager.USER_ID)) {
                throw new GXException("请提供钱包地址或者登录...");
            }
            if (null == address && null != param.getLong(GXTokenManager.USER_ID)) {
                final UWalletEntity walletEntity = uWalletService.getWalletByUserId(Optional.ofNullable(param.getLong("user_id")).orElse(0L));
                address = walletEntity.getAddress();
            }
            final Web3j web3jHttp = Web3j.build(new HttpService(ethConfig.getHttpClientUrl()));
            final TransactionManager transactionManager = new ReadonlyTransactionManager(web3jHttp, ethConfig.getContractAddress());
            final USDTContract contract = USDTContract.load(ethConfig.getContractAddress(), web3jHttp, transactionManager, new DefaultGasProvider());
            final EthGetBalance ethBalance = web3jHttp.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
            final BigInteger tokenBalance;
            tokenBalance = contract.balanceOf(address).send();
            final BigDecimal ethNumber = Convert.fromWei(String.valueOf(ethBalance.getBalance()), Convert.Unit.ETHER);
            final BigDecimal tokenNumber = Convert.fromWei(String.valueOf(tokenBalance), Convert.Unit.MWEI);
            return Dict.create().set("eth", ethNumber).set("token", tokenNumber);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Dict.create();
    }

    @Override
    public boolean checkAddressType(String address) {
        final UWalletEntity entity = getOneByCondition(Dict.create().set("address", address));
        return null != entity;
    }

    @Override
    public Credentials getCredentials(long userId) {
        final UWalletEntity ethWalletEntity = uWalletService.getWalletByUserId(userId);
        if (null != ethWalletEntity) {
            String walletFile = ethConfig.getTargetDirectory() + File.separatorChar + ethWalletEntity.getWalletFile();
            String passPhase = GXAuthCodeUtils.authCodeDecode(ethWalletEntity.getPassword(), UWalletConstants.META_PASSWORD + ethWalletEntity.getDynamicCode());
            try {
                final Credentials credentials;
                credentials = WalletUtils.loadCredentials(passPhase, new File(walletFile));
                return credentials;
            } catch (Exception e) {
                log.error("getCredentials", e);
            }
        }
        return null;
    }

    @Override
    public UWalletEntity getWalletByUserId(long userId) {
        final UWalletEntity wallet = getOne(new QueryWrapper<UWalletEntity>().eq("user_id", userId));
        return wallet;
    }

    @Override
    public UWalletEntity getWalletByAddress(String address) {
        final UWalletEntity wallet;
        wallet = getOne(new QueryWrapper<UWalletEntity>().eq("address", address));
        return wallet;
    }

    @Override
    public UWalletEntity getWalletPlatForm() {
        return getWalletByUserId(UWalletConstants.PLATFORM_USER_ID);
    }

    //账户的以太币余额必须要 >= 0.0021
    public static class TetherGasProvider extends StaticGasProvider {
        static final BigInteger GAS_LIMIT = BigInteger.valueOf(2_100_000);
        static final BigInteger GAS_PRICE = BigInteger.valueOf(2_200_000_000L);
        //static final BigInteger GAS_PRICE = BigInteger.valueOf(100_000_000L);   //交易确认将变得很慢

        TetherGasProvider() {
            super(GAS_PRICE, GAS_LIMIT);
        }
    }
}
