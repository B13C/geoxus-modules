package com.geoxus.modules.message.controller.frontend;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.controller.GXController;
import com.geoxus.core.common.oauth.GXTokenManager;
import com.geoxus.core.common.util.GXHttpContextUtils;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.modules.message.entity.UserHasMessageEntity;
import com.geoxus.modules.message.service.UserHasMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-message/frontend")
public class UserHasMessageController implements GXController<UserHasMessageEntity> {
    @Autowired
    private UserHasMessageService userHasMessageService;

    @Override
    @PostMapping("/create")
    public GXResultUtils create(UserHasMessageEntity target) {
        final long userId = getUserIdFromToken(GXTokenManager.USER_TOKEN, GXTokenManager.USER_ID);
        target.setUserId(userId);
        final long id = userHasMessageService.create(target, GXHttpContextUtils.getHttpParam("param", Dict.class));
        return GXResultUtils.ok().putData(Dict.create().set("id", id));
    }

    @Override
    @PostMapping("/update")
    public GXResultUtils update(UserHasMessageEntity target) {
        final long userId = getUserIdFromToken(GXTokenManager.USER_TOKEN, GXTokenManager.USER_ID);
        target.setUserId(userId);
        final long id = userHasMessageService.update(target, GXHttpContextUtils.getHttpParam("param", Dict.class));
        return GXResultUtils.ok().putData(Dict.create().set("id", id));
    }

    @Override
    @PostMapping("/delete")
    public GXResultUtils delete(@RequestBody Dict param) {
        return GXResultUtils.ok().putData(Dict.create().set("status", userHasMessageService.delete(param)));
    }

    @Override
    @PostMapping("/list-or-search")
    public GXResultUtils listOrSearch(@RequestBody Dict param) {
        final long userId = getUserIdFromToken(GXTokenManager.USER_TOKEN, GXTokenManager.USER_ID);
        param.set(GXTokenManager.USER_ID, userId);
        return GXResultUtils.ok().putData(userHasMessageService.listOrSearch(param));
    }

    @Override
    @PostMapping("/detail")
    public GXResultUtils detail(Dict param) {
        return GXResultUtils.ok().putData(userHasMessageService.detail(param));
    }

    @PostMapping("/read-message")
    public GXResultUtils readMessage(@RequestBody Dict param) {
        final long userId = getUserIdFromToken(GXTokenManager.USER_TOKEN, GXTokenManager.USER_ID);
        param.set(GXTokenManager.USER_ID, userId);
        return GXResultUtils.ok().putData(userHasMessageService.readMessage(param));
    }

    @PostMapping("/unread-message")
    public GXResultUtils unReadMessage(@RequestBody Dict param) {
        final long userId = getUserIdFromToken(GXTokenManager.USER_TOKEN, GXTokenManager.USER_ID);
        param.set(GXTokenManager.USER_ID, userId);
        return GXResultUtils.ok().putData(userHasMessageService.unReadMessage(param));
    }
}
