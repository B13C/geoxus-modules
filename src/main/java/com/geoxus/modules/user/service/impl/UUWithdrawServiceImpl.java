package com.geoxus.modules.user.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.common.constant.GXBaseBuilderConstants;
import com.geoxus.core.common.exception.GXException;
import com.geoxus.core.common.util.GXSyncEventBusCenterUtils;
import com.geoxus.core.common.vo.GXBusinessStatusCode;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.modules.user.constant.UBalanceConstants;
import com.geoxus.modules.user.constant.UWithdrawConstants;
import com.geoxus.modules.user.entity.UWithdrawEntity;
import com.geoxus.modules.user.event.UWithdrawEvent;
import com.geoxus.modules.user.mapper.UWithdrawMapper;
import com.geoxus.modules.user.service.UBalanceService;
import com.geoxus.modules.user.service.UWithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UUWithdrawServiceImpl extends ServiceImpl<UWithdrawMapper, UWithdrawEntity> implements UWithdrawService {
    @Autowired
    private UBalanceService uBalanceService;

    @Override
    @Transactional
    public long create(UWithdrawEntity target, Dict param) {
        final Float amount = Convert.toFloat(JSONUtil.getByPath(JSONUtil.parse(target.getExt()), "amount"));
        final Integer userId = param.getInt("user_id");
        final boolean enoughBalance = uBalanceService.enoughBalance(userId, UBalanceConstants.AVAILABLE_BALANCE_TYPE, amount);
        if (!enoughBalance) {
            throw new GXException("余额不足!");
        }
        target.setUserId(userId);
        target.setWithdrawSn(IdUtil.getSnowflake(1, 1).nextId());
        saveOrUpdate(target);
        uBalanceService.reduceBalance(userId, UBalanceConstants.AVAILABLE_BALANCE_TYPE, amount, Dict.create().set("association_core_model_id", target.getCoreModelId()));
        final UWithdrawEvent event = new UWithdrawEvent("create_withdraw", target, Dict.create().set("amount", amount));
        GXSyncEventBusCenterUtils.getInstance().post(event);
        return target.getWithdrawId();
    }

    @Override
    @Transactional
    public long update(UWithdrawEntity target, Dict param) {
        final Float amount = Convert.toFloat(JSONUtil.getByPath(JSONUtil.parse(target.getExt()), "amount"));
        final Integer userId = param.getInt("user_id");
        final boolean enoughBalance = uBalanceService.enoughBalance(userId, UBalanceConstants.AVAILABLE_BALANCE_TYPE, amount);
        if (!enoughBalance) {
            throw new GXException("余额不足!");
        }
        target.setUserId(userId);
        saveOrUpdate(target);
        uBalanceService.reduceBalance(userId, UBalanceConstants.AVAILABLE_BALANCE_TYPE, amount, Dict.create().set("association_core_model_id", target.getCoreModelId()));
        final UWithdrawEvent event = new UWithdrawEvent("update_withdraw", target, Dict.create().set("amount", amount));
        GXSyncEventBusCenterUtils.getInstance().post(event);
        return target.getWithdrawId();
    }

    @Override
    public boolean delete(Dict param) {
        final Dict condition = Dict.create().set(UWithdrawConstants.PRIMARY_KEY, param.getInt(UWithdrawConstants.PRIMARY_KEY));
        return modifyStatus(GXBusinessStatusCode.DELETED.getCode(), condition, GXBaseBuilderConstants.NON_OPERATOR);
    }

    @Override
    public GXPagination listOrSearch(Dict param) {
        final GXPagination pagination = generatePage(param);
        return pagination;
    }

    @Override
    public Dict detail(Dict param) {
        final Dict detail = baseMapper.detail(param);
        return detail;
    }

    @Override
    public boolean decline(Dict param) {
        final UWithdrawEntity entity = getOne(new QueryWrapper<UWithdrawEntity>().allEq(Dict.create().set("withdraw_number", param.getLong("withdraw_number"))));
        if (null != entity) {
            final JSONObject obj = JSONUtil.parseObj(entity.getExt());
            param.remove("withdraw_number");
            obj.putAll(param);
            entity.setExt(obj.toString());
            updateById(entity);
            return true;
        }
        return false;
    }
}
