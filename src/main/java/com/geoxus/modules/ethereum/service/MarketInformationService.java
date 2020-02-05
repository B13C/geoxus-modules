package com.geoxus.modules.ethereum.service;

import java.util.List;
import java.util.Map;

/**
 * 获取以太坊行情数据
 */
public interface MarketInformationService {
    List<Map<String, Object>> marketInformation(List<String> params);
}
