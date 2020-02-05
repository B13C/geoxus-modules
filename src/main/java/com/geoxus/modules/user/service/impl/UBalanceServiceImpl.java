package com.geoxus.modules.user.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.common.event.GXSlogEvent;
import com.geoxus.core.common.exception.GXException;
import com.geoxus.core.common.vo.GXBusinessStatusCode;
import com.geoxus.core.common.util.GXSyncEventBusCenterUtils;
import com.geoxus.modules.user.constant.UBalanceConstants;
import com.geoxus.modules.user.entity.UBalanceEntity;
import com.geoxus.modules.user.mapper.UBalanceMapper;
import com.geoxus.modules.user.service.UBalanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
public class UBalanceServiceImpl extends ServiceImpl<UBalanceMapper, UBalanceEntity> implements UBalanceService {
    @Override
    public boolean enoughBalance(long userId, int type, double amount) {
        final UBalanceEntity balanceEntity = getOneEntityByCondition(userId, type);
        return balanceEntity.getBalance() >= amount;
    }

    @Override
    public UBalanceEntity getUBalanceByType(long userId, int type) {
        return getOneEntityByCondition(userId, type);
    }

    @Override
    public List<UBalanceEntity> getUBalanceListByType(int type) {
        final List<UBalanceEntity> list;
        list = Collections.unmodifiableList(list(new QueryWrapper<UBalanceEntity>().eq("type", type)));
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public double increaseBalance(long userId, int type, double amount, Dict appendInfo) {
        final UBalanceEntity entity = getOneEntityByCondition(userId, type);
        final double oldBalance = entity.getBalance();
        final double newBalance = oldBalance + amount;
        entity.setUserId(userId);
        entity.setType(type);
        entity.setBalance(newBalance);
        boolean b = saveOrUpdate(entity);
        if (!b) {
            throw new GXException(StrUtil.format("新增余额失败 >>> 余额类型 : {}  >>> 用户 : {}  >>>  金额 : {}", type, userId, amount));
        }
        final Dict data = Dict.create()
                .set("old_balance", oldBalance)
                .set("new_balance", newBalance)
                .set("amount", amount)
                .set("user_id", userId)
                .set("append_info", appendInfo)
                .set("balance_type", type)
                .set("old_version", entity.getVersion())
                .set("remark", Optional.ofNullable(appendInfo.getStr("remark")).orElse("新增用户余额"))
                .set("status", GXBusinessStatusCode.NORMAL.getCode());
        final String source = Optional.ofNullable(appendInfo.getStr("source")).orElse("increment_balance");
        final GXSlogEvent<Dict> event = new GXSlogEvent<>(source, data, "s_log", Dict.create().set("user_id", userId).set("model_id", entity.getBalanceId()).set("core_model_id", UBalanceConstants.CORE_MODEL_ID));
        GXSyncEventBusCenterUtils.getInstance().post(event);
        return newBalance;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public double reduceBalance(long userId, int type, double amount, Dict appendInfo) {
        final UBalanceEntity entity = getOneEntityByCondition(userId, type);
        final double oldBalance = entity.getBalance();
        if (oldBalance < amount) {
            log.error(StrUtil.format("{}:{}-账户余额不足...", userId, type));
            return -1;
        }
        final double newBalance = oldBalance - amount;
        entity.setType(type);
        entity.setUserId(userId);
        entity.setBalance(newBalance);
        boolean b = saveOrUpdate(entity);
        if (!b) {
            throw new GXException(StrUtil.format("扣除余额失败 >>> 余额类型 : {}  >>> 用户 : {}  >>>  金额 : {}", type, userId, amount));
        }
        final Dict data = Dict.create()
                .set("old_balance", oldBalance)
                .set("new_balance", newBalance)
                .set("amount", "-" + amount)
                .set("user_id", userId)
                .set("append_info", appendInfo)
                .set("balance_type", type)
                .set("old_version", entity.getVersion())
                .set("remark", Optional.ofNullable(appendInfo.getStr("remark")).orElse("扣除用户余额"))
                .set("status", GXBusinessStatusCode.NORMAL.getCode());
        final String source = Optional.ofNullable(appendInfo.getStr("source")).orElse("reduce_balance");
        final GXSlogEvent<Dict> event = new GXSlogEvent<>(source, data, "s_log", Dict.create().set("user_id", userId).set("model_id", entity.getBalanceId()).set("core_model_id", UBalanceConstants.CORE_MODEL_ID));
        GXSyncEventBusCenterUtils.getInstance().post(event);
        return newBalance;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean transferAccounts(long fromUserId, long toUserId, String amount, int type, Dict appendInfo) {
        if (!enoughBalance(fromUserId, type, Double.parseDouble(Optional.ofNullable(appendInfo.getStr("amount")).orElse("0")))) {
            throw new GXException("余额不足");
        }
        reduceBalance(fromUserId, type, Double.parseDouble(Optional.ofNullable(appendInfo.getStr("amount")).orElse("0")), appendInfo);
        increaseBalance(toUserId, type, Double.parseDouble(amount), appendInfo);
        return true;
    }

    private UBalanceEntity getOneEntityByCondition(long userId, long type) {
        Map<String, Long> conditionMap = new HashMap<>();
        conditionMap.put("user_id", userId);
        conditionMap.put("type", type);
        return Optional.ofNullable(getOne(new QueryWrapper<UBalanceEntity>().allEq(conditionMap))).orElse(new UBalanceEntity());
    }
}
