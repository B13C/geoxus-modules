package com.geoxus.modules.contents.controller.backend;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.annotation.GXRequestBodyToBeanAnnotation;
import com.geoxus.core.common.controller.GXController;
import com.geoxus.core.common.util.GXHttpContextUtils;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.modules.contents.entity.CommentEntity;
import com.geoxus.modules.contents.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController("backendComment")
@RequestMapping("/comment/backend")
public class CommentController implements GXController<CommentEntity> {
    @Autowired
    private CommentService commentService;

    @Override
    @PostMapping("/create")
    public GXResultUtils create(@Valid @GXRequestBodyToBeanAnnotation CommentEntity target) {
        final long param = commentService.create(target, GXHttpContextUtils.getHttpParam("param", Dict.class));
        return GXResultUtils.ok().putData(Dict.create().set("id", param));
    }

    @Override
    @PostMapping("/update")
    public GXResultUtils update(@Valid @GXRequestBodyToBeanAnnotation CommentEntity target) {
        final long param = commentService.update(target, GXHttpContextUtils.getHttpParam("param", Dict.class));
        return GXResultUtils.ok().putData(Dict.create().set("id", param));
    }

    @Override
    @PostMapping("/delete")
    public GXResultUtils delete(@RequestBody Dict param) {
        return GXResultUtils.ok().putData(Dict.create().set("status", commentService.delete(param)));
    }

    @Override
    @PostMapping("/list-or-search")
    public GXResultUtils listOrSearch(@RequestBody Dict param) {
        return GXResultUtils.ok().putData(commentService.listOrSearch(param));
    }

    @Override
    @PostMapping("/detail")
    public GXResultUtils detail(@RequestBody Dict param) {
        return GXResultUtils.ok().putData(commentService.detail(param));
    }
}
