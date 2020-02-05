package com.geoxus.modules.user.service;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.framework.service.GXBaseService;
import com.geoxus.modules.user.entity.UBalanceEntity;

import java.util.List;

public interface UBalanceService extends GXBaseService<UBalanceEntity> {
    /**
     * 判断用户是否由足够的额余额
     *
     * @param userId 用户ID
     * @param type   余额类型
     * @return boolean
     */
    boolean enoughBalance(long userId, int type, double amount);

    /**
     * 获取用户具体类型的余额
     *
     * @param userId 用户ID
     * @param type   用户类型
     * @return
     */
    UBalanceEntity getUBalanceByType(long userId, int type);

    /**
     * 根据类型获取列表信息
     *
     * @param type 余额类型
     * @return
     */
    List<UBalanceEntity> getUBalanceListByType(int type);

    /**
     * 新增用户账户余额
     *
     * @param userId
     * @param type
     * @param amount
     * @param appendInfo
     * @return
     */
    double increaseBalance(long userId, int type, double amount, Dict appendInfo);

    /**
     * 减少用户账户余额
     *
     * @param userId
     * @param type
     * @param amount
     * @param appendInfo
     * @return
     */
    double reduceBalance(long userId, int type, double amount, Dict appendInfo);

    /**
     * 平台内部之间的转账
     *
     * @param fromUserId
     * @param toUserId
     * @param amount
     * @param type
     * @param appendInfo
     * @return
     */
    boolean transferAccounts(long fromUserId, long toUserId, String amount, int type, Dict appendInfo);
}
