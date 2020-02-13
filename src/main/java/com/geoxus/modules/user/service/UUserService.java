package com.geoxus.modules.user.service;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.service.GXUUserService;
import com.geoxus.modules.user.entity.UUserEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface UUserService extends GXUUserService<UUserEntity> {
    /**
     * 检测手机号是是否存在
     *
     * @param param
     * @return
     */
    Dict checkPhone(Dict param);

    /**
     * 通过用户Token获取用户信息
     *
     * @param token
     * @return
     */
    UUserEntity getUserInfoByToken(String token);

    /**
     * 通过条件获取用户信息
     *
     * @param condition
     * @return
     */
    UUserEntity getUserInfoByCondition(Dict condition);

    /**
     * 修改用户密码
     *
     * @param param
     * @param user
     * @return
     */
    boolean changePassword(Dict param, UUserEntity user);

    /**
     * 修改用户支付密码
     *
     * @param param
     * @return
     */
    boolean changePayPassword(Dict param, UUserEntity user);

    /**
     * 通过用户名密码登录
     *
     * @param param
     * @return
     */
    Dict loginByUserNamePwd(Dict param);

    /**
     * 通过手机号密码登录
     *
     * @param param
     * @return
     */
    Dict loginByPhonePwd(Dict param);

    /**
     * 通过OpenID登录
     *
     * @param param
     * @return
     */
    Dict loginByOpenId(Dict param);

    /**
     * 通过手机验证码登录
     *
     * @param param
     * @return
     */
    Dict loginByPhoneVerificationCode(@RequestBody Dict param);

    /**
     * 绑定用户信息
     *
     * @param param
     * @return
     */
    Dict binding(Dict param);

    /**
     * 检测用户名是否存在
     *
     * @param param
     * @return
     */
    Dict checkUserExists(Dict param);

    /**
     * 检测是否绑定过手机号码
     *
     * @param param
     * @param userEntity
     * @return
     */
    Dict checkBindingPhone(Dict param, UUserEntity userEntity);

    /**
     * 修改手机号码
     *
     * @param param
     * @param userEntity
     * @return
     */
    Dict changePhone(Dict param, UUserEntity userEntity);

    /**
     * 通过手机验证码找回密码
     *
     * @param param
     * @return
     */
    Dict findBackPasswordByPhone(Dict param);

    /**
     * 通过邮箱找回密码
     *
     * @param param
     * @return
     */
    Dict findBackPasswordByEmail(Dict param);

    /**
     * 用户充值
     *
     * @param param
     * @param userEntity
     * @return
     */
    Dict recharge(Dict param, UUserEntity userEntity);

    /**
     * 用户转账
     *
     * @param fromUserId
     * @param toUserId
     * @param type
     * @param amount
     * @return
     */
    Dict transferAccounts(long fromUserId, long toUserId, int type, double amount);

    /**
     * 获取用户的子级
     *
     * @param param
     * @param user
     * @return
     */
    List<Dict> children(Dict param, UUserEntity user);

    /**
     * 修改会员级别
     *
     * @param param
     * @param userEntity
     * @return
     */
    boolean changeGrade(Dict param, UUserEntity userEntity);

    /**
     * 冻结用户
     *
     * @param param
     * @return
     */
    boolean frozen(Dict param);

    /**
     * 解冻用户
     *
     * @param param
     * @return
     */
    boolean unfreeze(Dict param);

    /**
     * 更新新用户到老用户
     *
     * @param param
     * @return
     */
    boolean changeUserNewToOld(Dict param);

    /**
     * 登出
     *
     * @param param
     * @return
     */
    boolean logout(Dict param);

    /**
     * 判断状态的值
     *
     * @param expect
     * @param actual
     * @return
     */
    boolean checkStatus(int expect, int actual);

    default boolean checkVerifyCode(Dict param) {
        return true;
    }
}