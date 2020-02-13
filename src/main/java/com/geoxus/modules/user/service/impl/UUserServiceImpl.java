package com.geoxus.modules.user.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.extra.mail.MailUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.common.annotation.GXCheckRequestVerifyCodeAnnotation;
import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;
import com.geoxus.core.common.annotation.GXLoginAnnotation;
import com.geoxus.core.common.annotation.GXLoginUserAnnotation;
import com.geoxus.core.common.constant.GXBaseBuilderConstants;
import com.geoxus.core.common.event.GXSlogEvent;
import com.geoxus.core.common.exception.GXException;
import com.geoxus.core.common.oauth.GXTokenManager;
import com.geoxus.core.common.service.GXEMailService;
import com.geoxus.core.common.service.GXSendSMSService;
import com.geoxus.core.common.util.GXSpringContextUtils;
import com.geoxus.core.common.util.GXSyncEventBusCenterUtils;
import com.geoxus.core.common.vo.GXBusinessStatusCode;
import com.geoxus.core.common.vo.GXResultCode;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.core.framework.service.GXCoreModelService;
import com.geoxus.modules.user.constant.UBalanceConstants;
import com.geoxus.modules.user.constant.UUserConstants;
import com.geoxus.modules.user.entity.SUserTokenEntity;
import com.geoxus.modules.user.entity.UUserEntity;
import com.geoxus.modules.user.event.*;
import com.geoxus.modules.user.mapper.UUserMapper;
import com.geoxus.modules.user.service.SUserTokenService;
import com.geoxus.modules.user.service.UBalanceService;
import com.geoxus.modules.user.service.UUserService;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class UUserServiceImpl extends ServiceImpl<UUserMapper, UUserEntity> implements UUserService {
    @GXFieldCommentAnnotation(zh = "验证码字段的名字")
    public static final String VERIFY_CODE = "verify_code";

    @Autowired
    private SUserTokenService sUserTokenService;

    @Autowired
    private UBalanceService uBalanceService;

    @Autowired
    private GXCoreModelService coreModelService;

    @Autowired
    private GXEMailService gxEMailService;

    @Override
    public Dict checkPhone(Dict param) {
        final UUserEntity entity = getUserByUserNameOrPhone(param);
        if (null == entity) {
            return Dict.create().set("code", GXResultCode.TARGET_NOT_FOUND.getCode()).set("msg", "系统没有该手机号");
        }
        if (entity.getStatus() == GXBusinessStatusCode.FREEZE.getCode()) {
            return Dict.create().set("code", GXBusinessStatusCode.FREEZE.getCode()).set("msg", "该手机号已被冻结");
        }
        if (entity.getStatus() == GXBusinessStatusCode.DELETED.getCode()) {
            return Dict.create().set("code", GXBusinessStatusCode.DELETED.getCode()).set("msg", "系统有该手机号,已被删除");
        }
        return Dict.create().set("code", 0).set("msg", "");
    }

    @Override
    public UUserEntity getUserInfoByToken(String token) {
        if (null == token) {
            return null;
        }
        final Map<String, Object> userToken = GXTokenManager.decodeUserToken(token);
        final Object o = Optional.ofNullable(userToken.get(GXTokenManager.USER_ID)).orElse(null);
        if (null == o) {
            return null;
        }
        return getById((Integer) o);
    }

    @Override
    public UUserEntity getUserInfoByCondition(Dict condition) {
        return getOne(new QueryWrapper<UUserEntity>().allEq(condition));
    }

    @Override
    public boolean changePassword(Dict param, UUserEntity user) {
        if (null != user) {
            final String oldPassword = param.getStr("old_password");
            final String newPassword = param.getStr("new_password");
            if (!user.getPassword().equals(SecureUtil.md5(oldPassword + user.getSalt()))) {
                throw new GXException("旧密码不正确");
            }
            if (user.getPassword().equals(SecureUtil.md5(newPassword + user.getSalt()))) {
                throw new GXException("新密码与旧密码相同");
            }
            final String salt = RandomUtil.randomString(8);
            user.setSalt(salt);
            user.setPassword(SecureUtil.md5(newPassword + salt));
            return updateById(user);
        }
        return false;
    }

    @Override
    public boolean changePassword(Dict param) {
        final String password = param.getStr("password");
        final String salt = RandomUtil.randomString(8);
        final Dict data = Dict.create().set("password", SecureUtil.md5(password.concat(salt))).set("salt", salt);
        final Dict condition = Dict.create().set(UUserConstants.PRIMARY_KEY, param.getLong(UUserConstants.PRIMARY_KEY));
        return updateFieldBySQL(UUserEntity.class, data, condition);
    }

    @Override
    public boolean changePayPassword(Dict param, UUserEntity user) {
        if (null != user) {
            final String payPassword = param.getStr("pay_password");
            final String salt = RandomUtil.randomString(8);
            user.setPaySalt(salt);
            user.setPayPassword(SecureUtil.md5(payPassword + salt));
            return updateById(user);
        }
        return false;
    }

    @Override
    @GXCheckRequestVerifyCodeAnnotation()
    public Dict loginByUserNamePwd(Dict param) {
        final String username = param.getStr("username");
        final String password = param.getStr("password");
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            throw new GXException(GXResultCode.INPUT_TOO_SHORT);
        }
        final UUserEntity entity = getOne(new QueryWrapper<UUserEntity>().eq("username", username));
        if (null == entity) {
            throw new GXException(GXResultCode.USER_NOT_EXISTS);
        }
        if (!StrUtil.equals(entity.getPassword(), SecureUtil.md5(password + entity.getSalt()))) {
            throw new GXException(GXResultCode.PASSWORD_ERROR);
        }
        if (checkStatus(GXBusinessStatusCode.NORMAL.getCode(), entity.getStatus())) {
            throw new GXException(GXResultCode.STATUS_ERROR);
        }
        final String token = GXTokenManager.generateUserToken(entity.getUserId(), Dict.create().set("phone", Dict.create().set("phone", Optional.ofNullable(entity.getPhone()).orElse(""))));
        sUserTokenService.createOrUpdate(token, entity.getUserId());
        final UserLoginAfterEvent loginAfterEvent = new UserLoginAfterEvent("login", entity, Dict.create());
        GXSyncEventBusCenterUtils.getInstance().post(loginAfterEvent);
        return Dict.create().set(GXTokenManager.USER_TOKEN, token).set("is_new", getSingleJSONFieldValue(entity, "ext.is_new", Integer.class, 1));
    }

    @Override
    @GXCheckRequestVerifyCodeAnnotation()
    public Dict loginByPhonePwd(Dict param) {
        final String phone = Optional.ofNullable(param.getStr("phone")).orElse(param.getStr("username"));
        final String password = param.getStr("password");
        if (StrUtil.isNotBlank(phone) && StrUtil.isNotBlank(password)) {
            final UUserEntity entity = getUserByUserNameOrPhone(Dict.create().set("phone", phone).set("username", phone));
            if (null != entity && StrUtil.equals(entity.getPassword(), SecureUtil.md5(password + entity.getSalt()))) {
                if (checkStatus(GXBusinessStatusCode.NORMAL.getCode(), entity.getStatus())) {
                    throw new GXException(GXResultCode.STATUS_ERROR);
                }
                final String token = GXTokenManager.generateUserToken(entity.getUserId(), Dict.create().set("phone", Optional.ofNullable(entity.getPhone()).orElse("")));
                sUserTokenService.createOrUpdate(token, Convert.toLong(entity.getUserId()));
                final UserLoginAfterEvent loginAfterEvent = new UserLoginAfterEvent("login", entity, Dict.create());
                GXSyncEventBusCenterUtils.getInstance().post(loginAfterEvent);
                return Dict.create().set(GXTokenManager.USER_TOKEN, token).set("is_new", getSingleJSONFieldValue(entity, "ext.is_new", Integer.class, 1));
            }
        }
        return Dict.create().set(GXTokenManager.USER_TOKEN, "");
    }

    @Override
    public Dict loginByOpenId(Dict param) {
        final Dict condition = Dict.create();
        final String openId = param.getStr("openid");
        if (null != openId) {
            condition.set("wx_open_id", openId);
            final UUserEntity entity = getOne(new QueryWrapper<UUserEntity>().allEq(condition));
            if (null != entity) {
                if (checkStatus(GXBusinessStatusCode.NORMAL.getCode(), entity.getStatus())) {
                    throw new GXException(GXResultCode.STATUS_ERROR);
                }
                final String token = GXTokenManager.generateUserToken(Convert.toLong(entity.getUserId()), Dict.create().set("phone", Optional.ofNullable(entity.getPhone()).orElse("")));
                final Dict dict = Dict.create().set(GXTokenManager.USER_TOKEN, token).set("bind_phone", true);
                if (StrUtil.isBlank(entity.getPhone())) {
                    dict.set("bind_phone", false);
                }
                sUserTokenService.createOrUpdate(token, Convert.toLong(entity.getUserId()));
                final UserLoginAfterEvent loginAfterEvent = new UserLoginAfterEvent("login", entity, Dict.create());
                GXSyncEventBusCenterUtils.getInstance().post(loginAfterEvent);
                return dict.set("is_new", getSingleJSONFieldValue(entity, "ext.is_new", Integer.class, 1));
            }
        }
        return Dict.create().set(GXTokenManager.USER_TOKEN, "").set("bind_phone", false);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Dict loginByPhoneVerificationCode(Dict param) {
        final String verificationCode = param.getStr(VERIFY_CODE);
        final String phone = param.getStr("phone");
        if (StrUtil.isNotBlank(phone) && StrUtil.isNotBlank(verificationCode)) {
            final boolean verification = getSendSMSService().verification(phone, verificationCode);
            if (verification) {
                long userId;
                final UUserEntity entity = Optional.ofNullable(getOne(new QueryWrapper<UUserEntity>().eq("phone", phone))).orElse(new UUserEntity());
                final Integer autoRegister = param.getInt("auto_register");
                if (0 == entity.getUserId() && autoRegister != null && autoRegister == 1) {
                    entity.setPhone(phone);
                    entity.setPassword(RandomUtil.randomString(8));
                    userId = create(entity, Dict.create());
                } else {
                    userId = entity.getUserId();
                }
                if (checkStatus(GXBusinessStatusCode.NORMAL.getCode(), entity.getStatus())) {
                    throw new GXException(GXResultCode.STATUS_ERROR);
                }
                if (userId > 0) {
                    final String token = GXTokenManager.generateUserToken(userId, Dict.create().set("phone", Optional.ofNullable(entity.getPhone()).orElse("")));
                    sUserTokenService.createOrUpdate(token, userId);
                    final UserLoginAfterEvent loginAfterEvent = new UserLoginAfterEvent("login", entity, Dict.create());
                    GXSyncEventBusCenterUtils.getInstance().post(loginAfterEvent);
                    return Dict.create().set(GXTokenManager.USER_TOKEN, token).set("is_new", getSingleJSONFieldValue(entity, "ext.is_new", Integer.class, 1));
                }
            }
        }
        return Dict.create().set(GXTokenManager.USER_TOKEN, "");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GXCheckRequestVerifyCodeAnnotation()
    public Dict binding(Dict param) {
        final String bindType = param.getStr("bind_type");
        final String openId = param.getStr("open_id");
        UUserEntity entity = null;
        if (null != openId) {
            if (StrUtil.equals("wx", bindType)) {
                param.set("wx_open_id", openId);
                entity = getOne(new QueryWrapper<UUserEntity>().eq("wx_open_id", openId));
            } else if (StrUtil.equals("qq", bindType)) {
                param.set("qq_open_id", openId);
                entity = getOne(new QueryWrapper<UUserEntity>().eq("qq_open_id", openId));
            } else if (StrUtil.equals("ali", bindType)) {
                param.set("ali_pay_open_id", openId);
                entity = getOne(new QueryWrapper<UUserEntity>().eq("ali_pay_open_id", openId));
            }
            if (null == entity) {
                entity = param.toBean(UUserEntity.class);
            }
            final String password = Optional.ofNullable(param.getStr("password")).orElse(RandomUtil.randomString(8));
            final String salt = RandomUtil.randomString(8);
            entity.setPassword(SecureUtil.md5(password + salt));
            final long userId = create(entity, Dict.create());
            final String token = GXTokenManager.generateUserToken(userId, Dict.create().set("phone", Optional.ofNullable(entity.getPhone()).orElse("")));
            sUserTokenService.createOrUpdate(token, userId);
            return Dict.create().set("status", 0).set("msg", "绑定成功").set(GXTokenManager.USER_TOKEN, token).set("is_new", getSingleJSONFieldValue(entity, "ext.is_new", Integer.class, 1));
        }
        return Dict.create().set("status", 1).set("msg", "失败");
    }

    @Override
    public Dict checkUserExists(Dict param) {
        final String field = param.getStr("field");
        final String value = param.getStr("value");
        return getOne(new QueryWrapper<UUserEntity>().eq(field, value)) != null
                ? Dict.create().set("status", true).set("msg", "用户存在")
                : Dict.create().set("status", false).set("msg", "用户不存在");
    }

    @Override
    public Dict checkBindingPhone(Dict param, UUserEntity userEntity) {
        return StrUtil.isBlank(userEntity.getPhone()) ? Dict.create().set("bind_phone", false) : Dict.create().set("bind_phone", true);
    }

    @Override
    @GXCheckRequestVerifyCodeAnnotation()
    public Dict changePhone(Dict param, UUserEntity userEntity) {
        userEntity.setPhone(param.getStr("phone"));
        updateById(userEntity);
        return Dict.create().set("status", true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Dict findBackPasswordByPhone(Dict param) {
        final String verifyCode = param.getStr(VERIFY_CODE);
        final String phone = param.getStr("phone");
        final Dict condition = Dict.create().set("phone", phone);
        final String templateName = param.getStr("template_name");
        final UUserEntity user = getOne(new QueryWrapper<UUserEntity>().allEq(condition));
        if (getSendSMSService().verification(phone, verifyCode) && null != user) {
            final String password = RandomUtil.randomString(8);
            final String salt = RandomUtil.randomString(8);
            user.setSalt(salt);
            user.setPassword(SecureUtil.md5(password + salt));
            final boolean status = updateById(user);
            if (status) {
                getSendSMSService().send(phone, templateName, Dict.create().set("code", password));
                return Dict.create().set("status", status);
            }
        }
        return Dict.create().set("status", false);
    }

    @Override
    public Dict findBackPasswordByEmail(Dict param) {
        final String verifyCode = param.getStr(VERIFY_CODE);
        final String email = param.getStr("email");
        final Dict condition = Dict.create().set("email", email);
        final UUserEntity user = getOne(new QueryWrapper<UUserEntity>().allEq(condition));
        if (gxEMailService.verification(email, verifyCode) && null != user) {
            final String password = RandomUtil.randomString(8);
            final String salt = RandomUtil.randomString(8);
            user.setSalt(salt);
            user.setPassword(SecureUtil.md5(password + salt));
            final boolean status = updateById(user);
            if (status) {
                final String content = StrUtil.format("您的密码已经需改成功! 新密码为: {} , 请尽快登录系统修改!", password);
                final String sendResult = MailUtil.send(user.getEmail(), "修改密码提醒", content, false);
                if (StrUtil.isNotBlank(sendResult)) {
                    return Dict.create().set("status", true);
                }
            }
        }
        return Dict.create().set("status", false);
    }

    @Override
    public Dict recharge(Dict param, UUserEntity userEntity) {
        final long userId = Convert.toLong(userEntity.getUserId());
        final Float amount = param.getFloat("amount");
        final double balance = uBalanceService.increaseBalance(userId, UBalanceConstants.AVAILABLE_BALANCE_TYPE, amount, Dict.create().set("msg", "用户充值"));
        return Dict.create().set("status", balance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Dict transferAccounts(long fromUserId, long toUserId, int type, double amount) {
        if (!uBalanceService.enoughBalance(fromUserId, type, amount)) {
            throw new GXException("账户余额不足!", GXException.NORMAL_STATUS);
        }
        uBalanceService.reduceBalance(fromUserId, type, amount, Dict.create().set("remark", "用户转账"));
        uBalanceService.increaseBalance(toUserId, type, amount, Dict.create().set("remark", "用户转账"));
        return Dict.create().set("status", true);
    }

    @Override
    @GXLoginAnnotation
    public List<Dict> children(@RequestBody Dict param, @GXLoginUserAnnotation UUserEntity user) {
        param.set(UUserConstants.PRIMARY_KEY, user.getUserId());
        return baseMapper.children(param);
    }

    @Override
    public boolean changeGrade(Dict param, UUserEntity userEntity) {
        final boolean grade = updateJSONFieldMultiValue(userEntity, Dict.create().set("ext", Dict.create().set("grade", param.getInt("grade")).set("status", GXBusinessStatusCode.WAIT_REVIEW.getCode())));
        final int oldGrade = getSingleJSONFieldValue(userEntity, "ext.grade", Integer.class);
        final Dict target = Dict.create();
        target.set("old_grade", oldGrade);
        target.set("new_grade", param.getInt("grade"));
        final Dict data = Dict.create();
        data.set("core_model_id", UUserConstants.CORE_MODEL_ID);
        data.set("model_id", userEntity.getUserId());
        data.set("user_id", userEntity.getUserId());
        final GXSlogEvent<Dict> event = new GXSlogEvent<>("common", target, "s_log", data);
        GXSyncEventBusCenterUtils.getInstance().post(event);
        return grade;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GXCheckRequestVerifyCodeAnnotation()
    public long create(UUserEntity target, Dict param) {
        final UUserEntity entity = getUserByUserNameOrPhone(Dict.create().set("phone", target.getPhone()).set("username", target.getUsername()));
        if (null != entity) {
            log.error("{}", GXResultCode.USER_NAME_EXIST.getMsg());
            return -1;
        }
        if (null == target.getUsername() && null != target.getPhone()) {
            target.setUsername(target.getPhone());
        }
        final String phoneMatchReg = "^[1](([3][0-9])|([4][5-9])|([5][0-3,5-9])|([6][5,6])|([7][0-8])|([8][0-9])|([9][1,8,9]))[0-9]{8}$";
        if (null == target.getPhone() && null != target.getUsername() && Validator.isMactchRegex(phoneMatchReg, target.getUsername())) {
            target.setPhone(target.getUsername());
        }
        final UserSaveBeforeEvent saveBeforeEvent = new UserSaveBeforeEvent("user-save-before", target, Dict.create());
        GXSyncEventBusCenterUtils.getInstance().post(saveBeforeEvent);
        target = saveBeforeEvent.getTargetEntity();
        if (0 != target.getParentId()) {
            final UUserEntity parentEntity = getById(target.getParentId());
            if (null != parentEntity) {
                String path = parentEntity.getPath() + "," + parentEntity.getUserId();
                target.setPath(path);
                target.setHierarchy(parentEntity.getHierarchy() + 1);
            }
        }
        if (StrUtil.isNotBlank(target.getPassword())) {
            final String salt = RandomUtil.randomString(8);
            target.setSalt(salt);
            target.setPassword(SecureUtil.md5(target.getPassword() + salt));
        }
        if (StrUtil.isNotBlank(target.getPayPassword())) {
            final String salt = RandomUtil.randomString(8);
            target.setPaySalt(salt);
            target.setPayPassword(SecureUtil.md5(target.getPassword() + salt));
        }
        target.setUserId(IdUtil.getSnowflake(1, 1).nextId());
        final boolean save = save(target);
        handleMedia(target, Convert.toLong(target.getUserId()), coreModelService.getModelTypeByModelId(target.getCoreModelId(), "Users"), Dict.create());
        if (save) {
            sUserTokenService.createOrUpdate(GXTokenManager.generateUserToken(Convert.toLong(target.getUserId()), Dict.create().set("phone", Optional.ofNullable(target.getPhone()).orElse(""))), Convert.toLong(target.getUserId()));
        }
        final UserSaveAfterEvent userSaveAfterEvent = new UserSaveAfterEvent("user-save-after", target, param);
        GXSyncEventBusCenterUtils.getInstance().post(userSaveAfterEvent);
        return Convert.toLong(target.getUserId());
    }

    @Override
    @GXCheckRequestVerifyCodeAnnotation()
    public long update(UUserEntity target, Dict param) {
        if (StrUtil.isNotBlank(target.getPassword())) {
            final String salt = RandomUtil.randomString(8);
            target.setSalt(salt);
            target.setPassword(SecureUtil.md5(target.getPassword() + salt));
        }
        if (StrUtil.isNotBlank(target.getPayPassword())) {
            final String salt = RandomUtil.randomString(8);
            target.setPaySalt(salt);
            target.setPayPassword(SecureUtil.md5(target.getPassword() + salt));
        }
        final UserUpdateBeforeEvent userUpdateBeforeEvent = new UserUpdateBeforeEvent("user-update-before", target, Dict.create());
        GXSyncEventBusCenterUtils.getInstance().post(userUpdateBeforeEvent);
        target = userUpdateBeforeEvent.getTargetEntity();
        updateById(target);
        handleMedia(target, Convert.toLong(target.getUserId()), coreModelService.getModelTypeByModelId(target.getCoreModelId(), "User"), Dict.create());
        final UserUpdateAfterEvent userUpdateAfterEvent = new UserUpdateAfterEvent("user-update-after", target, param);
        GXSyncEventBusCenterUtils.getInstance().post(userUpdateAfterEvent);
        return Convert.toInt(target.getUserId());
    }

    @Override
    public boolean delete(Dict param) {
        final Dict condition = Dict.create().set(UUserConstants.PRIMARY_KEY, param.getLong(UUserConstants.PRIMARY_KEY));
        return modifyStatus(GXBusinessStatusCode.DELETED.getCode(), condition, GXBaseBuilderConstants.NON_OPERATOR);
    }

    @Override
    public GXPagination listOrSearch(Dict param) {
        return generatePage(param);
    }

    @Override
    public Dict detail(Dict param) {
        final Dict detail = baseMapper.detail(param);
        if (null != detail) {
            return detail;
        }
        return Dict.create();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Dict verifyUserToken(String token) {
        if (null == token) {
            return null;
        }
        Dict dict = GXTokenManager.decodeUserToken(token);
        if (dict != null && !dict.isEmpty()) {
            long userId = dict.getLong(GXTokenManager.USER_ID);
            SUserTokenEntity entity = sUserTokenService.getOne(new QueryWrapper<SUserTokenEntity>().eq("user_id", userId).eq("type", 2));
            if (null == entity) {
                final String phone = dict.getStr("phone");
                final String username = dict.getStr("username");
                UUserEntity uUserEntity = getUserByUserNameOrPhone(Dict.create().set("phone", phone).set("username", username));
                if (null == uUserEntity || StrUtil.isBlank(phone)) {
                    return null;
                }
                final String userToken = GXTokenManager.generateUserToken(uUserEntity.getUserId(), Dict.create().set("phone", Optional.ofNullable(uUserEntity.getPhone()).orElse(phone)));
                sUserTokenService.createOrUpdate(userToken, Convert.convert(Long.class, uUserEntity.getUserId()));
                entity = sUserTokenService.getOne(new QueryWrapper<SUserTokenEntity>().eq("user_id", Convert.convert(Long.class, uUserEntity.getUserId())).eq("type", 2));
            }
            int currentTime = (int) DateUtil.currentSeconds();
            if (entity.getExpiredAt() < currentTime) {
                return null;
            }
            if (entity.getExpiredAt() - currentTime <= GXTokenManager.USER_EXPIRES_REFRESH) {
                entity.setExpiredAt(currentTime + GXTokenManager.EXPIRES);
                sUserTokenService.updateById(entity);
            }
            dict.put(GXTokenManager.USER_ID, entity.getUserId());
            dict.putIfAbsent(GXTokenManager.USER_TOKEN, entity.getToken());
        }
        return dict;
    }

    @Override
    public boolean frozen(Dict param) {
        final Dict condition = Dict.create().set(UUserConstants.PRIMARY_KEY, param.getLong(UUserConstants.PRIMARY_KEY));
        return modifyStatus(GXBusinessStatusCode.FREEZE.getCode(), condition, GXBaseBuilderConstants.OR_OPERATOR);
    }

    @Override
    public boolean unfreeze(Dict param) {
        final Dict condition = Dict.create().set(UUserConstants.PRIMARY_KEY, param.getLong(UUserConstants.PRIMARY_KEY));
        return modifyStatus(GXBusinessStatusCode.FREEZE.getCode(), condition, GXBaseBuilderConstants.NEGATION_OPERATOR);
    }

    @Override
    public boolean changeUserNewToOld(Dict param) {
        final Table<String, String, Object> extData = HashBasedTable.create();
        extData.put("ext", "is_new", 0);
        final Dict data = Dict.create().set("ext", extData);
        final Dict condition = Dict.create().set("user_id", param.getLong(UUserConstants.PRIMARY_KEY));
        return updateFieldBySQL(UUserEntity.class, data, condition);
    }

    @Override
    public boolean logout(Dict param) {
        return sUserTokenService.logout(param);
    }

    @Override
    public boolean checkStatus(int expect, int actual) {
        return expect == actual;
    }

    /**
     * 通过用户名或者手机号码获取用户信息
     *
     * @param param
     * @return
     */
    private UUserEntity getUserByUserNameOrPhone(Dict param) {
        final String value = Optional.ofNullable(param.getStr("phone")).orElse(param.getStr("username"));
        if (null != value && StrUtil.isNotBlank(value)) {
            return Optional.ofNullable(getOne(new QueryWrapper<UUserEntity>().eq("phone", value))).orElse(getOne(new QueryWrapper<UUserEntity>().eq("username", value)));
        }
        return null;
    }

    @Override
    public boolean validateExists(Object value, String field, ConstraintValidatorContext constraintValidatorContext, Dict param) throws UnsupportedOperationException {
        log.info("validateExists : {} , field : {}", value, field);
        return null != getById(Convert.toInt(value));
    }

    private GXSendSMSService getSendSMSService() {
        return GXSpringContextUtils.getBean(GXSendSMSService.class);
    }
}
