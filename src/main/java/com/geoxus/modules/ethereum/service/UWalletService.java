package com.geoxus.modules.ethereum.service;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.framework.service.GXBaseService;
import com.geoxus.modules.ethereum.contract.USDTContract;
import com.geoxus.modules.ethereum.entity.UWalletEntity;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

public interface UWalletService extends GXBaseService<UWalletEntity> {
    /**
     * 创建钱包
     *
     * @param userId
     * @return
     */
    String create(Long userId) throws Exception;

    /**
     * 处理用户USDT交易
     *
     * @param event
     */
    boolean processUSDTTransaction(USDTContract.TransferEventResponse event);

    /**
     * 从平台账户转入一定量的以太坊到用户的账户，便于在平台收取用户的Token时作为手续费
     * 需要扣除用户Token的量,用于作为平台手续费(ETH+平台转账)
     *
     * @param to     转入的账号
     * @param amount 转入金额
     * @return TransactionReceipt
     */
    TransactionReceipt transferETHToAccount(String to, Double amount);

    /**
     * Token转出到其他平台或者其他的账户
     *
     * @param from        转出的账号(用户的以太坊地址)
     * @param to          转入的账号(用户的以太坊地址)
     * @param amount      转入金额
     * @param balanceType 余额类型
     * @param appendParam
     * @return TransactionReceipt
     */
    TransactionReceipt transferTokenToAccount(String from, String to, String amount, int balanceType, Dict appendParam);

    /**
     * 平台内部账户之间的转账
     *
     * @param to     对方地址
     * @param userId 当前用户ID
     * @param amount 转账金额
     * @return
     */
    boolean platFormInternalTransfer(String to, long userId, String amount);

    /**
     * 用户提币
     *
     * @param param
     * @return
     */
    boolean mentionMoney(Dict param);

    /**
     * 获取当前用户区块链上的数据
     *
     * @param param
     * @return
     */
    Dict getBlockChainBalance(Dict param);

    /**
     * 检测地址是否属于平台的的地址
     *
     * @param address
     * @return
     */
    boolean checkAddressType(String address);

    /**
     * 获取当前用户的钱包信息
     *
     * @param userId 用户ID
     * @return UEthWalletEntity
     */
    UWalletEntity getWalletByUserId(long userId);

    /**
     * 通过钱包地址获取钱包信息
     *
     * @param address 钱包地址
     * @return
     */
    UWalletEntity getWalletByAddress(String address);

    /**
     * 获取平台钱包地址
     *
     * @return
     */
    UWalletEntity getWalletPlatForm();

    /**
     * 获取用户交易证书
     *
     * @param userId 交易证书密码
     * @return Credentials
     */
    Credentials getCredentials(long userId);
}
