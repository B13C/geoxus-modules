package com.geoxus.modules.user.controller.frontend;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.annotation.GXLoginAnnotation;
import com.geoxus.core.common.annotation.GXLoginUserAnnotation;
import com.geoxus.core.common.annotation.GXRequestBodyToBeanAnnotation;
import com.geoxus.core.common.controller.GXController;
import com.geoxus.core.common.oauth.GXTokenManager;
import com.geoxus.core.common.util.GXHttpContextUtils;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.modules.user.constant.UUserConstants;
import com.geoxus.modules.user.entity.UUserEntity;
import com.geoxus.modules.user.service.UBalanceService;
import com.geoxus.modules.user.service.UUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController("frontendUserController")
@RequestMapping("/user/frontend")
public class UserController implements GXController<UUserEntity> {
    @Autowired
    private UUserService userService;

    @Autowired
    private UBalanceService uBalanceService;

    /**
     * 检验手机号
     *
     * @param param
     * @return
     */
    @PostMapping("/check-phone")
    public GXResultUtils checkPhone(@RequestBody Dict param) {
        return GXResultUtils.ok().putData(Dict.create().set("status", userService.checkPhone(param)));
    }

    @Override
    @PostMapping("/create")
    public GXResultUtils create(@Valid @GXRequestBodyToBeanAnnotation UUserEntity target) {
        final long userId = userService.create(target, GXHttpContextUtils.getHttpParam("param", Dict.class));
        return GXResultUtils.ok().putData(Dict.create().set(GXTokenManager.USER_ID, userId).set(GXTokenManager.USER_TOKEN, GXTokenManager.generateUserToken(userId, Dict.create().set("phone", Optional.ofNullable(target.getPhone()).orElse("")))));
    }

    @Override
    @PostMapping("/update")
    @GXLoginAnnotation
    public GXResultUtils update(@Valid @GXRequestBodyToBeanAnnotation UUserEntity target) {
        final long userId = getUserIdFromToken(GXTokenManager.USER_TOKEN, GXTokenManager.USER_ID);
        if (userId > 0) {
            target.setUserId(userId);
        }
        userService.update(target, GXHttpContextUtils.getHttpParam("param", Dict.class));
        return GXResultUtils.ok().putData(Dict.create().set(GXTokenManager.USER_ID, userId).set(GXTokenManager.USER_TOKEN, GXTokenManager.generateUserToken(userId, Dict.create().set("phone", Optional.ofNullable(target.getPhone()).orElse("")))));
    }

    @Override
    @PostMapping("/delete")
    @Transactional(rollbackFor = Exception.class)
    public GXResultUtils delete(@RequestBody Dict param) {
        return GXResultUtils.ok().putData(Dict.create().set("status", userService.delete(param)));
    }

    @Override
    @PostMapping("/list-or-search")
    public GXResultUtils listOrSearch(@RequestBody Dict param) {
        return GXResultUtils.ok().putData(Dict.create());
    }

    @Override
    @PostMapping("/detail")
    @GXLoginAnnotation
    public GXResultUtils detail(@RequestBody Dict param) {
        final long userId = getUserIdFromToken(GXTokenManager.USER_TOKEN, GXTokenManager.USER_ID);
        param.set(UUserConstants.PRIMARY_KEY, userId);
        return GXResultUtils.ok().putData(userService.detail(param));
    }

    @PostMapping("/change-password")
    @GXLoginAnnotation
    public GXResultUtils changePassword(@RequestBody Dict param, @GXLoginUserAnnotation UUserEntity user) {
        final boolean b = userService.changePassword(param, user);
        return GXResultUtils.ok().putData(Dict.create().set("status", b));
    }

    @PostMapping("/change-pay-password")
    @GXLoginAnnotation
    public GXResultUtils changePayPassword(@RequestBody Dict param, @GXLoginUserAnnotation UUserEntity user) {
        final boolean b = userService.changePayPassword(param, user);
        return GXResultUtils.ok().putData(Dict.create().set("status", b));
    }

    @PostMapping("/login-by-username")
    public GXResultUtils loginByUserNamePwd(@RequestBody Dict param) {
        final Dict dict = userService.loginByUserNamePwd(param);
        return GXResultUtils.ok().putData(dict);
    }

    @PostMapping("/login-by-phone")
    public GXResultUtils loginByPhonePwd(@RequestBody Dict param) {
        final Dict dict = userService.loginByPhonePwd(param);
        return GXResultUtils.ok().putData(dict);
    }

    @PostMapping("/login-by-verification-code")
    public GXResultUtils loginByVerificationCode(@RequestBody Dict param) {
        final Dict dict = userService.loginByVerificationCode(param);
        final String str = dict.getStr(GXTokenManager.USER_TOKEN);
        if (str.isEmpty()) {
            return GXResultUtils.error().putData(Dict.create().set("msg", "验证码错误或者用户不存在"));
        }
        return GXResultUtils.ok().putData(dict);
    }

    @PostMapping("/login-by-open-id")
    public GXResultUtils loginByOpenId(@RequestBody Dict param) {
        final Dict dict = userService.loginByOpenId(param);
        return GXResultUtils.ok().putData(dict);
    }

    @PostMapping("/binding")
    public GXResultUtils bindUser(@RequestBody Dict param) {
        return GXResultUtils.ok().putData(userService.binding(param));
    }

    @PostMapping("/check-user-exists")
    public GXResultUtils checkUserName(@RequestBody Dict param) {
        return GXResultUtils.ok().putData(userService.checkUserExists(param));
    }

    @PostMapping("/check-binding-phone")
    public GXResultUtils checkBindingPhone(@RequestBody Dict param, @GXLoginUserAnnotation UUserEntity userEntity) {
        return GXResultUtils.ok().putData(userService.checkBindingPhone(param, userEntity));
    }

    @PostMapping("/change-phone")
    @GXLoginAnnotation
    public GXResultUtils changePhone(@RequestBody Dict param, @GXLoginUserAnnotation UUserEntity userEntity) {
        return GXResultUtils.ok().putData(userService.changePhone(param, userEntity));
    }

    @PostMapping("/find-password-by-phone")
    public GXResultUtils findBackPasswordByPhone(@RequestBody Dict param) {
        return GXResultUtils.ok().putData(userService.findBackPasswordByPhone(param));
    }

    @PostMapping("/find-password-by-email")
    public GXResultUtils findBackPasswordByEmail(@RequestBody Dict param) {
        return GXResultUtils.ok().putData(userService.findBackPasswordByEmail(param));
    }

    @PostMapping("/recharge")
    @GXLoginAnnotation
    public GXResultUtils recharge(@RequestBody Dict param, @GXLoginUserAnnotation UUserEntity userEntity) {
        return GXResultUtils.ok().putData(userService.recharge(param, userEntity));
    }

    @PostMapping("/balance")
    @GXLoginAnnotation
    public GXResultUtils balance(@RequestBody Dict param, @GXLoginUserAnnotation UUserEntity user) {
        return GXResultUtils.ok().putData(uBalanceService.getUBalanceByType(Convert.toLong(user.getUserId()), param.getInt("type")));
    }

    @PostMapping("/sub-level")
    @GXLoginAnnotation
    public GXResultUtils subLevel(@RequestBody Dict param, @GXLoginUserAnnotation UUserEntity user) {
        final List<Dict> dictList = userService.children(param, user);
        return GXResultUtils.ok().putData(dictList);
    }

    @PostMapping("/change-grade")
    @GXLoginAnnotation
    public GXResultUtils changeGrade(@RequestBody Dict param, @GXLoginUserAnnotation UUserEntity user) {
        final boolean b = userService.changeGrade(param, user);
        return GXResultUtils.ok().putData(Dict.create().set("status", b));
    }

    @PostMapping("/change-user-new-to-old")
    @GXLoginAnnotation
    public GXResultUtils changeUserNewToOld(@RequestBody Dict param) {
        final long userId = getUserIdFromToken(GXTokenManager.USER_TOKEN, GXTokenManager.USER_ID);
        final boolean b = userService.changeUserNewToOld(Dict.create().set(UUserConstants.PRIMARY_KEY, userId));
        return GXResultUtils.ok().putData(Dict.create().set("status", b));
    }

    @PostMapping("/user-logout")
    @GXLoginAnnotation
    public GXResultUtils userLogout(@RequestBody Dict param) {
        final long userId = getUserIdFromToken(GXTokenManager.USER_TOKEN, GXTokenManager.USER_ID);
        final boolean b = userService.logout(Dict.create().set("user_id", userId));
        return GXResultUtils.ok().putData(Dict.create().set("status", b));
    }
}
