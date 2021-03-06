package com.geoxus.modules.contents.controller.backend;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.annotation.GXRequestBodyToEntityAnnotation;
import com.geoxus.core.common.controller.GXController;
import com.geoxus.core.common.util.GXHttpContextUtils;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.modules.contents.constant.CommentConstants;
import com.geoxus.modules.contents.entity.CommentEntity;
import com.geoxus.modules.contents.service.CommentService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
    @RequiresPermissions("comment-create")
    public GXResultUtils create(@Valid @GXRequestBodyToEntityAnnotation CommentEntity target) {
        final long param = commentService.create(target, GXHttpContextUtils.getHttpParam("param", Dict.class));
        return GXResultUtils.ok().putData(Dict.create().set(CommentConstants.PRIMARY_KEY, param));
    }

    @Override
    @PostMapping("/update")
    @RequiresPermissions("comment-update")
    public GXResultUtils update(@Valid @GXRequestBodyToEntityAnnotation CommentEntity target) {
        final long param = commentService.update(target, GXHttpContextUtils.getHttpParam("param", Dict.class));
        return GXResultUtils.ok().putData(Dict.create().set(CommentConstants.PRIMARY_KEY, param));
    }

    @Override
    @PostMapping("/delete")
    @RequiresPermissions("comment-delete")
    public GXResultUtils delete(@RequestBody Dict param) {
        return GXResultUtils.ok().putData(Dict.create().set("status", commentService.delete(param)));
    }

    @Override
    @PostMapping("/list-or-search")
    @RequiresPermissions("comment-list-or-search")
    public GXResultUtils listOrSearch(@RequestBody Dict param) {
        return GXResultUtils.ok().putData(commentService.listOrSearchPage(param));
    }

    @Override
    @PostMapping("/detail")
    @RequiresPermissions("comment-detail")
    public GXResultUtils detail(@RequestBody Dict param) {
        return GXResultUtils.ok().putData(commentService.detail(param));
    }
}
