package com.geoxus.modules.pay.ali.service;

import cn.hutool.core.lang.Dict;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.*;
import com.alipay.api.request.MonitorHeartbeatSynRequest;
import com.alipay.api.response.*;

/**
 * 支付宝支付相关Service
 */
public interface AliPayTradeService {

    /**
     * 支付宝条码支付
     *
     * @param tradePayModel AlipayTradePayModel
     * @return AlipayTradePayResponse
     */
    AlipayTradePayResponse pay(AlipayTradePayModel tradePayModel) throws AlipayApiException;

    /**
     * 交易查询
     *
     * @param outTradeNo
     * @return
     */
    AlipayTradeQueryResponse queryByOutTradeNo(String outTradeNo);

    /**
     * 交易查询
     *
     * @param tradeNo
     * @return
     */
    AlipayTradeQueryResponse queryByTradeNo(String tradeNo);

    /**
     * 转账查询
     *
     * @param outBizNo 商户转账唯一订单号
     * @return
     */
    AlipayFundTransOrderQueryResponse queryByOutBizNo(String outBizNo);

    /**
     * 转账查询
     *
     * @param orderId 支付宝转账单据号
     * @return
     */
    AlipayFundTransOrderQueryResponse queryByOrderId(String orderId);

    /**
     * 统一收单交易撤销接口
     *
     * @param outTradeNo
     * @return
     */
    AlipayTradeCancelResponse cancelByOutTradeNo(String outTradeNo);

    /**
     * 统一收单交易撤销接口
     *
     * @param tradeNo
     * @return
     */
    AlipayTradeCancelResponse cancelByTradeNo(String tradeNo);

    /**
     * 统一收单交易撤销接口
     *
     * @param tradeCancel
     * @return
     */
    AlipayTradeCancelResponse cancel(AlipayTradeCancelModel tradeCancel);

    /**
     * 统一收单交易退款接口
     *
     * @param outTradeNo
     * @param refundAmount
     * @return
     */
    AlipayTradeRefundResponse refundByOutTradeNo(String outTradeNo, String refundAmount);

    /**
     * 统一收单交易退款接口
     *
     * @param tradeNo
     * @param refundAmount
     * @return
     */
    AlipayTradeRefundResponse refundByTradeNo(String tradeNo, String refundAmount);

    /**
     * 统一收单交易退款接口
     *
     * @param tradeRefund
     * @return
     */
    AlipayTradeRefundResponse refund(AlipayTradeRefundModel tradeRefund);

    /**
     * 下载账单
     *
     * @param billDownloadurlQuery
     * @return AlipayDataDataserviceBillDownloadurlQueryResponse
     */
    AlipayDataDataserviceBillDownloadurlQueryResponse downloadBill(AlipayDataDataserviceBillDownloadurlQueryModel billDownloadurlQuery);

    /**
     * 支付宝转账到其他账户
     *
     * @param transfer AlipayFundTransToAccountTransfer
     * @return 请求的响应
     */
    AlipayFundTransToaccountTransferResponse transferToAccount(AlipayFundTransToaccountTransferModel transfer);

    /**
     * App支付
     *
     * @param param
     * @return
     */
    String appPay(Dict param);

    /**
     * 网页支付
     *
     * @param param
     * @return
     */
    String pagePay(Dict param);

    /**
     * 交易保障接口
     *
     * @param request
     * @return
     */
    MonitorHeartbeatSynResponse sendHeartBeat(MonitorHeartbeatSynRequest request);
}