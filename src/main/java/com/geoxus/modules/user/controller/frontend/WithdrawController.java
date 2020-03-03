package com.geoxus.modules.user.controller.frontend;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.annotation.GXApiIdempotentAnnotation;
import com.geoxus.core.common.annotation.GXLoginAnnotation;
import com.geoxus.core.common.annotation.GXRequestBodyToEntityAnnotation;
import com.geoxus.core.common.controller.GXController;
import com.geoxus.core.common.oauth.GXTokenManager;
import com.geoxus.core.common.util.GXHttpContextUtils;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.core.common.vo.GXBusinessStatusCode;
import com.geoxus.modules.user.entity.UUserEntity;
import com.geoxus.modules.user.entity.UWithdrawEntity;
import com.geoxus.modules.user.service.UUserService;
import com.geoxus.modules.user.service.UWithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController("frontendWithdrawController")
@RequestMapping("/withdraw/frontend")
public class WithdrawController implements GXController<UWithdrawEntity> {
    @Autowired
    private UWithdrawService uWithdrawService;

    @Autowired
    private UUserService uUserService;

    @Override
    @PostMapping("/create")
    @GXLoginAnnotation
    @GXApiIdempotentAnnotation
    public GXResultUtils create(@Valid @GXRequestBodyToEntityAnnotation UWithdrawEntity target) {
        final long userId = getUserIdFromToken(GXTokenManager.USER_TOKEN, GXTokenManager.USER_ID);
        final UUserEntity userEntity = uUserService.getById(userId);
        final String username = uUserService.getSingleJSONFieldValueByEntity(userEntity, "ext.username", String.class);
        final Integer type = Optional.ofNullable(uUserService.getSingleJSONFieldValueByEntity(userEntity, "ext.type", Integer.class)).orElse(0);
        final Dict extData = Dict.create().set("status", GXBusinessStatusCode.WAIT_REVIEW.getCode()).set("remark", "").set("username", username).set("type", type);
        final long i = uWithdrawService.create(target, Dict.create().set("user_id", userId));
        return GXResultUtils.ok().putData(Dict.create().set("id", i));
    }

    @Override
    @PostMapping("/update")
    @GXLoginAnnotation
    @GXApiIdempotentAnnotation
    public GXResultUtils update(@Valid @GXRequestBodyToEntityAnnotation UWithdrawEntity target) {
        final long userId = getUserIdFromToken(GXTokenManager.USER_TOKEN, GXTokenManager.USER_ID);
        final UUserEntity userEntity = uUserService.getById(userId);
        final String username = uUserService.getSingleJSONFieldValueByEntity(userEntity, "ext.username", String.class);
        final Integer type = Optional.ofNullable(uUserService.getSingleJSONFieldValueByEntity(userEntity, "ext.type", Integer.class)).orElse(0);
        final Dict extData = Dict.create().set("status", GXBusinessStatusCode.WAIT_REVIEW.getCode()).set("remark", "").set("username", username).set("type", type);
        final long i = uWithdrawService.update(target, Dict.create().set("user_id", userId));
        return GXResultUtils.ok().putData(Dict.create().set("id", i));
    }

    @Override
    @PostMapping("/delete")
    @GXLoginAnnotation
    public GXResultUtils delete(@RequestBody Dict param) {
        return null;
    }

    @Override
    @PostMapping("list-or-search")
    @GXLoginAnnotation
    public GXResultUtils listOrSearch(@RequestBody Dict param) {
        final UUserEntity info = uUserService.getUserInfoByToken(GXHttpContextUtils.getHttpServletRequest().getHeader(GXTokenManager.USER_TOKEN));
        param.set(GXTokenManager.USER_ID, info.getUserId());
        return GXResultUtils.ok().putData(uWithdrawService.listOrSearchPage(param));
    }

    @Override
    @PostMapping("/detail")
    @GXLoginAnnotation
    public GXResultUtils detail(@RequestBody Dict param) {
        return GXResultUtils.ok().putData(uWithdrawService.detail(param));
    }
}
