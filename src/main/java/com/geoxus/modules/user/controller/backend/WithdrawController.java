package com.geoxus.modules.user.controller.backend;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.annotation.GXRequestBodyToBeanAnnotation;
import com.geoxus.core.common.constant.GXBaseBuilderConstants;
import com.geoxus.core.common.controller.GXController;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.core.common.vo.GXBusinessStatusCode;
import com.geoxus.modules.user.constant.UWithdrawConstants;
import com.geoxus.modules.user.entity.UWithdrawEntity;
import com.geoxus.modules.user.service.UWithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController("backendWithdrawController")
@RequestMapping("/withdraw/backend")
public class WithdrawController implements GXController<UWithdrawEntity> {
    @Autowired
    private UWithdrawService UWithdrawService;

    @Override
    @PostMapping("/create")
    public GXResultUtils create(@Valid @GXRequestBodyToBeanAnnotation UWithdrawEntity target) {
        return null;
    }

    @Override
    @PostMapping("/update")
    public GXResultUtils update(@Valid @GXRequestBodyToBeanAnnotation UWithdrawEntity target) {
        return null;
    }

    @Override
    @PostMapping("/delete")
    public GXResultUtils delete(@RequestBody Dict param) {
        final boolean status = UWithdrawService.delete(param);
        return GXResultUtils.ok().putData(Dict.create().set("status", status));
    }

    @Override
    @PostMapping("/list-or-search")
    public GXResultUtils listOrSearch(@RequestBody Dict param) {
        return GXResultUtils.ok().putData(UWithdrawService.listOrSearch(param));
    }

    @Override
    @PostMapping("/detail")
    public GXResultUtils detail(@RequestBody Dict param) {
        final Dict detail = UWithdrawService.detail(param);
        return GXResultUtils.ok().putData(detail);
    }

    @PostMapping("/approve")
    public GXResultUtils approve(@RequestBody Dict param) {
        final Dict condition = Dict.create().set(UWithdrawConstants.PRIMARY_KEY, param.getInt(UWithdrawConstants.PRIMARY_KEY));
        final boolean status = UWithdrawService.modifyStatus(GXBusinessStatusCode.APPROVE.getCode(), condition, GXBaseBuilderConstants.NON_OPERATOR);
        return GXResultUtils.ok().putData(Dict.create().set("status", status));
    }

    @PostMapping("/decline")
    public GXResultUtils decline(@RequestBody Dict param) {
        param.set("status", GXBusinessStatusCode.DECLINE.getCode());
        final boolean status = UWithdrawService.decline(param);
        return GXResultUtils.ok().putData(Dict.create().set("status", status));
    }
}
