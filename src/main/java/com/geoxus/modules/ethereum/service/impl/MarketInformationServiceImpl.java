package com.geoxus.modules.ethereum.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.geoxus.modules.ethereum.service.MarketInformationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MarketInformationServiceImpl implements MarketInformationService {
    private static final String URL = "http://api.coindog.com/api/v1/currency/ranks";

    @Override
    @Cacheable(value = "marketInfo", key = "#params.hashCode()")
    public List<Map<String, Object>> marketInformation(List<String> params) {
        final byte[] bytes = HttpRequest.get(URL).execute().bodyBytes();
        final List<Map<String, Object>> retData = new ArrayList<>();
        try {
            final String s = new String(bytes, Charset.forName("UTF-8"));
            final List<Map<String, Object>> lists = JSONUtil.toBean(s, List.class);
            for (int i = 0; i < params.size(); i++) {
                for (Map<String, Object> map : lists) {
                    if (map.get("currency").equals(params.get(0))) {
                        retData.add(map);
                        continue;
                    }
                }
            }
            return retData;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
