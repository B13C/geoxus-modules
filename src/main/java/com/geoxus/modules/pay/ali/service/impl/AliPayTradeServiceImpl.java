package com.geoxus.modules.pay.ali.service.impl;

import cn.hutool.core.lang.Dict;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.*;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.geoxus.modules.pay.ali.enums.AliPayTradeErrorCode;
import com.geoxus.modules.pay.ali.exception.AliPayException;
import com.geoxus.modules.pay.ali.service.AliPayTradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AliPayTradeServiceImpl implements AliPayTradeService {
    @Autowired
    private AlipayClient alipayClient;

    @Override
    public AlipayTradePayResponse pay(AlipayTradePayModel tradePayModel) throws AlipayApiException {
        AlipayTradePayRequest request = new AlipayTradePayRequest(); //创建API对应的request类
        request.setBizModel(tradePayModel); //设置业务参数
        return alipayClient.execute(request);  //通过alipayClient调用API，获得对应的response类
    }

    @Override
    public AlipayTradeQueryResponse queryByOutTradeNo(String outTradeNo) {
        AlipayTradeQueryModel request = new AlipayTradeQueryModel();
        request.setOutTradeNo(outTradeNo);
        return query(request);
    }

    @Override
    public AlipayTradeQueryResponse queryByTradeNo(String tradeNo) {
        AlipayTradeQueryModel request = new AlipayTradeQueryModel();
        request.setTradeNo(tradeNo);
        return query(request);
    }

    @Override
    public AlipayFundTransOrderQueryResponse queryByOutBizNo(String outBizNo) {
        AlipayFundTransOrderQueryModel request = new AlipayFundTransOrderQueryModel();
        request.setOutBizNo(outBizNo);
        return query(request);
    }

    @Override
    public AlipayFundTransOrderQueryResponse queryByOrderId(String orderId) {
        AlipayFundTransOrderQueryModel request = new AlipayFundTransOrderQueryModel();
        request.setOrderId(orderId);
        return query(request);
    }

    @Override
    public AlipayTradeCancelResponse cancelByOutTradeNo(String outTradeNo) {
        AlipayTradeCancelModel request = new AlipayTradeCancelModel();
        request.setOutTradeNo(outTradeNo);
        return cancel(request);
    }

    @Override
    public AlipayTradeCancelResponse cancelByTradeNo(String tradeNo) {
        AlipayTradeCancelModel request = new AlipayTradeCancelModel();
        request.setTradeNo(tradeNo);
        return cancel(request);
    }

    @Override
    public AlipayTradeCancelResponse cancel(AlipayTradeCancelModel tradeCancel) {
        AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
        request.setBizModel(tradeCancel);
        AlipayTradeCancelResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            log.error(e.getMessage(), e);
        }
        return response;
    }

    @Override
    public AlipayTradeRefundResponse refundByOutTradeNo(String outTradeNo, String refundAmount) {
        AlipayTradeRefundModel request = new AlipayTradeRefundModel();
        request.setOutTradeNo(outTradeNo);
        request.setRefundAmount(refundAmount);
        return refund(request);
    }

    @Override
    public AlipayTradeRefundResponse refundByTradeNo(String tradeNo, String refundAmount) {
        AlipayTradeRefundModel request = new AlipayTradeRefundModel();
        request.setTradeNo(tradeNo);
        request.setRefundAmount(refundAmount);
        return refund(request);
    }

    @Override
    public AlipayTradeRefundResponse refund(AlipayTradeRefundModel tradeRefund) {
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setBizModel(tradeRefund);
        AlipayTradeRefundResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            log.error(e.getMessage(), e);
        }
        return response;
    }

    @Override
    public AlipayDataDataserviceBillDownloadurlQueryResponse downloadBill(AlipayDataDataserviceBillDownloadurlQueryModel billDownloadurlQuery) {
        AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();
        request.setBizModel(billDownloadurlQuery);
        AlipayDataDataserviceBillDownloadurlQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            log.error(e.getMessage(), e);
        }
        return response;
    }

    @Override
    public AlipayFundTransToaccountTransferResponse transferToAccount(AlipayFundTransToaccountTransferModel transfer) {
        AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
        request.setBizModel(transfer);
        AlipayFundTransToaccountTransferResponse response;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            log.error(e.getMessage(), e);
            throw new AliPayException(AliPayTradeErrorCode.TRADE_TRANSFER_ERROR.getId(), AliPayTradeErrorCode.TRADE_TRANSFER_ERROR.getMsg());
        }
        if (response.isSuccess()) {
            return response;
        } else {
            throw new AliPayException(500, Integer.valueOf(response.getCode()), response.getSubMsg());
        }
    }

    @Override
    public String appPay(Dict param) {
        try {
            final AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
            AlipayTradeAppPayModel payModel = new AlipayTradeAppPayModel();
            payModel.setTotalAmount(param.getStr("total_amount"));
            payModel.setOutTradeNo(param.getStr("out_trade_no"));
            payModel.setBody(param.getStr("body"));
            payModel.setSubject(param.getStr("subject"));
            payModel.setProductCode(param.getStr("product_code"));
            request.setNotifyUrl(param.getStr("notify_url"));
            request.setBizModel(payModel);
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            log.info(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
            return response.getBody();
        } catch (AlipayApiException e) {
            log.error("App支付异常", e);
            throw new AliPayException(AliPayTradeErrorCode.TRADE_PAY_ERROR.getId(), AliPayTradeErrorCode.TRADE_PAY_ERROR.getMsg());
        }
    }

    @Override
    public String pagePay(Dict param) {
        try {
            final AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
            AlipayTradePagePayModel payModel = new AlipayTradePagePayModel();
            payModel.setTotalAmount(param.getStr("total_amount"));
            payModel.setOutTradeNo(param.getStr("out_trade_no"));
            payModel.setBody(param.getStr("body"));
            payModel.setSubject(param.getStr("subject"));
            payModel.setProductCode(param.getStr("product_code"));
            request.setNotifyUrl(param.getStr("notify_url"));
            request.setBizModel(payModel);
            AlipayTradePagePayResponse response = alipayClient.pageExecute(request);
            return response.getBody();
        } catch (AlipayApiException e) {
            log.error("网页支付异常", e);
            throw new AliPayException(AliPayTradeErrorCode.TRADE_PAY_ERROR.getId(), AliPayTradeErrorCode.TRADE_PAY_ERROR.getMsg());
        }
    }

    @Override
    public MonitorHeartbeatSynResponse sendHeartBeat(MonitorHeartbeatSynRequest request) {
        MonitorHeartbeatSynResponse response = null;
        try {
            response = alipayClient.execute(request);
            log.info("交易保障数据上传：" + response.getBody());
        } catch (AlipayApiException e) {
            log.error("交易保障数据上传失败：", e);
            throw new AliPayException(500, "send error");
        }
        return response;
    }

    private AlipayTradeQueryResponse query(AlipayTradeQueryModel tradeQueryModel) {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizModel(tradeQueryModel);
        AlipayTradeQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
            log.info("支付宝订单支付查询返回： \n" + response.getBody());
        } catch (AlipayApiException e) {
            log.error("支付宝订单支付查询失败", e);
            throw new AliPayException(AliPayTradeErrorCode.TRADE_QUERY_ERROR.getId(), "trade error");
        }
        return response;
    }

    private AlipayFundTransOrderQueryResponse query(AlipayFundTransOrderQueryModel alipayFundTransOrderQueryModel) {
        AlipayFundTransOrderQueryRequest request = new AlipayFundTransOrderQueryRequest();
        request.setBizModel(alipayFundTransOrderQueryModel);
        AlipayFundTransOrderQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
            log.info("支付宝转账结果查询返回： \n" + response.getBody());
        } catch (AlipayApiException e) {
            log.error("支付宝转账结果查询失败", e);
            throw new AliPayException(AliPayTradeErrorCode.TRADE_QUERY_ERROR.getId(), "query error");
        }
        return response;
    }
}
