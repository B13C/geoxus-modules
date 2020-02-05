package com.geoxus.modules.ethereum.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.modules.ethereum.entity.UTransactionLogEntity;
import com.geoxus.modules.ethereum.mapper.UTransactionLogMapper;
import com.geoxus.modules.ethereum.service.UTransactionLogService;
import org.springframework.stereotype.Service;

@Service
public class UTransactionLogServiceImpl extends ServiceImpl<UTransactionLogMapper, UTransactionLogEntity> implements UTransactionLogService {
}
