package com.geoxus.modules.message.controller.backend;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.annotation.GXRequestBodyToEntityAnnotation;
import com.geoxus.core.common.controller.GXController;
import com.geoxus.core.common.util.GXHttpContextUtils;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.modules.message.entity.MessageEntity;
import com.geoxus.modules.message.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController("backendMessageController")
@RequestMapping("/message/backend")
public class MessageController implements GXController<MessageEntity> {
    @Autowired
    private MessageService messageService;

    @Override
    @PostMapping("/create")
    public GXResultUtils create(@Valid @GXRequestBodyToEntityAnnotation MessageEntity target) {
        final long i = messageService.create(target, GXHttpContextUtils.getHttpParam("param", Dict.class));
        return GXResultUtils.ok().putData(Dict.create().set("id", i));
    }

    @Override
    @PostMapping("/update")
    public GXResultUtils update(@Valid @GXRequestBodyToEntityAnnotation MessageEntity target) {
        final long i = messageService.update(target, GXHttpContextUtils.getHttpParam("param", Dict.class));
        return GXResultUtils.ok().putData(Dict.create().set("id", i));
    }

    @Override
    @PostMapping("/delete")
    public GXResultUtils delete(@RequestBody Dict param) {
        final boolean status = messageService.delete(param);
        return GXResultUtils.ok().putData(Dict.create().set("status", status));
    }

    @Override
    @PostMapping("/list-or-search")
    public GXResultUtils listOrSearch(@RequestBody Dict param) {
        return GXResultUtils.ok().putData(messageService.listOrSearchPage(param));
    }

    @Override
    @PostMapping("/detail")
    public GXResultUtils detail(@RequestBody Dict param) {
        return GXResultUtils.ok().putData(messageService.detail(param));
    }
}
