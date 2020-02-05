package com.geoxus.modules.message.controller.frontend;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.controller.GXController;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.modules.message.entity.MessageEntity;
import com.geoxus.modules.message.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("frontendMessageController")
@RequestMapping("/message/frontend")
public class MessageController implements GXController<MessageEntity> {
    @Autowired
    private MessageService messageService;

    @Override
    public GXResultUtils create(MessageEntity target) {
        return null;
    }

    @Override
    public GXResultUtils update(MessageEntity target) {
        return null;
    }

    @Override
    public GXResultUtils delete(Dict param) {
        return null;
    }

    @Override
    public GXResultUtils listOrSearch(Dict param) {
        return null;
    }

    @Override
    @PostMapping("/detail")
    public GXResultUtils detail(@RequestBody Dict param) {
        return GXResultUtils.ok().putData(messageService.detail(param));
    }
}
