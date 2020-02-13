package com.geoxus.modules.user.controller.backend;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.annotation.GXRequestBodyToBeanAnnotation;
import com.geoxus.core.common.controller.GXController;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.modules.user.constant.UUserConstants;
import com.geoxus.modules.user.entity.UUserEntity;
import com.geoxus.modules.user.service.UUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController("backendUserController")
@RequestMapping("/user/backend")
public class UserController implements GXController<UUserEntity> {
    @Autowired
    private UUserService userService;

    @Override
    @PostMapping("/create")
    @Transactional(rollbackFor = Exception.class)
    public GXResultUtils create(@Valid @GXRequestBodyToBeanAnnotation UUserEntity target) {
        final long i = userService.create(target, Dict.create());
        return GXResultUtils.ok().putData(Dict.create().set("id", i));
    }

    @Override
    @PostMapping("/update")
    public GXResultUtils update(@Valid @GXRequestBodyToBeanAnnotation UUserEntity target) {
        final long i = userService.update(target, Dict.create());
        return GXResultUtils.ok().putData(Dict.create().set("id", i));
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
        final GXPagination pagination = userService.listOrSearch(param);
        return GXResultUtils.ok().putData(pagination);
    }

    @PostMapping("/children")
    public GXResultUtils children(@RequestBody Dict param) {
        final UUserEntity user = userService.getById(param.getInt(UUserConstants.PRIMARY_KEY));
        final List<Dict> dictList = userService.children(param, user);
        return GXResultUtils.ok().putData(dictList);
    }

    @Override
    @PostMapping("/detail")
    public GXResultUtils detail(@RequestBody Dict param) {
        return GXResultUtils.ok().putData(userService.detail(param));
    }

    @PostMapping("/change-grade")
    public GXResultUtils changeGrade(@RequestBody Dict param) {
        final UUserEntity user = userService.getById(param.getLong(UUserConstants.PRIMARY_KEY));
        final boolean b = userService.changeGrade(param, user);
        return GXResultUtils.ok().putData(Dict.create().set("status", b));
    }

    @PostMapping("/frozen")
    public GXResultUtils frozen(@RequestBody Dict param) {
        final boolean frozen = userService.frozen(param);
        return GXResultUtils.ok().putData(Dict.create().set("status", frozen));
    }

    @PostMapping("/unfreeze")
    public GXResultUtils unfreeze(@RequestBody Dict param) {
        final boolean unfreeze = userService.unfreeze(param);
        return GXResultUtils.ok().putData(Dict.create().set("status", unfreeze));
    }
}
